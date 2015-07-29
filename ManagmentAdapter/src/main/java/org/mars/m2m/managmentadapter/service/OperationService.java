/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Content;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class OperationService 
{
    Logger logger = (Logger) LoggerFactory.getLogger(OperationService.class);
    ResponsePrimitive response = null;
    ObjectFactory of = new ObjectFactory();

    public OperationService() {
    }
    
    public ResponsePrimitive create(RequestPrimitive request)
    {
        return response;
    }
    
    public ResponsePrimitive retrieve(RequestPrimitive request)
    {
        ProofOfConcept pr = new ProofOfConcept();
        response = pr.getResp();
        response.setRequestIdentifier(request.getRequestIdentifier());
        
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(request.getTo());
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response resp = invocationBuilder.get();
        
        System.out.println(resp.getStatus());
        String strp = resp.readEntity(String.class);
        System.out.println(strp);
        
        Content content = new Content();
        content.setReturnedContent(strp);
        
        //ContentInstance content = 
        
        PrimitiveContent prm = new PrimitiveContent();
        prm.getAny().add(content);
        response.setContent(prm);
        pr.generateResponsePrimitive();
        return response;
    }
    
    public ResponsePrimitive update(RequestPrimitive request)
    {
         return response;
    }
    
    
    public ResponsePrimitive delete(RequestPrimitive request)
    {
         return response;
    }
    
    
    public ResponsePrimitive notify(RequestPrimitive request)
    {
         return response;
    }
}
