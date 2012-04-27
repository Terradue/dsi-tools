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

import static com.google.inject.Scopes.SINGLETON;
import static com.sun.jersey.api.client.Client.create;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
        bind( X509TrustManager.class ).toProvider( X509TrustManagerProvider.class ).in( SINGLETON );
        bind( ClientConfig.class ).toProvider( RestClientConfigProvider.class ).in( SINGLETON );
        bind( Client.class ).toProvider( RestClientProvider.class ).in( SINGLETON );
    }

    public static final class X509TrustManagerProvider
        implements Provider<X509TrustManager>
    {

        @Override
        public X509TrustManager get()
        {
            return new X509TrustManager()
            {

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted( X509Certificate[] chain, String authType )
                    throws CertificateException
                {
                    // do nothing
                }

                @Override
                public void checkClientTrusted( X509Certificate[] chain, String authType )
                    throws CertificateException
                {
                    // do nothing
                }

            };
        }

    }

    public static final class SSLContextProvider
        implements Provider<SSLContext>
    {

        private final X509TrustManager trustManager;

        @Inject
        public SSLContextProvider( X509TrustManager trustManager )
        {
            this.trustManager = trustManager;
        }

        @Override
        public SSLContext get()
        {
            try
            {
                SSLContext context = SSLContext.getInstance( "TLS" );
                context.init( new KeyManager[] {}, new TrustManager[] { trustManager }, null );
                return context;
            }
            catch ( Exception e )
            {
                throw new IllegalStateException( "Impossible to initialize SSL context", e );
            }
        }

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
