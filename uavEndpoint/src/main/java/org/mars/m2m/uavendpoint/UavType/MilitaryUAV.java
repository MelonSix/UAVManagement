/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.UavType;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.mars.m2m.Devices.MissileDispatcher;
import org.mars.m2m.Devices.TemperatureSensor;
import org.mars.m2m.Devices.ThreatSensor;
import org.mars.m2m.apiExtension.LeshanClientExt;
import org.mars.m2m.apiExtension.LwM2mObjectInitializer;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;
import org.mars.m2m.uavendpoint.Exceptions.DeviceStarterDetailsException;
import org.mars.m2m.uavendpoint.Helpers.AbstractDevice;
import org.mars.m2m.uavendpoint.Helpers.DeviceHelper;
import org.mars.m2m.uavendpoint.Model.DeviceStarterDetails;
import org.mars.m2m.uavendpoint.Validation.StarterValidator;
import org.mars.m2m.uavendpoint.omaObjects.Device;

/**
 *
 * @author AG BRIGHTER
 */
public class MilitaryUAV implements Runnable {
    /**
     * Keeps track of all devices (lwm2m clients) owned by this particular UAV
     */
    ArrayList<AbstractDevice> uavOwnedDevices = new ArrayList<>();
    
    final UAVConfiguration uavConfig;
    
    /**
     * Default constructor
     */
    public MilitaryUAV() {
        this(null);
    }
    
    /**
     * For initializing the uav configuration object
     * @param config The uav configuration to be used for this UAV
     */
    public MilitaryUAV(UAVConfiguration config) {
        if(config == null)
        this.uavConfig = new UAVConfiguration();
        else
            this.uavConfig = config;
    }    
        
    /**
     * The threat sensor device within the UAV
     * The threat sensor board can be also viewed as consisting of other objects
     */
    public static class ThreatSensorDeviceClient extends AbstractDevice
    {        
        private ThreatSensor threatSensor;
        private Device device;

        public ThreatSensorDeviceClient()
        {
            this(null);
            this.device = new Device();
            this.threatSensor = new ThreatSensor();
        }  
        
        /**
         * For setting up a device within the UAV
         * @param lwm2mClientDetails 
         */
        public ThreatSensorDeviceClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.threatSensor = new ThreatSensor();
            try
            {
                if(lwm2mClientDetails != null)
                {                    
                    this.localHostName = lwm2mClientDetails.getLocalHostName();
                    this.localPort = lwm2mClientDetails.getLocalPort();
                    this.serverHostName = lwm2mClientDetails.getServerHostName();
                    this.serverPort = lwm2mClientDetails.getServerPort();
                    this.objectModelFilename = lwm2mClientDetails.getObjectModelFileName();
                    this.endpointName = lwm2mClientDetails.getEndPointName();
                    
                    StarterValidator.notNull(localHostName);
                    StarterValidator.notWellKnownPort(localPort);
                    StarterValidator.notNull(serverHostName);
                    if(StarterValidator.notPositive(serverPort))
                        throw new DeviceStarterDetailsException("Specified server port has to be positive");
                    
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
                ex.printStackTrace();
            }
        }
        
        /**
         * Starts the device within the UAV
         */
        public synchronized void StartDevice()
        {
            if(canDeviceStart.getAndSet(false))
            {
                /** Monitors the state of the UAV */
                this.deviceStarted = true;
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.LoadCustomObjectModel(this,this.objectModelFilename);

                LwM2mObjectInitializer initializer;
                initializer = new LwM2mObjectInitializer(customModel);
                
                //attach instance
                initializer.setInstanceForObject(12202, threatSensor);
                initializer.setInstanceForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(12202,3,0,1);

                // Create client
                final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
                final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);

                client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
                        enablers));

                // Start the client
                client.start();

                // register to the server provided
                final String endpointIdentifier = this.endpointName ;//UUID.randomUUID().toString();
                
                /**
                 * LwM2M client registration
                 */
                this.registrationID = DeviceHelper.register(client, endpointIdentifier);               
            }
            else
            {
                this.deviceStarted = false;
                System.err.append("Device already started");
            }
        }                    
        
    }
    
    /**
     * 
     */
    public static class MissileDispatchClient extends AbstractDevice
    {
        MissileDispatcher missileDispatch;
        
        public MissileDispatchClient()
        {
            this(null);
            this.missileDispatch = new MissileDispatcher();
        }
        
        /**
         * 
         * @param lwm2mClientDetails 
         */
        public MissileDispatchClient(DeviceStarterDetails lwm2mClientDetails) 
        {
            this.missileDispatch = new MissileDispatcher();
            try
            {
                if(lwm2mClientDetails != null)
                {                    
                    this.localHostName = lwm2mClientDetails.getLocalHostName();
                    this.localPort = lwm2mClientDetails.getLocalPort();
                    this.serverHostName = lwm2mClientDetails.getServerHostName();
                    this.serverPort = lwm2mClientDetails.getServerPort();
                    this.objectModelFilename = lwm2mClientDetails.getObjectModelFileName();
                    this.endpointName = lwm2mClientDetails.getEndPointName();
                    
                    StarterValidator.notNull(localHostName);
                    StarterValidator.notWellKnownPort(localPort);
                    StarterValidator.notNull(serverHostName);
                    if(StarterValidator.notPositive(serverPort))
                        throw new DeviceStarterDetailsException("Specified server port has to be positive");
                    
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
                ex.printStackTrace();
            }
        }
        
               
        /**
         * Starts the device within the UAV
         */
        public synchronized void StartDevice()
        {
            if(canDeviceStart.getAndSet(false))
            {
                /** Monitors the state of the UAV */
                this.deviceStarted = true;
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.LoadCustomObjectModel(this,this.objectModelFilename);

                LwM2mObjectInitializer initializer;
                initializer = new LwM2mObjectInitializer(customModel);
                
                //attach instance
                initializer.setInstanceForObject(12203, missileDispatch);
                List<ObjectEnabler> enablers = initializer.create(12203);

                // Create client
                final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
                final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);

                client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
                        enablers));

                // Start the client
                client.start();

                // register to the server provided
                final String endpointIdentifier = this.endpointName ;//UUID.randomUUID().toString();
                
                /**
                 * LwM2M client registration
                 */
                this.registrationID = DeviceHelper.register(client, endpointIdentifier);               
            }
            else
            {
                this.deviceStarted = false;
                System.err.append("Device already started");
            }
        }
    }
    
    /**
     * 
     */
    public static class TemperatureSensorClient extends AbstractDevice
    {   
        /**
         * Temperature sensor device object for the temperature sensor lwm2m client
         */
        TemperatureSensor tempSensor;
        
        /**
         * Default constructor
         */
        public TemperatureSensorClient() 
        {
            this(null);
            this.tempSensor = new TemperatureSensor();
        }
        
        /**
         * 
         * @param lwm2mClientDetails 
         */
        public TemperatureSensorClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.tempSensor = new TemperatureSensor();
            try
            {
                if(lwm2mClientDetails != null)
                {                    
                    this.localHostName = lwm2mClientDetails.getLocalHostName();
                    this.localPort = lwm2mClientDetails.getLocalPort();
                    this.serverHostName = lwm2mClientDetails.getServerHostName();
                    this.serverPort = lwm2mClientDetails.getServerPort();
                    this.objectModelFilename = lwm2mClientDetails.getObjectModelFileName();
                    this.endpointName = lwm2mClientDetails.getEndPointName();
                    
                    StarterValidator.notNull(localHostName);
                    StarterValidator.notWellKnownPort(localPort);
                    StarterValidator.notNull(serverHostName);
                    if(StarterValidator.notPositive(serverPort))
                        throw new DeviceStarterDetailsException("Specified server port has to be positive");
                    
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
                ex.printStackTrace();
            }
        }
        
        /**
         * Starts the device within the UAV
         */
        public synchronized void StartDevice()
        {
            if(canDeviceStart.getAndSet(false))
            {
                /** Monitors the state of the UAV */
                this.deviceStarted = true;
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.LoadCustomObjectModel(this,this.objectModelFilename);

                LwM2mObjectInitializer initializer;
                initializer = new LwM2mObjectInitializer(customModel);
                
                //attach instance
                initializer.setInstanceForObject(3303, tempSensor);
                List<ObjectEnabler> enablers = initializer.create(3303);

                // Create client
                final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
                final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);

                client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
                        enablers));

                // Start the client
                client.start();

                // register to the server provided
                final String endpointIdentifier = this.endpointName ;//UUID.randomUUID().toString();
                
                /**
                 * LwM2M client registration
                 */
                this.registrationID = DeviceHelper.register(client, endpointIdentifier);               
            }
            else
            {
                this.deviceStarted = false;
                System.err.append("Device already started");
            }
        }
    }
    
    /**
     * 
     */
    public static class FlightControlClient
    {
        
    }
    
    /**
     * Main method
     */
    @Override
    public void run()/* throws InterruptedException*/
    {        
        DeviceStarterDetails threatDevDtls;
        threatDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8080, "127.0.0.1", 5683, "/uavObjectModel.json", "Threat sensor", uavConfig);
        ThreatSensorDeviceClient threatSensorDev = new ThreatSensorDeviceClient(threatDevDtls);
        threatSensorDev.StartDevice();
        //Thread.sleep(10000);
        //DeviceHelper.stopDevice(threatSensorDev);
        
        uavOwnedDevices.add(threatSensorDev);
        
//        DeviceStarterDetails missileDisDtls;
//        missileDisDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
//                8092, "127.0.0.1", 5683, "/uavObjectModel.json", "Missile dispatcher", uavConfig);
//        MissileDispatchClient mislDisClient = new MissileDispatchClient(missileDisDtls);
//        mislDisClient.StartDevice();
//        
//        uavOwnedDevices.add(mislDisClient);
//        
//        DeviceStarterDetails tempSenDtls;
//        tempSenDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
//                8095, "127.0.0.1", 5683, "/uavObjectModel.json", "IPSO Temperature sensor", uavConfig);
//        TemperatureSensorClient tempSenClient = new TemperatureSensorClient(tempSenDtls);
//        tempSenClient.StartDevice();
//        
//        uavOwnedDevices.add(tempSenClient);
    }
    
}