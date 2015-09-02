/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.core;

import java.util.ArrayList;
import org.eclipse.leshan.server.client.Client;
import org.mars.m2m.managementserver.Listeners.DeviceReporter;

/**
 *
 * @author AG BRIGHTER
 */
public class HandleDeviceReporting 
{
    private ArrayList<DeviceReporter> deviceReporters;

    public HandleDeviceReporting() 
    {
        this.deviceReporters = new ArrayList<>();
    }
    
    /**
     * Registers a reporter
     * @param reporter The reporter to be registered
     */
    public void addDeviceReporterListener(DeviceReporter reporter)
    {
        if(this.deviceReporters != null)
        {
            this.deviceReporters.add(reporter);
        }
    }
    
    /**
     * Performs the reporting to the specified URL in the <code>StaticConfigs</code> class
     * @param c The client to be reported to the specified URL
     */
    public void performReporting(Client c)
    {
        for(DeviceReporter dr : deviceReporters)
        {
            dr.reportDevice(c);
        }
    }
}
