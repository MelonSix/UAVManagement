/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mars.m2m.demo.controlcenter.analysis.ChartDatastore;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.ui.ControlCenter;

/**
 *
 * @author AG BRIGHTER
 */
public class AnalysisUtils 
{
    
    static final Map<Integer,ArrayList<Integer>> communicatedObstacles = new HashMap<>();
    static final Map<Integer,ArrayList<Integer>> communicatedThreats = new HashMap<>();
    
    public synchronized static void recordThreatCommunication(Threat threat, AttackerModel attacker) 
    {
        if(isRecordNotExisting(threat.getIndex(), attacker.getIndex(), communicatedThreats))
        {
            ChartDatastore chartDatastore = new ChartDatastore();
            chartDatastore.putBroadcastRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
            if (threat.getThreatType().toString().equals(attacker.getAttackerType().toString())) 
            {
                chartDatastore.putRegisterInfoShareRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
            }
            addCommunicatedRecord(threat.getIndex(), attacker.getIndex(), communicatedThreats);
        }
    }

    public synchronized static void recordObstacleCommunication(Obstacle obs, AttackerModel attacker)
    {
        if(isRecordNotExisting(obs.getIndex(), attacker.getIndex(), communicatedObstacles))
        {
            ChartDatastore chartDatastore = new ChartDatastore();
            chartDatastore.putBroadcastRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
            chartDatastore.putRegisterInfoShareRecord(ControlCenter.CURRENT_SIMULATION_TIME.get(), 1);
            addCommunicatedRecord(obs.getIndex(), attacker.getIndex(), communicatedObstacles);
        }
        
    }
    
    public static boolean isRecordNotExisting(int messageIndex, int uavIndex, Map<Integer,ArrayList<Integer>> dataList)
    {
        Map<Integer, ArrayList<Integer>> syncMap = Collections.synchronizedMap(dataList);
        if(syncMap.containsKey(messageIndex))
        {
            if(syncMap.get(messageIndex).contains(uavIndex))
            {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }
    
    public static void addCommunicatedRecord(int messageIndex, int uavIndex, final Map<Integer,ArrayList<Integer>> dataList)
    {
        synchronized(dataList)
        {
           if(dataList.containsKey(messageIndex))
           {
               dataList.get(messageIndex).add(uavIndex);
           }
           else
           {
               ArrayList<Integer> list = new ArrayList<>();
               list.add(uavIndex);
               dataList.put(messageIndex, list);
           }
        }
    }
}
