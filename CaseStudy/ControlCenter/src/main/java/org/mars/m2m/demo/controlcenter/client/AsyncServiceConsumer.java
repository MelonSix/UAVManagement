/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.client;

import org.mars.m2m.demo.controlcenter.model.SvcConsumerDetails;
import ch.qos.logback.classic.Logger;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.callback.AsyncServiceCallback;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 Consumes the various services exposed by the management server
 ServiceConsumer => Management Sever Service Consumer
 */
public class AsyncServiceConsumer 
{
    Logger logger = (Logger) LoggerFactory.getLogger(AsyncServiceConsumer.class);
    ObjectFactory of;
    Response response;
    Map<String, String> headerData;
    Map<String, Object> formData;
    
    /**
     * Default constructor
     */
    public AsyncServiceConsumer() {
        this.of = new ObjectFactory();
        this.response = null;
    }
    
    /**
     * Handles client get operations to the management server
     * @param consumerDtls
     * @return 
     */
    public Response handleGet(SvcConsumerDetails consumerDtls, final AsyncServiceCallback callback)
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
    
    /**
     * Handles client get operations to the management server
     * @param toUrl
     * @param callback 
     */
    public void handleGet(String toUrl, final AsyncServiceCallback callback)
    {        
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(toUrl);        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
                
        invBuilder.async().get(new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                callback.asyncServicePerformed(response);
            }

            @Override
            public void failed(Throwable throwable) {
                logger.error("Error loading devices from MA"); 
            }
        });
    }

    public Response handlePut(SvcConsumerDetails consumerDtls, String data, final AsyncServiceCallback callback) 
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
        response = invBuilder.put(Entity.entity(data, MediaType.APPLICATION_JSON));
        return response;
    }
    
    public Response handlePost(String to, Object data, String mediaType, final AsyncServiceCallback callback)
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(to);
        
        Invocation.Builder invBuilder = webTarget.request(mediaType);
        
        if(headerData != null)
        {
            //HTTP request header details
            for(String key : headerData.keySet())
            {
                invBuilder.header(key, headerData.get(key));
            }
        }
        //System.out.println(data);
        response = invBuilder.post(Entity.entity(data, mediaType));
        return response;
    }
    
    public Response handleDelete(SvcConsumerDetails consumerDetails, final AsyncServiceCallback callback)
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(consumerDetails.getRequest().getTo());
        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        
        if(headerData != null)
        {
            //HTTP request header details
            for(String key : headerData.keySet())
            {
                invBuilder.header(key, headerData.get(key));
            }
        }
        //System.out.println(data);
        response = invBuilder.delete();      
        return response;
    }
}
