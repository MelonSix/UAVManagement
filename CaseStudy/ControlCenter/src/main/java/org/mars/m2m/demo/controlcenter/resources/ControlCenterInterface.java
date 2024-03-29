/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.resources;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
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
    static int threatCounter=0;
    
    public ControlCenterInterface() {
        this.reflex = new ControlCenterReflexes();
        this.newDeviceServices = new NewDeviceServices();
        this.controlCenterServices = new ControlCenterServices();
    }
    
    @POST
    @Path("/reportConnectedDevice")
    @Consumes(MediaType.APPLICATION_JSON)
    public void acceptReportedDeviceData(final String data, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                ReportedLwM2MClient device = gson.fromJson(data, ReportedLwM2MClient.class);
                connectedDevices = newDeviceServices.addNewDevice(device);

                //reflex operations to endpoints        
                reflex.scoutingWaypointsReflex(connectedDevices);
                reflex.observationRequestReflex(device);

                asyncResponse.resume(Response.accepted().build()) ;
            }
        }).start();        
    }
    
    @POST
    @Path("/notification")
    @Consumes(MediaType.APPLICATION_XML)
    public void acceptNotification(final RequestPrimitive data, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        asyncResponse.resume(Response.accepted().build());
        new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                if (data != null && controlCenterServices.getKb() != null) 
                {
                    String content = Unmarshaller.getJsonContent(data);
                    Notification notification = Unmarshaller.getNotificationObject(content);
//                    System.out.println("notification received: "+content);
                    switch (Unmarshaller.determineNotificationType(notification))
                    {
                        case OBSTACLE:
                            Obstacle obs = (Obstacle) Unmarshaller.getObjectFromNotification(notification, Obstacle.class);
                            if (obs != null && !controlCenterServices.containsObstacle(obs)) 
                            {
                                controlCenterServices.addObstacle(obs);  
                                
                            }
                            break;
                        case THREAT:
                            Threat threat = (Threat) Unmarshaller.getObjectFromNotification(notification, Threat.class);
//                            System.out.println("Threat received");
                            if (threat != null && !controlCenterServices.containsThreat(threat)) {
                                threatCounter++;
                                controlCenterServices.addThreat(threat);
//                                System.out.println("Threats in kb no.: "+controlCenterServices.getKb().getThreats().size());
                            }
                            break;
                        case CONFLICT:
                            break;
                        case ATTACKER_THREAT_STATUS:
                             boolean isThreatDestroyed = (boolean) Unmarshaller.getObjectFromNotification(notification, Boolean.class);
                             controlCenterServices.findAttackerAndUpdate(data.getFrom(), isThreatDestroyed);                             
                            break;
                        case DESTROYED_THREAT_INDEX:
                            int index = (int) Unmarshaller.getObjectFromNotification(notification, Integer.class);
                            controlCenterServices.addDestroyedThreatIndex(index);
                            break;
                        default:
                            logger.info("INVALID NOTIFICATION OPTION RECEIVED");
                    }
                }
                invokeAsynchronousServices();   
            }
        }).start();
    }
    

    public ControlCenterServices getControlCenterServices() {
        return controlCenterServices;
    }
    
    /**
     * Sets the timeout, callback registration for an Asynchronous response's object
     * @param asyncResponse 
     */
    private void setAsyncResponseProperties(AsyncResponse asyncResponse) {
        //async properties
        asyncResponse.setTimeoutHandler(new TimeoutHandler() { 
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
                logger.info("MS operation time out");
            }
        });
        asyncResponse.setTimeout(60, TimeUnit.SECONDS);
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) 
                {
                    logger.error("Error reporting device to client");
                }
            }            
        });
        asyncResponse.register(new ConnectionCallback() {

            @Override
            public void onDisconnect(AsyncResponse disconnected) {
                logger.error("Client could not be contacted.");
            }
        });
    }   
    
    private void invokeAsynchronousServices() 
    {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(new Supplier<Integer>()
        {
            @Override
            public Integer get() {
                controlCenterServices.invokeAttackerUavServices();
                return 0;
            }
        });
    }
}
