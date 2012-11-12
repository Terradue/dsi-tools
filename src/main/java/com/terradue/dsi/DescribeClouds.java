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

import static javax.ws.rs.core.UriBuilder.fromUri;
import static java.lang.System.exit;
import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.GenericType;
import com.terradue.dsi.model.Cloud;

@Parameters( commandDescription = "List and describe DSI Clouds." )
public final class DescribeClouds
    extends AbstractDescribeCommand
{

    public static void main( String[] args )
    {
        exit( new DescribeClouds().execute( args ) );
    }

    @Parameter( names = { "--providerId" }, description = "filter the results by 'providerId' [OPTIONAL]" )
    private String providerId;

    @Parameter( names = { "--locationId" }, description = "filter the results by 'locationId' [OPTIONAL]" )
    private String locationId;

    @Parameter( names = { "--applianceId" }, description = "filter the results by 'applianceId' [OPTIONAL]" )
    private String applianceId;

    @Parameter( names = { "--qualityClassId" }, description = "filter the results by 'qualityClassId' [OPTIONAL]" )
    private String qualityClassId;

    @Parameter( names = { "--architecture" }, description = "filter the results by 'architecture' [OPTIONAL]" )
    private String architecture;

    @Parameter( names = { "--vmType" }, description = "filter the results by 'vmType' [OPTIONAL]" )
    private String vmType;

    @Parameter( names = { "--mem" }, description = "filter the results by 'mem' [OPTIONAL]" )
    private String mem;

    @Parameter( names = { "--cpu" }, description = "filter the results by 'cpu' [OPTIONAL]" )
    private String cpu;

    @Parameter( names = { "--group" }, description = "filter the results by 'group' [OPTIONAL]" )
    private String group;

    @Parameter( names = { "--perfomanceUnit" }, description = "filter the results by 'perfomanceUnit' [OPTIONAL]" )
    private String perfomanceUnit;

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.clouds" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

    @Override
    protected List<String> getDefaultFields()
    {
        return asList( "architecture", "locationId", "networkDownloadPerGB", "networkUploadPerGB", "providerId", "qualifierId", "qualityClassId", "runtimePerHour", "storagePerMonth", "vmType" );
    }

    @Override
    public void execute()
        throws Exception
    {
        UriBuilder uri = fromUri( serviceUrl );

        if ( providerId != null )
        {
            uri.queryParam( "providerId", providerId );
        }

        if ( locationId != null )
        {
            uri.queryParam( "locationId", locationId );
        }

        if ( applianceId != null )
        {
            uri.queryParam( "applianceId", applianceId );
        }

        if ( qualityClassId != null )
        {
            uri.queryParam( "qualityClassId", qualityClassId );
        }

        if ( architecture != null )
        {
            uri.queryParam( "architecture", architecture );
        }

        if ( vmType != null )
        {
            uri.queryParam( "vmType", vmType );
        }

        if ( mem != null )
        {
            uri.queryParam( "mem", mem );
        }

        if ( cpu != null )
        {
            uri.queryParam( "cpu", cpu );
        }

        if ( group != null )
        {
            uri.queryParam( "group", group );
        }

        if ( perfomanceUnit != null )
        {
            uri.queryParam( "perfomanceUnit", perfomanceUnit );
        }

        log( restClient.resource( uri.build() ).get( new GenericType<Collection<Cloud>>(){} ) );
    }

}
