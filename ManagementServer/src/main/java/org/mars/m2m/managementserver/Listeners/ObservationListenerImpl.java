/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.Listeners;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.server.observation.Observation;
import org.eclipse.leshan.server.observation.ObservationRegistryListener;
import org.mars.m2m.managementserver.json.ConfigGson;
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
        System.out.println("new value in observation");
        LwM2mResource res = (LwM2mResource)value;
        if (LOG.isInfoEnabled()) {
            LOG.info("Received notification from [{}] containing value [{}]", observation.getPath(),
                    value.toString()+", res: "+res.getValue().value.toString());
        }
        String data = new StringBuffer("{\"ep\":\"").append(observation.getClient().getEndpoint())
                .append("\",\"res\":\"").append(observation.getPath().toString()).append("\",\"val\":")
                .append(ConfigGson.getCustomGsonConfig().toJson(value)).append("}").toString();

        //sendEvent(EVENT_NOTIFICATION, data, observation.getClient().getEndpoint());
    }

    @Override
    public void newObservation(Observation observation) {
    }    
}
