/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.util;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig;
import org.mars.m2m.uavendpoint.Model.RequiredBootstrapInfo;
import static org.mars.m2m.uavendpoint.UavType.MilitaryUAV.log;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class UnmarshalBootstrap 
{
    public final Logger LOG = (Logger) LoggerFactory.getLogger(UnmarshalBootstrap.class);
    
    BootstrapConfig bootstrapConfig;
    
    public UnmarshalBootstrap() {
    }
    
    /**
     * Gets a {@link BootstrapConfig} instance for accessing the security and server configuration details
     * @param requiredBootstrapInfo
     * @return {@link BootstrapConfig} instance
     */
    public BootstrapConfig getBootstrapConfig(RequiredBootstrapInfo requiredBootstrapInfo)
    {
        //Bootstrap
        byte[] bootstrapInfo;
        try {
            bootstrapInfo = DeviceHelper.bootStrapLwM2mClient(requiredBootstrapInfo);
            Gson gson = new Gson();
            bootstrapConfig = gson.fromJson(new String(bootstrapInfo), BootstrapConfig.class);
            return bootstrapConfig;
        } catch (InterruptedException ex) {
            log.error(ex.toString());
            return null;
        }
    }
}
