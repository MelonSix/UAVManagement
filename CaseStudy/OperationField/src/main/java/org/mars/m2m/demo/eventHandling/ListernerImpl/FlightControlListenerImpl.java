/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.ListernerImpl;

import ch.qos.logback.classic.Logger;
import org.mars.m2m.demo.eventHandling.Listerners.FlightControlListener;
import org.mars.m2m.demo.eventHandling.eventObject.FlightControlEventObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class FlightControlListenerImpl implements FlightControlListener
{  
    private static final Logger logger = (Logger) LoggerFactory.getLogger(FlightControlEventObject.class);
    
    @Override
    public void updateYwaypoints(FlightControlEventObject e) {
        e.getScout().setMove_at_y_coordinate_task(e.getMove_at_y_coordinate_task());
    }
    
}
