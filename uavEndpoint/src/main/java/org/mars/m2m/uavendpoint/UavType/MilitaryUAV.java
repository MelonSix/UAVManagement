/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.UavType;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.mars.m2m.Devices.AltitudeSensor;
import org.mars.m2m.Devices.FlightControl;
import org.mars.m2m.Devices.MissileDispatcher;
import org.mars.m2m.Devices.TemperatureSensor;
import org.mars.m2m.Devices.ThreatSensor;
import org.mars.m2m.Devices.UAVmanager;
import org.mars.m2m.apiExtension.LwM2mObjectInitializer;
import org.mars.m2m.uavendpoint.Configuration.ThreatType;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;
import org.mars.m2m.uavendpoint.Exceptions.DeviceStarterDetailsException;
import org.mars.m2m.uavendpoint.Helpers.AbstractDevice;
import org.mars.m2m.uavendpoint.Helpers.DeviceHelper;
import org.mars.m2m.uavendpoint.Model.DeviceStarterDetails;
import org.mars.m2m.uavendpoint.Model.MinimumBoundingRectangle;
import org.mars.m2m.uavendpoint.Validation.StarterValidator;
import org.mars.m2m.uavendpoint.omaObjects.Device;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class MilitaryUAV implements Runnable {
    
    public static Logger log = (Logger) LoggerFactory.getLogger(MilitaryUAV.class);
    
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
            this.threatSensor = createThreatSensor();
        }  
        
        /**
         * For setting up a device within the UAV
         * @param lwm2mClientDetails 
         */
        public ThreatSensorDeviceClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.threatSensor = createThreatSensor();
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
                customModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(customModel);
                
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
                final String endpointIdentifier = UUID.randomUUID().toString();//this.endpointName ;//UUID.randomUUID().toString();
                
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
        
        private ThreatSensor createThreatSensor()
        {
            MinimumBoundingRectangle mbr = new MinimumBoundingRectangle((System.currentTimeMillis()*100), 
                    (System.currentTimeMillis()*100/6), (System.currentTimeMillis()*100/3), (System.currentTimeMillis()*100/2));
            Gson gson = new Gson();
            return new ThreatSensor(ThreatType.BIOLOGICAL_WEAPON, (int) System.currentTimeMillis()*100, 
                            (int)System.currentTimeMillis()*160, gson.toJson(mbr), 300, 400);
        }
        
    }
    
    /**
     * 
     */
    public static class MissileDispatchClient extends AbstractDevice
    {
        MissileDispatcher missileDispatch;
        Device device;
        
        public MissileDispatchClient()
        {
            this(null);
            this.device = new Device();
            this.missileDispatch = createMissileDispatcher();
        }
        
        /**
         * 
         * @param lwm2mClientDetails 
         */
        public MissileDispatchClient(DeviceStarterDetails lwm2mClientDetails) 
        {
            this.device = new Device();
            this.missileDispatch = createMissileDispatcher();
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
                customModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(customModel);
                
                //attach instance
                initializer.setInstancesForObject(12203, missileDispatch);
                initializer.setInstancesForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(12203, 3);

                // Create client
                final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
                final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);

                client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
                        enablers));

                // Start the client
                client.start();

                // register to the server provided
                final String endpointIdentifier = UUID.randomUUID().toString();//this.endpointName ;//UUID.randomUUID().toString();
                
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
        
        private MissileDispatcher createMissileDispatcher()
        {
            return new MissileDispatcher((int) (Math.random()*10), (int) (int) (Math.random()*10),
                    (int) (Math.random()*100), (int) (Math.random()*100));
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
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(customModel);
                
                //attach instance
                initializer.setInstancesForObject(3303, tempSensor);
                initializer.setInstancesForObject(3, device);
                List<ObjectEnabler> enablers = initializer.create(3303, 3);

                // Create client
                final InetSocketAddress clientAddress = new InetSocketAddress(localHostName, localPort);
                final InetSocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPort);

                client = new LeshanClient(clientAddress, serverAddress, new ArrayList<LwM2mObjectEnabler>(
                        enablers));

                // Start the client
                client.start();

                // register to the server provided
                final String endpointIdentifier = UUID.randomUUID().toString();//this.endpointName ;//UUID.randomUUID().toString();
                
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
        
        private TemperatureSensor createTemperatureSensor()
        {
            return new TemperatureSensor((float)(Math.random()*100/2), (float)(Math.random()*6), 
                        (float)(Math.random()*10), (float)(Math.random()*100));
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
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(customModel);
                
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
                final String endpointIdentifier = UUID.randomUUID().toString();//this.endpointName ;//UUID.randomUUID().toString();
                
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
            this.uavManager = createUAVmanager();
        }
        
        public UAVManagerClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.uavManager = createUAVmanager();
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
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(customModel);
                
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
                final String endpointIdentifier = UUID.randomUUID().toString();// this.endpointName ;//UUID.randomUUID().toString();
                
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
        
        public UAVmanager createUAVmanager()
        {
            String focusModel= "Chengdu Pterodactyl I (Wing Loong)";
            String origin = "China";
            String manufacturer = "Chengdu Aircraft Industry Group - China";
            String initialYearOfService = "2014";
            float length = 9;
            float width = 14;//.00f;
            float height = 2;//.77f;
            float weight_empty = 0;//.00f;
            float weight_mtow = 1100;
            String powerPlant = "1x Conventionally-powered engine driving a three-bladed propeller";
            float maximumSpeed = 174;
            float maximumRange = 5000;
            float serviceCeiling = 16404;
            float rateOfClimb = 0;
            float payloadCapability = 99;//.79f;
            float cruiseSpeed = 300;
            String launchType = "Catapult launch";
            int maximumFlightTime = 86400;
            float wingspan = 100;
            int operatingTemperature_lowest = 5;
            int operatingTemperature_highest = 80;
            
            return new UAVmanager(focusModel, origin, manufacturer, initialYearOfService, length, width, height, 
                    weight_empty, weight_mtow, powerPlant, maximumSpeed, maximumRange, serviceCeiling, rateOfClimb, 
                    payloadCapability, cruiseSpeed, launchType, maximumFlightTime, wingspan, operatingTemperature_lowest, 
                    operatingTemperature_highest);
        }
    }
    
    public static class AltitudeSensorClient extends AbstractDevice
    {
        AltitudeSensor altitudeSensor;
        Device device;

        public AltitudeSensorClient() {
            this.device = new Device();
            this.altitudeSensor = creatAltitudeSensor();
        }
        
        public AltitudeSensorClient(DeviceStarterDetails lwm2mClientDetails)
        {
            this.device = new Device();
            this.altitudeSensor = creatAltitudeSensor();
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
            
                //Gets the model
                LwM2mModel customModel;
                customModel = DeviceHelper.getObjectModel(this.objectModelFilename);

                ObjectsInitializer initializer;
                initializer = new ObjectsInitializer(customModel);
                
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
                final String endpointIdentifier = UUID.randomUUID().toString();//this.endpointName;
                
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
        
        private AltitudeSensor creatAltitudeSensor()
        {
            return new AltitudeSensor((float)(Math.random()*10), (float)(Math.random()*8));
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
                8081, "127.0.0.1", 5683, "/uavObjectModel.json", "UAV Manager", uavConfig);
        UAVManagerClient UAVmanagerDev = new UAVManagerClient(UAVmanagerDevDtls);
        UAVmanagerDev.StartDevice();
        log.info("[{}] UAV manager started",this.getClass().getName());
        
        /**
         * Threat sensor
         */
        DeviceStarterDetails threatDevDtls;
        threatDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8087, "127.0.0.1", 5683, "/uavObjectModel.json", "Threat sensor", uavConfig);
        ThreatSensorDeviceClient threatSensorDev = new ThreatSensorDeviceClient(threatDevDtls);
        threatSensorDev.StartDevice();
        //Thread.sleep(10000);
        //DeviceHelper.stopDevice(threatSensorDev);
        log.info("[{}] Threat sensor started",this.getClass().getName());        
        uavOwnedDevices.add(threatSensorDev);
        
        /**
         * Missile dispatcher
         */
        DeviceStarterDetails missileDisDtls;
        missileDisDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8092, "127.0.0.1", 5683, "/uavObjectModel.json", "Missile dispatcher", uavConfig);
        MissileDispatchClient mislDisClient = new MissileDispatchClient(missileDisDtls);
        mislDisClient.StartDevice();
        log.info("[{}] Missile dispatcher started",this.getClass().getName());
        uavOwnedDevices.add(mislDisClient);
        
        /**
         * Temperature sensor
         */
        DeviceStarterDetails tempSenDtls;
        tempSenDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8095, "127.0.0.1", 5683, "/uavObjectModel.json", "IPSO Temperature sensor", uavConfig);
        TemperatureSensorClient tempSenClient = new TemperatureSensorClient(tempSenDtls);
        tempSenClient.StartDevice();
        log.info("[{}] Temperature sensor started",this.getClass().getName());
        uavOwnedDevices.add(tempSenClient);
        
        /**
         * Altitude sensor
         */
        DeviceStarterDetails altitudeSenDtls;
        altitudeSenDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(), 
                8096, "127.0.0.1", 5683, "/uavObjectModel.json", "Altitude sensor", uavConfig);
        AltitudeSensorClient altitudeSenClient = new AltitudeSensorClient(altitudeSenDtls);
        altitudeSenClient.StartDevice();
        log.info("[{}] Altitude sensor started",this.getClass().getName());
        uavOwnedDevices.add(altitudeSenClient);
    }
    
}