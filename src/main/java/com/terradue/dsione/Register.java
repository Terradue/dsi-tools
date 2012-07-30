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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@MetaInfServices
@Parameters( commandDescription = "Register a previously uploaded image for use with an OpenNebula Cloud." )
public final class Register
    extends BaseTool
{

    @Parameter( names = { "-H", "--headers" }, description = "Display column headers" )
    private boolean headers = false;

    @Override
    public void execute()
        throws Exception
    {
        // TODO
    }

}
