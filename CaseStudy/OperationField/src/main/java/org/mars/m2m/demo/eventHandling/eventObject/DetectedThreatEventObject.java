/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.eventObject;

import org.mars.m2m.demo.model.Threat;
import org.mars.m2m.demo.uav.Scout;

/**
 *
 * @author AG BRIGHTER
 */
public class DetectedThreatEventObject 
{
    private Scout scout;
    private Threat threat;

    public DetectedThreatEventObject() {
    }

    public DetectedThreatEventObject(Scout scout, Threat threat) {
        this.scout = scout;
        this.threat = threat;
    }
    
    public Scout getScout() {
        return scout;
    }

    public void setScout(Scout scout) {
        this.scout = scout;
    }

    public Threat getThreat() {
        return threat;
    }

    public void setThreat(Threat threat) {
        this.threat = threat;
    }

    
}
