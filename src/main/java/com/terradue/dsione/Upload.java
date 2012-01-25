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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

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

        logger.info( "Uploading image: {}...", image );

        /*
         * try { httpClient.executeRequest( new RequestBuilder( "GET" ) .setUrl( mainSettings.getServiceUri() +
         * "services/api/clouds/uploadTicket" ) .addQueryParameter( "providerId", providerId ) .addQueryParameter(
         * "qualifierId", qualifierId ) .addQueryParameter( "applianceName", applianceName ) .addQueryParameter(
         * "applianceDescription", applianceDescription ) .build() ); } catch ( IOException e ) { throw new
         * RuntimeException( "An error occurred while uploading the image: " + e.getMessage(), e ); }
         */
    }

}
