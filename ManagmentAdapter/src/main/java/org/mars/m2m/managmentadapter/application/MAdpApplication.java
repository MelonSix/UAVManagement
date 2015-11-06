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
import javax.swing.UIManager;
import org.mars.m2m.managmentadapter.applicationConfiguration.MAdpConfiguration;
import org.mars.m2m.managmentadapter.health.ClientsResourceHealth;
import org.mars.m2m.managmentadapter.resources.AdapterServiceInterface;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class MAdpApplication extends Application<MAdpConfiguration> {
    
    Logger logger = (Logger) LoggerFactory.getLogger(MAdpApplication.class);

    @Override
    public void run(MAdpConfiguration t, Environment e) throws Exception {
        
        //resources
        AdapterServiceInterface mgmntRes = new AdapterServiceInterface();
        
        //expose resource
        e.jersey().register(mgmntRes);
        
        //healthcheck resources
        e.healthChecks().register("Clients Resource healthcheck", new ClientsResourceHealth());
        showGUI();
    }
    
    @Override
    public void initialize(Bootstrap<MAdpConfiguration> bootstrap) {
        // nothing to do yet
    }
    
    public static void main(String [] args) throws Exception
    {
        //ProofOfConcept prCpt = new ProofOfConcept();
        //prCpt.generateRequestPrimitive();
        //TODO:
        //System.out.println(OneM2mTimestamp.getTimeStamp());
        new MAdpApplication().run(args);
    }
    
    public void showGUI()
    {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(ConfigUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        //</editor-fold>
            
        ConfigUI maUI = new ConfigUI();
        maUI.setVisible(true);
    }
}
