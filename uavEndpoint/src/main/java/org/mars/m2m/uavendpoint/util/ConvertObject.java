/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.util;

import org.eclipse.leshan.server.bootstrap.BootstrapConfig.ServerConfig;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig.ServerSecurity;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mSecurity;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mServer;

/**
 * This class copies the values from the created instances of {@link BootstrapConfig} details to customs ones
 * @author AG BRIGHTER
 */
public class ConvertObject 
{

    public ConvertObject() {
    }
    
    /**
     * Converts the {@link ServerSecurity} instance to an equivalent class.<br/>
     * The rational is to have a custom method outside the API's {@link ServerSecurity} instance so that other
     * custom operations can be done or it can be extended.
     * @param s The {@link ServerSecurity} instance/object
     * @return The converted {@link OmaLwM2mSecurity} instance/object
     */
    public static OmaLwM2mSecurity toLwM2mSecurity(ServerSecurity s)
    {
        return new OmaLwM2mSecurity()//The OMA resource IDs
                .setUri(s.uri)//0
                .setBootstrapServer(s.bootstrapServer)//1
                .setSecurityMode(s.securityMode)//2
                .setPublicKeyOrId(s.publicKeyOrId)//3
                .setServerPublicKeyOrId(s.serverPublicKeyOrId)//4
                .setSecretKey(s.secretKey)//5
                .setSmsSecurityMode(s.smsSecurityMode)//6
                .setSmsBindingKeyParam(s.smsBindingKeyParam)//7
                .setSmsBindingKeySecret(s.smsBindingKeySecret)//8
                .setServerSmsNumber(s.serverSmsNumber)//9
                .setServerId(s.serverId)//10
                .setClientOldOffTime(s.clientOldOffTime);//11
    }
    
    /**
     * Converts the {@link ServerConfig} instance to an equivalent class.<br/>
     * The rational is to have a custom method outside the API's {@link ServerConfig} instance so that other
     * custom operations can be done or it can be extended.
     * @param s The {@link ServerConfig} instance/object
     * @return The converted {@link OmaLwM2mServer} instance/object
     */
    public static OmaLwM2mServer toLwM2mServer(ServerConfig s)
    {
        return new OmaLwM2mServer()//the OMA LwM2M resource IDs
                .setShortId(s.shortId)//1
                .setLifetime(s.lifetime)//1
                .setDefaultMinPeriod(s.defaultMinPeriod)//2
                .setDefaultMaxPeriod(s.defaultMaxPeriod)//3
                .setDisableTimeout(s.disableTimeout)//5
                .setNotifIfDisabled(s.notifIfDisabled)//6
                .setBinding(s.binding);//7
    }
}
