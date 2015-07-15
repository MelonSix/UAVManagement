/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.omaObjects;

import java.util.Map;
import org.mars.m2m.uavendpoint.Configuration.UavKeyManagment;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;

/**
 *
 * @author AG BRIGHTER
 */
public class Security {
    
    /**
     * For the list of all lwm2m Servers that the UAV can reach
     */
    public  Map connectedLwm2mServers;    
    
    /**
     * Gets the configuration of the uav
     */
    UAVConfiguration uavConfig = null;
    private int securityMode = -1;
    private UavKeyManagment uavKeyManagement = null;
    private int UavTriggerBootstrapDelay = -1;
    
    /**
     * Instantiates a security object with no configuration
     */
    public Security() {
        this(null);
    }
    
    /**
     * 
     * @param uavConfig The configuration of the UAV to be used for initializing the security resources
     */
    public Security(UAVConfiguration uavConfig) 
    {        
        if(uavConfig != null)
        {
            this.uavConfig = uavConfig;
            this.securityMode = uavConfig.getSecurityMode();
            this.connectedLwm2mServers = uavConfig.connectedLwm2mServers;

            /**
             * Creates a key management object with the UAV's configuration
             */
            this.uavKeyManagement = new UavKeyManagment(uavConfig);
            this.UavTriggerBootstrapDelay = uavConfig.clientHoldOffTime;
        }
    }

    public Map getConnectedLwm2mServers() {
        return connectedLwm2mServers;
    }

    public void setConnectedLwm2mServers(Map connectedLwm2mServers) {
        this.connectedLwm2mServers = connectedLwm2mServers;
    }

    public UAVConfiguration getUavConfig() {
        return uavConfig;
    }

    public void setUavConfig(UAVConfiguration uavConfig) {
        this.uavConfig = uavConfig;
    }

    public int getSecurityMode() {
        return securityMode;
    }

    public void setSecurityMode(int securityMode) {
        this.securityMode = securityMode;
    }

    public UavKeyManagment getUavKeyManagement() {
        return uavKeyManagement;
    }

    public void setUavKeyManagement(UavKeyManagment uavKeyManagement) {
        this.uavKeyManagement = uavKeyManagement;
    }

    public int getUavTriggerBootstrapDelay() {
        return UavTriggerBootstrapDelay;
    }

    public void setUavTriggerBootstrapDelay(int UavTriggerBootstrapDelay) {
        this.UavTriggerBootstrapDelay = UavTriggerBootstrapDelay;
    }

    @Override
    public String toString() {
        return "Security{" + "connectedLwm2mServers=" + connectedLwm2mServers 
                + ", securityMode=" + securityMode 
                + ", UavTriggerBootstrapDelay=" + UavTriggerBootstrapDelay + '}';
    }
}
