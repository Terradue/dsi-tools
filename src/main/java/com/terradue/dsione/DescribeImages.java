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

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.Collection;

import org.kohsuke.MetaInfServices;

import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.terradue.dsione.model.Appliance;

@MetaInfServices( value = Command.class )
@Parameters( commandNames = "desimg", commandDescription = "List and describe previously uploaded images of a user to be used with an OpenNebula Cloud." )
public final class DescribeImages
    extends AbstractDescribeCommand
{

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.appliances" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    @Override
    public void execute()
        throws Exception
    {
        if ( !id.isEmpty() )
        {
            try
            {
                log( restClient.resource( new StringBuilder( serviceUrl )
                                         .append( '/' )
                                         .append( id.iterator().next() )
                                         .toString() )
                               .get( Appliance.class ),
                     headers );
            }
            catch ( UniformInterfaceException e )
            {
                if ( HTTP_NOT_FOUND == e.getResponse().getClientResponseStatus().getStatusCode() )
                {
                    logger.info( "Image {} not found ", id.iterator().next() );
                }
            }
        }
        else
        {
            log( restClient.resource( serviceUrl ).get( new GenericType<Collection<Appliance>>(){} ) );
        }
    }

}
