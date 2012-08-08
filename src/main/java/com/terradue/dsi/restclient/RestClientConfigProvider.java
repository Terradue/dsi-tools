package com.terradue.dsi.restclient;

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

import javax.net.ssl.SSLContext;

import org.sonatype.spice.jersey.client.ahc.config.DefaultAhcConfig;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ning.http.client.resumable.ResumableIOExceptionFilter;
import com.sun.jersey.api.client.config.ClientConfig;

public final class RestClientConfigProvider
    implements Provider<ClientConfig>
{

    private final SSLContext context;

    @Inject
    public RestClientConfigProvider( SSLContext context )
    {
        this.context = context;
    }

    @Override
    public ClientConfig get()
    {
        DefaultAhcConfig config = new DefaultAhcConfig();
        config.getAsyncHttpClientConfigBuilder()
              .setRequestTimeoutInMs( 45 * 60 * 60 * 1000 ) // 45 minutes
              .setAllowPoolingConnection( true )
              .addIOExceptionFilter( new ResumableIOExceptionFilter() )
              .setMaximumConnectionsPerHost( 10 )
              .setMaximumConnectionsTotal( 100 )
              .setFollowRedirects( true )
              .setSSLContext( context );
        return config;
    }

}
