package com.terradue.dsione;

/*
 *  Copyright 2012 Terradue srl
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.beust.jcommander.JCommander;

public final class Main
{

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        final Map<String, Command> commands = new HashMap<String, Command>();
        commands.put( "describe-images", new DescribeImages() );
        commands.put( "describe-instances", new DescribeInstances() );
        commands.put( "register", new Register() );
        commands.put( "run-instances", new RunInstances() );
        // commands.put( "server", new Server() );
        commands.put( "terminate-instances", new TerminateInstances() );
        commands.put( "upload", new Upload() );

        OnDsiProgram onDsiProgram = new OnDsiProgram();
        JCommander commander = new JCommander( onDsiProgram );
        commander.setProgramName( System.getProperty( "app.name" ) );

        // load commands
        for ( Entry<String, Command> command : commands.entrySet() )
        {
            commander.addCommand( command.getKey(), command.getValue() );
        }

        commander.parse( args );

        if ( onDsiProgram.isPrintHelp() )
        {
            commander.usage();
            System.exit( -1 );
        }

        if ( onDsiProgram.isShowVersion() )
        {
            printVersionInfo();
            System.exit( -1 );
        }

        if ( onDsiProgram.isDebug() )
        {
            System.setProperty( "log.level", "DEBUG" );
        }
        else
        {
            System.setProperty( "log.level", "INFO" );
        }

        // assume SLF4J is bound to logback in the current environment
        final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        try
        {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext( lc );
            // the context was probably already configured by default configuration
            // rules
            lc.reset();
            configurator.doConfigure( Main.class.getClassLoader().getResourceAsStream( "logback-config.xml" ) );
        }
        catch ( JoranException je )
        {
            // StatusPrinter should handle this
        }

        final Logger logger = LoggerFactory.getLogger( Main.class );

        logger.info( "" );
        logger.info( "------------------------------------------------------------------------" );
        logger.info( "{}", System.getProperty( "app.name" ) );
        logger.info( "------------------------------------------------------------------------" );
        logger.info( "" );

        String parsedCommand = commander.getParsedCommand();
        if ( parsedCommand == null || !commands.containsKey( parsedCommand ) )
        {
            throw new IllegalArgumentException( format( "%s argument can not be processed", parsedCommand ) );
        }

        long start = currentTimeMillis();
        int exit = 0;

        Throwable error = null;
        try
        {
            commands.get( parsedCommand ).execute( onDsiProgram );
        }
        catch ( Throwable t )
        {
            exit = -1;
            error = t;
        }
        finally
        {
            logger.info( "" );
            logger.info( "------------------------------------------------------------------------" );
            logger.info( "{} {}", System.getProperty( "app.name" ), ( exit < 0 ) ? "FAILURE" : "SUCCESS" );

            if ( exit < 0 )
            {
                logger.info( "" );

                if ( onDsiProgram.isDebug() )
                {
                    logger.error( "Execution terminated with errors", error );
                }
                else
                {
                    logger.error( "Execution terminated with errors: {}", error.getMessage() );
                }

                logger.info( "" );
            }

            logger.info( "Total time: {}s", ( ( currentTimeMillis() - start ) / 1000 ) );
            logger.info( "Finished at: {}", new Date() );

            final Runtime runtime = Runtime.getRuntime();
            final int megaUnit = 1024 * 1024;
            logger.info( "Final Memory: {}M/{}M", ( runtime.totalMemory() - runtime.freeMemory() ) / megaUnit,
                         runtime.totalMemory() / megaUnit );

            logger.info( "------------------------------------------------------------------------" );

            System.exit( exit );
        }
    }

    private static void printVersionInfo()
    {
        Properties properties = new Properties();
        InputStream input =
            Main.class.getClassLoader().getResourceAsStream( "META-INF/maven/com.terradue/ondsi-tools/pom.properties" );

        if ( input != null )
        {
            try
            {
                properties.load( input );
            }
            catch ( IOException e )
            {
                // ignore, just don't load the properties
            }
            finally
            {
                try
                {
                    input.close();
                }
                catch ( IOException e )
                {
                    // close quietly
                }
            }
        }

        System.out.printf( "%s %s (%s)%n",
                           properties.getProperty( "name" ),
                           properties.getProperty( "version" ),
                           properties.getProperty( "build" ) );
        System.out.printf( "Java version: %s, vendor: %s%n",
                           System.getProperty( "java.version" ),
                           System.getProperty( "java.vendor" ) );
        System.out.printf( "Java home: %s%n", System.getProperty( "java.home" ) );
        System.out.printf( "Default locale: %s_%s, platform encoding: %s%n",
                           System.getProperty( "user.language" ),
                           System.getProperty( "user.country" ),
                           System.getProperty( "sun.jnu.encoding" ) );
        System.out.printf( "OS name: \"%s\", version: \"%s\", arch: \"%s\", family: \"%s\"%n",
                           System.getProperty( "os.name" ),
                           System.getProperty( "os.version" ),
                           System.getProperty( "os.arch" ),
                           getOsFamily() );
    }

    private static final String getOsFamily()
    {
        String osName = System.getProperty( "os.name" ).toLowerCase();
        String pathSep = System.getProperty( "path.separator" );

        if ( osName.indexOf( "windows" ) != -1 )
        {
            return "windows";
        }
        else if ( osName.indexOf( "os/2" ) != -1 )
        {
            return "os/2";
        }
        else if ( osName.indexOf( "z/os" ) != -1 || osName.indexOf( "os/390" ) != -1 )
        {
            return "z/os";
        }
        else if ( osName.indexOf( "os/400" ) != -1 )
        {
            return "os/400";
        }
        else if ( pathSep.equals( ";" ) )
        {
            return "dos";
        }
        else if ( osName.indexOf( "mac" ) != -1 )
        {
            if ( osName.endsWith( "x" ) )
            {
                return "mac"; // MACOSX
            }
            return "unix";
        }
        else if ( osName.indexOf( "nonstop_kernel" ) != -1 )
        {
            return "tandem";
        }
        else if ( osName.indexOf( "openvms" ) != -1 )
        {
            return "openvms";
        }
        else if ( pathSep.equals( ":" ) )
        {
            return "unix";
        }

        return "undefined";
    }

}
