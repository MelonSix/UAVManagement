/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.core;

import javax.ws.rs.core.MediaType;
import org.mars.m2m.managementserver.client.ServiceConsumer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */

public class HandleObservation implements Runnable
{
    public static ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(HandleObservation.class);
    private final String entity_data;
    private final ServiceConsumer consumer;

    public HandleObservation(String entity_data) {
        this.entity_data = entity_data;
        consumer = new ServiceConsumer();
    }  
    
    @Override
    public void run() {
        try {
            consumer.handlePost("http://localhost:8070/ma/mgmtAdapter/notification", entity_data, MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
}