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
import static java.lang.String.format;
import static java.lang.System.exit;
import static javax.ws.rs.core.UriBuilder.fromUri;
import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.net.ftp.FTPSClient;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.terradue.dsi.model.UploadTicket;
import com.terradue.dsi.wire.FTPSClientProvider;

@Parameters( commandDescription = "Uploads an image on DSI Cloud" )
public final class UploadImage
    extends BaseTool
{

    public static void main( String[] args )
    {
        exit( new UploadImage().execute( args ) );
    }

    @Parameter( names = { "--appliance" }, description = "The DSI applicance name" )
    private String applianceName;

    @Parameter( names = { "--description" }, description = "The DSI applicance name" )
    private String applianceDescription;

    @Parameter( names = { "--provider" }, description = "The DSI provider ID" )
    private String providerId;

    @Parameter( names = { "--qualifier" }, description = "The DSI qualifier ID" )
    private String qualifierId;

    @Parameter( names = { "--operating-system" }, description = "The DSI applicance Operating System (optional)" )
    private String applianceOS = "Linux";

    @Parameter( names = { "--image" }, description = "Path to the image to upload", converter = FileConverter.class )
    private File image;

    @Inject
    private FTPSClient ftpsClient;

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.upload" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    public void setFtpsClient( FTPSClient ftpsClient )
    {
        this.ftpsClient = ftpsClient;
    }

    @Override
    protected void bindConfigurations()
    {
        super.bindConfigurations();
        bind( FTPSClient.class ).toProvider( FTPSClientProvider.class ).in( SINGLETON );
    }

    @Override
    public void execute()
        throws Exception
    {
        if ( !image.exists() || image.isDirectory() )
        {
            throw new IllegalArgumentException( format( "File %s must be an existing file (directories not supported)",
                                                        image ) );
        }

        logger.info( "Requesting FTP location where uploading images..." );

        UploadTicket uploadTicket = restClient.resource( fromUri( serviceUrl )
                                                         .queryParam( "providerId", providerId )
                                                         .queryParam( "qualifierId", qualifierId )
                                                         .queryParam( "applianceName", applianceName )
                                                         .queryParam( "applianceDescription", applianceDescription )
                                                         .queryParam( "applianceOS", applianceOS )
                                                         .build() ).get( UploadTicket.class );

        logger.info( "Uploading image: {} on {} (expires on {})...",
                     new Object[]
                     {
                         image.getAbsolutePath(),
                         uploadTicket.getFtpLocation().toString(),
                         uploadTicket.getExpirationDate()
                     } );

        logger.info( "Connecting to {}...", uploadTicket.getFtpLocation().getHost() );

        ftpsClient.connect( uploadTicket.getFtpLocation().getHost() );

        logger.info( "Connection extabilished!" );

        InputStream imageStream = null;
        try
        {
            imageStream = new FileInputStream( image );
            int reply = ftpsClient.getReplyCode();

            if ( !isPositiveCompletion( reply ) )
            {
                throw new RuntimeException( uploadTicket.getFtpLocation() + " refused connection" );
            }

            if ( !ftpsClient.changeWorkingDirectory( uploadTicket.getFtpLocation().getPath() ) )
            {
                throw new RuntimeException( format( "Impossible to access to %s directory on %s, contact the DSI OPS",
                                                    uploadTicket.getFtpLocation().getPath(),
                                                    uploadTicket.getFtpLocation().getHost() ) );
            }

            logger.info( "Storing image..." );

            if ( ftpsClient.storeUniqueFile( imageStream ) )
            {
                logger.info( "Image {} successfully stored", image );
            }
            else
            {
                throw new RuntimeException( "Impossible to store the image, contact the DSI support team" );
            }

        }
        finally
        {
            ftpsClient.disconnect();

            if ( imageStream != null )
            {
                try
                {
                    imageStream.close();
                }
                catch ( IOException e )
                {
                    // close quietly
                }
            }
        }
    }

}
