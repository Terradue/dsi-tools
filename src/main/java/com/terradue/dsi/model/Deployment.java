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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( FIELD )
@XmlRootElement( name = "deployment" )
public final class Deployment
{

    @XmlAttribute
    private int id;

    @XmlElement
    private boolean active;

    @XmlElement
    private int applianceId;

    @XmlElement
    private int cpuNumber;

    @XmlElement
    private String createdBy;

    @XmlElement
    private String creationDate;

    @XmlElement
    private int locationId;

    @XmlElement( name = "memSize" )
    private int memorySize;

    @XmlElement
    private String storageSize;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private String externalIpAddress;

    @XmlElement
    private String internalIpAddress;

    @XmlElement
    private String state;

    @XmlElement
    private String vncUri;

    @XmlElement
    private String vncPassword;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive( boolean active )
    {
        this.active = active;
    }

    public int getApplianceId()
    {
        return applianceId;
    }

    public void setApplianceId( int applianceId )
    {
        this.applianceId = applianceId;
    }

    public int getCpuNumber()
    {
        return cpuNumber;
    }

    public void setCpuNumber( int cpuNumber )
    {
        this.cpuNumber = cpuNumber;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate( String creationDate )
    {
        this.creationDate = creationDate;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId( int locationId )
    {
        this.locationId = locationId;
    }

    public int getMemorySize()
    {
        return memorySize;
    }

    public void setMemorySize( int memorySize )
    {
        this.memorySize = memorySize;
    }

    public String getStorageSize()
    {
        return storageSize;
    }

    public void setStorageSize( String storageSize )
    {
        this.storageSize = storageSize;
    }

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

    public String getExternalIpAddress()
    {
        return externalIpAddress;
    }

    public void setExternalIpAddress( String externalIpAddress )
    {
        this.externalIpAddress = externalIpAddress;
    }

    public String getInternalIpAddress()
    {
        return internalIpAddress;
    }

    public void setInternalIpAddress( String internalIpAddress )
    {
        this.internalIpAddress = internalIpAddress;
    }

    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    public String getVncUri()
    {
        return vncUri;
    }

    public void setVncUri( String vncUri )
    {
        this.vncUri = vncUri;
    }

    public String getVncPassword()
    {
        return vncPassword;
    }

    public void setVncPassword( String vncPassword )
    {
        this.vncPassword = vncPassword;
    }

}
