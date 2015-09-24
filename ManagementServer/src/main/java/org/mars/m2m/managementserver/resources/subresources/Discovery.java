/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.resources.subresources;

import java.util.concurrent.TimeUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.leshan.core.request.DiscoverRequest;
import org.eclipse.leshan.core.response.DiscoverResponse;
import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.client.Client;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Discovery 
{    
    private final ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Discovery.class);
    private final LwM2mServer server;
    private final String endpointID;
    
    public Discovery(LwM2mServer server, String endpointID) {
        this.server = server;
        this.endpointID = endpointID;
    }
    
    /**
     * Creates a request for discovering the resources implemented by a client for a particular object type.
     * @param objectId the object type
     * @param asyncResponse
     */
    @GET
    @Path("/{objectId}")
    public void discoverObject(@PathParam("objectId") final int objectId, @Suspended final AsyncResponse asyncResponse)
    {         
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client client = server.getClientRegistry().get(endpointID);
                DiscoverRequest discoverRequest = new DiscoverRequest(objectId);
                DiscoverResponse discoveryResponse = server.send(client, discoverRequest);
                Response build;
                if(discoveryResponse != null)        
                    build = Response.ok(discoveryResponse.getObjectLinks()).build();
                else
                    build = Response.serverError().build();
                asyncResponse.resume(build);
            }
        }).start();        
    }
    
    @GET
    @Path("/{objectId}/{instanceId}")
    public void discoverObjectInstance(@PathParam("objectId") final int objectId, @PathParam("instanceId") final int instanceId,
                                        @Suspended final AsyncResponse asyncResponse )
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client client = server.getClientRegistry().get(endpointID);
                DiscoverRequest discoverRequest = new DiscoverRequest(objectId, instanceId);
                DiscoverResponse discoveryResponse = server.send(client, discoverRequest);
                Response build;
                if(discoveryResponse != null)        
                    build = Response.ok(discoveryResponse.getObjectLinks()).build();
                else
                    build = Response.serverError().build();
                asyncResponse.resume(build);
            }
        }).start();        
    }
    
    @GET
    @Path("/{objectId}/{instanceId}/{resourceId}")
    public void discoverObjectInstance(@PathParam("objectId") final int objectId, @PathParam("instanceId") final int instanceId,
                                            @PathParam("resourceId") final int resourceId, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client client = server.getClientRegistry().get(endpointID);
                DiscoverRequest discoverRequest = new DiscoverRequest(objectId, instanceId, resourceId);
                DiscoverResponse discoveryResponse = server.send(client, discoverRequest);
                Response build;
                if(discoveryResponse != null)        
                    build = Response.ok(discoveryResponse.getObjectLinks()).build();
                else
                    build = Response.serverError().build();
                asyncResponse.resume(build);
            }
        }).start();        
    }
    
//    @GET
//    @Path("{id : .+}")
//    public Response error()
//    {
//        //throw new DiscoverResourcesException("Path not found");
//        ErrorMessage errorMessage = new ErrorMessage("Path not found", 404, "www.exceptionLinkHere.com");
//        return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
//    }
    
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
