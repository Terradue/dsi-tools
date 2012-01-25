package com.terradue.dsione.model;

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

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

@ObjectCreate( pattern = "uploadTicket" )
public final class UploadTicket
{

    @BeanPropertySetter( pattern = "uploadTicket/expirationDate" )
    private String expirationDate;

    @BeanPropertySetter( pattern = "uploadTicket/ftpUploadURL" )
    private String ftpLocation;

    @BeanPropertySetter( pattern = "uploadTicket/qualifierId" )
    private String qualifierId;

    public String getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate( String expirationDate )
    {
        this.expirationDate = expirationDate;
    }

    public String getFtpLocation()
    {
        return ftpLocation;
    }

    public void setFtpLocation( String ftpLocation )
    {
        this.ftpLocation = ftpLocation;
    }

    public String getQualifierId()
    {
        return qualifierId;
    }

    public void setQualifierId( String qualifierId )
    {
        this.qualifierId = qualifierId;
    }

}
