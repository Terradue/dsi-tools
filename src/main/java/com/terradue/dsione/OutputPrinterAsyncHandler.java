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

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;

final class OutputPrinterAsyncHandler
    implements AsyncHandler<Void>
{

    public void onThrowable( Throwable t )
    {
        throw new RuntimeException( "An error occurred during HTTP conversation: " + t.getMessage(), t );
    }

    public STATE onBodyPartReceived( HttpResponseBodyPart bodyPart )
        throws Exception
    {
        bodyPart.writeTo( System.out );
        return STATE.CONTINUE;
    }

    public STATE onStatusReceived( HttpResponseStatus responseStatus )
        throws Exception
    {
        if ( 200 != responseStatus.getStatusCode() )
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
        return null;
    }

}
