/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.application;

import ch.qos.logback.classic.Logger;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.mars.m2m.demo.controlcenter.appConfig.ControlCenterConfiguration;
import org.mars.m2m.demo.controlcenter.health.ClientsResourceHealth;
import org.mars.m2m.demo.controlcenter.resources.ControlCenterInterface;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ControlCenterApplication extends Application<ControlCenterConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterApplication.class);

    @Override
    public void run(ControlCenterConfiguration t, Environment e) throws Exception {
        
        //resources
        ControlCenterInterface cc = new ControlCenterInterface();
        
        //expose resource
        e.jersey().register(cc);
        
        //healthcheck resources
        e.healthChecks().register("Clients Resource healthcheck", new ClientsResourceHealth());
    }   
    
    @Override
    public void initialize(Bootstrap<ControlCenterConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args) throws Exception
    {
        new ControlCenterApplication().run(args);
    }
}
