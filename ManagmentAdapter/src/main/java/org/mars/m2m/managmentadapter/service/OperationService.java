/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import java.math.BigInteger;
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
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;
import org.mars.m2m.managmentadapter.resources.ManagementOpsResource;


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
     *
     * @param request
     * @param uriInfo
     * @return
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
    
    
    public ResponsePrimitive notify(RequestPrimitive request, UriInfo uriInfo)
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
        
        return primitiveResponse;
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
        resp.setFrom(uriInfo.getBaseUriBuilder().path(ManagementOpsResource.class).build().toString());
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
    
}
