/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AG BRIGHTER
 */
public class ChartDatastore 
{
    //Map<Time,NumberOfMessagesSent>
    private static final Map<Integer,Integer> messagesPerSecondData_broadcast = new HashMap<>();
    private static final Map<Integer,Integer> messagesPerSecondData_register = new HashMap<>();

    public ChartDatastore() {        
    }

    public static Map<Integer, Integer> getMessagesPerSecondData_broadcast() {
        synchronized(messagesPerSecondData_broadcast)
        {
            return messagesPerSecondData_broadcast;
        }
    }

    public static Map<Integer, Integer> getMessagesPerSecondData_register() {
        synchronized(messagesPerSecondData_register)
        {
            return messagesPerSecondData_register;
        }
    }
    
    public static synchronized void clearGraphDataStore()
    {
        messagesPerSecondData_broadcast.clear();
        messagesPerSecondData_register.clear();
    }
    
}
