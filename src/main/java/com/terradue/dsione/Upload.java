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
import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.util.EntityUtils.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.terradue.dsione.model.UploadTicket;

@Parameters( commandDescription = "Uploads an image for use with an OpenNebula Cloud" )
public final class Upload
    extends AbstractCommand
{

    public static void main( String[] args )
    {
        new Upload().execute( args );
    }

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

    @Parameter( arity = 1, description = "Path to the image to upload", converter = FileConverter.class )
    private List<File> images = new LinkedList<File>();

    @Override
    protected void execute()
        throws Exception
    {
        File image = images.get( 0 );

        if ( !image.exists() || image.isDirectory() )
        {
            throw new IllegalArgumentException( format( "File %s must be an existing file (directories not supported)",
                                                        image ) );
        }

        logger.info( "Requesting FTP location where uploading images..." );

        URI serviceUrl = getQueryUri( "clouds/uploadTicket",
                                      new BasicNameValuePair( "providerId", providerId ),
                                      new BasicNameValuePair( "qualifierId", qualifierId ),
                                      new BasicNameValuePair( "applianceName", applianceName ),
                                      new BasicNameValuePair( "applianceDescription", applianceDescription ),
                                      new BasicNameValuePair( "applianceOS", applianceOS ) );

        HttpResponse response = httpClient.execute( new HttpGet( serviceUrl ) );

        if ( SC_OK != response.getStatusLine().getStatusCode() )
        {
            throw new ClientProtocolException( "impossible to read web service response, DSI server replied: "
                                               + response.getStatusLine().getReasonPhrase() );
        }

        UploadTicket uploadTicket = newLoader( new FromAnnotationsRuleModule()
        {

            @Override
            protected void configureRules()
            {
                bindRulesFrom( UploadTicket.class );
            }

        } )
        .newDigester()
        .parse( new ByteArrayInputStream( toByteArray( response.getEntity() ) ) );

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
            // ftpClient.login( username, password ) TODO does FTP requires a login?
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
            // ftpClient.logout();
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
    }

}
