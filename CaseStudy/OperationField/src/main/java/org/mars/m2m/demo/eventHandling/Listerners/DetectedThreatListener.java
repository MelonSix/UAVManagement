/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.Listerners;

import java.util.EventListener;
import org.mars.m2m.demo.eventHandling.eventObject.DetectedThreatEventObject;

/**
 *
 * @author AG BRIGHTER
 */
public interface DetectedThreatListener extends EventListener {
    
    void fireSensorResourceUpdate(DetectedThreatEventObject eventObject);
}
