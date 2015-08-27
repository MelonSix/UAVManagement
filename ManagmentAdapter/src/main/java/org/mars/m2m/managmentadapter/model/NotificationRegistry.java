/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationRegistry 
{
    private static Map<String,ArrayList<RequestPrimitive>> registry = new HashMap<>();

    public NotificationRegistry() {
        
    }

    public static Map<String, ArrayList<RequestPrimitive>> getRegistry() {
        return registry;
    }

    public static void setRegistry(Map<String, ArrayList<RequestPrimitive>> registry) {
        NotificationRegistry.registry = registry;
    }
    
    public static void updateSubscribers(String key, ArrayList<RequestPrimitive> subscribers)
    {
        registry.replace(key, subscribers);
    }
    
    public static void setSubscribers(String key, ArrayList<RequestPrimitive> subscribers)
    {
        registry.put(key, subscribers);
    }
}
