package com.terradue.dsi.wire;

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

import static java.lang.Runtime.getRuntime;
import static org.sonatype.spice.jersey.client.ahc.AhcHttpClient.create;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.terradue.dsi.logging.Slf4jFilter;

public final class RestClientProvider
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
        final Client client = create( config );
        client.addFilter( new Slf4jFilter() );

        getRuntime().addShutdownHook( new Thread()
        {

            public void run()
            {
                client.destroy();
            }

        } );

        return client;
    }

}
