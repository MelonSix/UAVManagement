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
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.callback.AsyncServiceCallback;
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 *Asynchronously consumes the various services exposed by the management server
 ServiceConsumer => Management Sever Service Consumer
 */
public class AsyncServiceConsumer 
{
    Logger logger = (Logger) LoggerFactory.getLogger(AsyncServiceConsumer.class);
    Map<String, String> headerData;
    Map<String, Object> formData;
    
    /**
     * Default constructor
     */
    public AsyncServiceConsumer() {
    }
    
    /**
     * Handles client get operations to the management server
     * @param consumerDtls
     * @param callback 
     */
    public void handleGet(SvcConsumerDetails consumerDtls, final AsyncServiceCallback callback)
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
        invBuilder.async().get(new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                callback.asyncServicePerformed(response);//Calls the custom method
            }
            
            @Override
            public void failed(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void handlePut(SvcConsumerDetails consumerDtls, String data, final AsyncServiceCallback callback) 
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
        
        invBuilder.async().put(Entity.entity(data, MediaType.APPLICATION_JSON), new InvocationCallback<Response>() {
            @Override
            public void completed(Response response) {
                callback.asyncServicePerformed(response);
            }

            @Override
            public void failed(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void handlePost(SvcConsumerDetails consumerDetails, String data, final AsyncServiceCallback callback)
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
        invBuilder.async().post(Entity.entity(data, MediaType.APPLICATION_JSON), new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                callback.asyncServicePerformed(response);
            }

            @Override
            public void failed(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void handlePost(String to, Object data, final AsyncServiceCallback callback)
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(to);
        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        
        if(headerData != null)
        {
            //HTTP request header details
            for(String key : headerData.keySet())
            {
                invBuilder.header(key, headerData.get(key));
            }
        }
        invBuilder.async().post(Entity.entity(data, MediaType.APPLICATION_JSON), new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                callback.asyncServicePerformed(response);
            }

            @Override
            public void failed(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void handlePost(String to, Object data, String mediaType, final AsyncServiceCallback callback)
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(to);
        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        
        if(headerData != null)
        {
            //HTTP request header details
            for(String key : headerData.keySet())
            {
                invBuilder.header(key, headerData.get(key));
            }
        }
        invBuilder.async().post(Entity.entity(data, mediaType), new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
               callback.asyncServicePerformed(response);
            }

            @Override
            public void failed(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void handleDelete(SvcConsumerDetails consumerDetails, final AsyncServiceCallback callback)
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
        invBuilder.async().delete(new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                callback.asyncServicePerformed(response);
            }

            @Override
            public void failed(Throwable throwable) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });      
    }
}
