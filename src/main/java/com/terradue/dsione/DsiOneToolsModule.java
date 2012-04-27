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

import static com.sun.jersey.api.client.Client.create;

import org.sonatype.spice.jersey.client.ahc.config.DefaultAhcConfig;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;

final class DsiOneToolsModule
    extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind( ClientConfig.class ).toProvider( RestClientConfigProvider.class );
        bind( Client.class ).toProvider( RestClientProvider.class );
    }

    public static final class RestClientConfigProvider
        implements Provider<ClientConfig>
    {

        @Override
        public ClientConfig get()
        {
            DefaultAhcConfig config = new DefaultAhcConfig();
            return config;
        }

    }

    public static final class RestClientProvider
        implements Provider<Client>
    {

        private final ClientConfig config;

        @Inject
        public RestClientProvider( ClientConfig config )
        {
            this.config = config;
        }

        @Override
        public Client get()
        {
            return create( config );
        }

    }

}
