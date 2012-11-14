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

import javax.inject.Inject;
import javax.inject.Named;

import com.beust.jcommander.Parameters;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.terradue.dsi.model.AccountUser;

/**
 * @since 0.2
 */
@Parameters( commandDescription = "List and describe users." )
public final class DescribeUsers
    extends AbstractDescribeCommand
{

    public static void main( String[] args )
    {
        exit( new DescribeUsers().execute( args ) );
    }

    @Override
    @Inject
    public void setServiceUrl( @Named( "service.accounts" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    @Override
    protected List<String> getDefaultFields()
    {
        return asList( "id", "login", "firstName", "lastName", "email", "locked", "passwordSet" );
    }

    @Override
    protected void execute()
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
                                   .get( AccountUser.class ),
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
                        logger.warn( "User {} not found ", id );
                    }
                }
            }
        }
        else
        {
            log( restClient.resource( serviceUrl ).get( new GenericType<Collection<AccountUser>>(){} ) );
        }
    }

}
