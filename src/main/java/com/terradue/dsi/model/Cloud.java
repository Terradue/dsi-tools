package com.terradue.dsi.model;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

@XmlAccessorType( FIELD )
@XmlRootElement( name = "cloud" )
public final class Cloud
{

    @XmlElement
    private String architecture;

    @XmlElement
    private String locationId;

    @XmlElement
    private String networkDownloadPerGB;

    @XmlElement
    private String networkUploadPerGB;

    @XmlElement
    private String providerId;

    @XmlElement
    private String qualifierId;

    @XmlElement
    private String qualityClassId;

    @XmlElement
    private String runtimePerHour;

    @XmlElement
    private String storagePerMonth;

    @XmlElement
    private String vmType;

    /**
     * @return the architecture
     */
    public String getArchitecture()
    {
        return architecture;
    }

    /**
     * @param architecture the architecture to set
     */
    public void setArchitecture( String architecture )
    {
        this.architecture = architecture;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId( String locationId )
    {
        this.locationId = locationId;
    }

    /**
     * @return the networkDownloadPerGB
     */
    public String getNetworkDownloadPerGB()
    {
        return networkDownloadPerGB;
    }

    /**
     * @param networkDownloadPerGB the networkDownloadPerGB to set
     */
    public void setNetworkDownloadPerGB( String networkDownloadPerGB )
    {
        this.networkDownloadPerGB = networkDownloadPerGB;
    }

    /**
     * @return the networkUploadPerGB
     */
    public String getNetworkUploadPerGB()
    {
        return networkUploadPerGB;
    }

    /**
     * @param networkUploadPerGB the networkUploadPerGB to set
     */
    public void setNetworkUploadPerGB( String networkUploadPerGB )
    {
        this.networkUploadPerGB = networkUploadPerGB;
    }

    /**
     * @return the providerId
     */
    public String getProviderId()
    {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId( String providerId )
    {
        this.providerId = providerId;
    }

    /**
     * @return the qualifierId
     */
    public String getQualifierId()
    {
        return qualifierId;
    }

    /**
     * @param qualifierId the qualifierId to set
     */
    public void setQualifierId( String qualifierId )
    {
        this.qualifierId = qualifierId;
    }

    /**
     * @return the qualityClassId
     */
    public String getQualityClassId()
    {
        return qualityClassId;
    }

    /**
     * @param qualityClassId the qualityClassId to set
     */
    public void setQualityClassId( String qualityClassId )
    {
        this.qualityClassId = qualityClassId;
    }

    /**
     * @return the runtimePerHour
     */
    public String getRuntimePerHour()
    {
        return runtimePerHour;
    }

    /**
     * @param runtimePerHour the runtimePerHour to set
     */
    public void setRuntimePerHour( String runtimePerHour )
    {
        this.runtimePerHour = runtimePerHour;
    }

    /**
     * @return the storagePerMonth
     */
    public String getStoragePerMonth()
    {
        return storagePerMonth;
    }

    /**
     * @param storagePerMonth the storagePerMonth to set
     */
    public void setStoragePerMonth( String storagePerMonth )
    {
        this.storagePerMonth = storagePerMonth;
    }

    /**
     * @return the vmType
     */
    public String getVmType()
    {
        return vmType;
    }

    /**
     * @param vmType the vmType to set
     */
    public void setVmType( String vmType )
    {
        this.vmType = vmType;
    }

}
