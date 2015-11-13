/* 
 * Copyright (c) Yulin Zhang
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mars.m2m.demo.uav;

import org.mars.m2m.demo.Devices.UavAttackerDevice;
import org.mars.m2m.demo.world.World;
import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.mars.m2m.demo.LwM2mClients.AttackerDeviceClient;
import org.mars.m2m.demo.algorithm.RRT.RRTAlg;
import org.mars.m2m.demo.algorithm.RRT.RRTTree;
import org.mars.m2m.demo.config.NonStaticInitConfig;
import org.mars.m2m.demo.config.OpStaticInitConfig;
import org.mars.m2m.demo.enums.AttackerType;
import org.mars.m2m.demo.enums.ThreatType;
import org.mars.m2m.demo.model.Conflict;
import org.mars.m2m.demo.model.KnowledgeAwareInterface;
import org.mars.m2m.demo.model.Message;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.Target;
import org.mars.m2m.demo.model.Threat;
import org.mars.m2m.demo.model.shape.Circle;
import org.mars.m2m.demo.model.shape.Point;
import org.mars.m2m.demo.ui.AnimationPanel;
import org.mars.m2m.demo.util.BoundUtil;
import org.mars.m2m.demo.util.ConflictCheckUtil;
import org.mars.m2m.demo.util.DistanceUtil;
import org.mars.m2m.demo.util.VectorUtil;
import org.mars.m2m.demo.world.KnowledgeInterface;
import org.mars.m2m.demo.world.OntologyBasedKnowledge;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;
import org.mars.m2m.uavendpoint.Model.DeviceStarterDetails;
import org.mars.m2m.uavendpoint.util.DeviceHelper;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Yulin_Zhang
 */
public final class Attacker extends UAV implements KnowledgeAwareInterface 
{
    private final Gson gson;
    private volatile UAVPath path_planned_at_current_time_step;
    private int current_index_of_planned_path = 0; //index of waypoint
    private UAVPath path_planned_at_last_time_step;//the total path planned lately.
    private UAVPath history_path;//the history path of the attacker
    private boolean need_to_replan = true;
    private boolean replanned_at_current_time_step = false;
    private boolean moved_at_last_time = false;
    private boolean lockedToThreat;
    private final ArrayList<Threat> destroyedThreats;
    private UavAttackerDevice attackerDevice;
    private boolean threatDestroyed=true;//indicates on startup to CC that it can receive an assignment
        
    UAVConfiguration uavConfig;
    AnimationPanel animationPanel;
       
    /**
     * Gets the Lightweight M2M model for this UAV
     * */
    private LwM2mModel uavLwM2mModel;
    private AttackerDeviceClient attackerDeviceClient;
    private final DeviceHelper deviceHelper;

    private int fly_mode = 0;
    private int hovered_time_step = 0;
    private float[] goal_for_each_iteration;
    private int stucked_times=0;//when the flying angle and radar radius are not large enough, the uav could be stucked in front of the obstacle and not able to be moved.
    private int max_stucked_times=4;
    
    private RRTAlg rrt_alg;
    private RRTTree rrt_tree;
    private static Logger logger = (Logger) LoggerFactory.getLogger(Attacker.class);
    AttackerType attackerType;

    public static int FLYING_MODE = 0;
    public static int TARGET_LOCKED_MODE = 1;

    /**
     * Used to satisfy the condition for adding this class' instance in the <code>ObjectInitializer</code> of leshan
     */
    public Attacker() {
        super(0, null, 0, null, 0);
        this.attackerDevice = new UavAttackerDevice(this);
        this.gson = null;
        this.destroyedThreats = null;
        this.deviceHelper = null;
        this.kb = null;
    }

    /** constructor
     *
     * @param index
     * @param target
     * @param uav_type
     * @param center_coordinates
     * @param obstacles
     * @param remained_energy
     * @param attackerType
     * @param animationPanel
     */
    public Attacker(int index, Target target, int uav_type, float[] center_coordinates,
                                        ArrayList<Obstacle> obstacles, float remained_energy, AttackerType attackerType,                                        
                                        AnimationPanel animationPanel)
    {
        super(index, target, uav_type, center_coordinates, remained_energy);
        this.attackerDevice = new UavAttackerDevice(this);
        this.gson = new Gson();
        this.destroyedThreats = new ArrayList<>();
        Attacker.occupiedPorts = new ArrayList<>();
        this.deviceHelper = new DeviceHelper();
        this.attackerType = attackerType;
        this.animationPanel = animationPanel;
        
        
        this.uav_radar = new Circle(center_coordinates[0], center_coordinates[1], OpStaticInitConfig.attacker_radar_radius);
        this.path_planned_at_current_time_step = new UAVPath();
        this.history_path = new UAVPath();
        setPreviousWaypoint();
        this.kb = new OntologyBasedKnowledge();//OntologyBasedKnowledge();WorldKnowledge
        this.kb.setObstacles(obstacles);
        this.speed = OpStaticInitConfig.SPEED_OF_ATTACKER_ON_TASK;
        if (target == null) {
            rrt_alg = new RRTAlg(super.getCenter_coordinates(), null, OpStaticInitConfig.rrt_goal_toward_probability, World.bound_width, World.bound_height, OpStaticInitConfig.rrt_iteration_times, speed, null, this.getConflicts(), this.index);
        } else {
            rrt_alg = new RRTAlg(super.getCenter_coordinates(), target.getCoordinates(), OpStaticInitConfig.rrt_goal_toward_probability, World.bound_width, World.bound_height, OpStaticInitConfig.rrt_iteration_times, speed, null, this.getConflicts(), this.index);
        }
        initColor(index);
        
         //for LwM2M initialization of the UAV
        initUAV(new UAVConfiguration());
        attackThreatDaemon();
    }

 
    /** plan path for the attacker, with rrt path planning algorithm.
     * 
     */
    public void pathPlan() {
        //if the attacker need to replan and it has target
        if (this.need_to_replan && this.target_indicated_by_role != null) {
            this.path_planned_at_last_time_step = this.path_planned_at_current_time_step;
            int planning_times = 0;
            this.goal_for_each_iteration = target_indicated_by_role.getCoordinates();
            if (this.fly_mode == Attacker.TARGET_LOCKED_MODE && this.target_indicated_by_role.getIndex() != Threat.UAV_BASE_INDEX) {
                this.goal_for_each_iteration = this.genRandomHoveringGoal(goal_for_each_iteration, NonStaticInitConfig.threat_range_from_obstacles/2, this.getObstacles());
                this.speed=OpStaticInitConfig.SPEED_OF_ATTACKER_ON_DESTROYING_THREAT;
                this.rrt_alg.setMax_angle((float) Math.PI / 5);
            }else if(this.fly_mode== Attacker.FLYING_MODE && this.target_indicated_by_role.getIndex() == Threat.UAV_BASE_INDEX)
            {
                this.speed=OpStaticInitConfig.SPEED_OF_ATTACKER_IDLE;
                this.rrt_alg.setMax_angle((float) Math.PI / 6);
            }else if(this.fly_mode== Attacker.FLYING_MODE && this.target_indicated_by_role.getIndex() != Threat.UAV_BASE_INDEX)
            {
                this.speed=OpStaticInitConfig.SPEED_OF_ATTACKER_ON_TASK;
                this.rrt_alg.setMax_angle((float) Math.PI / 6);
            }
            
            
            if (this.target_indicated_by_role.getIndex() == Threat.UAV_BASE_INDEX) {
                logger.debug("find path for returning uav");
            } else {
                logger.debug("find path for busy uav");
            }
            UAVPath shortest_path = null;
            float shotest_path_length = Float.MAX_VALUE;
            planning_times = OpStaticInitConfig.rrt_planning_times_for_attacker;
            boolean available_path_found = false;
            int nums_of_trap = 0;
            this.runRRT();
//            for (int i = 0; i <= planning_times; i++) 
//            {
//                
//                available_path_found = available_path_found 
//                        || this.path_planned_at_current_time_step.pathReachEndPoint(this.target_indicated_by_role.getCoordinates());
//                if (!available_path_found && nums_of_trap < 10) {
//                    i--;
//                    nums_of_trap++;
//                    continue;
//                }
//                if (this.path_planned_at_current_time_step.getPath_length() < shotest_path_length) {
//                    shotest_path_length = this.path_planned_at_current_time_step.getPath_length();
//                    shortest_path = this.path_planned_at_current_time_step;
//                }
//            }
            boolean blocked = false;
            if (shortest_path != null) {
                Point path_dest = shortest_path.getLastWaypoint();
                if (path_dest.getX() == this.center_coordinates[0] && path_dest.getY() == this.center_coordinates[1])
                {
                    stucked_times++;
                    if(this.stucked_times>this.max_stucked_times)
                    {
                         stucked_times=0;
                         blocked = true;
                        //this.setVisible(false);
                    }
                    System.out.println("not able to plan path for this uav " + this.getIndex());
                    moveAttackerToBase(this, false);
                }else{
                    stucked_times=0;
                }
                this.runRRT();
                this.path_planned_at_current_time_step = shortest_path;
            } else {
                logger.error("null path");
            }
            this.setReplanned_at_current_time_step(true);
        } else {
            if (!this.need_to_replan) {
                logger.debug("no need to plan");
            }
            if (target_indicated_by_role == null) {
                logger.debug("no target");
            }
            this.setReplanned_at_current_time_step(false);
        }
    }

    /** run rrt algorithm, which is called by planpath method.
     * 
     */
    private void runRRT() {
        rrt_alg.setMax_delta_distance(this.speed);
        rrt_alg.setObstacles(this.getObstacles());
        rrt_alg.setGoal_coordinate(goal_for_each_iteration);
        rrt_alg.setInit_coordinate(center_coordinates);
        rrt_tree = rrt_alg.buildRRT(center_coordinates, current_angle);
        this.setPath_prefound(rrt_tree.getPath_found());
        this.resetCurrentIndexOfPath();
    }

    /** reset the index of waypoint.
     * 
     */
    public void resetCurrentIndexOfPath() {
        this.current_index_of_planned_path = 0;
    }

    /** move the attacker to the next waypoint it planned.
     *
     * @return
     */
    public boolean moveToNextWaypoint() 
    {
        if (this.target_indicated_by_role != null) 
        {
            current_index_of_planned_path++;
            if (path_planned_at_current_time_step.getWaypointNum() == 0 || current_index_of_planned_path >= path_planned_at_current_time_step.getWaypointNum()) {
                this.moved_at_last_time = false;
                this.setNeed_to_replan(true);
                return false;
            }
            Point current_waypoint = this.path_planned_at_current_time_step.getWaypoint(current_index_of_planned_path);
            float[] coordinate = current_waypoint.toFloatArray();
            setPreviousWaypoint();
            moveTo(coordinate[0], coordinate[1]);
            this.current_angle = (float) current_waypoint.getYaw();
            this.moved_at_last_time = true;
            this.setNeed_to_replan(false);
            if(path_planned_at_current_time_step.getWaypointNum() == 0 || current_index_of_planned_path == path_planned_at_current_time_step.getWaypointNum())
            {
                this.setNeed_to_replan(true);
            }
            return this.moved_at_last_time;
        } else {
            this.moved_at_last_time = false;
            this.setNeed_to_replan(true);
            return moved_at_last_time;
        }
    }

    /** check whether the remained energy of the attacker is enough to destory the target and return back to the uav base.
     * 
     * @param potential_target
     * @return 
     */
    public boolean isEnduranceCapReachable(Target potential_target) {
        float dist_to_potential_target = DistanceUtil.distanceBetween(this.center_coordinates, potential_target.getCoordinates());
        float dist_from_potential_target_to_uav_base = DistanceUtil.distanceBetween(potential_target.getCoordinates(), World.uav_base.getCoordinate());
        float path_parameter = 1.5f;
        if (path_parameter * (dist_to_potential_target + dist_from_potential_target_to_uav_base) > this.remained_energy) {
            return false;
        }
        return true;
    }

    public UAVPath getPath_planned_at_last_time_step() {
        return path_planned_at_last_time_step;
    }

    /** get path planning for the future timestep. This method is called by the UI, in order to rendering the future path of each attacker.
     * 
     * @return future path.
     */
    public UAVPath getFuturePath() {
        if (!this.isVisible()) {
            return null;
        }
        UAVPath future_path = new UAVPath();
        synchronized (path_planned_at_current_time_step) {
            for (int i = current_index_of_planned_path; i < path_planned_at_current_time_step.getWaypointNum(); i++) {
                future_path.addWaypointToEnd(path_planned_at_current_time_step.getWaypoint(i));
            }
        }
        return future_path;
    }

    /** set the role of the attacker.
     * 
     * @param target_indicated_by_role 
     */
    public void setTarget_indicated_by_role(Target target_indicated_by_role) {
        if (this.target_indicated_by_role == target_indicated_by_role) {
            return;
        }
        this.target_indicated_by_role = target_indicated_by_role;
    }

    /** record the previous waypoint to the history waypoint queue.
     * 
     */
    private void setPreviousWaypoint() {
        Point previous_waypoint = new Point(this.getCenter_coordinates()[0], this.getCenter_coordinates()[1], this.current_angle);
        this.history_path.addWaypointToEnd(previous_waypoint);
    }

    /** parsing the received msgs, The received msgs are parsed into obstacle, threat, or conflict. 
     * And the uav should replan when received new info.
     *
     * @param msg
     */
    private void parseMessage(Message msg) {
        int msg_type = msg.getMsg_type();
        if (msg_type == Message.CONFLICT_MSG) {
            Conflict conflict = (Conflict) msg;
            this.addConflict(conflict);
            this.setNeed_to_replan(true);
        } else if (msg_type == Message.OBSTACLE_MSG) {
            Obstacle obstacle = (Obstacle) msg;
            this.addObstacle(obstacle);
            if (this.getTarget_indicated_by_role() == null || !this.isObstacleInTargetMBR(obstacle.getShape().getBounds())) {
                this.setNeed_to_replan(true);
            }
        } else if (msg_type == Message.THREAT_MSG) {
            Threat threat = (Threat) msg;
            this.addThreat(threat);
            this.setNeed_to_replan(true);
        }
    }

    /**receive message and parse message
     *
     * @param msg
     */
    public void receiveMesage(Message msg) {
        if (msg != null) {
            parseMessage(msg);
        }
    }

    /** increase the time attacker hoverd on the target.
     * 
     */
    public void increaseHovered_time_step() {
        this.hovered_time_step++;
    }
    
    public void setNeed_to_replan(boolean need_to_replan) {
        this.need_to_replan = need_to_replan;
    }

    public boolean isReplanned_at_current_time_step() {
        return replanned_at_current_time_step;
    }

    public void setReplanned_at_current_time_step(boolean replanned_at_current_time_step) {
        this.replanned_at_current_time_step = replanned_at_current_time_step;
    }

    public float[] getPrevious_waypoint() {
        return this.history_path.getLastWaypoint().toFloatArray();
    }

    public boolean isMoved_at_last_time() {
        return moved_at_last_time;
    }

    public void setMoved_at_last_time(boolean moved_at_last_time) {
        this.moved_at_last_time = moved_at_last_time;
    }

    public LinkedList<Point> getPath_prefound() {
        return path_planned_at_current_time_step.getWaypointsAsLinkedList();
    }

    public void setPath_prefound(UAVPath path_prefound) {
        this.path_planned_at_current_time_step = path_prefound;
    }

    public RRTTree getRrt_tree() {
        return rrt_tree;
    }

    @Override
    public ArrayList<Obstacle> getObstacles() {
        return this.kb.getObstacles();
    }

    @Override
    public ArrayList<Conflict> getConflicts() {
        return this.kb.getConflicts();
    }

    @Override
    public synchronized ArrayList<Threat> getThreats() {
        return this.kb.getThreats();
    }

    @Override
    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.kb.setObstacles(obstacles);
    }

    @Override
    public void setConflicts(ArrayList<Conflict> conflicts) {
        this.kb.setConflicts(conflicts);
    }

    @Override
    public void setThreats(ArrayList<Threat> threats) {
        this.kb.setThreats(threats);
    }

    @Override
    public void addObstacle(Obstacle obs) {
        if (!this.kb.containsObstacle(obs)) {
            this.kb.addObstacle(obs);
        }
    }

    @Override
    public void addConflict(Conflict conflict) {
        this.kb.addConflict(conflict);
    }

    @Override
    public void addThreat(Threat threat) {
        synchronized(kb)
        {
            this.kb.addThreat(threat);
        }
    }

    private float[] genRandomHoveringGoalV1(float[] threat_location, float hover_radius, ArrayList<Obstacle> obstacles) {
        float[] random_goal_coordinate = new float[2];
        double random_theta = Math.random() * Math.PI * 2;
        random_goal_coordinate[0] = threat_location[0] + (float) Math.cos(random_theta) * hover_radius;
        random_goal_coordinate[1] = threat_location[1] + (float) Math.sin(random_theta) * hover_radius;
        boolean collisioned = true;
        boolean withinBound = BoundUtil.withinBound(random_goal_coordinate[0], random_goal_coordinate[1], World.bound_width, World.bound_height);
        while (collisioned || !withinBound) {
            random_goal_coordinate[0] = threat_location[0] + (float) Math.cos(random_theta) * hover_radius;
            random_goal_coordinate[1] = threat_location[1] + (float) Math.sin(random_theta) * hover_radius;
            if (!ConflictCheckUtil.checkPointInObstacles(obstacles, random_goal_coordinate[0], random_goal_coordinate[1])) {
                collisioned = false;
            } else {
                random_theta = Math.random() * Math.PI * 2;
            }
            withinBound = BoundUtil.withinBound(random_goal_coordinate[0], random_goal_coordinate[1], World.bound_width, World.bound_height);
            logger.debug("find hovering goal for attackers");
        }
        return random_goal_coordinate;
    }

    private float[] genRandomHoveringGoal(float[] threat_location, float hover_radius, ArrayList<Obstacle> obstacles) {
        double random_center_angle = VectorUtil.getAngleOfVectorRelativeToXCoordinate(threat_location[0]-this.center_coordinates[0], threat_location[1]-this.center_coordinates[1]);
        float[] random_goal_coordinate = new float[2];

        boolean collisioned = true;
        boolean withinBound = false;
        while (collisioned || !withinBound) {
            random_goal_coordinate[0] = threat_location[0] + (float) Math.cos(random_center_angle) * hover_radius;
            random_goal_coordinate[1] = threat_location[1] + (float) Math.sin(random_center_angle) * hover_radius;
            withinBound = BoundUtil.withinBound(random_goal_coordinate[0], random_goal_coordinate[1], World.bound_width, World.bound_height);
            collisioned=ConflictCheckUtil.checkPointInObstacles(obstacles, random_goal_coordinate[0], random_goal_coordinate[1]);
            int total_segment_num=4;
            for(int i=1;i<=total_segment_num;i++)
            {
                float[] temp_goal_coord=new float[2];
                temp_goal_coord[0]=threat_location[0] + (float) Math.cos(random_center_angle) * hover_radius*i/(total_segment_num+1);
                temp_goal_coord[1]=threat_location[1] + (float) Math.sin(random_center_angle) * hover_radius*i/(total_segment_num+1);
                collisioned=collisioned && ConflictCheckUtil.checkPointInObstacles(obstacles, random_goal_coordinate[0], random_goal_coordinate[1]);
            }
            random_center_angle+=Math.PI/36;
            logger.debug("find hovering goal for attackers");
        }
        return random_goal_coordinate;
    }

    public KnowledgeInterface getKb() {
        return kb;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public synchronized int getFly_mode() {
        return fly_mode;
    }

    public void setFly_mode(int fly_mode) {
        this.fly_mode = fly_mode;
    }

    public int getHovered_time_step() {
        return hovered_time_step;
    }

    public void setHovered_time_step(int hovered_time_step) {
        this.hovered_time_step = hovered_time_step;
    }

    public UAVPath getHistory_path() {
        return history_path;
    }

    @Override
    public synchronized boolean containsThreat(Threat threat) {
        return this.kb.containsThreat(threat);
    }

    @Override
    public synchronized boolean containsObstacle(Obstacle obstacle) {
        return this.kb.containsObstacle(obstacle);
    }
    
    /**
     * Initializes the UAV
     * @param config 
     */
    private void initUAV(UAVConfiguration config) 
    {
        if(config == null)
        this.uavConfig = new UAVConfiguration();
        else
            this.uavConfig = config;
        
        /**
         * Loads the LwM2mModel once for all devices
         */
       this.uavLwM2mModel  = DeviceHelper.getObjectModel(this.uavConfig.getObjectModelFile());
       this.attackerDeviceClient = startAttackerDevice();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Starts the attacker device">
    /**
     * 
     * @return A started instance of {@link AttackerDeviceClient} 
     */
    private AttackerDeviceClient startAttackerDevice()
    {
        int portNumber = selectPortNumber();
        /**
         * Threat sensor
         */
        DeviceStarterDetails attackerDevDtls;
        attackerDevDtls = new DeviceStarterDetails(uavConfig.getUavlocalhostAddress(),
                portNumber, "127.0.0.1", 5683, "attacker"+this.index+"-"+UUID.randomUUID().toString(), uavConfig, "127.0.0.1", 5070);
        AttackerDeviceClient uavAttackerDevice = new AttackerDeviceClient(uavLwM2mModel, attackerDevDtls, attackerDevice);
        uavAttackerDevice.StartDevice();
        deviceHelper.lwM2mClientDaemon(uavAttackerDevice);//invokes a background process for this device
        logger.info("Attacker device started");
        return uavAttackerDevice;
    }
//</editor-fold>

    public AttackerDeviceClient getAttackerDeviceClient() {
        return attackerDeviceClient;
    }

    public UAVPath getPath_planned_at_current_time_step() {
        return path_planned_at_current_time_step;
    }

    public int getCurrent_index_of_planned_path() {
        return current_index_of_planned_path;
    }

    public AttackerType getAttackerType() {
        return attackerType;
    }
    
    public boolean isNeed_to_replan() {
        return need_to_replan;
    }

    public UAVConfiguration getUavConfig() {
        return uavConfig;
    }

    public LwM2mModel getUavLwM2mModel() {
        return uavLwM2mModel;
    }

    public DeviceHelper getDeviceHelper() {
        return deviceHelper;
    }

    public float[] getGoal_for_each_iteration() {
        return goal_for_each_iteration;
    }

    public int getStucked_times() {
        return stucked_times;
    }

    public int getMax_stucked_times() {
        return max_stucked_times;
    }

    public RRTAlg getRrt_alg() {
        return rrt_alg;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static int getFLYING_MODE() {
        return FLYING_MODE;
    }

    public static int getTARGET_LOCKED_MODE() {
        return TARGET_LOCKED_MODE;
    }

    public double getMax_angle() {
        return max_angle;
    }

    public float getRemained_energy() {
        return remained_energy;
    }

    public static ArrayList<Integer> getOccupiedPorts() {
        return occupiedPorts;
    }

    public static int getCenter_height() {
        return center_height;
    }

    public static int getCenter_width() {
        return center_width;
    }

    public void setPath_planned_at_current_time_step(UAVPath path_planned_at_current_time_step) {
        this.path_planned_at_current_time_step = path_planned_at_current_time_step;
    }

    public void setCurrent_index_of_planned_path(int current_index_of_planned_path) {
        this.current_index_of_planned_path = current_index_of_planned_path;
    }

    public void setPath_planned_at_last_time_step(UAVPath path_planned_at_last_time_step) {
        this.path_planned_at_last_time_step = path_planned_at_last_time_step;
    }

    public void setHistory_path(UAVPath history_path) {
        this.history_path = history_path;
    }

    public void setUavConfig(UAVConfiguration uavConfig) {
        this.uavConfig = uavConfig;
    }

    public void setUavLwM2mModel(LwM2mModel uavLwM2mModel) {
        this.uavLwM2mModel = uavLwM2mModel;
    }

    public void setAttackerDeviceClient(AttackerDeviceClient attackerDeviceClient) {
        this.attackerDeviceClient = attackerDeviceClient;
    }

    public void setGoal_for_each_iteration(float[] goal_for_each_iteration) {
        this.goal_for_each_iteration = goal_for_each_iteration;
    }

    public void setStucked_times(int stucked_times) {
        this.stucked_times = stucked_times;
    }

    public void setMax_stucked_times(int max_stucked_times) {
        this.max_stucked_times = max_stucked_times;
    }

    public void setRrt_alg(RRTAlg rrt_alg) {
        this.rrt_alg = rrt_alg;
    }

    public void setRrt_tree(RRTTree rrt_tree) {
        this.rrt_tree = rrt_tree;
    }

    public static void setFLYING_MODE(int FLYING_MODE) {
        Attacker.FLYING_MODE = FLYING_MODE;
    }

    public static void setTARGET_LOCKED_MODE(int TARGET_LOCKED_MODE) {
        Attacker.TARGET_LOCKED_MODE = TARGET_LOCKED_MODE;
    }

    public void setCenter_color(Color center_color) {
        this.center_color = center_color;
    }

    public void setRadar_color(Color radar_color) {
        this.radar_color = radar_color;
    }

    public void setMax_angle(double max_angle) {
        this.max_angle = max_angle;
    }

    public void setRemained_energy(float remained_energy) {
        this.remained_energy = remained_energy;
    }

    public static void setOccupiedPorts(ArrayList<Integer> occupiedPorts) {
        UAV.occupiedPorts = occupiedPorts;
    }

    public static void setCenter_height(int center_height) {
        Unit.center_height = center_height;
    }

    public static void setCenter_width(int center_width) {
        Unit.center_width = center_width;
    }

    public boolean isLockedToThreat() {
        return lockedToThreat;
    }

    public void setLockedToThreat(boolean lockedToThreat) {
        this.lockedToThreat = lockedToThreat;
    }

    public synchronized ArrayList<Threat> getDestroyedThreats() {
        return destroyedThreats;
    }
    
    private void attackThreatDaemon()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() 
            {
                updateAll_local();
                if(kb.getThreats().size() > 0)
                {
                    for(Threat threat : kb.getThreats())
                    {
                        if(!getDestroyedThreats().contains(threat) && getFly_mode()!= Attacker.TARGET_LOCKED_MODE)
                        {
                            if (threat.getThreatType().toString().equals(getAttackerType().toString())) 
                            {
                                setTarget_indicated_by_role(threat);
                                setNeed_to_replan(true);
                                setSpeed(OpStaticInitConfig.SPEED_OF_ATTACKER_ON_TASK);
                                setFly_mode(Attacker.FLYING_MODE);
                            }
                        }
                    }
                }
            }
        }, 5000, 500);
    }
    
    public void updateAll_local()
    {
        if(visible) {
            pathPlan();
        }
        setNeed_to_replan(false);
        updateAttackerCoordinate();
        checkReplanningAccordingToAttackerMovement();
        checkThreatReached();
        detectAttackerEvent();
    }
    
    private void updateAttackerCoordinate() {
        if (!isVisible()) {
                return;
            }
            
            boolean moved = moveToNextWaypoint();
            if (moved) {
                logger.debug("attacker:" + getIndex() + " moved");
            }
    }
    
    /**During patrol,the attacker detect event by radar.
     * 
     */
    private void detectAttackerEvent() 
    {
        Attacker attacker = this;
        if (!attacker.isVisible()) {
                return;
            }
            ArrayList<Obstacle> obstacles = animationPanel.getWorld().getObstacles();
            int obs_list_size = obstacles.size();
            for (int i = 0; i < obs_list_size; i++) {
                Obstacle obs = obstacles.get(i);
                if (obs.getMbr().intersects(attacker.getUav_radar().getBounds())) {
                    if (!attacker.containsObstacle(obs)) {
                        attacker.addObstacle(obs);
                        attacker.setNeed_to_replan(true);
                    }
                }
            }
    }
    
    /** check whether the attacker should replan in  next time step. 
     * 
     */
    private void checkReplanningAccordingToAttackerMovement() {
        Attacker attacker = this;
        if (!attacker.isVisible() || attacker.getTarget_indicated_by_role() == null) {
            return;
        }
        boolean moved = attacker.isMoved_at_last_time();
        if (!moved) 
        {//not moved
            attacker.setNeed_to_replan(true);
            //returning to the base
            if (attacker.getTarget_indicated_by_role().getIndex() == Threat.UAV_BASE_INDEX) {
                float[] dummy_threat_coord = World.assignUAVPortInBase(attacker.getIndex());                    
                Threat dummy_threat = new Threat(Threat.UAV_BASE_INDEX, dummy_threat_coord, 0, ThreatType.DUMMY);
                attacker.setTarget_indicated_by_role(dummy_threat);
                attacker.setNeed_to_replan(true);
                attacker.setSpeed(OpStaticInitConfig.SPEED_OF_ATTACKER_IDLE);
                attacker.setFly_mode(Attacker.FLYING_MODE);
            } else {//have target to destroy
                float dist_to_target = DistanceUtil.distanceBetween(attacker.getCenter_coordinates(), attacker.getTarget_indicated_by_role().getCoordinates());
//                    logger.debug(attacker.getIndex() + " not moved and has target-------------start--------------");
//                    logger.debug("target index:" + attacker.getTarget_indicated_by_role().getIndex());
//                    logger.debug("target " + attacker.getTarget_indicated_by_role().getIndex() + " visible=" + this.getThreatsForUIRendering().get(attacker.getTarget_indicated_by_role().getIndex()).isEnabled());
//                    logger.debug("dist to target:" + dist_to_target);
//                    logger.debug("attacker mode:" + attacker.getFly_mode());
//                    logger.debug("not moved and has target-------------end--------------");

                if (attacker.getFly_mode() == Attacker.TARGET_LOCKED_MODE) {
                    attacker.setNeed_to_replan(true);
                } else {
                    if (dist_to_target < attacker.getUav_radar().getRadius() / 2) {
                        attacker.setNeed_to_replan(true);
                        attacker.setFly_mode(Attacker.TARGET_LOCKED_MODE);
                        attacker.setSpeed(OpStaticInitConfig.SPEED_OF_ATTACKER_ON_DESTROYING_THREAT);
                    } else {//not reaching target
                        attacker.setNeed_to_replan(true);
                        attacker.setFly_mode(Attacker.FLYING_MODE);
                        attacker.setSpeed(OpStaticInitConfig.SPEED_OF_ATTACKER_ON_TASK);
                    }
                }
            }
        } else {
                attacker.setNeed_to_replan(false);
        }
    }
    
    private void checkThreatReached() 
    {
        ArrayList<Obstacle> obstacles_in_the_world = animationPanel.getWorld().getObstacles();
        Attacker attacker = this;
        Target attacker_target = attacker.getTarget_indicated_by_role();
        if (!attacker.isVisible()) {
            return;
        }
        //Only when the attacker is in the base, its target indicated by the role is null
        if (attacker_target == null) {
            return;
        }
        int threat_index = attacker_target.getIndex();
        //threat_index=-1 means the attacker is returning to the base
        if (threat_index == Threat.UAV_BASE_INDEX) {
            float[] target_coord = attacker_target.getCoordinates();
            float[] attacker_coord = attacker.getCenter_coordinates();
            //if the attacker reached the base
            if (DistanceUtil.distanceBetween(attacker_coord, target_coord) < (attacker.getUav_radar().getRadius() / 3)) {
                attacker.setTarget_indicated_by_role(null);
                attacker.setNeed_to_replan(false);
                return;
            } else {
                attacker.setSpeed(OpStaticInitConfig.SPEED_OF_ATTACKER_IDLE);
                attacker.setFly_mode(Attacker.FLYING_MODE);
            }
            return;
        }
        
        //Otherwise, the attacker is flying toward its target
        ArrayList<Threat> threats_in_world = animationPanel.getWorld().getThreats();
        for (int j = 0; j < threats_in_world.size(); j++) {
            Threat threat = threats_in_world.get(j);
            //threat is destroyed
            if (!threat.isEnabled()) {
                continue;
            }
            //Otherwise, destroy its threat when the attacker is close to the threat
            if (threat_index == threat.getIndex()) 
            {
                float distance_to_target = DistanceUtil.distanceBetween(attacker.getCenter_coordinates(), threat.getCoordinates());
                if (distance_to_target < attacker.getUav_radar().getRadius() / 2) 
                {
                    if (attacker.getFly_mode() == Attacker.FLYING_MODE) 
                    {
                        attacker.setFly_mode(Attacker.TARGET_LOCKED_MODE);
                        attacker.setHovered_time_step(0);
                        threat.setMode(Threat.LOCKED_MODE);
                        attacker.setNeed_to_replan(true);
                        animationPanel.getWorld().lockAttackerToThreat(attacker, threat.getIndex());
                    } else if (attacker.getFly_mode() == Attacker.TARGET_LOCKED_MODE) 
                    {
                        if (attacker.getHovered_time_step() < OpStaticInitConfig.LOCKED_TIME_STEP_UNTIL_DESTROYED)
                        {
                            attacker.increaseHovered_time_step();
                        } 
                        else 
                        {
                            threat.setEnabled(false);
                            animationPanel.getWorld().updateDestroyedThreats(threat);
                            animationPanel.getWorld().threatDestroyedAndUnlocked(threat.getIndex());
                            int num_of_threat_remained = animationPanel.getWorld().getNum_of_threat_remained();
                            animationPanel.getWorld().setNum_of_threat_remained(--num_of_threat_remained);
                            
                            moveAttackerToBase(attacker, true);
                        }
                    }
                } 
                break;
            }
        }
    }

    private void moveAttackerToBase(Attacker attacker, boolean threatStatus) {
        this.current_angle =0;
        this.resetCurrentIndexOfPath();
        float[] dummy_threat_coord = World.assignUAVPortInBase(attacker.getIndex());
        Threat dummy_threat = new Threat(Threat.UAV_BASE_INDEX, dummy_threat_coord, 0, ThreatType.DUMMY);
        attacker.setTarget_indicated_by_role(dummy_threat);
        attacker.setNeed_to_replan(true);
        attacker.setSpeed(OpStaticInitConfig.SPEED_OF_ATTACKER_IDLE);
        attacker.setFly_mode(Attacker.FLYING_MODE);
        this.setThreatDestroyed(threatStatus);
    }

    public synchronized boolean isThreatDestroyed() {
        return threatDestroyed;
    }
    public synchronized void resetThreatDestroyed()
    {
        this.threatDestroyed = false;
    }
    public synchronized void setThreatDestroyed(boolean threatDestroyed) {
        this.threatDestroyed = threatDestroyed;
        this.attackerDevice.fireResourceChange(14);
    }
}
