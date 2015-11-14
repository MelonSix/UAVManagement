/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AG BRIGHTER
 */
public class GraphDatastore 
{
    //Map<Time,NumberOfMessagesSent>
    private static final Map<Integer,Integer> messagesPerSecondData = new HashMap<>();;

    public GraphDatastore() {        
    }

    public synchronized Map<Integer, Integer> getMessagesPerSecondData() {
        return Collections.unmodifiableMap(messagesPerSecondData);
    }
    
    public synchronized void clearGraphDataStore()
    {
        messagesPerSecondData.clear();
    }
    
}
