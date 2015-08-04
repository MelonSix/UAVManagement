/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import org.mars.m2m.managmentadapter.model.NotificationRegistry;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationListenerImpl implements NotificationListener
{    
    Notify notify;
    
    public NotificationListenerImpl() {
        this.notify = new Notify();
        notify.addNotificationListener(this);
    }
    
    public void sendNotification(NotificationRegistry registry)
    {
        notify.sendNotification(registry);
    }

    @Override
    public void sendToOriginator(NotificationObject notificationObject) {
        System.out.println("Fired event from sendToOriginator");
    }    
    
}
