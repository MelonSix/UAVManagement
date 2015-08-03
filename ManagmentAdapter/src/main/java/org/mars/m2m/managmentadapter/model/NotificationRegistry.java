/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationRegistry 
{
    private static Map<String,List<String>> registry = new HashMap<>();

    public NotificationRegistry() {
        
    }

    public static Map<String, List<String>> getRegistry() {
        return registry;
    }

    public static void setRegistry(Map<String, List<String>> registry) {
        NotificationRegistry.registry = registry;
    }    
    
}
