package com.terradue.dsione.model;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

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

@ObjectCreate( pattern = "deployments/deployment" )
public final class Deployment
{

    @SetProperty( pattern = "deployments/deployment", attributeName = "id" )
    private int id;

    @BeanPropertySetter( pattern = "deployments/deployment/active" )
    private boolean active;

    @BeanPropertySetter( pattern = "deployments/deployment/applianceId" )
    private int applianceId;

    @BeanPropertySetter( pattern = "deployments/deployment/cpuNumber" )
    private int cpuNumber;

    @BeanPropertySetter( pattern = "deployments/deployment/createdBy" )
    private String createdBy;

    @BeanPropertySetter( pattern = "deployments/deployment/locationId" )
    private int locationId;

    @BeanPropertySetter( pattern = "deployments/deployment/memSize" )
    private int memorySize;

    @BeanPropertySetter( pattern = "deployments/deployment/name" )
    private String name;

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

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

}
