/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.UavType;

import ch.qos.logback.classic.Logger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.impl.ObjectResource;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig;
import org.mars.m2m.Devices.AltitudeSensor;
import org.mars.m2m.Devices.FlightControl;
import org.mars.m2m.Devices.MissileDispatcher;
import org.mars.m2m.Devices.TemperatureSensor;
import org.mars.m2m.Devices.ThreatSensor;
import org.mars.m2m.Devices.UAVmanager;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;
import org.mars.m2m.uavendpoint.Exceptions.DeviceStarterDetailsException;
import org.mars.m2m.uavendpoint.util.AbstractDevice;
import org.mars.m2m.uavendpoint.util.DeviceHelper;
import org.mars.m2m.uavendpoint.util.UavObjectFactory;
import org.mars.m2m.uavendpoint.Model.DeviceStarterDetails;
import org.mars.m2m.uavendpoint.Model.RequiredBootstrapInfo;
import org.mars.m2m.uavendpoint.Validation.StarterValidator;
import org.mars.m2m.uavendpoint.omaObjects.Device;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mSecurity;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mServer;
import org.mars.m2m.uavendpoint.util.ConvertObject;
import org.mars.m2m.uavendpoint.util.UnmarshalBootstrap;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class MilitaryUAV implements Runnable {
    
    /**
     *
     */
    public static Logger log = (Logger) LoggerFactory.getLogger(MilitaryUAV.class);

    /**
     *
     */
    public static UavObjectFactory uavObjFactory = new UavObjectFactory();
    /**
     * Keeps track of all devices (lwm2m clients) owned by this particular UAV
     */
    ArrayList<AbstractDevice> uavOwnedDevices = new ArrayList<>();
    
    final UAVConfiguration uavConfig;
       
    /**
     * Gets the Lightweight M2M model for this UAV
     * */
    public static LwM2mModel uavCustomLwM2mModel;
    
    
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
        
        /**
         * Loads the LwM2mModel once for all devices
         */
       uavCustomLwM2mModel  = DeviceHelper.getObjectModel(this.uavConfig.getObjectModelFile());
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
            this.threatSensor = uavObjFactory.createThreatSensor();
        }  
        
        /**
         * For setting up a device within the UAV
         * @param lwm2mClientDetails 
         */
        public ThreatSensorDeviceClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.threatSensor = uavObjFactory.createThreatSensor();
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
            
//                //Gets the model
//                LwM2mModel uavCustomLwM2mModel;
//                uavCustomLwM2mModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(uavCustomLwM2mModel);
                
                //attach instance
                initializer.setInstancesForObject(12202, threatSensor);
                initializer.setInstancesForObject(3, device);
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
        
        //OMA LwM2M objects
        Device device;
        OmaLwM2mSecurity lwM2mSecurity;
        OmaLwM2mServer lwM2mServer;
        
        public MissileDispatchClient()
        {
            this(null);
            this.device = new Device();
            this.missileDispatch = uavObjFactory.createMissileDispatcher();
        }
        
        /**
         * 
         * @param lwm2mClientDetails 
         */
        public MissileDispatchClient(DeviceStarterDetails lwm2mClientDetails) 
        {
            this.device = new Device();
            this.missileDispatch = uavObjFactory.createMissileDispatcher();
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
                    this.bsAddress = lwm2mClientDetails.getBsAddress();
                    this.bsPortnumber = lwm2mClientDetails.getBsPortnumber();
                    
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
                log.info(ex.toString());
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
            
//                //Gets the model
//                LwM2mModel uavCustomLwM2mModel;
//                uavCustomLwM2mModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(uavCustomLwM2mModel);
                
                //attach instance
                initializer.setInstancesForObject(12203, missileDispatch);
                initializer.setInstancesForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(12203, 3);

                // Create client
                final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
                final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);
                
                CoapServer coapServer = new CoapServer();
                client = new LeshanClient(clientAddress, serverAddress, coapServer, new ArrayList<LwM2mObjectEnabler>(
                        enablers));

                // Start the client
                client.start();
                
                //Bootstrap
                UnmarshalBootstrap bootstrap = new UnmarshalBootstrap();
                BootstrapConfig bootstrapConfig;
                bootstrapConfig = bootstrap.getBootstrapConfig(new RequiredBootstrapInfo(endpointName, bsAddress, bsPortnumber, client));
                this.security = bootstrapConfig.security;
                this.servers = bootstrapConfig.servers;
                
                /**
                 * Remove using loop. reason: Will replace already added instances in a multi-instance scenario
                 */
                for(Integer i : this.security.keySet())
                {
                    lwM2mSecurity = ConvertObject.toLwM2mSecurity(this.security.get(i));
                    initializer.setInstancesForObject(0, lwM2mSecurity);
                }
                
                for(Integer i : this.servers.keySet())
                {
                    lwM2mServer = ConvertObject.toLwM2mServer(this.servers.get(i));
                    initializer.setInstancesForObject(1, lwM2mServer);
                } 
                //--------------------------------------------------------------------------
                
                //Addition of security and server resources to the endpoint
                enablers = initializer.create(0,1);
                List<LwM2mObjectEnabler> objectEnablers = new ArrayList<LwM2mObjectEnabler>(enablers);
                for (LwM2mObjectEnabler enabler : objectEnablers) {
                if (coapServer.getRoot().getChild(Integer.toString(enabler.getId())) != null) 
                {
                    throw new IllegalArgumentException("Trying to load Client Object of name '" + enabler.getId()
                            + "' when one was already added.");
                    }

                    final ObjectResource clientObject = new ObjectResource(enabler);
                    //clientObject.getAttributes().
                    coapServer.add(clientObject);
                }
                
                // register to the server provided
                final String endpointIdentifier = this.endpointName ;//UUID.randomUUID().toString();
                
                /**
                 * LwM2M client registration
                 */
                for(Integer i : this.servers.keySet())
                {
                    this.registrationID = DeviceHelper.register(client, endpointIdentifier);  
                }
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
        Device device;
        
        /**
         * Default constructor
         */
        public TemperatureSensorClient() 
        {
            this(null);
            this.device = new Device();
            this.tempSensor = new TemperatureSensor();
        }
        
        /**
         * 
         * @param lwm2mClientDetails 
         */
        public TemperatureSensorClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
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
            
//                //Gets the model
//                LwM2mModel uavCustomLwM2mModel;
//                uavCustomLwM2mModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(uavCustomLwM2mModel);
                
                //attach instance
                initializer.setInstancesForObject(3303, tempSensor);
                initializer.setInstancesForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(0, 1, 3303, 3);

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
    public static class FlightControlClient extends AbstractDevice
    {
        FlightControl flightControl;
        Device device;

        public FlightControlClient() {
            this.device = new Device();
            this.flightControl = new FlightControl();
        }

        public FlightControlClient(DeviceStarterDetails lwm2mClientDetails) 
        {
            this.device = new Device();
            this.flightControl = new FlightControl();
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
            
//                //Gets the model
//                LwM2mModel uavCustomLwM2mModel;
//                uavCustomLwM2mModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(uavCustomLwM2mModel);
                
                //attach instance
                initializer.setInstancesForObject(12205, flightControl);
                initializer.setInstancesForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(12205,3);

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
                log.error("Device already started");
            }
        } 
    }
    
    public static class UAVManagerClient extends AbstractDevice
    {
        UAVmanager uavManager;
        Device device;

        public UAVManagerClient() {
            this.device = new Device();
            this.uavManager = uavObjFactory.createUAVmanager();
        }
        
        public UAVManagerClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.uavManager = uavObjFactory.createUAVmanager();
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
                log.error(ex.getMessage());
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
            
//                //Gets the model
//                LwM2mModel uavCustomLwM2mModel;
//                uavCustomLwM2mModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(uavCustomLwM2mModel);
                
                //attach instance
                initializer.setInstancesForObject(12201, uavManager);   
                initializer.setInstancesForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(12201, 3);

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
    
    public static class AltitudeSensorClient extends AbstractDevice
    {
        AltitudeSensor altitudeSensor;
        Device device;

        public AltitudeSensorClient() {
            this.device = new Device();
            this.altitudeSensor = uavObjFactory.createAltitudeSensor();
        }
        
        public AltitudeSensorClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.altitudeSensor = uavObjFactory.createAltitudeSensor();
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
                log.error(ex.getMessage());
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
            
                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(uavCustomLwM2mModel);
                
                //attach instance
                initializer.setInstancesForObject(12205, altitudeSensor);  
                initializer.setInstancesForObject(3, device);  
                List<ObjectEnabler> enablers = initializer.create(12205, 3);

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
     * Main method
     */
    @Override
    public void run()/* throws InterruptedException*/
    { 
        log.info("Started execution in military uav run method");
        //StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
        
        /**
         * UAV Manager
         */
        DeviceStarterDetails UAVmanagerDevDtls;
        UAVmanagerDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8081, "127.0.0.1", 5683, "UAV Manager", uavConfig, "127.0.0.1", 5070);
        UAVManagerClient UAVmanagerDev = new UAVManagerClient(UAVmanagerDevDtls);
        UAVmanagerDev.StartDevice();
        log.info("UAV manager started");
        
        /**
         * Threat sensor
         */
        DeviceStarterDetails threatDevDtls;
        threatDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8087, "127.0.0.1", 5683, "Threat sensor", uavConfig, "127.0.0.1", 5070);
        ThreatSensorDeviceClient threatSensorDev = new ThreatSensorDeviceClient(threatDevDtls);
        threatSensorDev.StartDevice();
        //Thread.sleep(10000);
        //DeviceHelper.stopDevice(threatSensorDev);
        log.info("Threat sensor started");        
        uavOwnedDevices.add(threatSensorDev);
        
        /**
         * Missile dispatcher
         */
        DeviceStarterDetails missileDisDtls;
        missileDisDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8092, "127.0.0.1", 5683, "missileDispatcher", uavConfig, "127.0.0.1", 5070);
        MissileDispatchClient mislDisClient = new MissileDispatchClient(missileDisDtls);
        mislDisClient.StartDevice();
        log.info("Missile dispatcher started");
        uavOwnedDevices.add(mislDisClient);
        
        /**
         * Temperature sensor
         */
        DeviceStarterDetails tempSenDtls;
        tempSenDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8095, "127.0.0.1", 5683, "IPSO Temperature sensor", uavConfig, "127.0.0.1", 5070);
        TemperatureSensorClient tempSenClient = new TemperatureSensorClient(tempSenDtls);
        tempSenClient.StartDevice();
        log.info("Temperature sensor started");
        uavOwnedDevices.add(tempSenClient);
        
        /**
         * Altitude sensor
         */
        DeviceStarterDetails altitudeSenDtls;
        altitudeSenDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8096, "127.0.0.1", 5683, "Altitude sensor", uavConfig, "127.0.0.1", 5070);
        AltitudeSensorClient altitudeSenClient = new AltitudeSensorClient(altitudeSenDtls);
        altitudeSenClient.StartDevice();
        log.info("Altitude sensor started");
        uavOwnedDevices.add(altitudeSenClient);
    }
    
}
