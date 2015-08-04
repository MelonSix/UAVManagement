/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import ch.qos.logback.classic.Logger;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Notification;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Subscription;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class NotificationBuilder 
{
    private final Logger logger = (Logger) LoggerFactory.getLogger(NotificationBuilder.class);
    
    private final Subscription subscription;
    
    private Notification.NotificationEvent.OperationMonitor operationMonitor;
    
    private Notification.NotificationEvent notificationEvent;
    
    private Notification notification;

    public NotificationBuilder(Subscription subscription) {
        this.subscription = subscription;
    }
    
    public NotificationBuilder setUpOperationMonitor()
    {
        return this;
    }
    
    public NotificationBuilder setUpnotificationEvent()
    {
        return this;
    }
    
    public Notification build()
    {
        return this.notification;
    }
}
