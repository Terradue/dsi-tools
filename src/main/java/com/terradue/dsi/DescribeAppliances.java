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

import static java.util.Arrays.asList;

import static java.lang.System.exit;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.Collection;
import java.util.List;

import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.terradue.dsi.model.Appliance;

@Parameters( commandDescription = "List and describe available Appliances." )
public final class DescribeAppliances
    extends AbstractDescribeCommand
{

    public static void main( String[] args )
    {
        exit( new DescribeAppliances().execute( args ) );
    }

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.appliances" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    @Override
    protected List<String> getDefaultFields()
    {
        return asList( "id", "name", "description", "active", "architecture", "custom", "operatingSystem", "osId", "usageCount", "virtualMachineType", "deprecated" );
    }

    @Override
    public void execute()
        throws Exception
    {
        if ( !ids.isEmpty() )
        {
            boolean first = true;

            for ( String id : ids )
            {
                try
                {
                    log( restClient.resource( new StringBuilder( serviceUrl )
                                             .append( '/' )
                                             .append( id )
                                             .toString() )
                                   .get( Appliance.class ),
                         headers && first );

                    if ( first )
                    {
                        first = false;
                    }
                }
                catch ( UniformInterfaceException e )
                {
                    if ( HTTP_NOT_FOUND == e.getResponse().getClientResponseStatus().getStatusCode() )
                    {
                        logger.warn( "Image {} not found ", id );
                    }
                }
            }
        }
        else
        {
            log( restClient.resource( serviceUrl ).get( new GenericType<Collection<Appliance>>(){} ) );
        }
    }

}
