/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author AG BRIGHTER
 */
public class ChartDatastore 
{
    private final Lock classLock = new ReentrantLock(true);
    private static final Map<Integer,Integer> messagesPerSecondData_broadcast = new TreeMap<>();
    private static final Map<Integer,Integer> messagesPerSecondData_register = new TreeMap<>();

    public ChartDatastore() {        
    }

    public Map<Integer, Integer> getMessagesPerSecondData_broadcast() {
        classLock.lock();
        try 
        {
            return Collections.unmodifiableMap(messagesPerSecondData_broadcast);
        } finally {
            classLock.unlock();
        }
    }

    public Map<Integer, Integer> getMessagesPerSecondData_register() {
        classLock.lock();
        try 
        {
            return Collections.unmodifiableMap(messagesPerSecondData_register);
        } finally {
            classLock.unlock();
        }
    }
    
    public void clearChartDataStore()
    {
        messagesPerSecondData_broadcast.clear();
        messagesPerSecondData_register.clear();
    }
    
    public void putBroadcastRecord(int t, int v)
    {
        classLock.lock();
        try 
        {
            if(messagesPerSecondData_broadcast.containsKey(t))
            {
                int currentVal = messagesPerSecondData_broadcast.get(t);
                ++currentVal;
                messagesPerSecondData_broadcast.put(t, currentVal);
            }
            else
            {
                messagesPerSecondData_broadcast.put(t, v);
            }
        } finally 
        {
            classLock.unlock();
        }
    }
    
    public void putRegisterInfoShareRecord(int t, int v)
    {
        classLock.lock();
        try 
        {
            if(messagesPerSecondData_register.containsKey(t))
            {
                int currentVal = messagesPerSecondData_register.get(t);
                ++currentVal;
                messagesPerSecondData_register.put(t, currentVal);
            }
            else
            {
                messagesPerSecondData_register.put(t, v);
            }
        } finally 
        {
            classLock.unlock();
        }
    }
    
    public Map<Integer, Integer> getTotalMessagesSentInTimestep_broadcast()
    {
        classLock.lock();
        try 
        {
            Map<Integer, Integer> syncData = Collections.synchronizedMap(messagesPerSecondData_broadcast);
            Map<Integer,Integer> totalMessages = new TreeMap<>();
            int total = 0;
            for(Integer t : syncData.keySet())
            {
                total += syncData.get(t);
                totalMessages.put(t, total);
            }
            return totalMessages;
        } finally {
            classLock.unlock();
        }
    }
    
    public Map<Integer, Integer> getTotalMessagesSentInTimestep_register()
    {
        classLock.lock();
        try 
        {
            Map<Integer, Integer> syncData = Collections.synchronizedMap(messagesPerSecondData_register);
            Map<Integer,Integer> totalMessages = new TreeMap<>();
            int total = 0;
            for(Integer t : syncData.keySet())
            {
                total += syncData.get(t);
                totalMessages.put(t, total);
            }
            return totalMessages;
        } finally {
            classLock.unlock();
        }
    }
}
