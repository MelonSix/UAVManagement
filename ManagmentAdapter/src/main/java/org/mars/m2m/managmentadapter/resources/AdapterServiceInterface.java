/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.util.DmCommons;
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
    Operation operation;

    public AdapterServiceInterface() {
    }
    
    /**
     *Accepts requests that are posted to the management adapter
     * @param requestPrimitive The posted request to be processed by the management adapter
     * @param uriInfo
     * @return The response associated with this request
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public RequestPrimitive postRequest(RequestPrimitive requestPrimitive, @Context UriInfo uriInfo)
    {
        try 
        {
            System.out.println("Inside generate request function in proof of concept class");
            JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(requestPrimitive, System.out);
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return processRequest(requestPrimitive, uriInfo);
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
    
    
    /**
     * Process the request primitive by selecting the appropriate operation to handle the request
     * and delivering the response
     * @param request
     * @param uriInfo
     * @return 
     */
    public RequestPrimitive processRequest(RequestPrimitive request, UriInfo uriInfo)
    {
        RequestPrimitive response = null;
        operation = DmCommons.determineOneM2mOperation(request.getOperation());
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
