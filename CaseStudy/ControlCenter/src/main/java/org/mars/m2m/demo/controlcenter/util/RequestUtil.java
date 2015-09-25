/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import java.math.BigInteger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.callback.AsyncServiceCallback;
import org.mars.m2m.demo.controlcenter.client.AsyncServiceConsumer;
import org.mars.m2m.demo.controlcenter.client.ServiceConsumer;
import org.mars.m2m.demo.controlcenter.model.SvcConsumerDetails;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResourceType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.util.DmCommons;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class RequestUtil
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(RequestUtil.class);

    public RequestUtil() {
    }
    
    /**
     * Produces a <code>ResponsePrimitive</code> resource 
     * @param operation The OneM2M operation (C/R/U/D/N)
     * @param endpointURL The endpoint URL
     * @param senderURL The address to be used as the sender's URL
     * @param data The data to be received by the endpoint (in Json format)
     * @return An object of {@link RequestPrimitive}
     */
    public RequestPrimitive getRequestPrimitiveForData(Operation operation, String endpointURL, String senderURL, String data)
    {
        RequestPrimitive reqPri;
        reqPri = new RequestPrimitive();
        PrimitiveContent content;
        content = new PrimitiveContent();        
        reqPri.setOperation(operation.getValue());
        reqPri.setTo(endpointURL);//The recipient and sender addresses are swapped for the the response message
        reqPri.setFrom(senderURL);
        reqPri.setRequestIdentifier(DmCommons.generateID());
        reqPri.setResourceType(ResourceType.REQUEST.getValue());
        reqPri.setName("");
        ContentInstance contentInstance = getContentInstance(data);
        Container container = getContainer(contentInstance);
        content.getAny().add(container);
        reqPri.setContent(content);
        reqPri.setOriginatingTimestamp(DmCommons.getOneM2mTimeStamp());
        reqPri.setResultExpirationTimestamp(DmCommons.getOneM2mTimeStamp());
        
        return reqPri;
    }
    
    /**
     * Produces a container for a given <code>ContentInstance</code> resource
     * @param resource The resource to be packaged in a container
     * @return An object of {@link ContentInstance}
     */
    private Container getContainer(ContentInstance resource) {
        Container resourcesContainer = new Container();
                
        //Resource properties        
        resourcesContainer.setResourceType(ResourceType.CONTAINER.getValue());
        resourcesContainer.setCreationTime(DmCommons.getOneM2mTimeStamp());
        
        //container properties
        resourcesContainer.setStateTag(BigInteger.ZERO);
        resourcesContainer.setCreator("Control Center");
        resourcesContainer.setMaxNrOfInstances(BigInteger.valueOf(1));
        resourcesContainer.setMaxByteSize(BigInteger.valueOf(1024));
        resourcesContainer.setMaxInstanceAge(BigInteger.valueOf(86400));
        resourcesContainer.setCurrentByteSize(resource.getContentSize());
        resourcesContainer.getContentInstanceOrContainerOrSubscription().add(resource);
        return resourcesContainer;
    }
    
    /**
     * Produces a {@link ContentInstance} object with a given value as the content
     * @param value The value to be used as the content
     * @return A {@link ContentInstance} object
     */
    private ContentInstance getContentInstance(String value) {
        if(value==null)value="";//ensures the string is not null
        ContentInstance ci = new ContentInstance();
        ci.setStateTag(BigInteger.ZERO);
        ci.setContentInfo(MediaType.APPLICATION_JSON);
        ci.setContentSize(BigInteger.valueOf(value.getBytes().length));
        ci.setOntologyRef("");
        ci.setContent(value);
        ci.setCreationTime(DmCommons.getOneM2mTimeStamp());
        return ci;
    }
    
    public Object sendToEndpoint(String senderUrl, String endpointUrl, Operation op, String data, Class clazz)
    {
        try {
            ServiceConsumer sc = new ServiceConsumer();
            SvcConsumerDetails consumerDetails = new SvcConsumerDetails();
            RequestPrimitive requestPrimitiveForData = getRequestPrimitiveForData(op, endpointUrl, senderUrl, data);
            consumerDetails.setRequest(requestPrimitiveForData);
            //All operations are posted to the Management Adapter
            Response handlePost = sc.handlePost(CC_StaticInitConfig.mgmntAdapterURL, consumerDetails.getRequest(), MediaType.APPLICATION_XML);
            return handlePost.readEntity(clazz);
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }
    
    public void asyncSendToEndpoint(String senderUrl, String endpointUrl, Operation op, String data, Class clazz, AsyncServiceCallback<Response> callback)
    {
        try 
        {
            AsyncServiceConsumer sc = new AsyncServiceConsumer();
            SvcConsumerDetails consumerDetails = new SvcConsumerDetails();
            RequestPrimitive requestPrimitiveForData = getRequestPrimitiveForData(op, endpointUrl, senderUrl, data);
            consumerDetails.setRequest(requestPrimitiveForData);
            //All operations are posted to the Management Adapter
            sc.handlePost(CC_StaticInitConfig.mgmntAdapterURL, consumerDetails.getRequest(), MediaType.APPLICATION_XML, callback);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
    
    public String extractRequestData(RequestPrimitive requestPrimitive) {
        String content;
        Container container = (Container) requestPrimitive.getContent().getAny().get(0);
        ContentInstance ci = (ContentInstance) container.getContentInstanceOrContainerOrSubscription().get(0);
        content = (String) ci.getContent();
        return content;
    }
    
    public ReportedLwM2MClient getLwM2MClientFromTreeNode(DefaultMutableTreeNode node) {
        Object nodeInfo;
        nodeInfo = node.getUserObject();
        ReportedLwM2MClient client = (ReportedLwM2MClient) nodeInfo;
        return client;
    }
    
}
