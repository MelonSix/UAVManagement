/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.resources.subresources;

import java.util.concurrent.TimeUnit;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.client.Client;
import org.mars.m2m.managementserver.ListenersImpl.DeviceReporterImpl;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
public class AdminResource 
{
    private final ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AdminResource.class);
    private final LwM2mServer server;
    
    public AdminResource(LwM2mServer server) {
        this.server = server;
    }
    
    @POST
    @Path("/clearEndpoints")
    public void clearEndpoints(@Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (server.getClientRegistry().allClients().size()>0) 
                {
                    for (Client client : server.getClientRegistry().allClients()) 
                    {
                        server.getClientRegistry().deregisterClient(client.getRegistrationId());
                        if (server.getClientRegistry().allClients().isEmpty()) {
                            System.out.println("All clients successfully deregistered");
                            DeviceReporterImpl.counter = 0;
                            asyncResponse.resume(Response.ok().entity("All clients successfully deregistered").build());
                        } else {
                            System.out.println("WARNING!! Clients registry not empty");                            
                            asyncResponse.resume(Response.ok().entity("WARNING!! Clients registry not empty").build());
                        }
                    }
                }
                else {
                    asyncResponse.resume(Response.ok().entity("Clients registry is already empty").build());
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
