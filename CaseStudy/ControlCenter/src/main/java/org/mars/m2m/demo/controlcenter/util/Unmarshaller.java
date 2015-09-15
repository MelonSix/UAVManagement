/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.mars.m2m.demo.controlcenter.enumeration.NotificationType;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Notification;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class Unmarshaller 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Unmarshaller.class);

    public Unmarshaller() {
    }
    
    /**
     * Gets the data(which is in json format) out of the container resource
     * @param data
     * @return json Content received from the endpoint through the MA and MS
     */
    public static String getJsonContent(RequestPrimitive data)
    {
        Container container = (Container) data.getContent().getAny().get(0);
        ContentInstance contentInstance = (ContentInstance) container.getContentInstanceOrContainerOrSubscription().get(0);
        return contentInstance.getContent().toString();
    }
    
    /**
     * Gets an extracted notification information in json and returns a {@link Notification} object
     * @param extractedJson
     * @return 
     */
    public static Notification getNotificationObject(String extractedJson)
    {
        try {
            Gson gson = new Gson();
            Notification notification = gson.fromJson(extractedJson, Notification.class);
            return notification;
        } catch (JsonSyntaxException jsonSyntaxException) {
            logger.error(jsonSyntaxException.toString());
            return null;
        }
    }
    
    /**
     * Determine whether a notification is a threat or obstacle or conflict notification
     * using the path of the endpoint resource sending the notification
     * @param notification
     * @return The type of notification
     */
    public static NotificationType determineNotificationType(Notification notification)
    {
        try 
        {
            //e.g. "path": "/12206/0/0" -> [][12206][0][0]
            int objectID = Integer.parseInt(notification.getPath().split("/")[1]);            
            switch (objectID) {
                case 12206:
                    return NotificationType.OBSTACLE;
                case 12202:
                    return NotificationType.THREAT;
                default:
                    return NotificationType.INVALID;
            }
        } catch (NumberFormatException e) {
           logger.error(e.toString());
           return NotificationType.INVALID;
        }
    }
    
    /**
     * Gets a required object from a notification
     * @param n Notification object
     * @param clazz
     * @return 
     */
    public static Object getObjectFromNotification(Notification n, Class clazz)
    {
        try 
        { 
            Gson gson = new Gson();
            return gson.fromJson(n.getContent().getValue().toString(), clazz);
        } catch (JsonSyntaxException e) {
            logger.error(e.toString());
            return null;
        }
    }
}
