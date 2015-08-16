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
import java.net.InetSocketAddress;
import org.eclipse.leshan.server.security.SecurityStore;
import org.mars.m2m.bootstrapserver.Configuration.BootstrapServerConfiguration;
import org.mars.m2m.bootstrapserver.Health.BootStrapResourceHealth;
import org.mars.m2m.bootstrapserver.Resources.BootstrapEndpoint;
import org.mars.m2m.bootstrapserver.Services.BootstrapSecurityStore;
import org.mars.m2m.bootstrapserver.Services.BootstrapStoreImpl;
import org.mars.m2m.bootstrapserver.core.BsServer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class BootstrapServerApplication extends Application<BootstrapServerConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(BootstrapServerApplication.class);
    
    BootstrapStoreImpl bsStore;
    SecurityStore securityStore;

    public BootstrapServerApplication() {
        this.bsStore = new BootstrapStoreImpl();
        this.securityStore = new BootstrapSecurityStore(bsStore);
    }   

    @Override
    public void run(BootstrapServerConfiguration t, Environment e) throws Exception 
    {
        //starts Bootstrap server
        startLwm2mBootstrapServer(t);
        
        //resources
        BootstrapEndpoint bootstrapEndpoint = new BootstrapEndpoint(bsStore, securityStore);
        
        //expose resource
        e.jersey().register(bootstrapEndpoint);
        
        //healthcheck resources
        e.healthChecks().register("Clients Resource healthcheck", new BootStrapResourceHealth());
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
        BsServer bsServer;
        
        //if the server has predefined address and port number to listen on
        if(/*config.getLwm2mBootstrapServerAddress()!= null
                && */config.getLwm2mBootstrapServerPortnum()> 0)
        { 
            bsServer =
            new BsServer(
            new InetSocketAddress(config.getLwm2mBootstrapServerAddress(), config.getLwm2mBootstrapServerPortnum()),
            new InetSocketAddress(config.getSecureLwm2mBootstrapServerAddress(), config.getSecureLwm2mBootstrapServerPortnum()),
            bsStore, securityStore);
            
            bsServer.start();
        }
        
    }
}
