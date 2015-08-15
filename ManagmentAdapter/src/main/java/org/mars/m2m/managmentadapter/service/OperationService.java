/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
//import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.client.ServiceConsumer;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.StdEventCats;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.dmcore.util.DmCommons;
import org.mars.m2m.managmentadapter.model.DiscoveryList;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;
import org.mars.m2m.managmentadapter.resources.MgmtAdptrInterface;


/**
 *
 * @author AG BRIGHTER
 */
public class OperationService 
{
    Logger logger = (Logger) LoggerFactory.getLogger(OperationService.class);
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
    public OperationService() {
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
    public ResponsePrimitive create(RequestPrimitive request, UriInfo uriInfo)
    {
        //ObjectMapper objectMapper = new ObjectMapper();
        String data = extractRequestData(request);
        
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        headerData.put("content-type", MediaType.APPLICATION_JSON);
        consumerDtls.setHeaderData(headerData);

        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handlePost(consumerDtls, data);
        int statusCode = serviceResponse.getStatus();

        //Sets up the data in a <container> resource
        prepareContainer(serviceResponse);

        //resource <responsePrimitive> or <response> 
        primitiveResponse = prepareRespPrimitive(request, statusCode, container);
        
        return primitiveResponse;
    }
    
    /**
     * Retrieve request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public ResponsePrimitive retrieve(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        consumerDtls.setHeaderData(headerData);
                
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handleGet(consumerDtls);
        int statusCode = serviceResponse.getStatus();
        
        //Sets up the data in a <container> resource
        prepareContainer(serviceResponse);
        
        //resource <responsePrimitive> or <response> 
        primitiveResponse = prepareRespPrimitive(request, statusCode, container);
        
        return primitiveResponse;
    }
    
    /**
     * Update request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public ResponsePrimitive update(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        String requestData = extractRequestData(request);
        headerData.put("content-type", MediaType.APPLICATION_JSON);
        consumerDtls.setHeaderData(headerData);
        
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handlePut(consumerDtls,requestData);
        int statusCode = serviceResponse.getStatus();
        
        //Sets up the data in a <container> resource
        prepareContainer(serviceResponse);
        
        //resource <responsePrimitive> or <response> 
        primitiveResponse = prepareRespPrimitive(request, statusCode, container);
        
         return primitiveResponse;
    }
    
    /**
     * Delete request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public ResponsePrimitive delete(RequestPrimitive request, UriInfo uriInfo)
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
        prepareContainer(serviceResponse);
        
        //resource <responsePrimitive> or <response> 
        primitiveResponse = prepareRespPrimitive(request, statusCode, container);
        return primitiveResponse;
    }
    
    /**
     * Notify request handling
     * @param request The request
     * @param uriInfo Injected information for the method in events that the URI details are needed
     * @return A {@link ResponsePrimitive} instance
     */
    public ResponsePrimitive notify(RequestPrimitive request, UriInfo uriInfo)
    {        
        if(addToRegistry(request))
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
            prepareContainer(serviceResponse);

            //resource <responsePrimitive> or <response> 
            primitiveResponse = prepareRespPrimitive(request, statusCode, container);
        }
        
        return primitiveResponse;
    }
    
    /**
     * Discovery on an endpoint
     * @param request
     * @param uriInfo
     * @return 
     */
    public ResponsePrimitive discover(RequestPrimitive request, UriInfo uriInfo)
    {
        //sets the request details to be sent to the client to consume a service
        this.uriInfo = uriInfo;
        consumerDtls.setRequest(request);
        consumerDtls.setHeaderData(headerData);
                
        //Gets the response of a consuming client's request
        Response serviceResponse = msConsumer.handleGet(consumerDtls);
        int statusCode = serviceResponse.getStatus();
        DiscoveryList discoveryList = parseDiscoveredData(serviceResponse);
        
        //Sets up the data in a <container> resource
        //prepareContainer(serviceResponse);
        
        //resource <responsePrimitive> or <response> 
        //primitiveResponse = prepareRespPrimitive(request, statusCode, container);
        
        return primitiveResponse;
    }
    
    /**
     * Wraps the discovered response entity
     * @param resp The discovery request's response
     * @return A {@link DiscoveryList} instance containing the discovered data
     */
    public DiscoveryList parseDiscoveredData(Response resp)
    {
        Gson gson = new Gson();  
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"data\"").append(":")
                    .append(resp.readEntity(String.class))
                .append("}");
        System.out.println(sb.toString());
        return gson.fromJson(sb.toString(), DiscoveryList.class);
    }
    
    /**
     * This method registers a Notify request for future callback invocations
     * @param request The notify request
     * @return true for a successful registration or false if otherwise
     */
    public boolean addToRegistry(RequestPrimitive request)
    {                
        try 
        {
            //extract endpoint resource path {objectID}/{instanceID}/{resourceID}
            String[] endpointTarget = request.getTo().split("/");
            String resourcePath = endpointTarget[endpointTarget.length - 3] + "/"
                    + endpointTarget[endpointTarget.length - 2] + "/"
                    + endpointTarget[endpointTarget.length - 2];
            String notifyTarget = request.getFrom();

            if (NotificationRegistry.getRegistry().containsKey(resourcePath)) 
            { //if this resource is already in the registry with its corresponding subscribers
                ArrayList subscribers = NotificationRegistry.getRegistry().get(resourcePath);
                subscribers.add(notifyTarget);
                NotificationRegistry.updateSubscribers(resourcePath, subscribers);
            } else {//if this resource does not have any entry in the registry
                ArrayList<String> subscribers = new ArrayList<>();
                subscribers.add(notifyTarget);
                NotificationRegistry.setSubscribers(resourcePath, subscribers);
            }  
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }   
    }
    
    /**
     * Sets up the &lt;response&gt; or &lt;responsePrimitive&gt; resource to be sent to the originator of the request
     * @param req The &lt;request&gt; resource or request primitive
     * @param statusCode The returned status code from the management server
     * @param container The &lt;container&gt; resource associated with this &lt;resource&gt; resource
     * @return &ltresponse&gt; resource to be sent to the originator
     */
    public ResponsePrimitive prepareRespPrimitive(RequestPrimitive req, int statusCode, Container container)
    {
        ResponsePrimitive resp = of.createResponsePrimitive();
        resp.setResponseStatusCode(DmCommons.getOneM2mStatusCode(statusCode).getValue());
        resp.setRequestIdentifier(req.getRequestIdentifier());
        primitiveContent.getAny().add(container);
        resp.setContent(primitiveContent);
        resp.setTo(req.getFrom());
        resp.setFrom(uriInfo.getBaseUriBuilder().path(MgmtAdptrInterface.class).build().toString());
        resp.setOriginatingTimestamp(DmCommons.getOneM2mTimeStamp());
        resp.setResultExpirationTimestamp(DmCommons.setOneM2mTimeStamp(new GregorianCalendar(2015,8,1,0,0,0)));
        resp.setEventCategory(StdEventCats.DEFAULT.name());
        return resp;
    }
    
    /**
     * Gets the data within a <container> resource's <contentInstance> resource
     * @param requestPrimitive The request sent by the originator
     * @return The content/data as string
     */
    public String extractRequestData(RequestPrimitive requestPrimitive)
    {
        String content = null;
        
        container = (Container) requestPrimitive.getContent().getAny().get(0);
        contentInstance = (ContentInstance) container.getContentInstanceOrContainerOrSubscription().get(0);
        content = (String) contentInstance.getContent();
        
        return content;
    }
        
    /**
     * Sets up the &lt;container&gt; resource which shares the data instances among entities.
     * <br/>
     * It is used as a mediator that buffers data exchange
     * @param resp The response from the exposed management server web service
     */
    public void prepareContainer(Response resp) 
    {
        //gets returned data as string        
        String entityAsStringData = resp.readEntity(String.class);
        
        /**
         * Handles Container stuff
         * 
         * Container[ ContentInstance[ Content[resp] ] ]
         */
        //resource <container>
        container.setStateTag(BigInteger.ZERO);
        container.setCreator("");
        container.setMaxNrOfInstances(BigInteger.valueOf(1));
        container.setMaxByteSize(BigInteger.valueOf(1024));
        container.setMaxInstanceAge(BigInteger.valueOf(86400));//in seconds
        container.setCurrentByteSize(BigInteger.valueOf(resp.getLength()));
        container.setLocationID("");
        container.setOntologyRef("");
        
            // resource <contentInstance>
            contentInstance.setStateTag(BigInteger.ZERO);
            MediaType mediaType = resp.getMediaType();
            contentInstance.setContentInfo(mediaType.toString());
            contentInstance.setContentSize(BigInteger.valueOf(resp.getLength()));
            contentInstance.setOntologyRef("");
            contentInstance.setContent(entityAsStringData);
        
        container.getContentInstanceOrContainerOrSubscription().add(contentInstance);
    }
    
    /**
     * Produces a {@link ContentInstance} object with a given value as the content
     * @param value The value to be used as the content
     * @return A {@link ContentInstance} object
     */
    public ContentInstance getContentInstance(String value)
    {
        ContentInstance ci = new ContentInstance();
        // resource <contentInstance>
        ci.setStateTag(BigInteger.ZERO);
        ci.setContentInfo(MediaType.APPLICATION_JSON);
        ci.setContentSize(BigInteger.valueOf(20));
        ci.setOntologyRef("");
        ci.setContent(value);
        return ci;
    }
    
    /**
     * Produces a Container for a given number of {@link ContentInstance} objects
     * @param contentInstances The given object(s)
     * @return An instance of {@link Container}
     */
    public Container getContainer(ContentInstance... contentInstances)
    {
        Container container = new Container();
        /**
         * Handles Container stuff
         * 
         * Container[ ContentInstance[ Content[contentInstances] ] ]
         */
        //resource <container>
        container.setStateTag(BigInteger.ZERO);
        container.setCreator("");
        container.setMaxNrOfInstances(BigInteger.valueOf(1));
        container.setMaxByteSize(BigInteger.valueOf(1024));
        container.setMaxInstanceAge(BigInteger.valueOf(86400));//in seconds
        container.setCurrentByteSize(BigInteger.valueOf(2));
        container.setLocationID("");
        container.setOntologyRef("");
        for(ContentInstance ci : contentInstances)
        {        
            container.getContentInstanceOrContainerOrSubscription().add(ci);
        }
        
        return container;
    }
    
}
