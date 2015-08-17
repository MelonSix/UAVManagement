/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.resources.subresources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.leshan.core.request.DiscoverRequest;
import org.eclipse.leshan.core.response.DiscoverResponse;
import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.client.Client;
import org.mars.m2m.managementserver.exceptions.DiscoverResourcesException;
import org.mars.m2m.managementserver.model.ErrorMessage;
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
     * @return HTTP response containing the response entity
     */
    @GET
    @Path("/{objectId}")
    public Response discoverObject(@PathParam("objectId") int objectId)
    {         
        Client client = server.getClientRegistry().get(endpointID);
        DiscoverRequest discoverRequest = new DiscoverRequest(objectId);
        DiscoverResponse discoveryResponse = server.send(client, discoverRequest);
        if(discoveryResponse != null)        
            return Response.ok(discoveryResponse.getObjectLinks()).build();
        else
            return Response.serverError().build();
    }
    
    @GET
    @Path("/{objectId}/{instanceId}")
    public Response discoverObjectInstance(@PathParam("objectId") int objectId, @PathParam("instanceId") int instanceId)
    {
        Client client = server.getClientRegistry().get(endpointID);
        DiscoverRequest discoverRequest = new DiscoverRequest(objectId, instanceId);
        DiscoverResponse discoveryResponse = server.send(client, discoverRequest);
        if(discoveryResponse != null)        
            return Response.ok(discoveryResponse.getObjectLinks()).build();
        else
            return Response.serverError().build();
    }
    
    @GET
    @Path("/{objectId}/{instanceId}/{resourceId}")
    public Response discoverObjectInstance(@PathParam("objectId") int objectId, @PathParam("instanceId") int instanceId,
                                            @PathParam("resourceId") int resourceId)
    {
        Client client = server.getClientRegistry().get(endpointID);
        DiscoverRequest discoverRequest = new DiscoverRequest(objectId, instanceId, resourceId);
        DiscoverResponse discoveryResponse = server.send(client, discoverRequest);
        if(discoveryResponse != null)        
            return Response.ok(discoveryResponse.getObjectLinks()).build();
        else
            return Response.serverError().build();
    }
    
//    @GET
//    @Path("{id : .+}")
//    public Response error()
//    {
//        //throw new DiscoverResourcesException("Path not found");
//        ErrorMessage errorMessage = new ErrorMessage("Path not found", 404, "www.exceptionLinkHere.com");
//        return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
//    }
}
