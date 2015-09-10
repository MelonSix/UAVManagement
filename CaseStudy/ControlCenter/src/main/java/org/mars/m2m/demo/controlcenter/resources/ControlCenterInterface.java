/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.resources;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.services.ControlCenterReflex;
import org.mars.m2m.demo.controlcenter.services.NewDeviceServices;
import org.mars.m2m.demo.controlcenter.util.UavUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
public class ControlCenterInterface 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterInterface.class);
    private final NewDeviceServices newDeviceServices;
    private ArrayList<ReportedLwM2MClient> connectedDevices;
    private ControlCenterReflex reflex;
    
    public ControlCenterInterface() {
        this.reflex = new ControlCenterReflex();
        this.newDeviceServices = new NewDeviceServices();
    }
    
    @POST
    @Path("/reportConnectedDevice")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response acceptReportedDeviceData(String data)
    {
        Gson gson = new Gson();
        ReportedLwM2MClient device = gson.fromJson(data, ReportedLwM2MClient.class);
        connectedDevices = newDeviceServices.addNewDevice(device);
        System.out.println(device.toString());
        
        //reflex operations to endpoints
        reflex.scoutingWaypointsReflex(connectedDevices);
        
        return Response.accepted().build();
    }
}
