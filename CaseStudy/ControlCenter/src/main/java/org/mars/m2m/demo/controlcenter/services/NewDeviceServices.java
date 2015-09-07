/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.services;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class NewDeviceServices
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(NewDeviceServices.class);
    
    private ArrayList<ReportedLwM2MClient> connectedDevices;

    public NewDeviceServices() {
        this.connectedDevices = new ArrayList<>();
    }
    
    /**
     * Register a newly connected device
     * @param device 
     */
    public void addNewDevice(ReportedLwM2MClient device)
    {
        if(this.connectedDevices != null && !this.connectedDevices.contains(device))
        {
            this.connectedDevices.add(device);
        }
    }
    
}
