/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class ServiceConsumer 
{

    public ServiceConsumer() {
    }
    
    /**
     * 
     * @param toAddress
     * @param data
     * @param mediaType
     * @return 
     */
    public Response handlePost(String toAddress, String data, String mediaType)
    {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(toAddress);
        
        Invocation.Builder invBuilder = webTarget.request(MediaType.APPLICATION_JSON);        
        
        Response response = invBuilder.post(Entity.entity(data, MediaType.valueOf(mediaType)));
        
        return response;
    }
}
