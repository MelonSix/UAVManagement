/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mars.m2m.demo.controlcenter.analysis.ChartDatastore;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
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
    

    public static void recordCommunication() {
        synchronized(ControlCenter.chartDatastore)
        {
            ControlCenter.chartDatastore.getMessagesPerSecondData_broadcast().putAll(getMessagesPerSecondData_broadcast());
            ControlCenter.chartDatastore.getMessagesPerSecondData_register().putAll(getMessagesPerSecondData_register());
            setTotalMessagesSentInTimestep_broadcast();
            setTotalMessagesSentInTimestep_register();
        }
        
    }
    
    public static void getDataForAnalysis_registerbased() 
    {
        synchronized (messagesPerSecondData_register)
        {
            messagesPerSecondData_register.put(ControlCenter.CURRENT_SIMULATION_TIME.get(), 
                CC_StaticInitConfig.TOTAL_MESSAGES_SENT_IN_CURRENT_SIMULATION_TIMESTEP_registerbased.incrementAndGet());
        }
    }

    public static void getDataForAnalysis_broadcast() {
        synchronized (messagesPerSecondData_broadcast)
        {
            messagesPerSecondData_broadcast.put(ControlCenter.CURRENT_SIMULATION_TIME.get(), 
                CC_StaticInitConfig.TOTAL_MESSAGES_SENT_IN_CURRENT_SIMULATION_TIMESTEP_broadcast.incrementAndGet());
        }
    }

    public static Map<Integer, Integer> getMessagesPerSecondData_register() {
        synchronized (messagesPerSecondData_register) {
            return Collections.unmodifiableMap(messagesPerSecondData_register);
        }
    }

    public static Map<Integer, Integer> getMessagesPerSecondData_broadcast() {
        synchronized (messagesPerSecondData_broadcast) {
            return Collections.unmodifiableMap(messagesPerSecondData_broadcast);
        }
    }

    public static void recordThreatCommunication(Threat threat, AttackerModel attacker) {
        getDataForAnalysis_broadcast();
        if (threat != null && attacker != null) {
            if (threat.getThreatType().toString().equals(attacker.getAttackerType().toString())) {
                getDataForAnalysis_registerbased();
            }
        }
    }
    
    public static void setTotalMessagesSentInTimestep_broadcast()
    {
        Map<Integer, Integer> syncData = Collections.synchronizedMap(getMessagesPerSecondData_broadcast());
        Map<Integer,Integer> totalMessages = new HashMap<>();
        int total = 0;
        for(Integer t : syncData.keySet())
        {
            total += syncData.get(t);
            totalMessages.put(t, total);
        }
        ControlCenter.chartDatastore.getTotalMessages_broadcast().putAll(totalMessages); 
    }
    
    public static void setTotalMessagesSentInTimestep_register()
    {
        Map<Integer, Integer> syncData = Collections.synchronizedMap(getMessagesPerSecondData_register());
        Map<Integer,Integer> totalMessages = new HashMap<>();
        int total = 0;
        for(Integer t : syncData.keySet())
        {
            total += syncData.get(t);
            totalMessages.put(t, total);
        }
        ControlCenter.chartDatastore.getTotoalMessages_infoShare().putAll(totalMessages);
    }

    public static void recordObstacleCommunication() {
        getDataForAnalysis_broadcast();
        getDataForAnalysis_registerbased();
    }
    
}
