/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import java.util.EventObject;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationObject extends EventObject
{
    private final NotificationRegistry notificationRegistry;

    public NotificationObject(Object source, NotificationRegistry notificationRegistry) {
        super(source);
        this.notificationRegistry = notificationRegistry;
    }

    public NotificationRegistry getNotificationRegistry() {
        return notificationRegistry;
    }   
    
    
}
