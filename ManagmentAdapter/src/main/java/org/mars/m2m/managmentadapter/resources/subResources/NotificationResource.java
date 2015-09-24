/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources.subResources;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.io.IOUtils;
import org.mars.m2m.managmentadapter.Notification.NotificationListenerImpl;
import org.mars.m2m.managmentadapter.Notification.Notify;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;
import org.slf4j.LoggerFactory;

/**
 * This class is used for receiving notifications from the MS and then forwarding it
 * @author BRIGHTER AGYEMANG
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource 
{
    private final ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(NotificationResource.class);
    private final Notify notify;
    private final NotificationListenerImpl listenerImpl;
    
    public NotificationResource() {
        this.notify = new Notify();
        this.listenerImpl = new NotificationListenerImpl();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void processReceivedObservation(@Context final HttpServletRequest req, @Context UriInfo uriInfo, 
            @Suspended final AsyncResponse asyncResponse)
    {
        //async properties
        asyncResponse.setTimeoutHandler(new TimeoutHandler() { 
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
                
            }
        });
        asyncResponse.setTimeout(60, TimeUnit.SECONDS);
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) 
                {
                    log.error("Error forwarding notification to client");
                }
            }            
        });
        asyncResponse.register(new ConnectionCallback() {

            @Override
            public void onDisconnect(AsyncResponse disconnected) {
                log.error("Client could not be contacted.");
            }
        });
        
        //thread for each request
        new Thread(new Runnable() 
        {

            @Override
            public void run()
            {
                try 
                {            
                    String content="[]";
                    try 
                    {
                        content = IOUtils.toString(req.getInputStream());
                    } catch (IOException iOException) {}

                    //registers a callback
                    notify.addNotificationListener(listenerImpl);

                    //triggers a notification event
                    notify.sendNotification(new NotificationRegistry(), content);           
                    asyncResponse.resume(Response.ok().build());
                } 
                catch (Exception ex) 
                {
                   log.error(ex.toString());
                   asyncResponse.resume(Response.serverError().build());
                }
            }
        }).start();        
    }
}
