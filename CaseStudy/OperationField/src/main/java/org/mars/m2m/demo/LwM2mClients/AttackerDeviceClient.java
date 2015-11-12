/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.LwM2mClients;

import ch.qos.logback.classic.Logger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.californium.core.CoapServer;
import org.mars.m2m.apiExtension.LeshanClientExt;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.util.Validate;
import org.mars.m2m.demo.Devices.UavAttackerDevice;
import org.mars.m2m.demo.eventHandling.callerImpl.InterceptRequestImpl;
import org.mars.m2m.uavendpoint.Exceptions.DeviceStarterDetailsException;
import org.mars.m2m.uavendpoint.Model.DeviceStarterDetails;
import org.mars.m2m.uavendpoint.Validation.StarterValidator;
import org.mars.m2m.uavendpoint.omaObjects.Device;
import org.mars.m2m.uavendpoint.util.AbstractDevice;
import org.mars.m2m.uavendpoint.util.BootstrapedRegistrationHandler;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AttackerDeviceClient extends AbstractDevice
{
   private Logger log = (Logger) LoggerFactory.getLogger(AttackerDeviceClient.class);
   private Device device;
   private BootstrapedRegistrationHandler bsRegHandler;
   private LwM2mModel uavLwM2mModel;
    private final UavAttackerDevice attackerDevice;

   /**
    * For setting up a device within the UAV
     * @param lwM2mObjModel The object model to be used by the client
    * @param lwm2mClientDetails 
     * @param attacker 
    */
   public AttackerDeviceClient(LwM2mModel lwM2mObjModel, DeviceStarterDetails lwm2mClientDetails, UavAttackerDevice attacker)
   {
       this.bsRegHandler = new BootstrapedRegistrationHandler();
       this.device = new Device();
       this.attackerDevice = attacker;
       
       try
       {
           if(lwm2mClientDetails != null)
           {     
               Validate.notNull(lwM2mObjModel);
               this.uavLwM2mModel = lwM2mObjModel;
               this.localHostName = lwm2mClientDetails.getLocalHostName();
               this.localPort = lwm2mClientDetails.getLocalPort();
               this.serverHostName = lwm2mClientDetails.getServerHostName();
               this.serverPort = lwm2mClientDetails.getServerPort();
               this.objectModelFilename = lwm2mClientDetails.getObjectModelFileName();
               this.endpointName = lwm2mClientDetails.getEndPointName();
               this.bsAddress = lwm2mClientDetails.getBsAddress();
               this.bsPortnumber = lwm2mClientDetails.getBsPortnumber();

               StarterValidator.notNull(localHostName);
               StarterValidator.notWellKnownPort(localPort);
               StarterValidator.notNull(serverHostName);
               if(StarterValidator.notPositive(serverPort)) {
                   throw new DeviceStarterDetailsException("Specified server port has to be positive");
               }

               canDeviceStart.set(true);
           }
           else
           {
               System.out.println("Starter details cannot be null");
               throw new DeviceStarterDetailsException("Device starter details error");
           }
       }
       catch(Exception ex)
       {
           log.error(ex.toString());
       }
   }

   /**
    * Starts the device within the UAV
    */
   @Override
   public synchronized void StartDevice()
   {
       if(canDeviceStart.getAndSet(false))
       {
           /** Monitors the state of the UAV */
           this.deviceStarted = true;

           ObjectsInitializer initializer;
           initializer = new ObjectsInitializer(this.uavLwM2mModel);

           //attach instance
           initializer.setInstancesForObject(12207, attackerDevice);
           initializer.setInstancesForObject(3, device);
           List<ObjectEnabler> enablers = initializer.create(12207,3);

           // Create client
           final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
           //final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);

           CoapServer coapServer = new CoapServer();
           client = new LeshanClientExt(clientAddress, coapServer, new ArrayList<LwM2mObjectEnabler>(
                   enablers));
           client.addInterceptor(new InterceptRequestImpl());

           // Start the client
           client.start();

           //performs registration using bootstrap approach on the client
           bsRegHandler.doBsRegistration(initializer, coapServer, clientAddress, this);

           /**
            * LwM2M client registration
            */
           //this.registrationID = DeviceHelper.register(client, endpointIdentifier);               
       }
       else
       {
           this.deviceStarted = false;
           System.err.append("Device already started");
       }
   }        

    public Device getDevice() {
        return device;
    }
    
}
