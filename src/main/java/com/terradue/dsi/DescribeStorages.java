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

import static java.lang.System.exit;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.List;

import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.terradue.dsi.model.NetworkStorage;

/**
 * @since 0.2
 */
@Parameters( commandDescription = "List and describe available Network Storages" )
public final class DescribeStorages
    extends AbstractDescribeCommand
{

    public static void main( String[] args )
    {
        exit( new DescribeStorages().execute( args ) );
    }

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.storages" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    @Override
    protected List<String> getDefaultFields()
    {
        return asList( "id", "name", "description", "provider", "size", "exportProtocol", "exportUrl", "network" );
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
                                   .get( NetworkStorage.class ),
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
                        logger.warn( "Network Storage {} not found ", id );
                    }
                }
            }
        }
        else
        {
            log( restClient.resource( serviceUrl ).get( new GenericType<Collection<NetworkStorage>>(){} ) );
        }
    }

}
