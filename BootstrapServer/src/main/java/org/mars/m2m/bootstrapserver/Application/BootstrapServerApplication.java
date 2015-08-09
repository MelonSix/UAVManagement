/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.bootstrapserver.Application;

import ch.qos.logback.classic.Logger;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.leshan.server.californium.impl.LwM2mBootstrapServerImpl;
import org.mars.m2m.bootstrapserver.Configuration.BootstrapServerConfiguration;
import org.mars.m2m.bootstrapserver.Resources.BootstrapEndpoint;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class BootstrapServerApplication extends Application<BootstrapServerConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(BootstrapServerApplication.class);

    @Override
    public void run(BootstrapServerConfiguration t, Environment e) throws Exception {
        
        //resources
        BootstrapEndpoint bootstrapEndpoint = new BootstrapEndpoint();
        
        //expose resource
        e.jersey().register(bootstrapEndpoint);
        
        //healthcheck resources
        //e.healthChecks().register("Clients Resource healthcheck", new ClientsResourceHealth());
    }   
    
    @Override
    public void initialize(Bootstrap<BootstrapServerConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args) throws Exception
    {
        new BootstrapServerApplication().run(args);
    }
    
    public void startLwm2mBootstrapServer(BootstrapServerConfiguration config)
    {
        /**
         * LwM2M Bootstrap server
         */
        LwM2mBootstrapServerImpl bsServer;
        
        //if the server has predefined address and port number to listen on
        if(config.getLwm2mBootstrapServerAddress()!= null 
                && config.getLwm2mBootstrapServerPortnum()> 0)
        {
            
        }
    }
}
