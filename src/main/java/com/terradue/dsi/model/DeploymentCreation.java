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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( FIELD )
@XmlRootElement( name = "deploymentCreationInfo " )
public final class DeploymentCreation
{

    @XmlElement
    private int applianceId;

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private String deploymentClusterId;

    @XmlElement
    private int performanceUnit;

    @XmlElement
    private int memoryMb;

    @XmlElement
    private int virtualCPUs;

    @XmlElement
    private boolean useExternalIp;

    @XmlElement
    private boolean permanentIp;

    @XmlElement
    private Network network;

    @XmlElement
    private UserDelegates userDelegates;

    @XmlElement
    private String endDate;

    @XmlElement
    private String providerId;

    @XmlElement
    private String qualifierId;

    @XmlElement
    private String reservationId;

    /**
     * @return the applianceId
     */
    public int getApplianceId()
    {
        return applianceId;
    }

    /**
     * @param applianceId the applianceId to set
     */
    public void setApplianceId( int applianceId )
    {
        this.applianceId = applianceId;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * @return the deploymentClusterId
     */
    public String getDeploymentClusterId()
    {
        return deploymentClusterId;
    }

    /**
     * @param deploymentClusterId the deploymentClusterId to set
     */
    public void setDeploymentClusterId( String deploymentClusterId )
    {
        this.deploymentClusterId = deploymentClusterId;
    }

    /**
     * @return the performanceUnit
     */
    public int getPerformanceUnit()
    {
        return performanceUnit;
    }

    /**
     * @param performanceUnit the performanceUnit to set
     */
    public void setPerformanceUnit( int performanceUnit )
    {
        this.performanceUnit = performanceUnit;
    }

    /**
     * @return the memoryMb
     */
    public int getMemoryMb()
    {
        return memoryMb;
    }

    /**
     * @param memoryMb the memoryMb to set
     */
    public void setMemoryMb( int memoryMb )
    {
        this.memoryMb = memoryMb;
    }

    /**
     * @return the virtualCPUs
     */
    public int getVirtualCPUs()
    {
        return virtualCPUs;
    }

    /**
     * @param virtualCPUs the virtualCPUs to set
     */
    public void setVirtualCPUs( int virtualCPUs )
    {
        this.virtualCPUs = virtualCPUs;
    }

    /**
     * @return the useExternalIp
     */
    public boolean isUseExternalIp()
    {
        return useExternalIp;
    }

    /**
     * @param useExternalIp the useExternalIp to set
     */
    public void setUseExternalIp( boolean useExternalIp )
    {
        this.useExternalIp = useExternalIp;
    }

    /**
     * @return the permanentIp
     */
    public boolean isPermanentIp()
    {
        return permanentIp;
    }

    /**
     * @param permanentIp the permanentIp to set
     */
    public void setPermanentIp( boolean permanentIp )
    {
        this.permanentIp = permanentIp;
    }

    /**
     * @return the network
     */
    public Network getNetwork()
    {
        return network;
    }

    /**
     * @param network the network to set
     */
    public void setNetwork( Network network )
    {
        this.network = network;
    }

    /**
     * @return the userDelegates
     */
    public UserDelegates getUserDelegates()
    {
        return userDelegates;
    }

    /**
     * @param userDelegates the userDelegates to set
     */
    public void setUserDelegates( UserDelegates userDelegates )
    {
        this.userDelegates = userDelegates;
    }

    /**
     * @return the endDate
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
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
     * @return the reservationId
     */
    public String getReservationId()
    {
        return reservationId;
    }

    /**
     * @param reservationId the reservationId to set
     */
    public void setReservationId( String reservationId )
    {
        this.reservationId = reservationId;
    }

    public static final class Builder
    {

        private int applianceId;

        private String name;

        private String description;

        private String deploymentClusterId;

        private int performanceUnit;

        private int memoryMb;

        private int virtualCPUs;

        private boolean useExternalIp;

        private boolean permanentIp;

        private Network network;

        private UserDelegates userDelegates;

        private String endDate;

        private String providerId;

        private String qualifierId;

        private String reservationId;

        /**
         * @param applianceId the applianceId to set
         */
        public Builder withApplianceId( int applianceId )
        {
            this.applianceId = applianceId;
            return this;
        }

        /**
         * @param name the name to set
         */
        public Builder withName( String name )
        {
            this.name = name;
            return this;
        }

        /**
         * @param description the description to set
         */
        public Builder withDescription( String description )
        {
            this.description = description;
            return this;
        }

        /**
         * @param deploymentClusterId the deploymentClusterId to set
         */
        public Builder withDeploymentClusterId( String deploymentClusterId )
        {
            this.deploymentClusterId = deploymentClusterId;
            return this;
        }

        /**
         * @param performanceUnit the performanceUnit to set
         */
        public Builder withPerformanceUnit( int performanceUnit )
        {
            this.performanceUnit = performanceUnit;
            return this;
        }

        /**
         * @param memoryMb the memoryMb to set
         */
        public Builder withMemoryMb( int memoryMb )
        {
            this.memoryMb = memoryMb;
            return this;
        }

        /**
         * @param virtualCPUs the virtualCPUs to set
         */
        public Builder withVirtualCPUs( int virtualCPUs )
        {
            this.virtualCPUs = virtualCPUs;
            return this;
        }

        /**
         * @param useExternalIp the useExternalIp to set
         */
        public Builder withUseExternalIp( boolean useExternalIp )
        {
            this.useExternalIp = useExternalIp;
            return this;
        }

        /**
         * @param permanentIp the permanentIp to set
         */
        public Builder withPermanentIp( boolean permanentIp )
        {
            this.permanentIp = permanentIp;
            return this;
        }

        /**
         * @param networkId the network to set
         */
        public Builder withNetwork( String networkId )
        {
            Network network = new Network();
            network.setId( networkId );
            this.network = network;
            return this;
        }

        /**
         * @param userDelegates the userDelegates to set
         */
        public Builder withUserDelegates( List<String> usersId )
        {
            List<AccountUser> accountUsers = new ArrayList<AccountUser>( usersId.size() );
            for ( String userId : usersId )
            {
                AccountUser accountUser = new AccountUser();
                accountUser.setId( userId );
                accountUsers.add( accountUser );
            }

            UserDelegates userDelegates = new UserDelegates();
            userDelegates.setAccountUser( accountUsers );
            this.userDelegates = userDelegates;
            return this;
        }

        /**
         * @param endDate the endDate to set
         */
        public Builder withEndDate( String endDate )
        {
            this.endDate = endDate;
            return this;
        }

        /**
         * @param providerId the providerId to set
         */
        public Builder withProviderId( String providerId )
        {
            this.providerId = providerId;
            return this;
        }

        /**
         * @param qualifierId the qualifierId to set
         */
        public Builder withQualifierId( String qualifierId )
        {
            this.qualifierId = qualifierId;
            return this;
        }

        /**
         * @param reservationId the reservationId to set
         */
        public Builder withReservationId( String reservationId )
        {
            this.reservationId = reservationId;
            return this;
        }

        public DeploymentCreation build()
        {
            DeploymentCreation deploymentCreation = new DeploymentCreation();
            deploymentCreation.setApplianceId( applianceId );
            deploymentCreation.setName( name );
            deploymentCreation.setDescription( description );
            deploymentCreation.setDeploymentClusterId( deploymentClusterId );
            deploymentCreation.setPerformanceUnit( performanceUnit );
            deploymentCreation.setMemoryMb( memoryMb );
            deploymentCreation.setVirtualCPUs( virtualCPUs );
            deploymentCreation.setUseExternalIp( useExternalIp );
            deploymentCreation.setPermanentIp( permanentIp );
            deploymentCreation.setNetwork( network );
            deploymentCreation.setUserDelegates( userDelegates );
            deploymentCreation.setEndDate( endDate );
            deploymentCreation.setProviderId( providerId );
            deploymentCreation.setQualifierId( qualifierId );
            deploymentCreation.setReservationId( reservationId );
            return deploymentCreation;
        }

    }

}
