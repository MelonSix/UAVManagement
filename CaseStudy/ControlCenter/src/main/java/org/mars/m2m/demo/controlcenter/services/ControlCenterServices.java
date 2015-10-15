/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.services;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.tree.DefaultMutableTreeNode;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
import org.mars.m2m.demo.controlcenter.dispatcher.BroadcastMessageDispatcher;
import org.mars.m2m.demo.controlcenter.dispatcher.DummyMessageDispatcher;
import org.mars.m2m.demo.controlcenter.dispatcher.MessageDispatcher;
import org.mars.m2m.demo.controlcenter.dispatcher.RegisteredMessageDispatcher;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.Conflict;
import org.mars.m2m.demo.controlcenter.model.KnowledgeAwareInterface;
import org.mars.m2m.demo.controlcenter.model.KnowledgeInterface;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Target;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.model.shape.Point;
import org.mars.m2m.demo.controlcenter.util.AttackerUtils;
import org.mars.m2m.demo.controlcenter.util.DistanceUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ControlCenterServices implements KnowledgeAwareInterface
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterServices.class);
    public static int time_step=0;
    private ExecutorService  executor;
    private int inforshare_algorithm = 0; //distinction between information-sharing algrithm
    
    private MessageDispatcher msg_dispatcher;
    private KnowledgeInterface kb;//this is set upon initialization of the Control Center GUI so it can be accessed across board
    private Map<Integer, LinkedList<Point>> way_point_for_uav;
    private float scout_speed;
    private boolean need_to_assign_role = false;

    private int scout_remained = -1;
    private boolean scout_scanned_over = false;
    private int sub_team_size = 2;
    private boolean simulationStartable;
    private final AttackerUtils attackerUtils;

    public Map<Integer, Set<Integer>> locked_threat;

    /** constructor of the control center.
     * 
     */
    public ControlCenterServices() {
        this.attackerUtils = new AttackerUtils();
        way_point_for_uav = new HashMap<>();
        locked_threat = new HashMap<>();  
        kb = new OntologyBasedKnowledge();
                    
        //share information in different ways
        if (this.inforshare_algorithm == CC_StaticInitConfig.BROADCAST_INFOSHARE) {
            this.msg_dispatcher = new BroadcastMessageDispatcher(this);
        } else if (this.inforshare_algorithm == CC_StaticInitConfig.REGISTER_BASED_INFORSHARE) {
            this.msg_dispatcher = new RegisteredMessageDispatcher(this);
        } else if (this.inforshare_algorithm == CC_StaticInitConfig.NONE_INFORSHARE) {
            this.msg_dispatcher = new DummyMessageDispatcher(this);
        }
    }
    
    

    /** assign the area to be scanned by each scout.
     * 
     * @return 
     */
    public static LinkedList<Float> roleAssignForScouts() {
        int scout_num = HandleTree.scoutsNode.getChildCount();
        
        int index = CC_StaticInitConfig.currentScoutIndex.getAndIncrement();

        float average_region_height = CC_StaticInitConfig.bound_height * 1.0f / scout_num;
        int task_num = (int) Math.ceil(average_region_height / (CC_StaticInitConfig.scout_radar_radius * 2));
        
            LinkedList<Float> move_at_y_coordinate_task = new LinkedList<>();
            float init_y_coord = average_region_height * index + CC_StaticInitConfig.scout_radar_radius;
            for (int task_index = 0; task_index < task_num; task_index++) {
                float coord_y = init_y_coord + task_index * CC_StaticInitConfig.scout_radar_radius * 2;
                if (coord_y - init_y_coord > average_region_height) {
                    coord_y = init_y_coord + average_region_height;
                }
                move_at_y_coordinate_task.add(coord_y);
            }
            
            //System.out.println("cc-roleassignfor :"+Arrays.asList(move_at_y_coordinate_task));
        return move_at_y_coordinate_task;        
    }

    
    /** assign role for uavs with subteam (size=this.subteam_size), considering the special case:
 attacker i should be assigned with role i. Other role should be assigned
     * to the nearest uav
     *
     * @param assigned_attacker_index
     * @param assigned_role_index
     */
    private void roleAssignForAttackerWithSubTeam(int assigned_attacker_index, int assigned_role_index) 
    {
        System.out.println("*******************role assign***********************************\n");
        
        executor = Executors.newFixedThreadPool(5);
        TreeSet<Integer> assigned_attacker = new TreeSet<>();
        ArrayList<Threat> threats = kb.getThreats();
        ArrayList<AttackerModel> attackersList = ReadAttackers.getAttackers();
        int threat_num = threats.size();
        int attacker_num = HandleTree.attackersNode.getChildCount();

        for (int i = 0; i < threat_num; i++) 
        {
            Threat threat = threats.get(i);
            if (!threat.isEnabled()) {
                continue;
            }
            Set<Integer> attackers_locked = getLockedAttackers();
            if(attackers_locked==null)
            {
                attackers_locked=new TreeSet<>();
            }
            assigned_attacker.addAll(attackers_locked);
            //manually assign
            if (threat.getIndex() == assigned_role_index) {
                for(AttackerModel attacker : attackersList)
                {                    
                    if (!attacker.isOnline()) 
                    {
                        continue;
                    }
                    if (assigned_attacker_index == attacker.getIndex()) 
                    {
                        if (attacker.getTarget_indicated_by_role() == null || 
                                    attacker.getTarget_indicated_by_role().getIndex() != threat.getIndex()) 
                        {
                            attacker.setFlightMode(CC_StaticInitConfig.FLYING_MODE);
                        }
                        attackerUtils.update.setTarget_indicated_by_role(threat, attacker);
                        attackerUtils.update.setSpeed(CC_StaticInitConfig.SPEED_OF_ATTACKER_ON_TASK, attacker);
                        attackerUtils.update.setReplan(true, attacker);
                        assigned_attacker.add(assigned_attacker_index);
                        break;
                    }
                }
                continue;
            }
            
            int remained_team_size=this.sub_team_size-attackers_locked.size();
            ArrayList<AttackerModel> attacker_arr_to_assign = new ArrayList<>();
            ArrayList<Float> attacker_dist_to_assign = new ArrayList<>();
            for (AttackerModel current_attacker : attackersList) 
            {
                        
                if (!current_attacker.isEnduranceCapReachable(threat)) {
                    continue;
                }
                if (assigned_attacker_index == current_attacker.getIndex()) {
                    continue;
                }
                if (!current_attacker.isOnline()) {
                    continue;
                }
                if (attackers_locked.contains(current_attacker.getIndex())) {
                    continue;
                }
                if (assigned_attacker.contains(current_attacker.getIndex())) {
                    continue;
                }

                float dist_between_uav_and_role = DistanceUtil.distanceBetween(current_attacker.getCenterCoordinates(), threat.getCoordinates());
                int index_to_insert = 0;
                boolean attacker_added = false;
                for (float attacker_dist : attacker_dist_to_assign) {
                    if (dist_between_uav_and_role < attacker_dist) {
                        attacker_added = true;
                        break;
                    }
                    index_to_insert++;
                }
                if (attacker_added) {
                    attacker_dist_to_assign.add(index_to_insert, dist_between_uav_and_role);
                    attacker_arr_to_assign.add(index_to_insert, current_attacker);

                    if (attacker_dist_to_assign.size() > remained_team_size) {
                        attacker_dist_to_assign.remove(remained_team_size);
                        attacker_arr_to_assign.remove(remained_team_size);
                    }
                } else if (attacker_dist_to_assign.size() < remained_team_size) {
                    attacker_dist_to_assign.add(dist_between_uav_and_role);
                    attacker_arr_to_assign.add(current_attacker);
                }
                
            }
            
            if (attacker_arr_to_assign.size() >= remained_team_size) {
                for (AttackerModel attacker : attacker_arr_to_assign) {
                    if (attacker.getFlightMode()== CC_StaticInitConfig.TARGET_LOCKED_MODE) {
                        continue;
                    }
                    assigned_attacker.add(attacker.getIndex());
                    attackerUtils.update.setTarget_indicated_by_role(threat, attacker);
                    attackerUtils.update.setSpeed(CC_StaticInitConfig.SPEED_OF_ATTACKER_ON_TASK, attacker);
                    attackerUtils.update.setReplan(true, attacker);
                    attackerUtils.update.setFlightMode(CC_StaticInitConfig.FLYING_MODE, attacker);
                }
            }
        }

        for (AttackerModel current_attacker : attackersList) 
        {
            if(current_attacker.getFlightMode() == CC_StaticInitConfig.TARGET_LOCKED_MODE)
            {
                continue;
            }
            if (!assigned_attacker.contains(current_attacker.getIndex()) && current_attacker.getTarget_indicated_by_role() 
                    != null && current_attacker.getTarget_indicated_by_role().getIndex() != -1) 
            {
                float[] dummy_threat_coord = current_attacker.getUavPositionInBaseStation();
                Threat dummy_threat = new Threat(-1, dummy_threat_coord, 0, 0);
                attackerUtils.update.setTarget_indicated_by_role(dummy_threat, current_attacker);
                attackerUtils.update.setReplan(true, current_attacker);
                attackerUtils.update.setSpeed(CC_StaticInitConfig.SPEED_OF_ATTACKER_IDLE, current_attacker);
                attackerUtils.update.setFlightMode(CC_StaticInitConfig.FLYING_MODE, current_attacker);
            }
        }
        need_to_assign_role = false;
    }
    

    /** lock the attacker to the threat. when attacker is close enough to the attacker, the method is called.
     * 
     * @param attacker_index
     * @param threat_index 
     */
    public void lockAttackerToThreat(Integer attacker_index, Integer threat_index) {
        Set<Integer> attackers_locked = this.locked_threat.get(threat_index);
        if (attackers_locked == null) {
            attackers_locked = new TreeSet<Integer>();
        }
        attackers_locked.add(attacker_index);
        this.locked_threat.put(threat_index, attackers_locked);
    }

    /** unlock the attacker to the threat. when attacker is destroyed, this method is called.
     * 
     * @param attacker_index
     * @param threat_index 
     */
    public void unlockAttacerFromThreat(Integer attacker_index, Integer threat_index) {
        Set<Integer> attackers_locked = this.locked_threat.get(threat_index);
        if (attackers_locked != null) {
            attackers_locked.remove(attacker_index);
        }
        if (attackers_locked == null) {
            this.locked_threat.remove(threat_index);
        } else {
            this.locked_threat.put(threat_index, attackers_locked);
        }
    }
    

    /** unlock all the assigned attackers, when the threat is destroyed.
     * 
     * @param threat_index 
     */
    public void threatDestroyedAndUnlocked(Integer threat_index) {
//        Set<Integer> assigned_attackers = this.locked_threat.remove(threat_index);
//        if (assigned_attackers == null) {
//            return;
//        }
//        for (Integer attacker_index : assigned_attackers) {
//            AttackerModel attacker = this.attackers.get(attacker_index);
//            attacker.setFly_mode(AttackerModel.FLYING_MODE);
//            float[] dummy_threat_coord = World.assignUAVPortInBase(attacker.getIndex());
//            Threat dummy_threat = new Threat(Threat.UAV_BASE_INDEX, dummy_threat_coord, 0, 0);
//            attacker.setTarget_indicated_by_role(dummy_threat);
//            attacker.setNeed_to_replan(true);
//            attacker.setSpeed(CC_StaticInitConfig.SPEED_OF_ATTACKER_IDLE);
//            attacker.setFly_mode(AttackerModel.FLYING_MODE);
//            attacker.setHovered_time_step(0);
//        }
    }

    /**
     *
     * @return
     */
    @Override
    public synchronized ArrayList<Obstacle> getObstacles() {
        return kb.getObstacles();
    }

    public float getScout_speed() {
        return scout_speed;
    }

    public void setScout_speed(float scout_speed) {
        this.scout_speed = scout_speed;
    }

    @Override
    public synchronized ArrayList<Conflict> getConflicts() {
        return kb.getConflicts();
    }

    public boolean isScout_scanned_over() {
        return scout_scanned_over;
    }

    public void setScout_scanned_over(boolean scout_scanned_over) {
        this.scout_scanned_over = scout_scanned_over;
    }

    @Override
    public synchronized ArrayList<Threat> getThreats() {
        return kb.getThreats();
    }

    @Override
    public void setObstacles(ArrayList<Obstacle> obstacles) {
        kb.setObstacles(obstacles);
    }

    @Override
    public void setConflicts(ArrayList<Conflict> conflicts) {
        kb.setConflicts(conflicts);
    }

    @Override
    public void setThreats(ArrayList<Threat> threats) {
        kb.setThreats(threats);
        this.need_to_assign_role = true;
    }

    @Override
    public void addObstacle(Obstacle obs) {
        kb.addObstacle(obs);
    }

    @Override
    public void addConflict(Conflict conflict) {
        kb.addConflict(conflict);
    }

    @Override
    public void addThreat(Threat threat) {
        kb.addThreat(threat);
        this.need_to_assign_role = true;
    }

    @Override
    public boolean containsThreat(Threat threat) {
        return kb.containsThreat(threat);
    }

    @Override
    public boolean containsObstacle(Obstacle obstacle) {
        return kb.containsObstacle(obstacle);
    }

    public boolean isNeed_to_assign_role() {
        return need_to_assign_role;
    }

    public void setNeed_to_assign_role(boolean need_to_assign_role) {
        this.need_to_assign_role = need_to_assign_role;
    }

    public KnowledgeInterface getKb() {
        return kb;
    }

    public void setKb(KnowledgeInterface kb) {
        this.kb = kb;
    }

    public synchronized boolean isSimulationStartable() {
        return simulationStartable;
    }

    public synchronized void setSimulationStartable(boolean simulationStartable) {
        this.simulationStartable = simulationStartable;
    }
    
    

    /**share information every 3 time step.
     *
     */
    public void shareInfoAfterRegistration() {
        if (this.time_step % 3 == 0) {
            this.msg_dispatcher.decideAndSumitMsgToSend();
            this.msg_dispatcher.dispatch();
        }
    }

    /**register information requirement for attackers, according to its target and location.
     *
     */
    public void registerInfoRequirement() 
    {
        ArrayList<AttackerModel> attackersList = ReadAttackers.getAttackers();
        for (AttackerModel attacker : attackersList) 
        {
            if (!attacker.isOnline()) {
                continue;
            }
            Target target = attacker.getTarget_indicated_by_role();
            if (target != null) {
                float[] attacker_coord = attacker.getCenterCoordinates();
                this.msg_dispatcher.register(attacker.getIndex(), attacker_coord, target);
            }
        }
    }
    
    /** calls the control center to assign role for each attacker.
     * 
     */
    public void roleAssignmentInControlCenter() {
        if (this.isNeed_to_assign_role()) {
            this.roleAssignForAttackerWithSubTeam(-1, -1);
        }
        this.setNeed_to_assign_role(false);
    } 
    
    /**
     * Gets all attackers that have been locked to a particular threat
     * @return 
     */
    private Set<Integer> getLockedAttackers()
    {
        Set<Integer> locked = new TreeSet<>();
        ArrayList<AttackerModel> attackersList = ReadAttackers.getAttackers();
        for (AttackerModel attacker : attackersList) 
        {
            if(attacker.isAttackerLocked())
                locked.add(attacker.getIndex());
        }
        return locked;
    }
}
