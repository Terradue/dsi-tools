package com.terradue.dsione.restclient;

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

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public final class SSLContextProvider
    implements Provider<SSLContext>
{

    private final KeyManager[] keyManagers;

    private final TrustManager[] trustManagers;

    @Inject
    public SSLContextProvider( KeyManager[] keyManagers, TrustManager[] trustManagers )
    {
        this.keyManagers = keyManagers;
        this.trustManagers = trustManagers;
    }

    @Override
    public SSLContext get()
    {
        try
        {
            SSLContext context = SSLContext.getInstance( "TLS" );
            context.init( keyManagers, trustManagers, null );
            return context;
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Impossible to initialize SSL context", e );
        }
    }

}
