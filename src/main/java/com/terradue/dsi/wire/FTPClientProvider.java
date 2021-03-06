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

import it.sauronsoftware.ftp4j.FTPClient;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.net.ssl.SSLContext;

public final class FTPClientProvider
    implements Provider<FTPClient>
{

    @Inject
    private SSLContext sslContext;

    public void setSslContext( SSLContext sslContext )
    {
        this.sslContext = sslContext;
    }

    @Override
    public FTPClient get()
    {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setSSLSocketFactory( sslContext.getSocketFactory() );
        return ftpClient;
    }

}
