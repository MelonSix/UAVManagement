/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.util;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientExt;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig;
import org.mars.m2m.uavendpoint.Model.RegisteredClientDataList;

/**
 *This class groups together all the common elements that used by the various LwM2M clients of a device
 * @author AG BRIGHTER
 * 
 */
public abstract class AbstractDevice {
    
    /**
     * LwM2M client address
     */
    protected String localHostName;
    
    /**
     * LwM2M client port
     */
    protected int localPort;
    
    /**
     * LwM2M server address
     */
    protected String serverHostName;
    
    /**
     * LwM2M client port
     */
    protected int serverPort;
    
    /**
     * File containing object models
     */
    protected String objectModelFilename;
    
    /**
     * Monitors whether a client can start
     */
    protected AtomicBoolean canDeviceStart = new AtomicBoolean();
    
    /**
     * Determines whether a LwM2M client has already started
     */
    protected boolean deviceStarted = false;
    
    /**
     * Registration ID given to the LwM2M client after a successful registration
     */
    protected String registrationID;
    
    /**
     * The endpoint name or ID for the LwM2M client
     */
    protected String endpointName;

    /**
     * The leshan client / CoAP server for a device
     */
    protected LeshanClientExt client;
        
    /**
     * LwM2M Bootstrap server address
     */
    protected String bsAddress;
    
    /**
     * LwM2M Bootstrap server port number
     */
    protected int bsPortnumber;
    
    /**
     * LwM2M client Server instances
     */
    protected Map<Integer, BootstrapConfig.ServerConfig> servers;
    
    /**
     * LwM2M client OmaLwM2mSecurity instances
     */
    protected Map<Integer, BootstrapConfig.ServerSecurity> security;
    
    /**
     * Keeps all successful registrations to different LWM2M servers of the client
     */
    protected RegisteredClientDataList registeredClientDataList;
}
