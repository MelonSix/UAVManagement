/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.client;

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
import org.mars.m2m.managementserver.callback.AsyncServiceCallback;
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
        
    public void handlePost(String toAddress, String data, String mediaType, final AsyncServiceCallback callback)
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(toAddress);
        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        
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
}
