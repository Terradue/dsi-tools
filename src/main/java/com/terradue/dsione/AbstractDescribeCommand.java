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

import static org.slf4j.LoggerFactory.getLogger;
import static java.beans.Introspector.getBeanInfo;
import static java.util.Collections.emptyList;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import org.slf4j.Logger;

import com.beust.jcommander.Parameter;
import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;

abstract class AbstractDescribeCommand<T>
    implements Command
{

    private final Logger logger = getLogger( getClass() );

    // CLI Parameters

    @Parameter( names = { "-H", "--headers" }, description = "Display column headers" )
    private boolean headers = false;

    @Parameter( arity = 1, description = "The image identification as returned by the upload command" )
    private List<String> id = emptyList();

    // injected

    @Inject
    private Client restClient;

    private String serviceUrl;

    public void setRestClient( Client restClient )
    {
        this.restClient = restClient;
    }

    public void setServiceUrl( String serviceUrl )
    {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public void execute()
        throws Exception
    {
        if ( !id.isEmpty() )
        {
            log( restClient.resource( new StringBuilder( serviceUrl )
                                     .append( '/' )
                                     .append( id.iterator().next() )
                                     .toString() )
                           .get( new GenericType<T>(){} ),
                 headers );
        }
        else
        {
            log( restClient.resource( serviceUrl ).get( new GenericType<List<T>>(){} ) );
        }
    }

    private void log( Collection<T> items )
        throws Exception
    {
        boolean first = true;

        for ( T item : items )
        {
            log( item, headers && first );

            if ( first )
            {
                first = false;
            }
        }
    }

    private void log( T item, boolean printHeaders )
        throws Exception
    {
        BeanInfo beanInfo = getBeanInfo( item.getClass() );

        if ( printHeaders )
        {
            Formatter header = new Formatter();

            for ( PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors() )
            {
                append( propertyDescriptor.getName(), header );
            }

            logger.info( header.toString() );
        }

        Formatter line = new Formatter();
        for ( PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors() )
        {
            append( propertyDescriptor.getReadMethod().invoke( item ), line );
        }

        logger.info( line.toString() );
    }

    private static <E> void append( E element, Formatter formatter )
    {
        formatter.format( "| %-32s %f |", element );
    }

}
