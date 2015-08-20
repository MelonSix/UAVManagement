/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mSecurity;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mServer;

/**
 *
 * @author AG BRIGHTER
 */
public class RegisteredClientData
{
    private OmaLwM2mSecurity lwM2mSecurity;
    private OmaLwM2mServer lwM2mServer;
    
    public RegisteredClientData() {
    }

    public OmaLwM2mSecurity getLwM2mSecurity() {
        return lwM2mSecurity;
    }

    public void setLwM2mSecurity(OmaLwM2mSecurity lwM2mSecurity) {
        this.lwM2mSecurity = lwM2mSecurity;
    }

    public OmaLwM2mServer getLwM2mServer() {
        return lwM2mServer;
    }

    public void setLwM2mServer(OmaLwM2mServer lwM2mServer) {
        this.lwM2mServer = lwM2mServer;
    }
    
    
}
