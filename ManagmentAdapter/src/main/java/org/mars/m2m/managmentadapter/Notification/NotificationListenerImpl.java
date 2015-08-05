/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import java.util.ArrayList;
import java.util.Map;
import org.mars.m2m.managmentadapter.client.ServiceConsumer;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationListenerImpl implements NotificationListener
{    
    Map<String,ArrayList<String>> registry;
    
    public NotificationListenerImpl() {
        //this.notify = new Notify();
        //notify.addNotificationListener(this);
    }
    
//    public void sendNotification(NotificationRegistry registry)
//    {
//        notify.sendNotification(registry);
//    }

    @Override
    public void sendToOriginator(NotificationObject notificationObject)
    {
        ServiceConsumer sc = new ServiceConsumer();
        
        registry = NotificationRegistry.getRegistry();
        for(String key : registry.keySet())
        {//Note: key == resource path {objectID}/{instanceID}/{resourceID}
            ArrayList<String> subscribers = registry.get(key);
            for(String subscriber : subscribers)
            {//for each subscriber of a resource, notify the subscriber
                sc.handlePost(subscriber, notificationObject.getData());
            }
        }
        //System.out.println("Fired event from sendToOriginator, data: "+notificationObject.getData());
    }    
    
}
