/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Configuration;

import org.mars.m2m.uavendpoint.Model.UavLwm2mServer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AG BRIGHTER
 */
public class UAVConfiguration {
    /**
     * The network details of this UAV
     */
    public String uavlocalhostAddress = "127.0.0.1";
    public int uavlocalhostPort = 8080;
    
    /**
     * For the list of all lwm2m Servers that the UAV can reach
     */
    public  Map connectedLwm2mServers;
    
    /**
     * Default key materials assigned to the UAV
     */
    public String UAV_POINT_X_HEX_STRING ="fcc28728c123b155be410fc1c0651da374fc6ebe7f96606e90d927d188894a73";
    public String UAV_POINT_Y_HEX_STRING ="d2ffaa73957d76984633fc1cc54d0b763ca0559a9dff9706e9f4557dacc3f52a";
    public String UAV_PRIVATE_KEY_HEX_STRING ="1dae121ba406802ef07c193c1ee4df91115aabd79c1ed7f4c0ef7ef6a5449411";
    public String ELLIPTIC_CURVE_SPEC = "secp256r1";
    
    /**
     * Determines which UDP payload security mode is used
     */    
    public int securityMode;
    
    /**
     * Client Hold off Time
     * The number of seconds to wait before initiating a client initiated bootstrap
     * once the UAV has determined it should initiate this bootstrap mode.
     */
    public short clientHoldOffTime;
    
    /**
     * Determines the maximum number of notifications to store for an offline LWM2M server
     */
    public int maxNotifications = 100;
    
    /**
     * Various power sources that can be available to a device
     * NO_POWER is added to occupy the 3rd Position as no option is available in the OMA list
     */
    public static enum PowerSources {DC_POWER, INTERNAL_BATTERY, EXTERNAL_BATTERY, NO_POWER, POWER_OVER_ETHERNET, USB, AC_POWER, SOLAR}
    
    /**
     * Battery Status
     * 
     */
    public static enum BatteryStatus {NORMAL, CHARGING, CHARGE_COMPLETE, DAMAGED, LOW_BATTERY, BATTERY_NOT_INSTALLED, BATTERY_INFO_UNKNOWN}
    
    /**
     * Device error codes
     */
    public static enum DeviceErrorCodes {NO_ERROR,
                                  LOW_BATTERY_POWER, 
                                  EXTERNAL_POWER_SUPPLY_OFF, 
                                  GPS_MODULE_FAILURE, 
                                  LOW_RECEIVED_SIGNAL_STRENGTH, 
                                  OUT_OF_MEMORY,
                                  SMS_FAILURE,
                                  IP_CONNECTIVITY_FAILURE,
                                  PERIPHERAL_MALFUNCTION
    }
    
    
    /**
     * Instantiates an object of the configuration class
     */
    public UAVConfiguration() {
        connectedLwm2mServers =  new HashMap<Integer, UavLwm2mServer>();
        
        /**
         * Ensures that the default short server ID (i.e 0) is not used
         * to identify an LWM2M server.
         */
        connectedLwm2mServers.put(0, null);
    }

    public int getSecurityMode() {
        return securityMode;
    }

    public void setSecurityMode(int securityMode) {
        this.securityMode = securityMode;
    }

    public String getUavlocalhostAddress() {
        return uavlocalhostAddress;
    }

    public void setUavlocalhostAddress(String uavlocalhostAddress) {
        this.uavlocalhostAddress = uavlocalhostAddress;
    }

    public int getUavlocalhostPort() {
        return uavlocalhostPort;
    }

    public void setUavlocalhostPort(int uavlocalhostPort) {
        this.uavlocalhostPort = uavlocalhostPort;
    }
    
    
    
}
