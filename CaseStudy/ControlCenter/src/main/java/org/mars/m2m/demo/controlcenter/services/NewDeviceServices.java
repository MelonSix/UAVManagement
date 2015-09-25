/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.services;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import org.apache.commons.lang3.Validate;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class NewDeviceServices
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(NewDeviceServices.class);
    
    private ArrayList<ReportedLwM2MClient> connectedDevices;
    private static HandleTree handleTree;

    public NewDeviceServices() {
        this.connectedDevices = new ArrayList<>();
       
    }
    
    /**Automatic population of JTree
     * Register a newly connected device
     * @param device The new device's details
     * @return The current list of connected devices
     */
    public ArrayList<ReportedLwM2MClient> addNewDevice(ReportedLwM2MClient device)
    {
        if(this.connectedDevices != null && !this.connectedDevices.contains(device))
        {
            if(this.connectedDevices.contains(device))
                this.connectedDevices.remove(device);
            this.connectedDevices.add(device);
        }
        Validate.notNull(handleTree);
        handleTree.populateJTree(connectedDevices);
        return this.connectedDevices;
    }
    
    /**User controlled population of JTree
     * Register a newly connected device
     * @param devices
     * @return The current list of connected devices
     */
    public ArrayList<ReportedLwM2MClient> addNewClientsOnDemand(ArrayList<ReportedLwM2MClient> devices)
    {
        Validate.notNull(handleTree);
        handleTree.populateJTree(devices);
        return devices;
    }

    public static void setHandleTree(HandleTree handleTree) {
        NewDeviceServices.handleTree = handleTree;
    }
    
    
}
