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

@Parameters( commandDescription = "Terminate the selected running instance" )
public final class TerminateInstances
    extends AbstractCommand
{

    public static void main( String[] args )
    {
        new TerminateInstances().execute( args );
    }

    @Parameter( arity = 1, description = "The instance identification as returned by the run-instances command" )
    private List<String> imageId = new LinkedList<String>();

    @Override
    protected void execute()
    {
        // TODO Auto-generated method stub
    }

}
