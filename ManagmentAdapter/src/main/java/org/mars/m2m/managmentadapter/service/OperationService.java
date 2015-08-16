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
import java.util.Arrays;
import java.util.GregorianCalendar;
//import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.client.ServiceConsumer;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.StdEventCats;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Resource;
import org.mars.m2m.dmcore.util.DmCommons;
import org.mars.m2m.managmentadapter.model.DiscoveredInstanceDetails;
import org.mars.m2m.managmentadapter.model.DiscoveryDetails;
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
        Set<Integer> instances = getInstancesOfObject(discoveryList);
        
                
        //resource <responsePrimitive> or <response> 
        primitiveResponse = prepareRespPrimitive(request, statusCode, getContainerForObject(discoveryList, instances));
        
        return primitiveResponse;
    }
    
    /**
     * Puts all the resources of an instance into a &lt;Container&gt; resource
     * and then the instance into a contentInstance. These are then packaged into
     * another container representing the instance together with its resources
     * 
     * <p>
     * [container [contentInstnce, container[ contentInstance, ...] ] ]
     * <br/>
     * <p/>
     * <p>
     *  _______________
     * |Object         | contentInstance -----------------------------------------------------------------
     * |_______________|                                                                                  |
     *          |        ________________                                                                 | container
     *          |--------|Instance       | contentInstance ---------------------------------              |
     *          |        |_______________|                                                  |--container -|
     *          |               |         ______________                                    |             |
     *          |               |---------| Resource    | contentInstance -----             |             |
     *          |               |         |_____________|                     |--container --             |
     *          |               |         _______________                     |                           |
     *          |               |---------| Resource    | contentInstance -----                           |
     *          |               |         |_____________|                                                 |
     *          |        ________________                                                                 | 
     *          |--------|Instance       | contentInstance ---------------------------------              |
     *          |        |_______________|                                                  |--container --
     *                          |         ______________                                    |
     *                          |---------| Resource    | contentInstance -----             |
     *                          |         |_____________|                     |--container -- 
     *                          |         _______________                     |
     *                          |---------| Resource    | contentInstance -----
     *                          |         |_____________|
     * </p>
     * @param discoveryList The discovered data
     * @param instances The unique instances in the discovered data of an object
     * @return A list of {@link Container} as the instances of this object
     */
    private Container getContainerForObject(DiscoveryList discoveryList, final Set<Integer> instancesIds)
    {
        Gson gson = new Gson();
        
        //gets the details of the object  - the element in the reported list
        DiscoveryDetails objectDetails = discoveryList.getData().get(0);
                
        ArrayList<Resource> instances;
        instances = new ArrayList<>();
        instances.add(getContentInstance(gson.toJson(objectDetails)));
         
        //for each instance of the object gather all of it's associated resources
        for(Integer i : instancesIds)
        {
            ArrayList<Resource> contentInstances = new ArrayList<>();
            
            for(DiscoveryDetails element : discoveryList.getData())
            {
                if(element.getObjectInstanceId() != null && 
                        i == Integer.parseInt(element.getObjectInstanceId()))
                {
                   contentInstances.add(getContentInstance(gson.toJson(element)));
                }
            }
            //Puts all the resource contentInstances into a single resourcesCcontainer for this instance
            Container c = getContainer(contentInstances);
            
            //froms instance details
            DiscoveredInstanceDetails instanceDetails = new DiscoveredInstanceDetails();
            instanceDetails.setUrl(objectDetails.getUrl()+"/"+i);
            instanceDetails.setObjectId(objectDetails.getObjectId());
            instanceDetails.setPath(objectDetails.getPath()+"/"+i);
            
            //Gets this instance's contentInstance
            ContentInstance ci = getContentInstance(gson.toJson(instanceDetails));
            
            //adds it to the instances list
            instances.add(getContainer(ci,c));
        }
        
                
        return getContainer(instances);
    }
    
    /**
     * Scans through the list and reports the unique instances of the object
     * @param discoveryList The discovered data
     * @return The instance IDs of all the available instances of the object
     */
    private Set<Integer> getInstancesOfObject(DiscoveryList discoveryList)
    {
        Set<Integer> instanceIds = new HashSet<>();
        
        for(DiscoveryDetails element : discoveryList.getData())
        {
            if(element.getObjectInstanceId() != null && 
                   !instanceIds.contains(Integer.parseInt(element.getObjectInstanceId())) )
            {
                instanceIds.add(Integer.parseInt(element.getObjectInstanceId()));
            }
        }
        
        return instanceIds;
    }
    
    /**
     * Wraps the discovered response entity
     * @param resp The discovery request's response
     * @return A {@link DiscoveryList} instance containing the discovered data
     */
    private DiscoveryList parseDiscoveredData(Response resp)
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
    private boolean addToRegistry(RequestPrimitive request)
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
     * @param container The &lt;resourcesCcontainer&gt; resource associated with this &lt;resource&gt; resource
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
     * Sets up the &lt;resourcesCcontainer&gt; resource which shares the data instances among entities.
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
     * Produces a Container for a given number of {@link Resource} objects
     * @param resources
     * @return An instance of {@link Container}
     */
    public Container getContainer(ArrayList<Resource> resources)
    {
        Container resourcesCcontainer = new Container();
        /**
         * Handles Container stuff
         * 
         * Container[ ContentInstance[ Content[contentInstances] ] ]
         */
        //resource <container>
        resourcesCcontainer.setStateTag(BigInteger.ZERO);
        resourcesCcontainer.setCreator("");
        resourcesCcontainer.setMaxNrOfInstances(BigInteger.valueOf(1));
        resourcesCcontainer.setMaxByteSize(BigInteger.valueOf(1024));
        resourcesCcontainer.setMaxInstanceAge(BigInteger.valueOf(86400));//in seconds
        resourcesCcontainer.setCurrentByteSize(BigInteger.valueOf(2));
        resourcesCcontainer.setLocationID("");
        resourcesCcontainer.setOntologyRef("");
        for(Resource ci : resources)
        {        
            resourcesCcontainer.getContentInstanceOrContainerOrSubscription().add(ci);
        }
        
        return resourcesCcontainer;
    }
    
    /**
     * Produces a Container for a given number of {@link Resource} objects
     * @param resources The passed resource(s)
     * @return An instance of {@link Container}
     */
    public Container getContainer(Resource... resources)
    {
        Container resourcesCcontainer = new Container();
        /**
         * Handles Container stuff
         * 
         * Container[ ContentInstance[ Content[contentInstances] ] ]
         */
        //resource <container>
        resourcesCcontainer.setStateTag(BigInteger.ZERO);
        resourcesCcontainer.setCreator("");
        resourcesCcontainer.setMaxNrOfInstances(BigInteger.valueOf(1));
        resourcesCcontainer.setMaxByteSize(BigInteger.valueOf(1024));
        resourcesCcontainer.setMaxInstanceAge(BigInteger.valueOf(86400));//in seconds
        resourcesCcontainer.setCurrentByteSize(BigInteger.valueOf(2));
        resourcesCcontainer.setLocationID("");
        resourcesCcontainer.setOntologyRef("");
        resourcesCcontainer.getContentInstanceOrContainerOrSubscription().addAll(Arrays.asList(resources));
        
        return resourcesCcontainer;
    }
    
}
