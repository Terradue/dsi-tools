package com.terradue.dsione;

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

import org.kohsuke.MetaInfServices;

import com.beust.jcommander.Parameters;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.terradue.dsione.model.Deployment;

@MetaInfServices( value = Command.class )
@Parameters( commandNames = "desinst", commandDescription = "List and describe running instances" )
public final class DescribeInstances
    extends AbstractDescribeCommand<Deployment>
{

    @Inject
    @Override
    public void setServiceUrl( @Named( "service.deployments" ) String serviceUrl )
    {
        super.setServiceUrl( serviceUrl );
    }

}
