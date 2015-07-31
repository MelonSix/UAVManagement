/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.resources;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mObjectInstance;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.request.ContentFormat;
import org.eclipse.leshan.core.request.CreateRequest;
import org.eclipse.leshan.core.request.DeleteRequest;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.util.Validate;
import org.mars.m2m.managementserver.core.ResponseManagement;
import org.mars.m2m.managementserver.json.ConfigGson;
import org.mars.m2m.managementserver.representations.ObjectResource;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/clients")
public class ClientsResource {
    private static ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ClientsResource.class);
    
    private final LwM2mServer server;
    
    private final Gson gson;
    
    @Context private HttpServletRequest req;
    @Context private HttpServletResponse resp;
    /**
     * Default constructor
     */
    public ClientsResource() {
        this(null);
    }   
    
    /**
     * 
     * @param server 
     */
    public ClientsResource(LwM2mServer server) {
        Validate.notNull(server);
        this.server = server;
        this.gson = ConfigGson.getCustomGsonConfig();       
    }
    
    /**
     * Gets all the connected clients
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllClients()
    {
        Collection<Client> clients = server.getClientRegistry().allClients();
        System.out.println(clients.size());
        String json = this.gson.toJson(clients.toArray(new Client[]{}));
        return json;
    }   
    
    /**
     * 
     * @param clientEndpoint
     * @return 
     */
    @GET
    @Path("/{clientEndpoint}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getClient(@PathParam("clientEndpoint") String clientEndpoint)
    {
        Client client = server.getClientRegistry().get(clientEndpoint);
        String jsonClient;
        jsonClient = this.gson.toJson(client);
        return jsonClient;        
    }
    
    /**
     * Read on an object instance
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @return 
     */
    @GET
    @Path("/{clientEndpoint}/{objectid}/{instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public String readInstanceResources(@PathParam("clientEndpoint") String clientEndpoint, 
                                        @PathParam("objectid") String objectid, @PathParam("instance") String instance)
    {
        String processedValResponse = null;
        String target = "/"+objectid+"/"+instance;
        System.out.println(target);
        Client client = server.getClientRegistry().get(clientEndpoint);
        if (client != null) {
            try {
                ReadRequest request = new ReadRequest(target);
                ValueResponse cResponse = server.send(client, request);
                processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        } 
        return processedValResponse;        
    }
    
    /**
     * Read on a resource
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid
     * @return 
     */
    @GET
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String readResource(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance, @PathParam("resourceid") String resourceid)
    {
        String processedValResponse = null;
        String target = "/"+objectid+"/"+instance+"/"+resourceid;
        Client client = server.getClientRegistry().get(clientEndpoint);
        if (client != null) {
            try {
                ReadRequest request = new ReadRequest(target);
                ValueResponse cResponse = server.send(client, request);
                processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
            } catch (IOException ex) {
                Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return processedValResponse;        
    }
    
    /**
     * Updates a particular resource
     * @param clientEndpoint The endpoint identifier
     * @param objectid The object ID of the resource to be updated
     * @param instance The object instance which the resource to be updated falls under
     * @param resourceid The resource ID to be used in locating and updating the resource
     * @param resource The deserialized resource object to be used for the update process
     * @return An String which is an already serialized Json value using a custom serializer
     */
    @PUT
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateResource(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance, @PathParam("resourceid") String resourceid, ObjectResource resource)
    {
        String processedValResponse = "[]";
        try 
        {
            String target = "/"+objectid+"/"+instance+"/"+resourceid;
            Client client = server.getClientRegistry().get(clientEndpoint);
            System.out.println(target+" "+clientEndpoint);
            LwM2mResource resce = parseData(resource);            
            if(resce != null)
            {
                WriteRequest writeRequest = new WriteRequest(target, resce, ContentFormat.TEXT, true);
                LwM2mResponse res = server.send(client, writeRequest);
                processedValResponse = ResponseManagement.processDeviceResponse(res);
            }
            
        } catch (Exception ex) 
        {
            log.error(ex.toString());
        }
        return processedValResponse;
    }
    
    /**
     * Observe request on a resource
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid
     * @return 
     */
    @POST
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}/observe")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String creatObserveRequest(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance, @PathParam("resourceid") String resourceid)
    {
        String processedValResponse = null;
        String target = "/"+objectid+"/"+instance+"/"+resourceid;
        Client client = server.getClientRegistry().get(clientEndpoint);
        
        if(client != null)
        {
            try {
                ObserveRequest request = new ObserveRequest(target);
                LwM2mResponse cResponse = server.send(client, request);
                processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
            } catch (IOException ex) {
                log.error(ex.toString());
            }
        }        
        return processedValResponse;
    }
    
    /**
     * Execute request
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid
     * @return 
     */
    @POST
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String executeRequest(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance, @PathParam("resourceid") String resourceid)
    {        
        String processedValResponse = null;
        String target = "/"+objectid+"/"+instance+"/"+resourceid;
        Client client = server.getClientRegistry().get(clientEndpoint);
        if(client != null)
        {
            try 
            {
                System.out.println(new String(IOUtils.toByteArray(req.getInputStream())));
                ExecuteRequest request = 
                        new ExecuteRequest(target,IOUtils.toByteArray(req.getInputStream()),null);
                LwM2mResponse cResponse = server.send(client, request);
                processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
            } catch (IOException ex) {
                Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return processedValResponse;
    }
    
    /**
     * Creates an instance of an object can have multiple instances in the model
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @return 
     */
    @POST
    @Path("/{clientEndpoint}/{objectid}/{instance}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String createRequest(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance)
    {
            String processedValResponse = null;
        try {
            String target = "/"+objectid+"/"+instance;
            Client client = server.getClientRegistry().get(clientEndpoint);
            LwM2mResponse cResponse = this.createRequest(client, target, req);
            processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
        } catch (IOException ex) {
            Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return processedValResponse;
    }
    
    /**
     * Cancels an already existing observation/notify event
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid 
     */
    @DELETE
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}/observe")
    public void deleteObservation(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance, @PathParam("resourceid") String resourceid)
    {
        String target = "/"+objectid+"/"+instance+"/"+resourceid;
        Client client = server.getClientRegistry().get(clientEndpoint);
        
        if(client != null)
        {
            server.getObservationRegistry().cancelObservation(client, target);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                resp.getWriter().format("No registered client with id '%s'", clientEndpoint).flush();
            } catch (IOException ex) {
                Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Deletes an instance of an object
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @return 
     */
    @DELETE
    @Path("/{clientEndpoint}/{objectid}/{instance}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteInstance(@PathParam("clientEndpoint") String clientEndpoint, @PathParam("objectid") String objectid,
                                @PathParam("instance") String instance)
    {
        String processedValResponse = null;
        String target = "/"+objectid+"/"+instance;
        Client client = server.getClientRegistry().get(clientEndpoint);
        
        if(client != null)
        {
            try {
                DeleteRequest request = new DeleteRequest(target);
                LwM2mResponse cResponse = server.send(client, request);
                processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
            }
            //return Response.status(Response.Status.OK).build();
            catch (IOException ex) {
                Logger.getLogger(ClientsResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return processedValResponse;
    }
    
    /**
     * For converting the value of the resource to appropriate type and then use that to create a lwm2mresource
     * @param resource the resource object created by JAX-RS
     * @return the LwM2M resource
     */
    public LwM2mResource parseData(ObjectResource resource) 
    {
        String datatype = resource.getDataType();
        try {            
            switch (datatype) {
                case "STRING":
                    return new LwM2mResource(resource.getId(), Value.newStringValue(String.valueOf(resource.getValue())));
                    
                case "INTEGER":
                    return new LwM2mResource(resource.getId(), Value.newIntegerValue(Integer.valueOf(resource.getValue().toString())));
                    
                case "LONG":
                    return new LwM2mResource(resource.getId(), Value.newLongValue(Long.valueOf(resource.getValue().toString())));
                    
                case "FLOAT":
                    return new LwM2mResource(resource.getId(), Value.newFloatValue(Float.valueOf(resource.getValue().toString())));
                    
                case "DOUBLE":
                    return new LwM2mResource(resource.getId(), Value.newDoubleValue(Double.valueOf(resource.getValue().toString())));
                    
                case "BOOLEAN":
                    return new LwM2mResource(resource.getId(), Value.newBooleanValue(Boolean.valueOf(resource.getValue().toString())));
                   
                case "OPAQUE":
                    return new LwM2mResource(resource.getId(), Value.newBinaryValue(resource.getValue().toString().getBytes()));
                    
                case "TIME"://ERROR PRONE!! TODO: Find appropriate approach to safely parse date types
                    return new LwM2mResource(resource.getId(), Value.newDateValue((Date) resource.getValue()));
                  
                default:
                    return null;
            }
        } catch (Exception e) {
                    log.error(e.toString());
                    return null;
        }
    }
    
    /**
     * Creates a new node
     * @param client
     * @param target
     * @param req
     * @return The new LwM2m node
     * @throws IOException 
     */
    private LwM2mResponse createRequest(Client client, String target, HttpServletRequest req)
            throws IOException {
        Map<String, String> parameters = new HashMap<>();
        String contentType = HttpFields.valueParameters(req.getContentType(), parameters);
        
        if ("application/json".equals(contentType)) 
        {
            String content = IOUtils.toString(req.getInputStream(), parameters.get("charset"));
            LwM2mNode node;
            try 
            {
                node = gson.fromJson(content, LwM2mNode.class);
            } 
            catch (JsonSyntaxException e) 
            {
                throw new IllegalArgumentException("unable to parse json to tlv:" + e.getMessage(), e);
            }
            
            if (!(node instanceof LwM2mObjectInstance)) 
            {
                throw new IllegalArgumentException("payload must contain an object instance");
            }
            return server.send(client, new CreateRequest(target, ((LwM2mObjectInstance) node).getResources().values()
                    .toArray(new LwM2mResource[0]), ContentFormat.TLV));
        } else 
        {
            throw new IllegalArgumentException("content type " + req.getContentType()
                    + " not supported for write requests");
        }
    }
}
