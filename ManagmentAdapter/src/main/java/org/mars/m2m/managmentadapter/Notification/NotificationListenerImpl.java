/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import java.util.ArrayList;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.managmentadapter.client.ServiceConsumer;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;
import org.mars.m2m.managmentadapter.service.AdapterServices;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationListenerImpl implements NotificationListener
{    
    Map<String,ArrayList<RequestPrimitive>> registry;
    
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
        AdapterServices adapterServices = new AdapterServices();
        
        registry = NotificationRegistry.getRegistry();
        for(String key : registry.keySet())
        {//Note: key == resource path {objectID}/{instanceID}/{resourceID}
            ArrayList<RequestPrimitive> subscribers = registry.get(key);
            for(RequestPrimitive subscriber : subscribers)
            {//for each subscriber of a resource, notify the subscriber
                sc.handlePost(subscriber.getFrom(), 
                        adapterServices.notificationResponse(subscriber, notificationObject.getData().toString()),
                        MediaType.APPLICATION_XML_TYPE);
            }
        }
        //System.out.println("Fired event from sendToOriginator, data: "+notificationObject.getData());
    }    
    
}
