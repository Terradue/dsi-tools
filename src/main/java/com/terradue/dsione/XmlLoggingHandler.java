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

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.util.EntityUtils.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.xml.sax.SAXException;

final class XmlLoggingHandler
    implements ResponseHandler<Void>
{

    private final Class<?> mappingType;

    private final boolean headers;

    public XmlLoggingHandler( Class<?> mappingType, boolean headers )
    {
        this.mappingType = mappingType;
        this.headers = headers;
    }

    @Override
    public Void handleResponse( HttpResponse response )
        throws ClientProtocolException, IOException
    {
        if ( SC_OK != response.getStatusLine().getStatusCode() )
        {
            throw new ClientProtocolException( "impossible to read web service response, DSI server replied: "
                                               + response.getStatusLine().getReasonPhrase() );
        }

        try
        {
            newLoader( new FromAnnotationsRuleModule()
            {

                @Override
                protected void configureRules()
                {
                    bindRulesFrom( mappingType );
                }

            } )
            .setStackAction( new LoggingStackAction( headers ) )
            .newDigester()
            .parse( new ByteArrayInputStream( toByteArray( response.getEntity() ) ) );
        }
        catch ( SAXException e )
        {
            throw new IOException( "an error occurred while reading service reply: " + e.getMessage() );
        }

        return null;
    }

}
