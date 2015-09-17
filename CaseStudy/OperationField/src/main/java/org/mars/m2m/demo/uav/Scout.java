/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import ch.qos.logback.classic.Logger;
import org.mars.m2m.demo.config.GraphicConfig;
import org.mars.m2m.demo.config.OpStaticInitConfig;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.mars.m2m.demo.LwM2mClients.FlightControlClient;
import org.mars.m2m.demo.LwM2mClients.ObstacleSensorClient;
import org.mars.m2m.demo.LwM2mClients.ThreatSensorClient;
import org.mars.m2m.demo.util.ConflictCheckUtil;
import org.mars.m2m.demo.util.VectorUtil;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.shape.Circle;
import org.mars.m2m.demo.world.KnowledgeInterface;
import org.mars.m2m.demo.world.OntologyBasedKnowledge;
import org.mars.m2m.demo.world.World;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;
import org.mars.m2m.uavendpoint.Model.DeviceStarterDetails;
import org.mars.m2m.uavendpoint.util.AbstractDevice;
import org.mars.m2m.uavendpoint.util.DeviceHelper;
import org.slf4j.LoggerFactory;

/**
 *
 * @author boluo
 */
public final class Scout extends UAV 
{
    private final Logger log = (Logger) LoggerFactory.getLogger(Scout.class);
    
    //lwm2m client stuffs
    /**
     * Keeps track of all devices (lwm2m clients) owned by this particular UAV
     */
    ArrayList<AbstractDevice> uavOwnedDevices = new ArrayList<>();
    
    UAVConfiguration uavConfig;
       
    /**
     * Gets the Lightweight M2M model for this UAV
     * */
    private LwM2mModel uavLwM2mModel;
    private ThreatSensorClient threatSensorClient;
    private ObstacleSensorClient obstacleSensorClient;
    private FlightControlClient flightControlClient;
    private final DeviceHelper deviceHelper;
    private KnowledgeInterface kb;
    
    /*Class variables */
    private float[] base_coordinate;
    private LinkedList<Float> move_at_y_coordinate_task;
    private Float current_y_coordinate_task = null;
    private int direction = 1;
    //private Reconnaissance reconnaissance;
    private int conflict_avoid = 1;

    public Scout(int index, int uav_type, float[] center_coordinates, float[] base_coordinate, float remained_energy) 
    {
        super(index, null, uav_type, center_coordinates,remained_energy);
        this.occupiedPorts = new ArrayList<>();
        this.deviceHelper = new DeviceHelper();
        this.kb = new OntologyBasedKnowledge();
        
        this.uav_radar = new Circle(center_coordinates[0], center_coordinates[1], OpStaticInitConfig.scout_radar_radius);
        this.base_coordinate = base_coordinate;
        this.move_at_y_coordinate_task = new LinkedList<>();
        this.max_angle = (float) Math.PI / 2;
        this.speed = OpStaticInitConfig.SPEED_OF_SCOUT;
        center_color = GraphicConfig.uav_colors.get(22);
        radar_color = new Color(center_color.getRed(), center_color.getGreen(), center_color.getBlue(), 128);
                
        //for LwM2M initialization of the UAV
        initUAV(new UAVConfiguration());
    }
    
    /**
     * For initializing the uav configuration object
     * @param config The uav configuration to be used for this UAV
     */
    public void initUAV(UAVConfiguration config) 
    {
        if(config == null)
        this.uavConfig = new UAVConfiguration();
        else
            this.uavConfig = config;
        
        /**
         * Loads the LwM2mModel once for all devices
         */
       this.uavLwM2mModel  = DeviceHelper.getObjectModel(this.uavConfig.getObjectModelFile());
       this.threatSensorClient = startThreatSensor();
       this.obstacleSensorClient = startObstacleSensor();
       this.flightControlClient = startFlightControl(); 
    }
    
    //<editor-fold defaultstate="collapsed" desc="Starts the threat sensor">
    /**
     *
     * @return A started instance of {@link ThreatSensorClient}
     */
    private ThreatSensorClient startThreatSensor()
    {
        int portNumber = selectPortNumber();
        /**
         * Threat sensor
         */
        DeviceStarterDetails threatDevDtls;
        threatDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(),
                portNumber, "127.0.0.1", 5683, "scout"+this.index+"-"+UUID.randomUUID().toString(), uavConfig, "127.0.0.1", 5070);
        ThreatSensorClient threatSensor = new ThreatSensorClient(uavLwM2mModel, threatDevDtls);
        threatSensor.StartDevice();
        deviceHelper.lwM2mClientDaemon(threatSensor);//invokes a background process for this device
        //Thread.sleep(10000);
        //DeviceHelper.stopDevice(threatSensor);
        log.info("Threat sensor started");
        uavOwnedDevices.add(threatSensor);
        return threatSensor;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Starts the obstacle sensor">
    /**
     * 
     * @return A started instance of {@link ObstacleSensorClient} 
     */
    private ObstacleSensorClient startObstacleSensor()
    {
        int portNumber = selectPortNumber();
        /**
         * Threat sensor
         */
        DeviceStarterDetails obstacleDevDtls;
        obstacleDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(),
                portNumber, "127.0.0.1", 5683, "scout"+this.index+"-"+UUID.randomUUID().toString(), uavConfig, "127.0.0.1", 5070);
        ObstacleSensorClient obstacleSensor = new ObstacleSensorClient(uavLwM2mModel, obstacleDevDtls);
        obstacleSensor.StartDevice();
        deviceHelper.lwM2mClientDaemon(obstacleSensor);//invokes a background process for this device
        //Thread.sleep(10000);
        //DeviceHelper.stopDevice(threatSensor);
        log.info("Obstacle sensor started");
        uavOwnedDevices.add(obstacleSensor);
        return obstacleSensor;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Starts Flight control">
    /**
     * 
     * @return A started instance of {@link FlightControlClient} 
     */
    private FlightControlClient startFlightControl()
    {
        int portNumber = selectPortNumber();
        /**
         * Threat sensor
         */
        DeviceStarterDetails flightDevDtls;
        flightDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(),
                portNumber, "127.0.0.1", 5683, "scout"+this.index+"-"+UUID.randomUUID().toString(), uavConfig, "127.0.0.1", 5070);
        FlightControlClient flightControl = new FlightControlClient(uavLwM2mModel, flightDevDtls);
        flightControl.StartDevice();
        deviceHelper.lwM2mClientDaemon(flightControl);//invokes a background process for this device
        //Thread.sleep(10000);
        //DeviceHelper.stopDevice(threatSensor);
        log.info("Flight control started");
        uavOwnedDevices.add(flightControl);
        return flightControl;
    }
//</editor-fold>
    
    
    /** to update the coordinate of the scout.
     * 
     * @return 
     */
    public boolean moveToNextWaypoint() {
        if (current_y_coordinate_task == null && move_at_y_coordinate_task.size() > 0) {
            current_y_coordinate_task = move_at_y_coordinate_task.removeFirst();
        } else if(current_y_coordinate_task == null && move_at_y_coordinate_task.size()==0){
            this.setVisible(false);
            return false;
        }
        float[] next_waypoint = new float[2];
        float[] goal_waypoint = new float[2];
        goal_waypoint[1] = current_y_coordinate_task;
        ArrayList<Obstacle> obstacles = this.kb.getObstacles();
        if (direction == 1) //move to the right
        {
            goal_waypoint[0] = this.center_coordinates[0] + this.speed;
            next_waypoint = extendTowardGoalWithDynamics(this.center_coordinates, this.current_angle, goal_waypoint, this.speed, this.max_angle);
            if (next_waypoint[0] > World.bound_width) {
                next_waypoint[0] -= this.speed;
                if (move_at_y_coordinate_task.size() == 0) {
                    current_y_coordinate_task=null;
                    return false;
                }
                current_y_coordinate_task = move_at_y_coordinate_task.removeFirst();
                goal_waypoint[1] = current_y_coordinate_task;
                direction = 0;
            } else if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                next_waypoint[0] = this.center_coordinates[0];
                next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                    conflict_avoid = -1 * conflict_avoid;
                    next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                }
            }
        }

        if (direction == 0)//move to the left
        {
            goal_waypoint[0] = this.center_coordinates[0] - this.speed;
            next_waypoint = extendTowardGoalWithDynamics(this.center_coordinates, this.current_angle, goal_waypoint, this.speed, this.max_angle);
            if (next_waypoint[0] < 0) {
                next_waypoint[0] += this.speed;
                if (move_at_y_coordinate_task.size() == 0) {
                    current_y_coordinate_task=null;
                    return false;
                }
                current_y_coordinate_task = move_at_y_coordinate_task.removeFirst();
                goal_waypoint[1] = current_y_coordinate_task;
                direction = 1;
            } else if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                next_waypoint[0] = this.center_coordinates[0];
                next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                if (ConflictCheckUtil.checkPointInObstacles(obstacles, next_waypoint[0], next_waypoint[1])) {
                    conflict_avoid = -1 * conflict_avoid;
                    next_waypoint[1] = this.center_coordinates[1] + conflict_avoid * this.speed;
                }
            }
        }
        this.current_angle = VectorUtil.getAngleOfVectorRelativeToXCoordinate(next_waypoint[0] - this.center_coordinates[0], next_waypoint[1] - this.center_coordinates[1]);
        moveTo(next_waypoint[0], next_waypoint[1]);
        return true;
    }
    
    /** set the responsible y coordinate to be scanned by the scout. The scout will scan the line at y coordinate from one side to the other.
     * 
     * @param move_at_y_coordinate_task 
     */
    public void setMove_at_y_coordinate_task(LinkedList<Float> move_at_y_coordinate_task) {
        System.out.println("Coordinates: "+Arrays.asList(move_at_y_coordinate_task));
        this.move_at_y_coordinate_task = move_at_y_coordinate_task;
    }

    public LinkedList<Float> getMove_at_y_coordinate_task() {
        return move_at_y_coordinate_task;
    }
    

    public ArrayList<AbstractDevice> getUavOwnedDevices() {
        return uavOwnedDevices;
    }

    public void setUavOwnedDevices(ArrayList<AbstractDevice> uavOwnedDevices) {
        this.uavOwnedDevices = uavOwnedDevices;
    }

    public UAVConfiguration getUavConfig() {
        return uavConfig;
    }

    public void setUavConfig(UAVConfiguration uavConfig) {
        this.uavConfig = uavConfig;
    }

    public LwM2mModel getUavLwM2mModel() {
        return uavLwM2mModel;
    }

    public void setUavLwM2mModel(LwM2mModel uavLwM2mModel) {
        this.uavLwM2mModel = uavLwM2mModel;
    }
    
    public ThreatSensorClient getThreatSensorLwM2mClient() {
        return threatSensorClient;
    }

    public FlightControlClient getFlightControlClient() {
        return flightControlClient;
    }

    public KnowledgeInterface getKb() {
        return kb;
    }

    public void setKb(KnowledgeInterface kb) {
        this.kb = kb;
    }

    public ObstacleSensorClient getObstacleSensorClient() {
        return obstacleSensorClient;
    }
    
    
}
