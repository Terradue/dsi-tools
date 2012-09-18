package com.terradue.dsi.model;

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

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @since 0.2
 */
@XmlAccessorType( FIELD )
@XmlRootElement( name = "networkStorageCreationInfoMPO" )
public final class NetworkStorageCreation
{

    @XmlElement( name = "networkStorageName" )
    private String name;

    @XmlElement( name = "networkStorageDescription" )
    private String description;

    @XmlElement( name = "networkStorageProvider" )
    private String provider;

    @XmlElement( name = "networkStorageSizeGb" )
    private int size;

    @XmlElement( name = "networkStorageExternalProtocol" )
    private String externalProtocol;

    @XmlElement( name = "network" )
    private Network network;

    @XmlElement( name = "providerId" )
    private String providerId;

    @XmlElement( name = "qualifierId" )
    private String qualifierId;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider( String provider )
    {
        this.provider = provider;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize( int size )
    {
        this.size = size;
    }

    public String getExternalProtocol()
    {
        return externalProtocol;
    }

    public void setExternalProtocol( String externalProtocol )
    {
        this.externalProtocol = externalProtocol;
    }

    public Network getNetwork()
    {
        return network;
    }

    public void setNetwork( Network network )
    {
        this.network = network;
    }

    public String getProviderId()
    {
        return providerId;
    }

    public void setProviderId( String providerId )
    {
        this.providerId = providerId;
    }

    public String getQualifierId()
    {
        return qualifierId;
    }

    public void setQualifierId( String qualifierId )
    {
        this.qualifierId = qualifierId;
    }

    public static final class Builder
    {

        private final NetworkStorageCreation networkStorage = new NetworkStorageCreation();

        public Builder()
        {
            networkStorage.setNetwork( new Network() );
        }

        public Builder setDescription( String description )
        {
            networkStorage.setDescription( description );
            return this;
        }

        public Builder setExternalProtocol( String externalProtocol )
        {
            networkStorage.setExternalProtocol( externalProtocol );
            return this;
        }

        public Builder setName( String name )
        {
            networkStorage.setName( name );
            return this;
        }

        public Builder setProvider( String provider )
        {
            networkStorage.setProvider( provider );
            return this;
        }

        public Builder setProviderId( String providerId )
        {
            networkStorage.setProviderId( providerId );
            return this;
        }

        public Builder setQualifierId( String qualifierId )
        {
            networkStorage.setQualifierId( qualifierId );
            return this;
        }

        public Builder setSize( int size )
        {
            networkStorage.setSize( size );
            return this;
        }

        public Builder setNetworkId( String networkId )
        {
            networkStorage.getNetwork().setId( networkId );
            return this;
        }

        public NetworkStorageCreation build()
        {
            return networkStorage;
        }

    }

}
