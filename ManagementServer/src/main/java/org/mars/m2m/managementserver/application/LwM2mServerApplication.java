/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.application;

import ch.qos.logback.classic.Logger;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.mars.m2m.managementserver.ListenersImpl.ClientRegistryListenerImpl;
import org.mars.m2m.managementserver.ListenersImpl.ObservationListenerImpl;
import org.mars.m2m.managementserver.applicationConfiguration.Lwm2mServerConfiguration;
import org.mars.m2m.managementserver.configs.StaticConfigs;
import org.mars.m2m.managementserver.core.CustomObjectModel;
import org.mars.m2m.managementserver.health.ClientsResourceHealth;
import org.mars.m2m.managementserver.resources.MgmtServerInterface;
import org.mars.m2m.managementserver.resources.ObjectModelSpec;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class LwM2mServerApplication extends Application<Lwm2mServerConfiguration>  {
    
    Logger logger = (Logger) LoggerFactory.getLogger(LwM2mServerApplication.class);
    /**
     * 
     */
    private LeshanServer lwServer;
    
    /**
     * 
     * @param bootstrap 
     */
    @Override
    public void initialize(Bootstrap<Lwm2mServerConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    @Override
    public void run(Lwm2mServerConfiguration configuration, Environment environment) throws Exception {
              initStaticConfigMembers(configuration);
              
        //lwm2m server
        startLwm2mServer(configuration);
        
        //resource objects
        MgmtServerInterface clientsRes = new MgmtServerInterface(lwServer);
        ObjectModelSpec objMdlRes = new ObjectModelSpec();
        
        //resources registration
        environment.jersey().register(clientsRes);
        environment.jersey().register(objMdlRes);
        
        //healthchecks
        environment.healthChecks().register("Clients Resource healthcheck", new ClientsResourceHealth(lwServer));
        
//        showUI(lwServer);
    }
    
    public void startLwm2mServer(Lwm2mServerConfiguration config)
    {
        //Build LWM2M server
        LeshanServerBuilder builder = new LeshanServerBuilder();
        
        //if the server has predefined address and port number to listen on
        if(config.getLwm2mserverAddress()!= null && config.getLwm2mserverportnum() > 0)
        {
            builder.setLocalAddress(config.getLwm2mserverAddress(), config.getLwm2mserverportnum());
        }
        
        builder.setObjectModelProvider(new CustomObjectModel());        
        this.lwServer = builder.build();
        this.lwServer.getClientRegistry().addListener(new ClientRegistryListenerImpl(lwServer));
        this.lwServer.getObservationRegistry().addListener(new ObservationListenerImpl());
        this.lwServer.start();
    }
    
    public void showUI(LeshanServer lwm2mServer)
    {
        ConfigUI msUI = new ConfigUI(lwServer);
        msUI.setVisible(true);
    }
    
    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception 
    {
        new LwM2mServerApplication().run(args);
    }

    private void initStaticConfigMembers(Lwm2mServerConfiguration configuration) {
        StaticConfigs.DEVICE_REPORTING_URL = configuration.getDeviceReportingUrl();
        StaticConfigs.NOTIFICATION_REPORTING_URL = configuration.getNotificationReportingUrl();
    }
    
}
