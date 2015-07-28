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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.managmentadapter.model.SvcConsumerDetails;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 * Consumes the various services exposed by the management server
 * MgmtSvrSvcConsumer => Management Sever Service Consumer
 */
public class MgmtSvrSvcConsumer 
{
    Logger logger = (Logger) LoggerFactory.getLogger(MgmtSvrSvcConsumer.class);
    ObjectFactory of;
    Response response;
    Map<String, String> headerData;
    Map<String, Object> formData;
    
    /**
     * Default constructor
     */
    public MgmtSvrSvcConsumer() {
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
        formData = consumerDtls.getFormData();
        
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
        
        if(formData != null)
        {
            //HTTP query parameters
            for (String key : formData.keySet()) {
                webTarget.queryParam(key, formData.get(key));
            }
        }
        
        response = invBuilder.get();
        return response;
    }
}
