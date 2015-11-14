/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Bootstrap;

import ch.qos.logback.classic.Logger;
import java.net.InetSocketAddress;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.leshan.client.LwM2mClient;
import org.eclipse.leshan.client.californium.impl.CoapClientRequestBuilder;
import org.eclipse.leshan.core.request.BootstrapRequest;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class BootstrapEp
{   
    private static Logger log = (Logger) LoggerFactory.getLogger(BootstrapEp.class);
    
    final InetSocketAddress serverAddress;
    final LwM2mClient client;
    private Request coapRequest;
    private Response coapResponse;
    private BootstrapRequest bootstrapRequest;
    private CoapClientRequestBuilder ccrb;

    public BootstrapEp(InetSocketAddress serverAddress, LwM2mClient client) {
        this.serverAddress = serverAddress;
        this.client = client;
    }   
            
    public byte[] performBootstrap(String endpointName) throws InterruptedException
    {
        ccrb = new CoapClientRequestBuilder(this.serverAddress, this.client);
        bootstrapRequest = new BootstrapRequest(endpointName);
        bootstrapRequest.accept(ccrb);
        coapRequest = ccrb.getRequest();
        //System.out.println("Req uri: "+coapRequest.getURI());
        coapRequest.setURI("coap://192.168.10.199:"+serverAddress.getPort()+"/bs?ep="+endpointName);
        System.out.println("Req uri: "+coapRequest.getURI());
        coapRequest.send();
        coapResponse = coapRequest.waitForResponse();  
        System.out.println(coapResponse.toString());
        return coapResponse.getPayload();
    }
}
