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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters( commandDescription = "OpenNebula-DSI CLI tools" )
public final class OnDsiProgram
{

    @Parameter( names = { "-h", "--help" }, description = "Display help information." )
    private boolean printHelp;

    @Parameter( names = { "-v", "--version" }, description = "Display version information." )
    private boolean showVersion;

    @Parameter( names = { "-X", "--debug" }, description = "Produce execution debug output." )
    private boolean debug;

    @Parameter( names = { "-u", "--username" }, description = "The DSI account username" )
    private String username;

    @Parameter( names = { "-p", "--password" }, description = "The DSI account password", password = true )
    private String password;

    @Parameter( names = { "-U", "--uri" }, description = "The DSI web service URI" )
    private String serviceUri = "https://testcloud.t-systems.com/ZimoryManage/";

    public boolean isPrintHelp()
    {
        return printHelp;
    }

    public boolean isShowVersion()
    {
        return showVersion;
    }

    public boolean isDebug()
    {
        return debug;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getServiceUri()
    {
        return serviceUri;
    }

}
