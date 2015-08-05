/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.diagnosticsandmonitoring.application;

import ch.qos.logback.classic.Logger;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.mars.m2m.diagnosticsandmonitoring.appConfiguration.DeviceDiagMonitConfiguration;
import org.mars.m2m.diagnosticsandmonitoring.health.ClientsResourceHealth;
import org.mars.m2m.diagnosticsandmonitoring.resources.ProofOfConcept;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class DeviceDiagMonitApplication extends Application<DeviceDiagMonitConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(DeviceDiagMonitApplication.class);

    @Override
    public void run(DeviceDiagMonitConfiguration t, Environment e) throws Exception {
        
        //resources
        ProofOfConcept concept = new ProofOfConcept();
        
        //expose resource
        e.jersey().register(concept);
        
        //healthcheck resources
        e.healthChecks().register("Clients Resource healthcheck", new ClientsResourceHealth());
    }   
    
    @Override
    public void initialize(Bootstrap<DeviceDiagMonitConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args) throws Exception
    {
        new DeviceDiagMonitApplication().run(args);
    }
}
