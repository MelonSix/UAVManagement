/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.mars.m2m.demo.controlcenter.model.Message;

/**
 * This class is used to keep the record of all communicated threats, conflicts, and obstacles
 * that have been sent to an endpoint
 * @author AG BRIGHTER
 */
public class MessageHistory 
{
    private final Map<String, ArrayList<Message>> communicatedThreats;
    private final Map<String, ArrayList<Message>> communicatedConflicts;
    private final Map<String, ArrayList<Message>> communicatedObstacles;

    public MessageHistory() 
    {
        this.communicatedObstacles = new HashMap<>();
        this.communicatedConflicts = new HashMap<>();
        this.communicatedThreats = new HashMap<>();
    }

    public synchronized Map<String, ArrayList<Message>> getCommunicatedThreats() {
        return communicatedThreats;
    }

    public synchronized Map<String, ArrayList<Message>> getCommunicatedConflicts() {
        return communicatedConflicts;
    }

    public synchronized Map<String, ArrayList<Message>> getCommunicatedObstacles() {
        return communicatedObstacles;
    }
}
