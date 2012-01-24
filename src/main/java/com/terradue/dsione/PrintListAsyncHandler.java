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
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;

final class PrintListAsyncHandler
    implements AsyncHandler<Void>
{

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private final Digester digester;

    public PrintListAsyncHandler( final Class<?> mappingType, boolean header )
    {
        digester = newLoader( new FromAnnotationsRuleModule()
        {

            @Override
            protected void configureRules()
            {
                bindRulesFrom( mappingType );
            }

        } )
        .setStackAction( new LoggingStackAction( header ) )
        .newDigester();
    }

    public void onThrowable( Throwable t )
    {
        throw new RuntimeException( "An error occurred during HTTP conversation: " + t.getMessage(), t );
    }

    public STATE onBodyPartReceived( HttpResponseBodyPart bodyPart )
        throws Exception
    {
        bodyPart.writeTo( baos );
        return STATE.CONTINUE;
    }

    public STATE onStatusReceived( HttpResponseStatus responseStatus )
        throws Exception
    {
        if ( HTTP_OK != responseStatus.getStatusCode() )
        {
            return STATE.ABORT;
        }
        return STATE.CONTINUE;
    }

    public STATE onHeadersReceived( HttpResponseHeaders headers )
        throws Exception
    {
        return STATE.CONTINUE;
    }

    public Void onCompleted()
        throws Exception
    {
        baos.flush();
        baos.close();

        // FIXME that is not efficient
        digester.parse( new ByteArrayInputStream( baos.toByteArray() ) );
        return null;
    }

}
