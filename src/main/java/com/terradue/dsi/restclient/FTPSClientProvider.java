package com.terradue.dsi.restclient;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.net.ssl.SSLContext;

import org.apache.commons.net.ftp.FTPSClient;

public final class FTPSClientProvider
    implements Provider<FTPSClient>
{

    @Inject
    private SSLContext sslContext;

    public void setSslContext( SSLContext sslContext )
    {
        this.sslContext = sslContext;
    }

    @Override
    public FTPSClient get()
    {
        return new FTPSClient( true, sslContext );
    }

}
