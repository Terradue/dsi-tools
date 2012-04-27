package com.terradue.dsione.model;

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

public final class Appliance
{

    private int id;

    private String architecture;

    private boolean custom;

    private String description;

    private String name;

    private String operatingSystem;

    private int usageCount;

    private String virtualMachineType;

    private boolean deprecated;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getArchitecture()
    {
        return architecture;
    }

    public void setArchitecture( String architecture )
    {
        this.architecture = architecture;
    }

    public boolean isCustom()
    {
        return custom;
    }

    public void setCustom( boolean custom )
    {
        this.custom = custom;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getOperatingSystem()
    {
        return operatingSystem;
    }

    public void setOperatingSystem( String operatingSystem )
    {
        this.operatingSystem = operatingSystem;
    }

    public int getUsageCount()
    {
        return usageCount;
    }

    public void setUsageCount( int usageCount )
    {
        this.usageCount = usageCount;
    }

    public String getVirtualMachineType()
    {
        return virtualMachineType;
    }

    public void setVirtualMachineType( String virtualMachineType )
    {
        this.virtualMachineType = virtualMachineType;
    }

    public boolean isDeprecated()
    {
        return deprecated;
    }

    public void setDeprecated( boolean deprecated )
    {
        this.deprecated = deprecated;
    }

}
