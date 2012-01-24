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

import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.ning.http.client.AsyncHttpClient;

@Parameters( commandDescription = "List and describe running instances" )
public final class DescribeInstances
    implements Command
{

    @Parameter( names = { "-H", "--headers" }, description = "Display column headers" )
    private boolean headers = false;

    @Parameter( arity = 1, description = "The image identification as returned by the upload command" )
    private List<String> imageId = new LinkedList<String>();

    public void execute( OnDsiProgram mainSettings, AsyncHttpClient httpClient )
    {
        // TODO Auto-generated method stub

    }

}
