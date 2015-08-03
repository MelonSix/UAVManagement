/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import java.awt.event.ActionListener;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public interface NotificationListener extends ActionListener
{
    public void sendToOriginator(NotificationObject notificationObject);
}
