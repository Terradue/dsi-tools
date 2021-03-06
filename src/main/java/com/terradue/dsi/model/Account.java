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

import static java.lang.String.format;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @since 0.2
 */
@XmlAccessorType( FIELD )
@XmlRootElement( name = "account" )
public final class Account
{

    @XmlElement( name = "accountId" )
    private String id;

    @XmlElement( name = "accountName" )
    private String name;

    @XmlElement( name = "userAccountOwner" )
    private AccountUser owner;

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId( String id )
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the owner
     */
    public AccountUser getOwner()
    {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner( AccountUser owner )
    {
        this.owner = owner;
    }

    @Override
    public String toString()
    {
        return format( "%s:%s", id, owner );
    }

}
