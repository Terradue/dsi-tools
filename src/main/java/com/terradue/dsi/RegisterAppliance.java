package com.terradue.dsi;

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

import static java.lang.System.exit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.GenericType;
import com.terradue.dsi.model.Deployment;
import com.terradue.dsi.model.DeploymentCreation;

@Parameters( commandDescription = "Register a previously uploaded image to be run." )
public final class RegisterAppliance
    extends BaseTool
{

    public static void main( String[] args )
    {
        exit( new RegisterAppliance().execute( args ) );
    }

    @Parameter( names = { "--appliance" }, description = "The DSI applicance ID" )
    private int applianceId;

    @Parameter( names = { "--name" }, description = "The deployment name" )
    private String name;

    @Parameter( names = { "--description" }, description = "The DSI deployment description" )
    private String description;

    @Parameter( names = { "--cluster" }, description = "The DSI deployment cluster ID" )
    private String deploymentClusterId;

    @Parameter( names = { "--perf" }, description = "The performance unit" )
    private int performanceUnit;

    @Parameter( names = { "--memory" }, description = "The memory (in Mb)" )
    private int memoryMb;

    @Parameter( names = { "--virtual-cpu" }, description = "The number of virtual CPUs" )
    private int virtualCPUs;

    @Parameter( names = { "--external-ip" }, description = "Flag to use the external IP" )
    private boolean useExternalIp;

    @Parameter( names = { "--permanent-ip" }, description = "Flag to use the permanent IP" )
    private boolean permanentIp;

    @Parameter( names = { "--network" }, description = "The DSI Network ID" )
    private String networkId;

    @Parameter( names = { "--users" }, description = "The users ID allowed to use the deployment" )
    private List<String> userDelegates = new ArrayList<String>();

    @Parameter( names = { "--end" }, description = "The end date (in yyyy-MM-ddThh:mm:ssZ format)" )
    private String endDate;

    @Parameter( names = { "--provider" }, description = "The DSI provider ID" )
    private String providerId;

    @Parameter( names = { "--qualifier" }, description = "The DSI qualifier ID" )
    private String qualifierId;

    @Parameter( names = { "--reservation" }, description = "The DSI reservation ID" )
    private String reservationId;

    @Inject
    @Named( "service.deployments" )
    private String deploymentsPath;

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.deployments" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    public void setDeploymentsPath( String deploymentsPath )
    {
        this.deploymentsPath = deploymentsPath;
    }

    @Override
    public void execute()
        throws Exception
    {
        logger.info( "Registering appliance {} ...", applianceId );
        DeploymentCreation deploymentCreation = new DeploymentCreation.Builder()
                                                .withApplianceId( applianceId )
                                                .withDeploymentClusterId( deploymentClusterId )
                                                .withDescription( description )
                                                .withEndDate( endDate )
                                                .withMemoryMb( memoryMb )
                                                .withName( name )
                                                .withNetwork( networkId )
                                                .withPerformanceUnit( performanceUnit )
                                                .withPermanentIp( permanentIp )
                                                .withProviderId( providerId )
                                                .withQualifierId( qualifierId )
                                                .withReservationId( reservationId )
                                                .withUseExternalIp( useExternalIp )
                                                .withUserDelegates( userDelegates )
                                                .withVirtualCPUs( virtualCPUs )
                                                .build();
        restClient.resource( serviceUrl ).post( deploymentCreation );

        Collection<Deployment> deployments = restClient.resource( deploymentsPath )
                                                       .get( new GenericType<Collection<Deployment>>(){} );

        for ( Deployment deployment : deployments )
        {
            if ( name.equals( deployment.getName() ) )
            {
                logger.info( "Deployment created with id: {}", deployment.getId() );
                return;
            }
        }
    }

}
