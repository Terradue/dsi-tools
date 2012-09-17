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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @since 0.2
 */
@XmlAccessorType( FIELD )
public final class AccountOwner
{

    @XmlElement
    private String id;

    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;

    @XmlElement
    private String email;

    @XmlElement
    private String login;

    @XmlElement
    private boolean locked;

    @XmlElement( name = "passwordSet" )
    private boolean pwdSet;

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
     * @return the firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail( String email )
    {
        this.email = email;
    }

    /**
     * @return the login
     */
    public String getLogin()
    {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin( String login )
    {
        this.login = login;
    }

    /**
     * @return the locked
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    public void setLocked( boolean locked )
    {
        this.locked = locked;
    }

    /**
     * @return the pwdSet
     */
    public boolean isPwdSet()
    {
        return pwdSet;
    }

    /**
     * @param pwdSet the pwdSet to set
     */
    public void setPwdSet( boolean pwdSet )
    {
        this.pwdSet = pwdSet;
    }

    @Override
    public String toString()
    {
        return login;
    }

}
