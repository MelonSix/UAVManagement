/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.deviceconfiguration.application;

import ch.qos.logback.classic.Logger;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.net.InetSocketAddress;
import org.eclipse.leshan.server.californium.impl.LwM2mBootstrapServerImpl;
import org.eclipse.leshan.server.security.SecurityStore;
import org.mars.m2m.deviceconfiguration.configuration.DeviceConfiguration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class DeviceConfigurationApp extends Application<DeviceConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(DeviceConfigurationApp.class);
      

    public DeviceConfigurationApp() {
        
    }   

    @Override
    public void run(DeviceConfiguration t, Environment e) throws Exception 
    {        
        //resources
        //BootstrapEndpoint bootstrapEndpoint = new BootstrapEndpoint(bsStore, securityStore);
        
        //expose resource
        //e.jersey().register(bootstrapEndpoint);
        
        //healthcheck resources
        //e.healthChecks().register("Clients Resource healthcheck", new BootStrapResourceHealth());
    }   
    
    @Override
    public void initialize(Bootstrap<DeviceConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args) throws Exception
    {
        new DeviceConfigurationApp().run(args);
    }
    
}
