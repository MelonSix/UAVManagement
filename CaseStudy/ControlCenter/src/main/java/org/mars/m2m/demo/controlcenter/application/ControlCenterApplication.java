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
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.health.ClientsResourceHealth;
import org.mars.m2m.demo.controlcenter.resources.ControlCenterInterface;
import org.mars.m2m.demo.controlcenter.ui.ControlCenter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ControlCenterApplication extends Application<ControlCenterConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterApplication.class);

    @Override
    public void run(ControlCenterConfiguration t, Environment e) throws Exception 
    {
        initStaticConfigMembers(t);
        
        //resources
        ControlCenterInterface cc = new ControlCenterInterface();
        
        //expose resource
        e.jersey().register(cc);
        
        //healthcheck resources
        e.healthChecks().register("Clients Resource healthcheck", new ClientsResourceHealth());
    }   
    
    /**
     * Initializes some member variables in {@link CC_StaticInitConfig}
     * @param t configuration object containing configurations from .yaml file
     */
    private void initStaticConfigMembers(ControlCenterConfiguration t) 
    {
        //sets the address configured from the yml file to the static config to make it accessible by all classes
        CC_StaticInitConfig.ccAddress = t.getControlCenter_address();
        
        //sets the notification url
        CC_StaticInitConfig.ccNotificationServiceURL = t.getControlCenter_notificationURL();
        
        //sets management server URL
        CC_StaticInitConfig.mgmntServerURL = t.getMgmntServerURL();
        
        //sets management adapter URL
        CC_StaticInitConfig.mgmntAdapterURL = t.getMgmntAdapterURL();
    }
    
    @Override
    public void initialize(Bootstrap<ControlCenterConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args) throws Exception
    {
        new ControlCenterApplication().run(args);
        Thread t = new Thread(new ControlCenter());
        t.start();
    }
}
