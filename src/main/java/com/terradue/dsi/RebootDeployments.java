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

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters( commandDescription = "Reboot the specified Deployment(s)." )
public final class RebootDeployments
    extends BaseTool
{

    public static void main( String[] args )
    {
        exit( new RebootDeployments().execute( args ) );
    }

    @Parameter( description = "The image identificator(s) as returned by the upload command" )
    protected List<String> ids = new LinkedList<String>();

    @Inject
    private StopDeployments stopInstances;

    @Inject
    private StartDeployments startInstances;

    public void setStopInstances( StopDeployments stopInstances )
    {
        this.stopInstances = stopInstances;
    }

    public void setStartInstances( StartDeployments startInstances )
    {
        this.startInstances = startInstances;
    }

    @Override
    public void execute()
        throws Exception
    {
        for ( String id : ids )
        {
            if ( stopInstances.stopInstance( id ) )
            {
                startInstances.startInstance( id );
            }
        }
    }

}
