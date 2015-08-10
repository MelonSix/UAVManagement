/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.bootstrapserver.Configuration;

import io.dropwizard.Configuration;

/**
 *
 * @author AG BRIGHTER
 */
public class BootstrapServerConfiguration extends Configuration
{
    private String lwm2mBootstrapServerAddress;
    private int lwm2mBootstrapServerPortnum;
    
    private String secureLwm2mBootstrapServerAddress;
    private int secureLwm2mBootstrapServerPortnum;
    
    public BootstrapServerConfiguration() {
    }
    
    public String getLwm2mBootstrapServerAddress() {
        return lwm2mBootstrapServerAddress;
    }

    public void setLwm2mBootstrapServerAddress(String lwm2mBootstrapServerAddress) {
        this.lwm2mBootstrapServerAddress = lwm2mBootstrapServerAddress;
    }

    public int getLwm2mBootstrapServerPortnum() {
        return lwm2mBootstrapServerPortnum;
    }

    public void setLwm2mBootstrapServerPortnum(int lwm2mBootstrapServerPortnum) {
        this.lwm2mBootstrapServerPortnum = lwm2mBootstrapServerPortnum;
    }

    public String getSecureLwm2mBootstrapServerAddress() {
        return secureLwm2mBootstrapServerAddress;
    }

    public void setSecureLwm2mBootstrapServerAddress(String secureLwm2mBootstrapServerAddress) {
        this.secureLwm2mBootstrapServerAddress = secureLwm2mBootstrapServerAddress;
    }

    public int getSecureLwm2mBootstrapServerPortnum() {
        return secureLwm2mBootstrapServerPortnum;
    }

    public void setSecureLwm2mBootstrapServerPortnum(int secureLwm2mBootstrapServerPortnum) {
        this.secureLwm2mBootstrapServerPortnum = secureLwm2mBootstrapServerPortnum;
    }
    
    
}
