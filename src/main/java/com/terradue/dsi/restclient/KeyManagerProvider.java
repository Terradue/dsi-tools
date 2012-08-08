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

import java.io.File;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import org.apache.commons.ssl.KeyMaterial;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;

public final class KeyManagerProvider
    implements Provider<KeyManager[]>
{

    private final File certificate;

    private final String password;

    @Inject
    public KeyManagerProvider( @Named( "user.certificate" ) File certificate,
                               @Named( "dsi.password" ) String password )
    {
        this.certificate = certificate;
        this.password = password;
    }

    @Override
    public KeyManager[] get()
    {
        final char[] password = this.password.toCharArray();

        try
        {
            final KeyStore store = new KeyMaterial( certificate, certificate, password ).getKeyStore();
            store.load( null, password );

            // initialize key and trust managers -> default behavior
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance( "SunX509" );
            // password for key and store have to be the same IIRC
            keyManagerFactory.init( store, password );

            return keyManagerFactory.getKeyManagers();
        }
        catch ( Exception e )
        {
            throw new ProvisionException( "Impossible to initialize SSL certificate/key", e );
        }
    }

}
