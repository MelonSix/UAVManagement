/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import java.math.BigInteger;
//import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.client.MgmtSvrSvcConsumer;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;


/**
 *
 * @author AG BRIGHTER
 */
public class OperationService 
{
    Logger logger = (Logger) LoggerFactory.getLogger(OperationService.class);
    ResponsePrimitive primitiveResponse;
    ObjectFactory of;
    MgmtSvrSvcConsumer msConsumer;
    Map<String, String> headerData;
    Map<String, Object> formData;
    SvcConsumerDetails consumerDtls;
    ContentInstance contentInstance;
    Container container;
    PrimitiveContent primitiveContent;

    public OperationService() {
        this.of = new ObjectFactory();
        this.primitiveContent = new PrimitiveContent();
        this.container = of.createContainer();
        this.consumerDtls = new SvcConsumerDetails();
        this.headerData = new HashMap<>();
        this.formData = new HashMap<>();
        this.primitiveResponse = null;
        this.msConsumer = new MgmtSvrSvcConsumer();
        this.contentInstance = of.createContentInstance();
    }
    
    public ResponsePrimitive create(RequestPrimitive request)
    {
        return primitiveResponse;
    }
    
    public ResponsePrimitive retrieve(RequestPrimitive request)
    {
        //sets the request details to be sent to the client to consume a service
        consumerDtls.setRequest(request);
        consumerDtls.setFormData(formData);
        consumerDtls.setHeaderData(headerData);
                
        //Gets the response of a consuming client's request
        Response resp = msConsumer.handleGet(consumerDtls);
        String strp = resp.readEntity(String.class);
        //System.out.println(strp);
        
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
            contentInstance.setContent(strp);
            
        container.getContentInstanceOrContainerOrSubscription().add(contentInstance);
        
        //resource <responsePrimitive> or <response>
        
        /**
         * Proof of concept
         * TODO: remove this part
         */
        ProofOfConcept pr = new ProofOfConcept();
        primitiveResponse = pr.getResp();
        primitiveResponse.setRequestIdentifier(request.getRequestIdentifier());
        primitiveContent.getAny().add(container);
        primitiveResponse.setContent(primitiveContent);
        
        return primitiveResponse;
    }
    
    public ResponsePrimitive update(RequestPrimitive request)
    {
         return primitiveResponse;
    }
    
    
    public ResponsePrimitive delete(RequestPrimitive request)
    {
         return primitiveResponse;
    }
    
    
    public ResponsePrimitive notify(RequestPrimitive request)
    {
         return primitiveResponse;
    }
}
