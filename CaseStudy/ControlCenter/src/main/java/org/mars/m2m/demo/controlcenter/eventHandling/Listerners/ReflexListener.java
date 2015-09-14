/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.eventHandling.Listerners;

import java.util.EventListener;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;

/**
 *
 * @author AG BRIGHTER
 */
public interface ReflexListener extends EventListener
{
    void sendScoutingPlan(ReportedLwM2MClient device);
    
    void sendObservationRequest(ReportedLwM2MClient device);
}
