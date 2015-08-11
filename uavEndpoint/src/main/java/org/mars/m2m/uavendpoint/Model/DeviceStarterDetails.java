/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;

/**
 *
 * @author AG BRIGHTER
 * 
 * <p> This class is akin to the key of a car <br/> It contains the details needed to start a device or the leshan client of a device </p>
 */
public class DeviceStarterDetails {
    
    /**
     * The required details to start a Device
     */
    private String localHostName;
    private int localPort;
    private String serverHostName;
    private int serverPort;
    private String objectModelFileName;
    private String endPointName;
    UAVConfiguration uavConfig;
    
    /**
     * Bootstrap server address
     */
    protected String bsAddress;
    
    /**
     * Bootstrap server port number
     */
    protected int bsPortnumber;

    /**
     * Default constructor
     */
    public DeviceStarterDetails() {
    }
    
    /**
     * Constructor to set all required details to start a Device
     * @param localHostName The localhost address/name of the CoAP leshan server
     * @param localPort The CoAP server's port number
     * @param serverHostName The lwm2m server/CoAP client address/name
     * @param serverPort The lwm2m server/CoAP client's port number
     * @param endpointName The name of the device
     * @param uavConfig A configuration object to expose the various global values of the UAV to the device so they can be used where necessary
     * @param bootstrapServerAddr Bootstrap server IP address
     * @param bootstrapServerPortnum Bootstrap server port number
     */
    public DeviceStarterDetails(String localHostName, 
                                int localPort, 
                                String serverHostName, 
                                int serverPort, 
//                                String objectModelFileName,
                                String endpointName,
                                UAVConfiguration uavConfig, String bootstrapServerAddr, int bootstrapServerPortnum) {
        this.localHostName = localHostName;
        this.localPort = localPort;
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        //this.objectModelFileName = objectModelFileName;
        this.uavConfig = uavConfig;
        this.endPointName = endpointName;
        this.bsAddress = bootstrapServerAddr;
        this.bsPortnumber = bootstrapServerPortnum;
    }

    public String getLocalHostName() {
        return localHostName;
    }

    public void setLocalHostName(String localHostName) {
        this.localHostName = localHostName;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getServerHostName() {
        return serverHostName;
    }

    public void setServerHostName(String serverHostName) {
        this.serverHostName = serverHostName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getObjectModelFileName() {
        return objectModelFileName;
    }

    public void setObjectModelFileName(String objectModelFileName) {
        this.objectModelFileName = objectModelFileName;
    }

    public UAVConfiguration getUavConfig() {
        return uavConfig;
    }

    public void setUavConfig(UAVConfiguration uavConfig) {
        this.uavConfig = uavConfig;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }         

    public String getBsAddress() {
        return bsAddress;
    }

    public void setBsAddress(String bsAddress) {
        this.bsAddress = bsAddress;
    }

    public int getBsPortnumber() {
        return bsPortnumber;
    }

    public void setBsPortnumber(int bsPortnumber) {
        this.bsPortnumber = bsPortnumber;
    }
    
}
