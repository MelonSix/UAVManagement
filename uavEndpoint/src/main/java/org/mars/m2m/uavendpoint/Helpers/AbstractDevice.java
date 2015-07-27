/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Helpers;

import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.leshan.client.californium.LeshanClient;

/**
 *
 * @author AG BRIGHTER
 * This class groups together all the common elements that used by the various lwm2m clients of a device
 */
public abstract class AbstractDevice {
    
    /**
     * Members that are common amongst all LwM2M clients
     */
    protected String localHostName;
    protected int localPort;
    protected String serverHostName;
    protected int serverPort;
    protected String objectModelFilename;
    protected AtomicBoolean canDeviceStart = new AtomicBoolean();
    protected boolean deviceStarted = false;
    protected String registrationID;
    protected String endpointName;

    /**
     * The leshan client / CoAP server for a device
     */
    protected LeshanClient client;
}
