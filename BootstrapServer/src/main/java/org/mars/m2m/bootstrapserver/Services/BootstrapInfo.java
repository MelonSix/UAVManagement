/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.bootstrapserver.Services;

import ch.qos.logback.classic.Logger;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig;
import org.eclipse.leshan.server.bootstrap.SecurityMode;
import org.eclipse.leshan.server.bootstrap.SmsSecurityMode;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class BootstrapInfo {
    
    Logger logger = (Logger) LoggerFactory.getLogger(BootstrapInfo.class);
    
    public BootstrapInfo() {
    }
    
    public BootstrapConfig.ServerConfig setUpServerConfig()
    {
        BootstrapConfig.ServerConfig serverConfig = new BootstrapConfig.ServerConfig();
        serverConfig.shortId=101;
        serverConfig.lifetime=60;
        serverConfig.defaultMinPeriod = 20;
        serverConfig.defaultMaxPeriod=60;
        serverConfig.disableTimeout =3600;
        serverConfig.notifIfDisabled = true;
        serverConfig.binding = BindingMode.U;
        serverConfig.toString();
        return serverConfig;
    }
    
    public BootstrapConfig.ServerSecurity setUpSecurityConfig()
    {
        String password = "mypassword";
        String publicKey = "pubKey";
        BootstrapConfig.ServerSecurity serverSecurity = new BootstrapConfig.ServerSecurity();
        serverSecurity.uri = "coap://127.0.0.1:5683";
        serverSecurity.bootstrapServer = false;
        serverSecurity.securityMode = SecurityMode.PSK;
        serverSecurity.serverPublicKeyOrId = publicKey.getBytes();
        serverSecurity.publicKeyOrId = publicKey.getBytes();
        serverSecurity.secretKey = password.getBytes();
        serverSecurity.smsSecurityMode = SmsSecurityMode.PROPRIETARY;
        serverSecurity.serverSmsNumber = "00233248864955";
        serverSecurity.smsBindingKeyParam = password.getBytes();
        serverSecurity.clientOldOffTime = 50000;
        serverSecurity.smsBindingKeySecret = password.getBytes();
        serverSecurity.serverId = 101;
        return serverSecurity;
    }
    
    public BootstrapStoreImpl setBootstrap(BootstrapStoreImpl bStore) 
            throws ConfigurationChecker.ConfigurationException
    {
        BootstrapConfig bootstrapConfig = new BootstrapConfig();
        bootstrapConfig.servers.put(1, setUpServerConfig());
        bootstrapConfig.security.put(0, setUpSecurityConfig());
        bStore.addConfig("missileDispatcher", bootstrapConfig);
        bStore.deleteConfig("");//for forcing write to file
        return bStore;
    }
    
}
