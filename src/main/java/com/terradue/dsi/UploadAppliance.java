package com.terradue.dsi;

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

import static com.google.inject.Scopes.SINGLETON;
import static it.sauronsoftware.ftp4j.FTPClient.SECURITY_FTP;
import static it.sauronsoftware.ftp4j.FTPClient.SECURITY_FTPES;
import static it.sauronsoftware.ftp4j.FTPClient.SECURITY_FTPS;
import static it.sauronsoftware.ftp4j.FTPClient.TYPE_BINARY;
import static it.sauronsoftware.ftp4j.FTPClient.TYPE_TEXTUAL;
import static java.lang.String.format;
import static java.lang.System.exit;
import static javax.ws.rs.core.UriBuilder.fromUri;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.apache.commons.io.FileUtils.write;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.copy;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.sun.jersey.api.client.GenericType;
import com.terradue.dsi.model.Appliance;
import com.terradue.dsi.model.UploadTicket;
import com.terradue.dsi.wire.FTPClientProvider;

@Parameters( commandDescription = "Upload an image on DSI Cloud" )
public final class UploadAppliance
    extends BaseTool
{

    public static void main( String[] args )
    {
        exit( new UploadAppliance().execute( args ) );
    }

    @Parameter( names = { "--appliance" }, description = "The DSI applicance name" )
    private String applianceName;

    @Parameter( names = { "--description" }, description = "The DSI applicance description" )
    private String applianceDescription;

    @Parameter( names = { "--provider" }, description = "The DSI provider ID" )
    private String providerId;

    @Parameter( names = { "--qualifier" }, description = "The DSI qualifier ID" )
    private String qualifierId;

    @Parameter( names = { "--operating-system" }, description = "The DSI applicance Operating System [OPTIONAL]" )
    private String applianceOS = "Linux";

    @Parameter( names = { "--appliance-id" }, description = "The DSI applicance OS ID" )
    private String applianceOsId;

    @Parameter(
        names = { "--image" },
        description = "Path of the dir containing the image descriptor (*.vmx) and the image (*.vmdk) to upload",
        converter = FileConverter.class
    )
    private File image;

    @Inject
    private FTPClient ftpsClient;

    @Inject
    @Named( "service.appliances" )
    private String appliancesPath;

    private final Map<String, Integer> ftpProtocolMappings = new HashMap<String, Integer>();

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.upload" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    public void setAppliancesPath( String appliancesPath )
    {
        this.appliancesPath = appliancesPath;
    }

    public void setFtpsClient( FTPClient ftpsClient )
    {
        this.ftpsClient = ftpsClient;
    }

    public UploadAppliance()
    {
        ftpProtocolMappings.put( "ftp", SECURITY_FTP );
        ftpProtocolMappings.put( "ftps", SECURITY_FTPS );
        ftpProtocolMappings.put( "ftpes", SECURITY_FTPES );
    }

    @Override
    protected void bindConfigurations()
    {
        super.bindConfigurations();
        bind( FTPClient.class ).toProvider( FTPClientProvider.class ).in( SINGLETON );
    }

    @Override
    public void execute()
        throws Exception
    {
        if ( !image.exists() || !image.isDirectory() )
        {
            throw new IllegalArgumentException( format( "File %s must be an existing directory", image ) );
        }

        File zipImage = zip( image );

        File md5File = md5( zipImage );

        logger.info( "Done! Requesting FTP location where uploading images..." );

        UploadTicket uploadTicket = restClient.resource( fromUri( serviceUrl )
                                                         .queryParam( "providerId", providerId )
                                                         .queryParam( "qualifierId", qualifierId )
                                                         .queryParam( "applianceName", applianceName )
                                                         .queryParam( "applianceDescription", applianceDescription )
                                                         .queryParam( "applianceOS", applianceOS )
                                                         .queryParam( "applianceOsId", applianceOsId )
                                                         .build() ).get( UploadTicket.class );

        logger.info( "Done! Uploading image: {}(.md5) on {} (expires on {})...",
                     new Object[]
                     {
                         zipImage.getAbsolutePath(),
                         uploadTicket.getFtpLocation().toString(),
                         uploadTicket.getExpirationDate()
                     } );

        logger.info( "Connecting to {}...", uploadTicket.getFtpLocation().getHost() );

        Integer securityLevel = ftpProtocolMappings.get( uploadTicket.getFtpLocation().getScheme().toLowerCase() );
        ftpsClient.setSecurity( securityLevel.intValue() );

        ftpsClient.connect( uploadTicket.getFtpLocation().getHost() );
        ftpsClient.setPassive( true );

        try
        {
            ftpsClient.login( "anonymous", "" );
            logger.info( "Successfully logged in! Moving to working directory {}",
                         uploadTicket.getFtpLocation().getPath() );

            ftpsClient.changeDirectory( uploadTicket.getFtpLocation().getPath() );

            upload( zipImage, TYPE_BINARY );
            upload( md5File, TYPE_TEXTUAL );
        }
        finally
        {
            logger.info( "Disconnecting from {} server...", uploadTicket.getFtpLocation().getHost() );

            if ( ftpsClient.isConnected() )
            {
                ftpsClient.disconnect( false );
            }

            logger.info( "FTP Connnection closed." );
        }

        Collection<Appliance> appliances = restClient.resource( appliancesPath )
                                                     .get( new GenericType<Collection<Appliance>>(){} );

        for ( Appliance appliance : appliances )
        {
            if ( applianceName.equals( appliance.getName() ) )
            {
                logger.info( "Image uploaded with id: {}", appliance.getId() );
                return;
            }
        }

        logger.warn( "Appliance is not available yet, back checking appliances later" );
    }

    private void upload( File file, int type )
        throws Exception
    {
        ftpsClient.setType( type );
        ftpsClient.upload( file, new UploadTransferListener( logger, file ) );
    }

    private File zip( File directory )
        throws IOException
    {
        Deque<File> queue = new LinkedList<File>();
        queue.push( directory );

        final URI base = directory.getParentFile().toURI();

        File zipFile = new File( directory.getParent(), format( "%s.zip", directory.getName() ) );

        logger.info( "Archiving directory {} to zip archive {}", directory, zipFile );

        FileOutputStream fos = null;
        ZipOutputStream os = null;
        try
        {
            fos = new FileOutputStream( zipFile );
            os = new ZipOutputStream( fos );

            while ( !queue.isEmpty() )
            {
                directory = queue.pop();

                for ( File kid : directory.listFiles() )
                {
                    String name = base.relativize( kid.toURI() ).getPath();

                    if ( kid.isDirectory() )
                    {
                        queue.push( kid );
                        name = name.endsWith( "/" ) ? name : name + "/";
                        os.putNextEntry( new ZipEntry( name ) );
                    }
                    else
                    {
                        logger.info( "Adding {} as ZIP entry...", name );

                        os.putNextEntry( new ZipEntry( name ) );

                        InputStream input = new FileInputStream( kid );
                        try
                        {
                            copy( input, os );
                        }
                        finally
                        {
                            closeQuietly( input );
                            os.flush();
                            os.closeEntry();

                            logger.info( "Done!" );
                        }
                    }
                }
            }
        }
        finally
        {
            try
            {
                os.finish();
            }
            finally
            {
                logger.info( "ZIP archive complete" );
                closeQuietly( fos );
                closeQuietly( os );
            }
        }

        return zipFile;
    }

    private File md5( File file )
        throws IOException
    {
        logger.info( "Creating MD5 chescksum for file {}...", file );

        File checksumFile = new File( file.getParent(), format( "%s.md5", file.getName() ) );

        InputStream data = new FileInputStream( file );

        try
        {
            String md5 = md5Hex( data );
            write( checksumFile, md5 );
        }
        finally
        {
            closeQuietly( data );
        }

        logger.info( "MD5 chescksum stored to file {}", checksumFile );

        return checksumFile;
    }

    private static final class UploadTransferListener
        implements FTPDataTransferListener
    {

        private final Logger logger;

        private final File toBeUploaded;

        private long transferred = 0;

        public UploadTransferListener( Logger logger, File toBeUploaded )
        {
            this.logger = logger;
            this.toBeUploaded = toBeUploaded;
        }

        @Override
        public void aborted()
        {
            logger.warn( "File {} transfer aborted unexpectetly, contact the DSI OPS", toBeUploaded );
        }

        @Override
        public void completed()
        {
            logger.info( "File {} trasfer complete", toBeUploaded );
        }

        @Override
        public void failed()
        {
            logger.error( "File {} transfer corrupted, contact the DSI OPS", toBeUploaded );
        }

        @Override
        public void started()
        {
            logger.info( "Started trasferring file {}...", toBeUploaded );
        }

        @Override
        public void transferred( int transferred )
        {
            this.transferred += transferred;
            System.out.printf( "%s%%\r", ( ( this.transferred * 100 ) / toBeUploaded.length() ) );
        }

    }

}
