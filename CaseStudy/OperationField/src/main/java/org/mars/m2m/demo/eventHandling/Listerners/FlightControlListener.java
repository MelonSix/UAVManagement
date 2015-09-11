/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.Listerners;

import java.util.EventListener;
import org.mars.m2m.demo.eventHandling.eventObject.FlightControlEventObject;

/**
 *
 * @author AG BRIGHTER
 */
public interface FlightControlListener extends EventListener {
    /**
     * For updating the Y tasks that are assigned to a scout
     * @param e
     */
    void updateYwaypoints(FlightControlEventObject e);
}
