/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.applicationConfiguration;

import io.dropwizard.Configuration;

/**
 *
 * @author AG BRIGHTER
 */
public class Lwm2mServerConfiguration extends Configuration  {
    
    private String lwm2mserverAddress;
    private int lwm2mserverportnum;

    public String getLwm2mserverAddress() {
        return lwm2mserverAddress;
    }

    public void setLwm2mserverAddress(String lwm2mserverAddress) {
        this.lwm2mserverAddress = lwm2mserverAddress;
    }

    public int getLwm2mserverportnum() {
        return lwm2mserverportnum;
    }

    public void setLwm2mserverportnum(int lwm2mserverportnum) {
        this.lwm2mserverportnum = lwm2mserverportnum;
    }
    
    
    
}
