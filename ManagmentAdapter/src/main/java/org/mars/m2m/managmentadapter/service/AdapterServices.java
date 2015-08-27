/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
//import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.client.ServiceConsumer;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.managmentadapter.model.DiscoveryList;
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;


/**
 *
 * @author AG BRIGHTER
 */
public class AdapterServices 
{
    Logger logger = (Logger) LoggerFactory.getLogger(AdapterServices.class);
    ServiceUtils serviceUtils;
    
    ResponsePrimitive primitiveResponse;
    ObjectFactory of;
    ServiceConsumer msConsumer;
    Map<String, String> headerData;
    Map<String, Object> formData;
    SvcConsumerDetails consumerDtls;
    ContentInstance contentInstance;
    Container container;
    PrimitiveContent primitiveContent;
    UriInfo uriInfo;
    
    /**
     * Default constructor for creating an instance of this class.
     * <br/>
     * Performs some initializations that are needed
     */
    public AdapterServices() {
        this.serviceUtils = new ServiceUtils();
        this.of = new ObjectFactory();
        this.primitiveContent = new PrimitiveContent();
        this.container = of.createContainer();
        this.consumerDtls = new SvcConsumerDetails();
        this.headerData = new HashMap<>();
        this.formData = new HashMap<>();
        this.primitiveResponse = null;
        this.msConsumer = new ServiceConsumer();
        this.contentInstance = of.createContentInstance();
    }
    
    /**
     * Create request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public RequestPrimitive create(RequestPrimitive request, UriInfo uriInfo)
    {
        //ObjectMapper objectMapper = new ObjectMapper();
        String data = serviceUtils.extractRequestData(request, this);
        
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        headerData.put("content-type", MediaType.APPLICATION_JSON);
        consumerDtls.setHeaderData(headerData);

        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handlePost(consumerDtls, data);
        int statusCode = serviceResponse.getStatus();

        //Sets up the data in a <container> resource
        serviceUtils.prepareContainer(serviceResponse, this);

        //resource <responsePrimitive> or <response> 
        //primitiveResponse = serviceUtils.prepareRespPrimitive(request, statusCode, container, this);
        
        return serviceUtils.prepareReqPrimitiveAsResponse(Operation.CREATE, request, container, this);
    }
    
    /**
     * Retrieve request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public RequestPrimitive retrieve(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        consumerDtls.setHeaderData(headerData);
                
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handleGet(consumerDtls);
        int statusCode = serviceResponse.getStatus();
        
        //Sets up the data in a <container> resource
        serviceUtils.prepareContainer(serviceResponse, this);
        
        //resource <responsePrimitive> or <response> 
        //primitiveResponse = serviceUtils.prepareRespPrimitive(request, statusCode, container, this);
        
        return serviceUtils.prepareReqPrimitiveAsResponse(Operation.CREATE, request, container, this);
    }
    
    /**
     * Update request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public RequestPrimitive update(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        String requestData = serviceUtils.extractRequestData(request, this);
        headerData.put("content-type", MediaType.APPLICATION_JSON);
        consumerDtls.setHeaderData(headerData);
        
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handlePut(consumerDtls,requestData);
        int statusCode = serviceResponse.getStatus();
        
        //Sets up the data in a <container> resource
        serviceUtils.prepareContainer(serviceResponse, this);
        
        //resource <responsePrimitive> or <response> 
        //primitiveResponse = serviceUtils.prepareRespPrimitive(request, statusCode, container, this);
        
         return serviceUtils.prepareReqPrimitiveAsResponse(Operation.UPDATE, request, container, this);
    }
    
    /**
     * Delete request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public RequestPrimitive delete(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        headerData.put("content-type", MediaType.APPLICATION_JSON);
        consumerDtls.setHeaderData(headerData);
        
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handleDelete(consumerDtls);
        int statusCode = serviceResponse.getStatus();
        
        //Sets up the data in a <container> resource
        serviceUtils.prepareContainer(serviceResponse, this);
        
        //resource <responsePrimitive> or <response> 
        //primitiveResponse = serviceUtils.prepareRespPrimitive(request, statusCode, container, this);
        
        return serviceUtils.prepareReqPrimitiveAsResponse(Operation.DELETE, request, container, this);
    }
    
    /**
     * Notify request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public RequestPrimitive notify(RequestPrimitive request, UriInfo uriInfo)
    {        
        if(serviceUtils.addToRegistry(request, this))
        {
            String data = null;//extractRequestData(request);

            request.setTo(request.getTo()+"/observe");

            //sets the request details to be sent to the client to consume a service
            this.uriInfo = uriInfo;
            consumerDtls.setRequest(request);
            headerData.put("content-type", MediaType.APPLICATION_JSON);
            consumerDtls.setHeaderData(headerData);

            //Gets the response of a consuming client's request
            Response serviceResponse = msConsumer.handlePost(consumerDtls, data);
            int statusCode = serviceResponse.getStatus();

            //Sets up the data in a <container> resource
            serviceUtils.prepareContainer(serviceResponse, this);

            //resource <responsePrimitive> or <response> 
            return serviceUtils.prepareReqPrimitiveAsResponse(Operation.CREATE, request, container, this);
            //primitiveResponse = serviceUtils.prepareRespPrimitive(request, statusCode, container, this);
        }
        
        return null;
    }
    
    /**
     * Receives a notification request and performs registration for the request on a particular endpoint
     * @param request The notification request in a {@link RequestPrimitive} instance
     * @param uriInfo The URI information of this particular request
     * @return An instance of {@link RequestPrimitive} for the originator
     */
    public RequestPrimitive prepareNotification(RequestPrimitive request, UriInfo uriInfo)
    {        
        if(serviceUtils.addToRegistry(request, this))
        {
            String data = null;//extractRequestData(request);

            request.setTo(request.getTo()+"/observe");

            //sets the request details to be sent to the client to consume a service
            this.uriInfo = uriInfo;
            consumerDtls.setRequest(request);
            headerData.put("content-type", MediaType.APPLICATION_JSON);
            consumerDtls.setHeaderData(headerData);

            //Gets the response of a consuming client's request
            Response serviceResponse = msConsumer.handlePost(consumerDtls, data);
            int statusCode = serviceResponse.getStatus();

            //Sets up the data in a <container> resource
            serviceUtils.prepareContainer(serviceResponse, this);

            //resource <responsePrimitive> or <response> 
            return serviceUtils.prepareReqPrimitiveAsResponse(Operation.CREATE, request, container, this);
            //primitiveResponse = serviceUtils.prepareRespPrimitive(request, statusCode, container, this);
        }
        
        return null;
    }
    
    /**
     * Discovery on an endpoint
     * @param request
     * @param uriInfo
     * @return 
     */
    public RequestPrimitive discover(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        consumerDtls.setHeaderData(headerData);
                
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handleGet(consumerDtls);
        int statusCode = serviceResponse.getStatus();
        DiscoveryList discoveryList = serviceUtils.parseDiscoveredData(serviceResponse);
        
        if(discoveryList != null)
        {
            Set<Integer> instances = serviceUtils.getInstancesOfObject(discoveryList);
        
                
            //resource <responsePrimitive> or <response> 
            return serviceUtils.prepareReqPrimitiveAsResponse(Operation.CREATE, request,
                                    serviceUtils.getContainerForObject(discoveryList, instances), this);
            
            /*primitiveResponse =
            serviceUtils.prepareRespPrimitive(request, statusCode,
            serviceUtils.getContainerForObject(discoveryList, instances), this);*/
        }
        
        return null;
    }
    
    /**
     * Forms a request primitive for a notification process
     * @param req The corresponding request for this notification
     * @param content The notification content
     * @return An instance of {@link RequestPrimitive} as the response
     */
    public RequestPrimitive notificationResponse(RequestPrimitive req, String content)
    {
        return serviceUtils.prepareReqPrimitiveAsResponse(Operation.UPDATE, req, serviceUtils.prepareNotificationContainer(content), this);
    }
    
}