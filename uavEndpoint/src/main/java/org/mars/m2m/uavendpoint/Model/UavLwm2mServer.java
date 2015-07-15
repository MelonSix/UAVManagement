/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *For keeping track of all LWM2M servers that a UAV is connected to
 * @author AG BRIGHTER
 */
public class UavLwm2mServer {
    
    private String lwm2mServerAddress; 
    private String lwm2mServerPort;
    private boolean isBootStrapServer=false;
    private String description;
    private Date dateAdded;
    private List notifications;
    
    /**
     * Resource definitions of LWM2M server
     */
    private int registrationLifetime;       //in seconds
    private int defaultObservationDelay;    //in seconds
    private int defaultMaxObservTime;       //in seconds
    private int diabledTimeout = 86400;     //in seconds
    private boolean canBeDisabled = false;
    private boolean storeNotifications = true;
    private boolean isOnline;
    
    public PublicKey lwm2mServerPubKey;
    
    //public static String LWM2MSERVER_PUBLIC_KEY = "1dae121ba406802ef07c193c1ee4df91115aabd79c1ed7f4c0ef7ef6a5449400";
    
    /**
     * Instantiates an lwm2m server record with default description
     */
    public UavLwm2mServer() {
        this(null);
    } 
    
    /**
     * Instantiates a lwm2m server record
     * @param toStringValue The value to be added to the toString of the object
     */
    public UavLwm2mServer(String toStringValue) {
        this.description = (toStringValue != null)? toStringValue :"";
        dateAdded = new Date();
        notifications = new ArrayList<Notification>();
    }

    public String getLwm2mServerAddress() {
        return lwm2mServerAddress;
    }

    public void setLwm2mServerAddress(String lwm2mServerAddress) {
        this.lwm2mServerAddress = lwm2mServerAddress;
    }

    public String getLwm2mServerPort() {
        return lwm2mServerPort;
    }

    public void setLwm2mServerPort(String lwm2mServerPort) {
        this.lwm2mServerPort = lwm2mServerPort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }  

    public boolean isIsBootStrapServer() {
        return isBootStrapServer;
    }

    public void setIsBootStrapServer(boolean isBootStrapServer) {
        this.isBootStrapServer = isBootStrapServer;
    }

    public PublicKey getLwm2mServerPubKey() {
        return lwm2mServerPubKey;
    }

    public void setLwm2mServerPubKey(PublicKey lwm2mServerPubKey) {
        this.lwm2mServerPubKey = lwm2mServerPubKey;
    }  
    
}
