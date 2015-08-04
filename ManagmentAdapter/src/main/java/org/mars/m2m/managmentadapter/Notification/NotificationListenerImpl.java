/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class NotificationListenerImpl implements NotificationListener
{    

    public NotificationListenerImpl() {
    }

    @Override
    public void sendToOriginator(NotificationObject notificationObject) {
        System.out.println("Fired event from sendToOriginator");
    }    
    
}
