/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.appConfig;

import io.dropwizard.Configuration;

/**
 *
 * @author AG BRIGHTER
 */
public class ControlCenterConfiguration extends Configuration 
{
    private String controlCenter_address;
    private String controlCenter_notificationURL;
    private String mgmntServerURL;
    private String mgmntAdapterURL;
    private String mgmntAdapterGetClients;
    
    public ControlCenterConfiguration() {
    }

    public void setControlCenter_address(String controlCenter_address) {
        this.controlCenter_address = controlCenter_address;
    }

    public String getControlCenter_address() {
        return controlCenter_address;
    }

    public void setControlCenter_notificationURL(String controlCenter_notificationURL) {
        this.controlCenter_notificationURL = controlCenter_notificationURL;
    }

    public String getControlCenter_notificationURL() {
        return controlCenter_notificationURL;
    }

    public String getMgmntServerURL() {
        return mgmntServerURL;
    }

    public void setMgmntServerURL(String mgmntServerURL) {
        this.mgmntServerURL = mgmntServerURL;
    }

    public String getMgmntAdapterURL() {
        return mgmntAdapterURL;
    }

    public void setMgmntAdapterURL(String mgmntAdapterURL) {
        this.mgmntAdapterURL = mgmntAdapterURL;
    }

    public String getMgmntAdapterGetClients() {
        return mgmntAdapterGetClients;
    }

    public void setMgmntAdapterGetClients(String mgmntAdapterGetClients) {
        this.mgmntAdapterGetClients = mgmntAdapterGetClients;
    }
    
    
}
