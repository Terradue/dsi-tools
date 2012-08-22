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

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.sun.jersey.api.client.UniformInterfaceException;

@Parameters( commandDescription = "Add a user to an account" )
public final class Authorize
    extends BaseTool
{

    public static void main( String[] args )
    {
        exit( new Authorize().execute( args ) );
    }

    @Parameter( description = "The user(s) account identificator(s)" )
    protected List<String> ids = new LinkedList<String>();

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.accounts" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    @Override
    public void execute()
        throws Exception
    {
        for ( String id : ids )
        {
            logger.info( "Authorizing user {}", id );

            try
            {
                restClient.resource( new StringBuilder( serviceUrl )
                                    .append( '/' )
                                    .append( id )
                                    .append( "/accountUsers" )
                                    .toString() )
                          .post();

                logger.info( "User {} successfully authorized", id );
            }
            catch ( UniformInterfaceException e )
            {
                logger.warn( "Impossible to autorize user {}: {}",
                             id, e.getResponse().getClientResponseStatus().getReasonPhrase() );
            }
        }
    }

}
