/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Helpers;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.request.DeregisterRequest;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.response.RegisterResponse;

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
     * @param client The client that calls this method
     * @param objectModelStr the filename with its extension e.g. myObjectModel.json
     * @return The LwM2M model created from the object model file
     */
    public static synchronized LwM2mModel LoadCustomObjectModel(Object client, String objectModelStr) 
    {
        InputStream myJsonStr;
        myJsonStr = client.getClass().getResourceAsStream(objectModelStr);
        LwM2mModel customModel;
        HashMap<Integer, ObjectModel> map = null;
        if(myJsonStr != null)
        {
            List<ObjectModel> objectModels = ObjectLoader.loadJsonStream(myJsonStr);
            map = new HashMap<>();
            for (ObjectModel objectModel : objectModels) {
                map.put(objectModel.id, objectModel);
            }
        }
        else
        {
            try {
                throw new Exception("Error loading json file");
            } catch (Exception ex) {
                Logger.getLogger(client.getClass().toString()).log(Level.SEVERE, null, ex);
            }
        }
        customModel = new LwM2mModel(map);
        return customModel;
    }  
        
    /**
     * Registers a LwM2M client with a LwM2M server
     * @param client The client to be registered
     * @param endpointIdentifier The endpoint's name
     * @return The registration ID assigned by the server
     */
    public static synchronized String register(final LeshanClient client, final String endpointIdentifier) {
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
    
}
