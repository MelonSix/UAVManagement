/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.LwM2mClient;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.request.DeregisterRequest;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.response.RegisterResponse;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig.ServerConfig;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig.ServerSecurity;
import org.mars.m2m.dmcore.Loader.LwM2mModelLoader;
import org.mars.m2m.uavendpoint.Bootstrap.BootstrapEp;
import org.mars.m2m.uavendpoint.Model.RequiredBootstrapInfo;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mSecurity;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mServer;

/**
 *
 * @author AG BRIGHTER
 */
public class DeviceHelper {
    
    /**
     * Stops a device already started
     * @param deviceClient
     */
    public static synchronized void stopDevice(AbstractDevice deviceClient)
    {
        if (deviceClient.registrationID != null) {
            System.out.println("\tDevice: Deregistering Client '" + deviceClient.registrationID + "'");
            deviceClient.canDeviceStart.set(true);
            deviceClient.client.send(new DeregisterRequest(deviceClient.registrationID));
            deviceClient.client.stop();
        }
    }  
            
    /**
     * Loads the Json file that contains the custom object model
     * @param objectModelStr the filename with its extension e.g. myObjectModel.json
     * @return The LwM2M model created from the object model file
     */
    public static synchronized LwM2mModel getObjectModel(String objectModelStr) 
    {
        LwM2mModel customModel;        
        customModel = LwM2mModelLoader.loadCustomObjectModel(objectModelStr);
        return customModel;
    }  
        
    /**
     * Registers a LwM2M client with a LwM2M server
     * @param client The client to be registered
     * @param endpointIdentifier The endpoint's name
     * @return The registration ID assigned by the server
     */
    public static synchronized String register(final LwM2mClient client, final String endpointIdentifier) {
        String registrationID=null;
        RegisterResponse response = client.send(new RegisterRequest(endpointIdentifier));

        // Report registration response.
        System.out.println("Device Registration (Success? " + response.getCode() + ")");
        if (response.getCode() == ResponseCode.CREATED) {
            System.out.println("\tDevice: Registered Client Location '" + response.getRegistrationID() + "'");
            registrationID = response.getRegistrationID();
        } else {
            // TODO Should we have a error message on response ?
            // System.err.println("\tDevice Registration Error: " + response.getErrorMessage());
            System.err.println("\tDevice Registration Error: " + response.getCode());
            System.err
                    .println("If you're having issues connecting to the LWM2M endpoint, try using the DTLS port instead");
        }
        return registrationID;
    }
    
    /**
     * Registers a LwM2M client with a LwM2M server using bootstrap information
     * @param client The client to be registered
     * @param endpointIdentifier The endpoint's name
     * @param sec Server configuration details
     * @param server Server security details for registration
     * @param sourceAddress
     * @param sourcePort
     * @return The registration ID assigned by the server
     */
    public static synchronized String register(final LwM2mClient client, final String endpointIdentifier, 
                                    OmaLwM2mSecurity sec, OmaLwM2mServer server, String sourceAddress, int sourcePort) 
    {
        try 
        {
            String registrationID=null;
            String[] lwm2mServerDetails = sec.getUri().split(":");//coap://ipaddr:port -> [coap][ipaddr][port]
            RegisterResponse response;
            RegisterRequest request = new RegisterRequest(endpointIdentifier,//endpoint Name
                    (long)server.getLifetime(),//lifetime
                    "LWM2M v1",//lwVersion
                    server.getBinding(),//binding Mode
                    sec.getServerSmsNumber(),//sms Number
                    null,//object Links
                    InetAddress.getByName(sourceAddress), // source Address
                    sourcePort,//source Port
                    new InetSocketAddress(lwm2mServerDetails[1], Integer.parseInt(lwm2mServerDetails[2])),//registrationEndpoint
                    new String(sec.getSecretKey()),//pskIdentity
                    null//public key
            );
            response = client.send(request);
            
            // Report registration response.
            System.out.println("Device Registration (Success? " + response.getCode() + ")");
            if (response.getCode() == ResponseCode.CREATED) {
                System.out.println("\tDevice: Registered Client Location '" + response.getRegistrationID() + "'");
                registrationID = response.getRegistrationID();
            } else {
                // TODO Should we have a error message on response ?
                // System.err.println("\tDevice Registration Error: " + response.getErrorMessage());
                System.err.println("\tDevice Registration Error: " + response.getCode());
                System.err
                        .println("If you're having issues connecting to the LWM2M endpoint, try using the DTLS port instead");
            }
            return registrationID;
        } catch (UnknownHostException ex) {
            Logger.getLogger(DeviceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * For performing bootstrap and getting the bootstrap information
     * @param info Holds the details to connect to the Bootstrap server and get the payload back
     * @return The bootstrap information as a byte array
     */
    public static byte[] bootStrapLwM2mClient(RequiredBootstrapInfo info) throws InterruptedException
    {
        if(info == null)
            return null;
        BootstrapEp bootstrapEp = new BootstrapEp(new InetSocketAddress(info.getServerAddr(), info.getServerPortnum()), info.getLwM2mClient());
        return bootstrapEp.performBootstrap(info.getEndpoint());
    }
    
}
