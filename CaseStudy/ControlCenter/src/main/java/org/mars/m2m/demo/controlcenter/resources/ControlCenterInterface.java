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
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Notification;
import org.mars.m2m.demo.controlcenter.services.ControlCenterReflexes;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.services.NewDeviceServices;
import org.mars.m2m.demo.controlcenter.util.Unmarshaller;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
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
    private final ControlCenterReflexes reflex;
    private final ControlCenterServices controlCenterServices;
    
    public ControlCenterInterface() {
        this.reflex = new ControlCenterReflexes();
        this.newDeviceServices = new NewDeviceServices();
        this.controlCenterServices = new ControlCenterServices();
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
        reflex.observationRequestReflex(device);
        
        return Response.accepted().build();
    }
    
    @POST
    @Path("/notification")
    @Consumes(MediaType.APPLICATION_XML)
    public Response acceptNotification(RequestPrimitive data)
    {
        if (data != null && controlCenterServices.getKb() != null) {
            String content = Unmarshaller.getJsonContent(data);
            Notification notification = Unmarshaller.getNotificationObject(content);
            switch (Unmarshaller.determineNotificationType(notification)) {
                case OBSTACLE:
                    Obstacle obs = (Obstacle) Unmarshaller.getObjectFromNotification(notification, Obstacle.class);
                    if (obs != null && !controlCenterServices.containsObstacle(obs)) {
                        controlCenterServices.addObstacle(obs);
                        System.out.println("Obstacle added to kb");
                    }
                    break;
                case THREAT:
                    Threat threat = (Threat) Unmarshaller.getObjectFromNotification(notification, Threat.class);
                    if (threat != null && !controlCenterServices.containsThreat(threat)) {
                        controlCenterServices.addThreat(threat);
                        System.out.println("Threat added to kb");
                    }
                    break;
                case CONFLICT:
                    break;
                default:
                    logger.info("INVALID NOTIFICATION OPTION RECEIVED");
            }
            System.out.println("Received notification: " + content);
            return Response.accepted().build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    public ControlCenterServices getControlCenterServices() {
        return controlCenterServices;
    }
}
