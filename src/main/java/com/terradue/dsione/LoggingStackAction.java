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

import static java.beans.Introspector.getBeanInfo;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Formatter;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.StackAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LoggingStackAction
    implements StackAction
{

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    private final boolean headers;

    private boolean printHeaders = false;

    public LoggingStackAction( boolean header )
    {
        this.headers = header;
    }

    @Override
    public <T> T onPush( Digester d, String stackName, T o )
    {
        return o;
    }

    @Override
    public <T> T onPop( Digester d, String stackName, T o )
    {
        try
        {
            BeanInfo beanInfo = getBeanInfo( o.getClass() );

            if ( headers && !printHeaders )
            {
                Formatter headerFormatter = new Formatter();

                for ( PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors() )
                {
                    headerFormatter.format( "| %s |", descriptor.getName() );
                }

                logger.info( headerFormatter.toString() );
            }

            Formatter propertiesFormatter = new Formatter();

            for ( PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors() )
            {
                propertiesFormatter.format( "| %s |", descriptor.getReadMethod().invoke( o ) );
            }

            logger.info( propertiesFormatter.toString() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "An error occurred while analyzing retrieved data: "
                                        + e.getMessage());
        }

        return o;
    }

}
