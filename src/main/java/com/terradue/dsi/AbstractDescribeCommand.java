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

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

abstract class AbstractDescribeCommand
    extends BaseTool
{

    // CLI Parameters

    @Parameter( names = { "--headers" }, description = "Display column headers" )
    protected boolean headers = false;

    @Parameter( names = { "--fields" }, description = "Comma separated fields list that have to be shown" )
    private List<String> fields = getDefaultFields();

    @Parameter( description = "The image identificator(s) as returned by the upload command" )
    protected List<String> ids = new LinkedList<String>();

    protected abstract List<String> getDefaultFields();

    protected final <T> void log( Collection<T> items )
        throws Exception
    {
        if ( items.isEmpty() )
        {
            logger.info( "0 items found" );
            return;
        }

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

    protected final <T> void log( T item, boolean printHeaders )
        throws Exception
    {
        if ( printHeaders )
        {
            Formatter header = new Formatter();

            for ( String field : fields )
            {
                append( field, header );
            }

            logger.info( header.toString() );
        }

        Formatter line = new Formatter();
        for ( String field : fields )
        {
            try
            {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor( field, item.getClass() );
                append( propertyDescriptor.getReadMethod().invoke( item ), line );
            }
            catch ( Exception e )
            {
                append( "-- NA --", line );
            }
        }

        logger.info( line.toString() );
    }

    private static <E> void append( E element, Formatter formatter )
    {
        formatter.format( "| %-20s |", element );
    }

}
