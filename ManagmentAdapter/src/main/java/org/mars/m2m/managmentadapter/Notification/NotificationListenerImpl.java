/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import java.util.ArrayList;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.managmentadapter.callback.AsyncServiceCallback;
import org.mars.m2m.managmentadapter.client.AsyncServiceConsumer;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;
import org.mars.m2m.managmentadapter.service.AdapterServices;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationListenerImpl implements NotificationListener, AsyncServiceCallback<Response> 
{    
    Map<String,ArrayList<RequestPrimitive>> registry;
    private static int notificationCounter=0;
    
    public NotificationListenerImpl() {
    }

    @Override
    public void sendToOriginator(NotificationObject notificationObject)
    {
        AsyncServiceConsumer sc = new AsyncServiceConsumer();
        AdapterServices adapterServices = new AdapterServices();
        
        registry = NotificationRegistry.getRegistry();
        for(String key : registry.keySet())
        {//Note: key == resource path {objectID}/{instanceID}/{resourceID}
            ArrayList<RequestPrimitive> subscribers = registry.get(key);
            for(RequestPrimitive subscriber : subscribers)
            {//for each subscriber of a resource, notify the subscriber
                sc.handlePost(subscriber.getFrom(), 
                        adapterServices.notificationResponse(subscriber, notificationObject.getData()),
                        MediaType.APPLICATION_XML, this);
            }
        }
    }    

    @Override
    public void asyncServicePerformed(Response response) {
        System.out.println("Notifications sent: "+(++notificationCounter));
    }
    
}
