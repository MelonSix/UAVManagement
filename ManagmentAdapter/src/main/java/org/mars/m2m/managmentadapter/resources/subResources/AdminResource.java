/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources.subResources;

import java.util.concurrent.TimeUnit;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import org.mars.m2m.managmentadapter.model.ReportedClients;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
public class AdminResource 
{
    private final ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AdminResource.class);
    
    public AdminResource() {
    }
    
    @POST
    @Path("/clearEndpoints")
    public void clearEndpoints(@Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() 
            {
                ReportedClients.getClients().clear();
                if(ReportedClients.getClients().isEmpty())
                {
                    System.out.println("All reported clients cleared");
                    asyncResponse.resume(Response.ok().entity("All reported clients cleared").build());
                }
                else {
                    System.out.println("Reported clients could not be cleared");
                    asyncResponse.resume(Response.ok().entity("Reported clients could not be cleared").build());
                }
            }
        }).start(); 
    }
    
    private void setAsyncResponseProperties(AsyncResponse asyncResponse) {
        //async properties
        asyncResponse.setTimeoutHandler(new TimeoutHandler() { 
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
                log.info("MS operation time out");
            }
        });
        asyncResponse.setTimeout(60, TimeUnit.SECONDS);
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) 
                {
                    log.error("Error reporting device to client");
                }
            }            
        });
        asyncResponse.register(new ConnectionCallback() {

            @Override
            public void onDisconnect(AsyncResponse disconnected) {
                log.error("Client could not be contacted.");
            }
        });
    }
}
