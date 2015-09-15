/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.ListernerImpl;

import com.google.gson.Gson;
import org.mars.m2m.demo.eventHandling.Listerners.DetectedThreatListener;
import org.mars.m2m.demo.eventHandling.eventObject.DetectedThreatEventObject;

/**
 *
 * @author AG BRIGHTER
 */
public class DetectedThreatListenerImpl implements DetectedThreatListener {

    @Override
    public void fireSensorResourceUpdate(DetectedThreatEventObject e) {
        Gson gson;
        gson = new Gson();
        String data = gson.toJson(e.getThreat());
        e.getScout().getThreatSensorLwM2mClient().getThreatSensor().setThreatInJson(data);
        System.out.println("Detected threat: "+data);
    }
    
}
