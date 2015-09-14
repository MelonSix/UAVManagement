/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.Listerners;

import java.util.EventListener;
import org.mars.m2m.demo.eventHandling.eventObject.DetectedObstacleEventObject;

/**
 *
 * @author AG BRIGHTER
 */
public interface DetectedObstacleListener extends EventListener
{
    /**
     * Updates the sensor value to have the current detected obstacle 
     * @param eventObject
     */
    void fireSensorResourceUpdate(DetectedObstacleEventObject eventObject);
}
