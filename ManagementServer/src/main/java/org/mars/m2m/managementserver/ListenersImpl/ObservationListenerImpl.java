/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.ListenersImpl;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.server.observation.Observation;
import org.eclipse.leshan.server.observation.ObservationRegistryListener;
import org.mars.m2m.dmcore.json.ConfigGson;
import org.mars.m2m.managementserver.core.HandleObservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ObservationListenerImpl implements ObservationRegistryListener {
    private static final Logger LOG = LoggerFactory.getLogger(ObservationListenerImpl.class);
            
    @Override
    public void cancelled(Observation observation) {
    }

    @Override
    public void newValue(Observation observation, LwM2mNode value) {
        StringBuilder dataBuilder =  new StringBuilder();
        String data = dataBuilder.append("{").append("\"path\"").append(":\"").append(observation.getPath()).append("\",")
                        .append("\"content\"")
                            .append(": [")
                                .append(ConfigGson.getCustomGsonConfig().toJson(value))
                            .append("]")
            .append("}").toString();
        HandleObservation handleObservation = new HandleObservation(data);
        Thread t = new Thread(handleObservation);
        t.start();
    }

    @Override
    public void newObservation(Observation observation) {
    }    
}

