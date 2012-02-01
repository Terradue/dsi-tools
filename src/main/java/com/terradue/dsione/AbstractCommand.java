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

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.client.utils.URIUtils.createURI;
import static org.apache.http.client.utils.URLEncodedUtils.format;
import static org.apache.http.util.EntityUtils.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters( commandDescription = "OpenNebula-DSI CLI tools" )
public abstract class AbstractCommand
    implements Command
{

    @Parameter( names = { "-h", "--help" }, description = "Display help information." )
    private boolean printHelp;

    @Parameter( names = { "-v", "--version" }, description = "Display version information." )
    private boolean showVersion;

    @Parameter( names = { "-X", "--debug" }, description = "Produce execution debug output." )
    private boolean debug;

    @Parameter( names = { "-u", "--username" }, description = "The DSI account username" )
    protected String username;

    @Parameter( names = { "-p", "--password" }, description = "The DSI account password", password = true )
    protected String password;

    @Parameter( names = { "-H", "--host" }, description = "The DSI web service URI" )
    protected String serviceHost = "testcloud.t-systems.com";

    @Parameter( names = { "-P", "--port" }, description = "The DSI web service URI" )
    protected int servicePort = 80;

    protected Logger logger;

    private HttpClient httpClient;

    @Override
    public final void execute( String...args )
    {
        JCommander commander = new JCommander( this );
        commander.setProgramName( System.getProperty( "app.name" ) );

        commander.parse( args );

        if ( printHelp )
        {
            commander.usage();
            System.exit( -1 );
        }

        if ( showVersion )
        {
            printVersionInfo();
            System.exit( -1 );
        }

        if ( debug )
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
            configurator.doConfigure( getClass().getClassLoader().getResourceAsStream( "logback-config.xml" ) );
        }
        catch ( JoranException je )
        {
            // StatusPrinter should handle this
        }

        logger = LoggerFactory.getLogger( getClass() );

        logger.info( "" );
        logger.info( "------------------------------------------------------------------------" );
        logger.info( "{}", System.getProperty( "app.name" ) );
        logger.info( "------------------------------------------------------------------------" );
        logger.info( "" );

        long start = currentTimeMillis();
        int exit = 0;

        Throwable error = null;

        final DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

        // username/password authentication
        defaultHttpClient.getCredentialsProvider().setCredentials( new AuthScope( serviceHost, servicePort ),
                                                                   new UsernamePasswordCredentials( username,
                                                                                                    password ) );

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local
        // auth cache
        DigestScheme digestAuth = new DigestScheme();
        // Suppose we already know the realm name
        digestAuth.overrideParamter( "realm", "some realm" );
        // Suppose we already know the expected nonce value
        digestAuth.overrideParamter( "nonce", "whatever" );
        authCache.put( new HttpHost( serviceHost, servicePort ), digestAuth );

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute( ClientContext.AUTH_CACHE, authCache );

        try
        {

            httpClient = defaultHttpClient;

            execute();
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

                if ( debug )
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

            httpClient.getConnectionManager().shutdown();

            System.exit( exit );
        }
    }

    protected abstract void execute()
        throws Exception;

    private URI getServiceUri( String path, NameValuePair... parameters )
        throws Exception
    {
        return createURI( "https",
                          serviceHost,
                          servicePort,
                          String.format( "/ZimoryManage/services/api/%s", path ),
                          format( asList( parameters ), "UTF-8" ),
                          null );
    }

    protected void invokeGetAndLog( Class<?> targetType, boolean showHeaders, String path, NameValuePair... parameters )
        throws Exception
    {
        URI serviceUrl = getServiceUri( path, parameters );

        httpClient.execute( new HttpGet( serviceUrl ), new XmlLoggingHandler( targetType, showHeaders ) );
    }

    protected <T> T invokeGet( final Class<T> resultType, String path, NameValuePair... parameters )
        throws Exception
    {
        URI serviceUrl = getServiceUri( path, parameters );

        HttpResponse response = httpClient.execute( new HttpGet( serviceUrl ) );

        if ( SC_OK != response.getStatusLine().getStatusCode() )
        {
            throw new ClientProtocolException( String.format( "impossible to read '%s' response, DSI server replied: ",
                                                              serviceUrl,
                                                              response.getStatusLine().getReasonPhrase() ) );
        }

        T returned = newLoader( new FromAnnotationsRuleModule()
        {

            @Override
            protected void configureRules()
            {
                bindRulesFrom( resultType );
            }

        } )
        .newDigester()
        .<T>parse( new ByteArrayInputStream( toByteArray( response.getEntity() ) ) );

        return returned;
    }

    private static void printVersionInfo()
    {
        Properties properties = new Properties();
        InputStream input = AbstractCommand.class.getClassLoader().getResourceAsStream( "META-INF/maven/com.terradue/ondsi-tools/pom.properties" );

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
