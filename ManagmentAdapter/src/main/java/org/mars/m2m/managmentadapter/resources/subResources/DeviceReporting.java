/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources.subResources;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.managmentadapter.DeviceReporting.DeviceReporterImpl;
import org.mars.m2m.managmentadapter.DeviceReporting.HandleDeviceReporting;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceReporting 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DeviceReporting.class);
    
    public DeviceReporting() {
    }
    
    /**
     * Receives the device data which is reported and forwards it
     * to the designated URL in <code>StaticConfigs</code> class
     * @param data The received data
     * @return Response
     */
    @POST
    public Response receiveNewDeviceReport(String data)
    {
        try 
        {
            DeviceReporterImpl reporterImpl = new DeviceReporterImpl();
            HandleDeviceReporting reporting = new HandleDeviceReporting();
            reporting.addDeviceReporterListener(reporterImpl);
            reporting.performReporting(data);
            return Response.ok().build();
        } catch (Exception e) 
        {
            return Response.serverError().build();
        }
    }
}
