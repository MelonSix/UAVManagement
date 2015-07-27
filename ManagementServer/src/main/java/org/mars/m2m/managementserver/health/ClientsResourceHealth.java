/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.health;

import com.codahale.metrics.health.HealthCheck;
import org.eclipse.leshan.server.LwM2mServer;

/**
 *
 * @author AG BRIGHTER
 */
public class ClientsResourceHealth extends HealthCheck {
    LwM2mServer server;
    public ClientsResourceHealth(LwM2mServer server) {
    }    
        
    @Override
    protected Result check() throws Exception {
        if(true)
        {
            return Result.healthy();
        }
        else
        {
            return Result.unhealthy("");
        }
    }
    
}
