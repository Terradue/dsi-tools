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
import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.terradue.dsione.model.UploadTicket;

@MetaInfServices
@Parameters( commandDescription = "Uploads an image for use with an OpenNebula Cloud" )
public final class Upload
    implements Command
{

    private final Logger logger = getLogger( getClass() );

    @Parameter( names = { "-a", "--appliance" }, description = "The DSI applicance name" )
    private String applianceName;

    @Parameter( names = { "-d", "--appliance-description" }, description = "The DSI applicance name" )
    private String applianceDescription;

    @Parameter( names = { "-P", "--provider" }, description = "The DSI provider ID" )
    private String providerId;

    @Parameter( names = { "-Q", "--qualifier" }, description = "The DSI qualifier ID" )
    private String qualifierId;

    @Parameter( names = { "-O", "--operating-system" }, description = "The DSI applicance Operating System (optional)" )
    private String applianceOS = "Linux";

    @Parameter( names = { "-u", "--username" }, description = "The DSI account username." )
    protected String username;

    @Parameter( names = { "-p", "--password" }, description = "The DSI account password.", password = true )
    protected String password;

    @Parameter( arity = 1, description = "Path to the image to upload", converter = FileConverter.class )
    private List<File> images = new LinkedList<File>();

    @Override
    public int execute()
        throws Exception
    {
        File image = images.get( 0 );

        if ( !image.exists() || image.isDirectory() )
        {
            throw new IllegalArgumentException( format( "File %s must be an existing file (directories not supported)",
                                                        image ) );
        }

        logger.info( "Requesting FTP location where uploading images..." );

        UploadTicket uploadTicket = null;
        /* UploadTicket uploadTicket = invokeGet( UploadTicket.class, "clouds/uploadTicket",
                                               new BasicNameValuePair( "providerId", providerId ),
                                               new BasicNameValuePair( "qualifierId", qualifierId ),
                                               new BasicNameValuePair( "applianceName", applianceName ),
                                               new BasicNameValuePair( "applianceDescription", applianceDescription ),
                                               new BasicNameValuePair( "applianceOS", applianceOS ) ); */

        logger.info( "Uploading image: {} on {} (expires on)...",
                     new String[]
                     {
                         image.getAbsolutePath(),
                         uploadTicket.getFtpLocation().toString(),
                         uploadTicket.getExpirationDate()
                     } );

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect( uploadTicket.getFtpLocation().getHost() );

        InputStream imageStream = null;
        try
        {
            imageStream = new FileInputStream( image );
            ftpClient.login( username, password );
            int reply = ftpClient.getReplyCode();

            if ( !isPositiveCompletion( reply ) )
            {
                throw new RuntimeException( uploadTicket.getFtpLocation() + " refused connection" );
            }

            if ( !ftpClient.changeWorkingDirectory( uploadTicket.getFtpLocation().getPath() ) )
            {
                throw new RuntimeException( "Impossible to access to "
                                            + uploadTicket.getFtpLocation().getPath()
                                            + " directory on "
                                            + uploadTicket.getFtpLocation().getHost()
                                            + ", contact the DSI support team" );
            }

            if ( ftpClient.storeUniqueFile( imageStream ) )
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
            ftpClient.logout();
            ftpClient.disconnect();

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

        return 0;
    }

}
