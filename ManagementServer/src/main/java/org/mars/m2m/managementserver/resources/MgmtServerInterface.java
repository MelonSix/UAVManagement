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
import java.util.concurrent.TimeUnit;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
import org.mars.m2m.dmcore.json.ConfigGson;
import org.mars.m2m.managementserver.core.ResponseManagement;
import org.mars.m2m.managementserver.model.ObjectResourceUpdate;
import org.mars.m2m.managementserver.resources.subresources.AdminResource;
import org.mars.m2m.managementserver.resources.subresources.Discovery;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/clients")
public class MgmtServerInterface {
    private static ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MgmtServerInterface.class);
    
    private final LwM2mServer server;
    
    private final Gson gson;
    
    @Context private HttpServletRequest req;
    /**
     * Default constructor
     */
    public MgmtServerInterface() {
        this(null);
    }   
    
    /**
     * 
     * @param server 
     */
    public MgmtServerInterface(LwM2mServer server) {
        Validate.notNull(server);
        this.server = server;
        this.gson = ConfigGson.getCustomGsonConfig();       
    }
    
    /**
     * Gets all the connected clients
     * @param asyncResponse 
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public void getAllClients(@Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        
        //thread for each request
        new Thread(new Runnable() {

            @Override
            public void run() {
                Collection<Client> clients = server.getClientRegistry().allClients();
                String json = gson.toJson(clients.toArray(new Client[]{})); 
                asyncResponse.resume(json);
            }
        }).start();
    }     
    
    /**
     * 
     * @param clientEndpoint
     * @param asyncResponse
     * @return 
     */
    @GET
    @Path("/{clientEndpoint}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getClient(@PathParam("clientEndpoint") final String clientEndpoint, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                Client client = server.getClientRegistry().get(clientEndpoint);
                String jsonClient = gson.toJson(client);
                asyncResponse.resume(jsonClient);
            }
        }).start();     
    }
    
    /**
     * Read on an object instance
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param asyncResponse
     * @return 
     */
    @GET
    @Path("/{clientEndpoint}/{objectid}/{instance}")
    @Produces(MediaType.APPLICATION_JSON)
    public void readInstanceResources(@PathParam("clientEndpoint") final String clientEndpoint, 
                                        @PathParam("objectid") final String objectid, @PathParam("instance") final String instance, 
                                        @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String processedValResponse = null;
                String target = "/"+objectid+"/"+instance;
                Client client = server.getClientRegistry().get(clientEndpoint);
                if (client != null) {
                    try {
                        ReadRequest request = new ReadRequest(target);
                        ValueResponse cResponse = server.send(client, request);
                        processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
                        asyncResponse.resume(processedValResponse);
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                } 
            }
        }).start();      
    }
    
    /**
     * Read on a resource
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid
     * @param asyncResponse
     * @return 
     */
    @GET
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public void readResource(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, @PathParam("resourceid") final String resourceid, 
                                @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String processedValResponse = null;
                String target = "/"+objectid+"/"+instance+"/"+resourceid;
                Client client = server.getClientRegistry().get(clientEndpoint);
                if (client != null) {
                    try {
                        ReadRequest request = new ReadRequest(target);
                        ValueResponse cResponse = server.send(client, request);
                        processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
                        asyncResponse.resume(processedValResponse);
                    } catch (IOException ex) {
                        log.error(ex.getMessage());
                    }
                } 
            }
        }).start();       
    }
    
    /**
     * Updates a particular resource
     * @param clientEndpoint The endpoint identifier
     * @param objectid The object ID of the resource to be updated
     * @param instance The object instance which the resource to be updated falls under
     * @param resourceid The resource ID to be used in locating and updating the resource
     * @param resource The deserialized resource object to be used for the update process
     * @param asyncResponse
     * @return An String which is an already serialized Json value using a custom serializer
     */
    @PUT
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateResource(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, @PathParam("resourceid") final String resourceid, 
                                final ObjectResourceUpdate resource, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String processedValResponse = null;
                try 
                {
                    String target = "/"+objectid+"/"+instance+"/"+resourceid;
                    Client client = server.getClientRegistry().get(clientEndpoint);            
                    LwM2mResource resce = parseData(resource);            
                    if(resce != null)
                    {
                        WriteRequest writeRequest = new WriteRequest(target, resce, ContentFormat.TEXT, true);
                        LwM2mResponse res = server.send(client, writeRequest);
                        processedValResponse = ResponseManagement.processDeviceResponse(res);
                    }

                } catch (Exception ex) 
                {
                    ex.printStackTrace();
                    log.error(ex.toString());
                }
                if(processedValResponse == null)
                {
                    processedValResponse = "{\"error\":\"Datatype parsing error\"}";
                }
                asyncResponse.resume(processedValResponse);
            }
        }).start();
    }
    
    /**
     * Creates an instance of an object that can have multiple instances in the model
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param asyncResponse 
     * @param req 
     */
    @POST
    @Path("/{clientEndpoint}/{objectid}/{instance}/")
    @Produces(MediaType.APPLICATION_JSON)
    public void createRequest(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, @Suspended final AsyncResponse asyncResponse, 
                                @Context final HttpServletRequest req)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String processedValResponse = null;
                try {
                    String target = "/"+objectid+"/"+instance;
                    Client client = server.getClientRegistry().get(clientEndpoint);
                    LwM2mResponse cResponse = processCreateRequest(client, target, req);
                    processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
                } catch (IOException ex) {
                    log.error(ex.toString());
                }
                if(processedValResponse == null)
                {
                    processedValResponse = "{\"error\":\"Datatype parsing error\"}";
                }
                asyncResponse.resume(processedValResponse);
            }
        }).start();
    }
    
    /**
     * Execute request
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid
     * @param execArg
     * @param asyncResponse 
     */
    @POST
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public void executeRequest(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, 
                                @PathParam("resourceid") final String resourceid, final String execArg, 
                                @Suspended final AsyncResponse asyncResponse)
    {        
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String processedValResponse = null;
                final String target = "/"+objectid+"/"+instance+"/"+resourceid;
                Client client = server.getClientRegistry().get(clientEndpoint);
                if(client != null)
                {
                    try 
                    {
                        //byte[] execData = IOUtils.toByteArray(req.getInputStream());
                        ExecuteRequest request = new ExecuteRequest(target,execArg.getBytes(),ContentFormat.JSON);//TODO: Model execArg to decide content format
        //                        new ExecuteRequest(target,IOUtils.toByteArray(req.getInputStream()),null);

                        LwM2mResponse cResponse = server.send(client, request);
                        processedValResponse = ResponseManagement.processDeviceResponse(cResponse);
                    } catch (IOException ex) {
                        Logger.getLogger(MgmtServerInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }        
                asyncResponse.resume(processedValResponse);
            }
        }).start();        
    }
    
    /**
     * Observe request on a resource
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid
     * @param asyncResponse
     * @return 
     */
    @POST
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}/observe")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void creatObserveRequest(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, 
                                @PathParam("resourceid") final String resourceid, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
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
                asyncResponse.resume(processedValResponse);
            }
        }).start();        
    }
    
    /**
     * Cancels an already existing observation/notify event
     * @param clientEndpoint
     * @param objectid
     * @param instance
     * @param resourceid 
     * @param asyncResponse  
     * @param resp  
     */
    @DELETE
    @Path("/{clientEndpoint}/{objectid}/{instance}/{resourceid}/observe")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteObservation(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, @PathParam("resourceid") final String resourceid, 
                                @Suspended final AsyncResponse asyncResponse, final @Context HttpServletResponse resp)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String target = "/"+objectid+"/"+instance+"/"+resourceid;
                Client client = server.getClientRegistry().get(clientEndpoint);

                Response build;
                if(client != null)
                {
                    server.getObservationRegistry().cancelObservation(client, target);
                    build = Response.ok("{\"Status\":\"CANCELED\"}", MediaType.APPLICATION_JSON).build();
                }
                else
                {
                    try {
                        resp.getWriter().format("No registered client with id '%s'", clientEndpoint).flush();
                    } catch (IOException ex) {
                        log.error(ex.toString());
                    }
                  build = Response.serverError().build();
                }
                asyncResponse.resume(build);
            }
        }).start();
    }
    
    /**
     * Deletes an instance of an object
     * @param clientEndpoint
     * @param objectid
     * @param instance 
     * @param asyncResponse 
     */
    @DELETE
    @Path("/{clientEndpoint}/{objectid}/{instance}/")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteInstance(@PathParam("clientEndpoint") final String clientEndpoint, @PathParam("objectid") final String objectid,
                                @PathParam("instance") final String instance, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        new Thread(new Runnable() {

            @Override
            public void run() {
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
                        log.error(ex.getMessage());
                    }
                }
                asyncResponse.resume(processedValResponse);
            }
        }).start();
    }
    
    /**
     *
     * @param clientEndpoint
     * @return
     */
    @Path("/discover/{clientEndpoint}")
    public Discovery performDiscovery(@PathParam("clientEndpoint") String clientEndpoint)
    {
        return new Discovery(server, clientEndpoint);
    }
    
    /**
     *For clearing the registered devices on the LWM2M server
     * @return
     */
    @Path("/admin")
    public AdminResource performDiscovery()
    {
        return new AdminResource(server);
    }
    
    /**
     * For converting the value of the resource to appropriate type and then use that to create a lwm2m resource
     * @param resource the resource object created by JAX-RS
     * @return the LwM2M resource
     */
    public LwM2mResource parseData(ObjectResourceUpdate resource) 
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
    public LwM2mResponse processCreateRequest(Client client, String target, HttpServletRequest req)
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
        asyncResponse.setTimeout(120, TimeUnit.SECONDS);
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
