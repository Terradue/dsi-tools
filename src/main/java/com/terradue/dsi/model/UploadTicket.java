package com.terradue.dsi.model;

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

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( FIELD )
@XmlRootElement( name = "uploadTicket" )
public final class UploadTicket
{

    @XmlElement( name = "expirationDate" )
    private String expirationDate;

    @XmlElement( name = "ftpUploadURL" )
    private URI ftpLocation;

    @XmlElement( name = "qualifierId" )
    private String qualifierId;

    public String getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate( String expirationDate )
    {
        this.expirationDate = expirationDate;
    }

    public URI getFtpLocation()
    {
        return ftpLocation;
    }

    public void setFtpLocation( URI ftpLocation )
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
