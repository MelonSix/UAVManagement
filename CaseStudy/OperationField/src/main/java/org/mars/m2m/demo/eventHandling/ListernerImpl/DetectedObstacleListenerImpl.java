/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.ListernerImpl;

import com.google.gson.Gson;
import org.mars.m2m.demo.eventHandling.Listerners.DetectedObstacleListener;
import org.mars.m2m.demo.eventHandling.eventObject.DetectedObstacleEventObject;

/**
 *
 * @author AG BRIGHTER
 */
public class DetectedObstacleListenerImpl implements DetectedObstacleListener {

    @Override
    public void fireSensorResourceUpdate(DetectedObstacleEventObject e) {
        Gson gson;
        gson = new Gson();
        String data = gson.toJson(e.getObstacle());
        e.getScout().getObstacleSensorClient().getObstacleSensor().setObstacleInJson(data);
        System.out.println("Detected obstacle: "+data);
    }
    
}
