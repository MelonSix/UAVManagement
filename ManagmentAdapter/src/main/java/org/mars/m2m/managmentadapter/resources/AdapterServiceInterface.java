/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.util.DmCommons;
import org.mars.m2m.managmentadapter.resources.subResources.AdminResource;
import org.mars.m2m.managmentadapter.resources.subResources.ConnectedClients;
import org.mars.m2m.managmentadapter.resources.subResources.DeviceReporting;
import org.mars.m2m.managmentadapter.resources.subResources.NotificationResource;
import org.mars.m2m.managmentadapter.service.AdapterServices;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/mgmtAdapter")
public class AdapterServiceInterface {
    
    Logger logger = (Logger) LoggerFactory.getLogger(AdapterServiceInterface.class);    
    ObjectMapper mapper;

    public AdapterServiceInterface() {
        this.mapper = new ObjectMapper();
    }
    
    /**
     *Accepts requests that are posted to the management adapter
     * @param requestPrimitive The posted request to be processed by the management adapter
     * @param uriInfo
     * @param asyncResponse
     * @param resp
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public void postRequest(final RequestPrimitive requestPrimitive, @Context final UriInfo uriInfo, 
            @Suspended final AsyncResponse asyncResponse, @Context final HttpServletResponse resp)
    {
        //async properties
        asyncResponse.setTimeoutHandler(new TimeoutHandler() { 
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
            }
        });
        asyncResponse.setTimeout(120, TimeUnit.SECONDS);
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) 
                {
                    logger.error("Error writing to client");
                }
            }            
        });
        asyncResponse.register(new ConnectionCallback() {

            @Override
            public void onDisconnect(AsyncResponse disconnected) {
                logger.error("Client could not be contacted.");
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
//                    JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
//                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//for debugging purposes
//                    jaxbMarshaller.marshal(requestPrimitive, System.out);//for debugging purposes
                    
                    //handles getting the response and resuming
                    RequestPrimitive processedRequest = processRequest(requestPrimitive, uriInfo);               
                    asyncResponse.resume(processedRequest);
                } catch (Exception ex) {
                    logger.error(ex.toString());
                }                
            }
        }).start();        
    }
    
    @Path("/notification")
    public NotificationResource getNotificationResource()
    {
        return new NotificationResource();
    }
    
    @Path("/processDeviceReporting")
    public DeviceReporting getDeviceReportingResource()
    {
        return new DeviceReporting();
    }
    
    @Path("/connectedClients")
    public ConnectedClients getConnectedClients()
    {
        return new ConnectedClients();
    }
    
    @Path("/admin")
    public AdminResource clearClients()
    {
        return new AdminResource();
    }
    
    /**
     * Process the request primitive by selecting the appropriate operation to handle the request
     * and delivering the response
     * @param request
     * @param uriInfo
     * @return 
     */
    public RequestPrimitive processRequest(final RequestPrimitive request, UriInfo uriInfo)
    {
        RequestPrimitive response = null;
        Operation operation = DmCommons.determineOneM2mOperation(request.getOperation());
        AdapterServices adapterSvc = new AdapterServices();
        
        try {
            switch (operation) {
                case CREATE:
                    response = adapterSvc.create(request, uriInfo);
                    break;
                case RETRIEVE:
                    response = (request.getTo().contains("discover"))?//threats a discover (a kind of retrieve request) request differently
                                    adapterSvc.discover(request, uriInfo):
                                    adapterSvc.retrieve(request, uriInfo);
                    break;
                case UPDATE:
                    response = adapterSvc.update(request, uriInfo);
                    break;
                case DELETE:
                    response = adapterSvc.delete(request, uriInfo);
                    break;
                case NOTIFY: 
                    System.out.println(request.getOperation()+", "+operation.toString());
                    JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//for debugging purposes
                    jaxbMarshaller.marshal(request, System.out);//for debugging purposes
                    response = adapterSvc.notify(request, uriInfo);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response;
    }
    
}
