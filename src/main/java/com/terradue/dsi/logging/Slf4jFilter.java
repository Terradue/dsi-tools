package com.terradue.dsi.logging;

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

import static com.sun.jersey.api.client.ClientRequest.getHeaderValue;
import static com.sun.jersey.core.util.ReaderWriter.writeTo;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.sun.jersey.api.client.AbstractClientRequestAdapter;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientRequestAdapter;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public final class Slf4jFilter
    extends ClientFilter
{

    private final Logger logger = getLogger( getClass() );

    @Override
    public ClientResponse handle( ClientRequest request )
        throws ClientHandlerException
    {
        if ( logger.isDebugEnabled() )
        {
            logRequest( request );

            logger.debug( "" );
        }

        ClientResponse response = getNext().handle( request );

        if ( logger.isDebugEnabled() )
        {
            logResponse( response );
        }

        return response;
    }

    private void logRequest( ClientRequest request )
    {
        // log the request method + URL
        logger.debug( "> {} {}", request.getMethod(), request.getURI().toASCIIString() );

        // log the outcoming headers
        for ( Entry<String, List<Object>> e : request.getHeaders().entrySet() )
        {
            List<Object> val = e.getValue();
            String header = e.getKey();

            if ( val.size() == 1 )
            {
                logger.debug( "> {}: {}", header, getHeaderValue( val.get( 0 ) ) );
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                boolean add = false;
                for ( Object o : val )
                {
                    if ( add )
                    {
                        sb.append( ',' );
                    }
                    add = true;
                    sb.append( getHeaderValue( o ) );
                }
                logger.debug( "> {}: {}", header, sb.toString() );
            }
        }

        // logging the body
        logger.debug( ">" );
        if ( request.getEntity() != null )
        {
            request.setAdapter( new Adapter( request.getAdapter() ) );
        }
    }

    private void logResponse( ClientResponse response )
    {
        // log the request method + URL
        logger.debug( "< {} {}",
                      response.getClientResponseStatus().getStatusCode(),
                      response.getClientResponseStatus().getReasonPhrase() );

        // log incoming headers
        for ( Entry<String, List<String>> e : response.getHeaders().entrySet() )
        {
            String header = e.getKey();
            for ( String value : e.getValue() )
            {
                logger.debug( "< {}: {}", header, value );
            }
        }

        // logging the body
        logger.debug( "<" );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = response.getEntityInputStream();
        try
        {
            writeTo( in, out );

            byte[] requestEntity = out.toByteArray();
            printEntity( requestEntity );
            response.setEntityInputStream( new ByteArrayInputStream( requestEntity ) );
        }
        catch ( IOException ex )
        {
            throw new ClientHandlerException( ex );
        }
    }

    private void printEntity( byte[] entity )
        throws IOException
    {
        if ( entity.length == 0 )
        {
            return;
        }
        logger.debug( new String( entity ) );
    }

    private final class Adapter
        extends AbstractClientRequestAdapter
    {

        Adapter( ClientRequestAdapter cra )
        {
            super( cra );
        }

        public OutputStream adapt( ClientRequest request, OutputStream out )
            throws IOException
        {
            return new LoggingOutputStream( getAdapter().adapt( request, out ) );
        }

    }

    private final class LoggingOutputStream
        extends OutputStream
    {

        private final OutputStream out;

        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LoggingOutputStream( OutputStream out )
        {
            this.out = out;
        }

        @Override
        public void write( byte[] b )
            throws IOException
        {
            baos.write( b );
            out.write( b );
        }

        @Override
        public void write( byte[] b, int off, int len )
            throws IOException
        {
            baos.write( b, off, len );
            out.write( b, off, len );
        }

        @Override
        public void write( int b )
            throws IOException
        {
            baos.write( b );
            out.write( b );
        }

        @Override
        public void close()
            throws IOException
        {
            printEntity( baos.toByteArray() );
            out.close();
        }

    }

}
