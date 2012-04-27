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

import static com.google.inject.Scopes.SINGLETON;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.google.inject.AbstractModule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;

public final class RestClientModule
    extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind( KeyManager[].class ).toProvider( KeyManagerProvider.class ).in( SINGLETON );
        bind( TrustManager[].class ).toProvider( TrustManagersProvider.class ).in( SINGLETON );
        bind( SSLContext.class ).toProvider( SSLContextProvider.class ).in( SINGLETON );
        bind( ClientConfig.class ).toProvider( RestClientConfigProvider.class ).in( SINGLETON );
        bind( Client.class ).toProvider( RestClientProvider.class ).in( SINGLETON );
    }

}
