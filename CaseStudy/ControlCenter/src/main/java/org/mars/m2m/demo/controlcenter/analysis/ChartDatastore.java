/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author AG BRIGHTER
 */
public class ChartDatastore 
{
    private final Map<Integer,Integer> messagesPerSecondData_broadcast;
    private final Map<Integer,Integer> messagesPerSecondData_register;
    
    private final Map<Integer,Integer> totalMessages_broadcast;
    private final Map<Integer,Integer> totoalMessages_infoShare;

    public ChartDatastore() {        
        this.totoalMessages_infoShare = new TreeMap<>();
        this.totalMessages_broadcast = new TreeMap<>();
        this.messagesPerSecondData_register = new TreeMap<>();
        this.messagesPerSecondData_broadcast = new TreeMap<>();
    }

    public Map<Integer, Integer> getMessagesPerSecondData_broadcast() {
        synchronized(messagesPerSecondData_broadcast)
        {
            return messagesPerSecondData_broadcast;
        }
    }

    public Map<Integer, Integer> getMessagesPerSecondData_register() {
        synchronized(messagesPerSecondData_register)
        {
            return messagesPerSecondData_register;
        }
    }
    
    public void clearChartDataStore()
    {
        messagesPerSecondData_broadcast.clear();
        messagesPerSecondData_register.clear();
    }

    public Map<Integer, Integer> getTotalMessages_broadcast() {
        synchronized(totalMessages_broadcast)
        {
            System.out.println("broadcast size: "+totalMessages_broadcast.size());
            return totalMessages_broadcast;
        }
    }

    public Map<Integer, Integer> getTotoalMessages_infoShare() {
        synchronized(totoalMessages_infoShare)
        {
            System.out.println("infoshare size: "+totoalMessages_infoShare.size());
            return totoalMessages_infoShare;
        }
    }
    
}
