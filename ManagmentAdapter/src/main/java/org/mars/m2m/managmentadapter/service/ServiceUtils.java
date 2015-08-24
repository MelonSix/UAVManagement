/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResourceType;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.StdEventCats;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Resource;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.util.DmCommons;
import org.mars.m2m.managmentadapter.model.DiscoveredInstanceDetails;
import org.mars.m2m.managmentadapter.model.DiscoveryDetails;
import org.mars.m2m.managmentadapter.model.DiscoveryList;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;
import org.mars.m2m.managmentadapter.resources.MgmtAdptrInterface;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ServiceUtils
{
    Logger logger = (Logger) LoggerFactory.getLogger(ServiceUtils.class);
    
    public ServiceUtils() {
    }

    /**
     * Scans through the list and reports the unique instances of the object
     * @param discoveryList The discovered data
     * @return The instance IDs of all the available instances of the object
     */
    public Set<Integer> getInstancesOfObject(DiscoveryList discoveryList) {
        Set<Integer> instanceIds = new HashSet<>();
        for (DiscoveryDetails element : discoveryList.getData()) {
            if (element.getObjectInstanceId() != null && !instanceIds.contains(Integer.parseInt(element.getObjectInstanceId()))) {
                instanceIds.add(Integer.parseInt(element.getObjectInstanceId()));
            }
        }
        return instanceIds;
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
     * |Object         | contentInstance -------------------------------------------------------------------
     * |_______________|                                                                                    |
     *          |        ________________                                                                   |-- container
     *          |--------|Instance       | contentInstance ----------------------------------               |
     *          |        |_______________|                                                  |-- container --|
     *          |               |         ______________                                    |               |
     *          |               |---------| Resource    | contentInstance- container -------|               | 
     *          |               |         |_____________|                                   |               |
     *          |               |         _______________                                   |               |
     *          |               |---------| Resource    | contentInstance- container -------                |
     *          |               |         |_____________|                                                   |
     *          |        ________________                                                                   |
     *          |--------|Instance       | contentInstance----------------------------------                |
     *          |        |_______________|                                                  |--container ---|
     *                          |         ______________                                    |               
     *                          |---------| Resource    | contentInstance-- container ------                
     *                          |         |_____________|                                   |           
     *                          |         _______________                                   |
     *                          |---------| Resource    | contentInstance-- container ------
     *                          |         |_____________|
     * </p>
     * @param discoveryList The discovered data
     * @param instancesIds The set of instances
     * @return A list of {@link Container} as the instances of this object
     */
    public Container getContainerForObject(DiscoveryList discoveryList, final Set<Integer> instancesIds) {
        Gson gson = new Gson();
        DiscoveryDetails objectDetails = discoveryList.getData().get(0);
        ArrayList<Resource> discoveredContent;
        discoveredContent = new ArrayList<>();
        
        if(discoveryList.getData().size() > 1)//case of a collection
        {
            //object handling
            discoveredContent.add(getContentInstance(gson.toJson(objectDetails)));
            
            //instance(s) handling
            for (Integer i : instancesIds)
            {
                //resource(s) handling
                ArrayList<Resource> objectInstanceResources = new ArrayList<>();
                for (DiscoveryDetails element : discoveryList.getData()) {
                    if (element.getObjectInstanceId() != null && i == Integer.parseInt(element.getObjectInstanceId())) {
                        objectInstanceResources.add(getContainer(getContentInstance(gson.toJson(element)), element.getPath()));
                    }
                }
                
                //Container objectInstanceResourcesContainer = getContainer(objectInstanceResources);
                DiscoveredInstanceDetails objectInstance = new DiscoveredInstanceDetails();
                objectInstance.setUrl(objectDetails.getUrl() + "/" + i);
                objectInstance.setObjectId(objectDetails.getObjectId());
                objectInstance.setPath(objectDetails.getPath() + "/" + i);
                ContentInstance objectInstanceContntInst = getContentInstance(gson.toJson(objectInstance));
                discoveredContent.add(addResourcesToInstance(getContainer(objectInstanceContntInst, objectInstance.getPath()), objectInstanceResources));
            }
        }
        else if(discoveryList.getData().size() == 1) //case of data about a particular instance or resource discovery e.g. /0/0/0 or /0/0
        {
            DiscoveryDetails element = discoveryList.getData().get(0);
            return getContainer(getContentInstance(gson.toJson(element)), element.getPath());       
        }
        
        //wraps the discovered data in a container for the response primitive to be sent to the originator of the request
        Container responseContainer = getContainer(discoveredContent); 
        //sets the resource and parent Ids of the object details contentInstance's container
        setResourceAndParentID(objectDetails.getPath(), responseContainer);
        
        return responseContainer;
    }

    /**
     * Wraps the discovered response entity
     * @param resp The discovery request's response
     * @return A {@link DiscoveryList} instance containing the discovered data
     */
    public DiscoveryList parseDiscoveredData(Response resp) {
        try 
        {
            Gson gson = new Gson();
            StringBuilder sb = new StringBuilder();
            sb.append("{").append("\"data\"").append(":").append(resp.readEntity(String.class)).append("}");
            System.out.println(sb.toString());
            return gson.fromJson(sb.toString(), DiscoveryList.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
            //throw new DiscoverResourcesException("Error performing discovery on resource");
            return null;
        }
    }

    /**
     * Produces a {@link ContentInstance} object with a given value as the content
     * @param value The value to be used as the content
     * @return A {@link ContentInstance} object
     */
    public ContentInstance getContentInstance(String value) {
        ContentInstance ci = new ContentInstance();
        ci.setStateTag(BigInteger.ZERO);
        ci.setContentInfo(MediaType.APPLICATION_JSON);
        ci.setContentSize(BigInteger.valueOf(20));
        ci.setOntologyRef("");
        ci.setContent(value);
        //setResourceAndParentID(path, ci);
        ci.setCreationTime(DmCommons.getOneM2mTimeStamp());
        return ci;
    }
    
    /**
     * 
     * @param path
     * @param m2mResource 
     */
    private void setResourceAndParentID(String path, Resource m2mResource) {
        String[] pathElements = path.substring(1).split("/");// e.g. /0/0/0 -> 0/0/0 -> [0][0][0]
        switch(pathElements.length)
        {
            case 3:// 3/0/1
                m2mResource.setResourceID(path);
                m2mResource.setParentID("/"+pathElements[0]+"/"+pathElements[1]);
                break;
            case 2:// 3/0
                m2mResource.setResourceID("/"+pathElements[0]+"/"+pathElements[1]);
                m2mResource.setParentID("/"+pathElements[0]);
                break;
            case 1:// 3
                m2mResource.setResourceID("/"+pathElements[0]);
                m2mResource.setParentID("");
                break;
            default:
        }
    }

    /**
     * Sets up the &lt;response&gt; or &lt;responsePrimitive&gt; resource to be sent to the originator of the request
     * @param req The &lt;request&gt; resource or request primitive
     * @param statusCode The returned status code from the management server
     * @param container The &lt;resourcesCcontainer&gt; resource associated with this &lt;resource&gt; resource
     * @param operationService
     * @return &ltresponse&gt; resource to be sent to the originator
     */
    public ResponsePrimitive prepareRespPrimitive(RequestPrimitive req, int statusCode, Container container, AdapterServices operationService) {
        ResponsePrimitive resp = operationService.of.createResponsePrimitive();
        resp.setResponseStatusCode(DmCommons.getOneM2mStatusCode(statusCode).getValue());
        resp.setRequestIdentifier(req.getRequestIdentifier());
        operationService.primitiveContent.getAny().add(container);
        resp.setContent(operationService.primitiveContent);
        resp.setTo(req.getFrom());
        resp.setFrom(operationService.uriInfo.getBaseUriBuilder().path(MgmtAdptrInterface.class).build().toString());
        resp.setOriginatingTimestamp(DmCommons.getOneM2mTimeStamp());
        resp.setResultExpirationTimestamp(DmCommons.setOneM2mTimeStamp(new GregorianCalendar(2015, 8, 1, 0, 0, 0)));
        resp.setEventCategory(StdEventCats.DEFAULT.name());
        return resp;
    }

    /**
     * This method registers a Notify request for future callback invocations
     * @param request The notify request
     * @param operationService
     * @return true for a successful registration or false if otherwise
     */
    public boolean addToRegistry(RequestPrimitive request, AdapterServices operationService) {
        try {
            String[] endpointTarget = request.getTo().split("/");
            String resourcePath = endpointTarget[endpointTarget.length - 3] + "/" + endpointTarget[endpointTarget.length - 2] + "/" + endpointTarget[endpointTarget.length - 2];
            String notifyTarget = request.getFrom();
            if (NotificationRegistry.getRegistry().containsKey(resourcePath)) {
                ArrayList subscribers = NotificationRegistry.getRegistry().get(resourcePath);
                subscribers.add(notifyTarget);
                NotificationRegistry.updateSubscribers(resourcePath, subscribers);
            } else {
                ArrayList<String> subscribers = new ArrayList<>();
                subscribers.add(notifyTarget);
                NotificationRegistry.setSubscribers(resourcePath, subscribers);
            }
            return true;
        } catch (Exception e) {
            operationService.logger.error(e.toString());
            return false;
        }
    }

    /**
     * Gets the data within a &lt;container&gt; resource's &lt;contentInstance&gt; resource
     * @param requestPrimitive The request sent by the originator
     * @param operationService
     * @return The content/data as string
     */
    public String extractRequestData(RequestPrimitive requestPrimitive, AdapterServices operationService) {
        String content;
        operationService.container = (Container) requestPrimitive.getContent().getAny().get(0);
        operationService.contentInstance = (ContentInstance) operationService.container.getContentInstanceOrContainerOrSubscription().get(0);
        content = (String) operationService.contentInstance.getContent();
        return content;
    }

    /**
     * Sets up the &lt;resourcesCcontainer&gt; resource which shares the data instances among entities.
     * <br/>
     * It is used as a mediator that buffers data exchange
     * @param resp The response from the exposed management server web service
     * @param operationService
     */
    public void prepareContainer(Response resp, AdapterServices operationService) {
        String entityAsStringData = resp.readEntity(String.class);
        operationService.container.setStateTag(BigInteger.ZERO);
        operationService.container.setCreator("");
        operationService.container.setMaxNrOfInstances(BigInteger.valueOf(1));
        operationService.container.setMaxByteSize(BigInteger.valueOf(1024));
        operationService.container.setMaxInstanceAge(BigInteger.valueOf(86400));
        operationService.container.setCurrentByteSize(BigInteger.valueOf(resp.getLength()));
        operationService.container.setLocationID("");
        operationService.container.setOntologyRef("");
        operationService.contentInstance.setStateTag(BigInteger.ZERO);
        MediaType mediaType = resp.getMediaType();
        operationService.contentInstance.setContentInfo(mediaType.toString());
        operationService.contentInstance.setContentSize(BigInteger.valueOf(resp.getLength()));
        operationService.contentInstance.setOntologyRef("");
        operationService.contentInstance.setContent(entityAsStringData);
        operationService.container.getContentInstanceOrContainerOrSubscription().add(operationService.contentInstance);
    }

    /**
     * Produces a Container for a given number of {@link Resource} objects
     * @param resources
     * @return An instance of {@link Container}
     */
    public Container getContainer(ArrayList<Resource> resources) {
        Container resourcesContainer = new Container();
        
        //Resource properties
        resourcesContainer.setResourceType(ResourceType.CONTAINER.getValue());
        resourcesContainer.setResourceID(null);
        resourcesContainer.setParentID(null);
        resourcesContainer.setCreationTime(DmCommons.getOneM2mTimeStamp());
        
        //Container properties
        resourcesContainer.setStateTag(BigInteger.ZERO);
        resourcesContainer.setCreator("");
        resourcesContainer.setMaxNrOfInstances(BigInteger.valueOf(1));
        resourcesContainer.setMaxByteSize(BigInteger.valueOf(1024));
        resourcesContainer.setMaxInstanceAge(BigInteger.valueOf(86400));
        resourcesContainer.setCurrentByteSize(BigInteger.valueOf(2));
        resourcesContainer.setLocationID("");
        resourcesContainer.setOntologyRef("");
        for (Resource res : resources) {
            
            if(res instanceof ContentInstance)//selects the 
            {
                
            }
            
            resourcesContainer.getContentInstanceOrContainerOrSubscription().add(res);
        }
        return resourcesContainer;
    }

    /**
     * Produces a Container for a given number of {@link Resource} objects
     * @param resource The passed resource(s)
     * @param elementPath
     * @return An instance of {@link Container}
     */
    public Container getContainer(ContentInstance resource, String elementPath) {
        Container resourcesContainer = new Container();
                
        //Resource properties        
        resourcesContainer.setResourceType(ResourceType.CONTAINER.getValue());
        setResourceAndParentID(elementPath, resourcesContainer);
        resourcesContainer.setCreationTime(DmCommons.getOneM2mTimeStamp());
        
        //container properties
        resourcesContainer.setStateTag(BigInteger.ZERO);
        resourcesContainer.setCreator("");
        resourcesContainer.setMaxNrOfInstances(BigInteger.valueOf(1));
        resourcesContainer.setMaxByteSize(BigInteger.valueOf(1024));
        resourcesContainer.setMaxInstanceAge(BigInteger.valueOf(86400));
        resourcesContainer.setCurrentByteSize(BigInteger.valueOf(2));
        resourcesContainer.setLocationID("");
        resourcesContainer.setOntologyRef("");
        resourcesContainer.getContentInstanceOrContainerOrSubscription().add(resource);
        return resourcesContainer;
    }
    
    /**
     * Adds resources packaged into containers to the container of the object instance they belong to 
     * @param instanceContainer The container of the object instance
     * @param resourceContainers The resources, packaged into containers, to be added to the container of the object instance
     * @return  The object instance container containing its {@link ContentInstance} object and the resource containers
     */
    private Container addResourcesToInstance(Container instanceContainer, final ArrayList<Resource> resourceContainers)
    {
        for(Resource resource : resourceContainers)
        {
            instanceContainer.getContentInstanceOrContainerOrSubscription().add(resource);
        }
        return instanceContainer;
    }
    
}
