/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import java.util.HashMap;
import java.util.Map;
import org.mars.m2m.demo.controlcenter.analysis.ChartDatastore;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.ui.ControlCenter;

/**
 *
 * @author AG BRIGHTER
 */
public class AnalysisUtils 
{
    
    static final Map<Integer,Integer> messagesPerSecondData_broadcast = new HashMap<>();
    static final Map<Integer,Integer> messagesPerSecondData_register = new HashMap<>();
    
    public static void recordThreatCommunication(Threat threat, AttackerModel attacker) 
    {
        ChartDatastore chartDatastore = new ChartDatastore();
        chartDatastore.putBroadcastRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
        if (threat != null && attacker != null) {
            if (threat.getThreatType().toString().equals(attacker.getAttackerType().toString())) 
            {
                chartDatastore.putRegisterInfoShareRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
            }
        }
    }

    public static void recordObstacleCommunication() {
        ChartDatastore chartDatastore = new ChartDatastore();
        chartDatastore.putBroadcastRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
        chartDatastore.putRegisterInfoShareRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
    }
    
}
