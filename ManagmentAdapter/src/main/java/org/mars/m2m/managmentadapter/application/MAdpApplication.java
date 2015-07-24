/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.application;

import ch.qos.logback.classic.Logger;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.mars.m2m.managmentadapter.applicationConfiguration.MAdpConfiguration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class MAdpApplication extends Application<MAdpConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(MAdpApplication.class);

    @Override
    public void run(MAdpConfiguration t, Environment e) throws Exception {
        logger.info("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void initialize(Bootstrap<MAdpConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args)
    {
        //TODO:
    }
}
