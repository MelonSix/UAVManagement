/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.bootstrapserver.Health;

import com.codahale.metrics.health.HealthCheck;

/**
 *
 * @author AG BRIGHTER
 */
public class BootStrapResourceHealth extends HealthCheck {
    
    public BootStrapResourceHealth() {
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
