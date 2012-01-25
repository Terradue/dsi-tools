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

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.terradue.dsione.model.Appliance;

@Parameters( commandDescription = "List and describe previously uploaded images of a user to be used with an OpenNebula Cloud." )
public final class DescribeImages
    extends AbstractCommand
{

    public static void main( String[] args )
    {
        new DescribeImages().execute( args );
    }

    @Parameter( names = { "-H", "--headers" }, description = "Display column headers" )
    private boolean headers = false;

    @Parameter( arity = 1, description = "The image identification as returned by the upload command" )
    private List<String> imageId = new LinkedList<String>();

    @Override
    protected void execute()
        throws Exception
    {
        StringBuilder requestPath = new StringBuilder( "appliances" );
        if ( !imageId.isEmpty() )
        {
            requestPath.append( '/' ).append( imageId.iterator().next() );
        }

        URI serviceUrl = getQueryUri( requestPath.toString() );

        httpClient.execute( new HttpGet( serviceUrl ), new XmlLoggingHandler( Appliance.class, headers ) );
    }

}
