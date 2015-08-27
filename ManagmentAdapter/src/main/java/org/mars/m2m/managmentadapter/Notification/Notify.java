/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.Notification;

import java.util.ArrayList;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class Notify 
{
    /**
     * Houses all the notification listener implementations
     */
    private ArrayList<NotificationListener> notifyListeners;

    public Notify() {
        notifyListeners = new ArrayList<>();
    }
    
    
    /**
     * Constructor for creating an object of this class with existing 
     * NotificationListener implementations
     * @param notifyListeners The existing implementations
     */
    public Notify(ArrayList<NotificationListener> notifyListeners) {
        this.notifyListeners = notifyListeners;
    }
    
    /**
     * Registers a notification listener
     * @param listener 
     */
    public void addNotificationListener(NotificationListener listener)
    {
        if(listener != null)
        {
            notifyListeners.add(listener);
        }
    }
    
    /**
     * de-registers a registered notification listener
     * @param listener 
     */
    public void removeNotificationListener(NotificationListener listener)
    {
        if(listener != null)
        {
            notifyListeners.remove(listener);
        }
    }
    
    /**
     * Calls the registered listeners to perform their respective send implementations
     * @param notificationRegistry The object used to access the static registry
     * containing all currently registered subscribers of an observation
     * @param data The notification content
     */
    public void sendNotification(NotificationRegistry notificationRegistry, Object data)
    {
        for(NotificationListener listener : this.notifyListeners)
        {
            listener.sendToOriginator(new NotificationObject(this, notificationRegistry, data));
        }
    }
}
