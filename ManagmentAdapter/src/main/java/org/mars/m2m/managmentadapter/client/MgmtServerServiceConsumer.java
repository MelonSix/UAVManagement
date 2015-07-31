/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.client;

import ch.qos.logback.classic.Logger;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 Consumes the various services exposed by the management server
 MgmtServerServiceConsumer => Management Sever Service Consumer
 */
public class MgmtServerServiceConsumer 
{
    Logger logger = (Logger) LoggerFactory.getLogger(MgmtServerServiceConsumer.class);
    ObjectFactory of;
    Response response;
    Map<String, String> headerData;
    Map<String, Object> formData;
    
    /**
     * Default constructor
     */
    public MgmtServerServiceConsumer() {
        this.of = new ObjectFactory();
        this.response = null;
    }
    
    /**
     * Handles client get operations to the management server
     * @param consumerDtls
     * @return 
     */
    public Response handleGet(SvcConsumerDetails consumerDtls)
    {
        headerData = consumerDtls.getHeaderData();
        
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(consumerDtls.getRequest().getTo());        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        
        if(headerData != null)
        {
            //HTTP request header details
            for(String key : headerData.keySet())
            {
                invBuilder.header(key, headerData.get(key));
            }
        }
        
        response = invBuilder.get();
        return response;
    }

    public Response handlePut(SvcConsumerDetails consumerDtls, String data) 
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(consumerDtls.getRequest().getTo());        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        
        if(headerData != null)
        {
            //HTTP request header details
            for(String key : headerData.keySet())
            {
                invBuilder.header(key, headerData.get(key));
            }
        }
        System.out.println(data);
        response = invBuilder.put(Entity.entity(data, MediaType.APPLICATION_JSON));
        return response;
    }
}
