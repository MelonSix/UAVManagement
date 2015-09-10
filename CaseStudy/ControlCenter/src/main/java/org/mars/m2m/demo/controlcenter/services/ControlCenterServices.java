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
import org.mars.m2m.demo.controlcenter.appConfig.StaticInitConfig;
import org.mars.m2m.demo.controlcenter.core.HandleTree;
import org.mars.m2m.demo.controlcenter.model.Conflict;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.model.shape.Point;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ControlCenterServices extends KnowledgeInterface
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterServices.class);

    private static KnowledgeInterface kb;//this is set upon initialization of the Control Center GUI
    private Map<Integer, LinkedList<Point>> way_point_for_uav;
    private float scout_speed;
    private boolean need_to_assign_role = true;

    private int scout_remained = -1;
    private boolean scout_scanned_over = false;
    private int sub_team_size = 2;

    public Map<Integer, Set<Integer>> locked_threat;

    /** constructor of the control center.
     * 
     * @param kb is the knowledge of the control center
     */
    public ControlCenterServices() {
        way_point_for_uav = new HashMap<>();
        locked_threat = new HashMap<>();
    }

    /** assign the area to be scanned by each scout.
     * 
     */
    private static int assignedScoutsCounter;
    public static LinkedList<Float> roleAssignForScouts() {
        int scout_num = HandleTree.scoutsNode.getChildCount();

        float average_region_height = StaticInitConfig.bound_height * 1.0f / scout_num;
        int task_num = (int) Math.ceil(average_region_height / (StaticInitConfig.scout_radar_radius * 2));
        
            LinkedList<Float> move_at_y_coordinate_task = new LinkedList<Float>();
            float init_y_coord = average_region_height * assignedScoutsCounter + StaticInitConfig.scout_radar_radius;
            for (int task_index = 0; task_index < task_num; task_index++) {
                float coord_y = init_y_coord + task_index * StaticInitConfig.scout_radar_radius * 2;
                if (coord_y - init_y_coord > average_region_height) {
                    coord_y = init_y_coord + average_region_height;
                }
                move_at_y_coordinate_task.add(coord_y);
            }
            
        return move_at_y_coordinate_task;        
    }

    
    /** assign role for uavs with subteam (size=this.subteam_size), considering the special case:
     * attacker i should be assigned with role j. Other role should be assigned
     * to the nearest uav
     *
     * @param assigned_attacker_index
     * @param assigned_role_index
     */
    public void roleAssignForAttackerWithSubTeam(int assigned_attacker_index, int assigned_role_index) 
    {
//        TreeSet<Integer> assigned_attacker = new TreeSet<Integer>();
//        ArrayList<Threat> threats = kb.getThreats();
//        int threat_num = threats.size();
//        int attacker_num = attackers.size();
//
//        for (int i = 0; i < threat_num; i++) {
//            Threat threat = threats.get(i);
//            if (!threat.isEnabled()) {
//                continue;
//            }
//            Set<Integer> attackers_locked=this.locked_threat.get(threat.getIndex());
//            if(attackers_locked==null)
//            {
//                attackers_locked=new TreeSet<Integer>();
//            }
//            assigned_attacker.addAll(attackers_locked);
//            //mannaually assign
//            if (threat.getIndex() == assigned_role_index) {
//                for (int j = 0; j < attacker_num; j++) {
//                    Attacker attacker = this.attackers.get(j);
//                    if (!attacker.isVisible()) {
//                        continue;
//                    }
//                    if (assigned_attacker_index == attacker.getIndex()) {
//                        if (attacker.getTarget_indicated_by_role() == null || attacker.getTarget_indicated_by_role().getIndex() != threat.getIndex()) {
//                            attacker.setFly_mode(Attacker.FLYING_MODE);
//                        }
//                        attacker.setTarget_indicated_by_role(threat);
//                        attacker.setSpeed(StaticInitConfig.SPEED_OF_ATTACKER_ON_TASK);
//                        attacker.setNeed_to_replan(true);
//                        assigned_attacker.add(assigned_attacker_index);
//                        break;
//                    }
//                }
//                continue;
//            }
//            
//            int remained_team_size=this.sub_team_size-attackers_locked.size();
//            ArrayList<Attacker> attacker_arr_to_assign = new ArrayList<Attacker>();
//            ArrayList<Float> attacker_dist_to_assign = new ArrayList<Float>();
//            for (int j = 0; j < attacker_num; j++) {
//                Attacker current_attacker = this.attackers.get(j);
//                if (!current_attacker.isEnduranceCapReachable(threat)) {
//                    continue;
//                }
//                if (assigned_attacker_index == current_attacker.getIndex()) {
//                    continue;
//                }
//                if (!current_attacker.isVisible()) {
//                    continue;
//                }
//                if (attackers_locked.contains(j)) {
//                    continue;
//                }
//                if (assigned_attacker.contains(current_attacker.getIndex())) {
//                    continue;
//                }
//
//                float dist_between_uav_and_role = DistanceUtil.distanceBetween(current_attacker.getCenter_coordinates(), threat.getCoordinates());
//                int index_to_insert = 0;
//                boolean attacker_added = false;
//                for (float attacker_dist : attacker_dist_to_assign) {
//                    if (dist_between_uav_and_role < attacker_dist) {
//                        attacker_added = true;
//                        break;
//                    }
//                    index_to_insert++;
//                }
//                if (attacker_added) {
//                    attacker_dist_to_assign.add(index_to_insert, dist_between_uav_and_role);
//                    attacker_arr_to_assign.add(index_to_insert, current_attacker);
//
//                    if (attacker_dist_to_assign.size() > remained_team_size) {
//                        attacker_dist_to_assign.remove(remained_team_size);
//                        attacker_arr_to_assign.remove(remained_team_size);
//                    }
//                } else if (attacker_dist_to_assign.size() < remained_team_size) {
//                    attacker_dist_to_assign.add(dist_between_uav_and_role);
//                    attacker_arr_to_assign.add(current_attacker);
//                }
//            }
//            
//            if (attacker_arr_to_assign.size() >= remained_team_size) {
//                for (Attacker attacker : attacker_arr_to_assign) {
//                    if (attacker.getFly_mode() == Attacker.TARGET_LOCKED_MODE) {
//                        continue;
//                    }
//                    assigned_attacker.add(attacker.getIndex());
//                    attacker.setTarget_indicated_by_role(threat);
//
//                    attacker.setSpeed(StaticInitConfig.SPEED_OF_ATTACKER_ON_TASK);
//                    attacker.setNeed_to_replan(true);
//                    attacker.setFly_mode(Attacker.FLYING_MODE);
//                }
//            }
//        }
//
//        for (int j = 0; j < attacker_num; j++) {
//            Attacker current_attacker = this.attackers.get(j);
//            if(current_attacker.getFly_mode()==Attacker.TARGET_LOCKED_MODE)
//            {
//                continue;
//            }
//            if (!assigned_attacker.contains(current_attacker.getIndex()) && current_attacker.getTarget_indicated_by_role() != null && current_attacker.getTarget_indicated_by_role().getIndex() != -1) {
//                float[] dummy_threat_coord = World.assignUAVPortInBase(current_attacker.getIndex());
//                Threat dummy_threat = new Threat(-1, dummy_threat_coord, 0, 0);
//                current_attacker.setTarget_indicated_by_role(dummy_threat);
//                current_attacker.setNeed_to_replan(true);
//                current_attacker.setSpeed(StaticInitConfig.SPEED_OF_ATTACKER_IDLE);
//                current_attacker.setFly_mode(Attacker.FLYING_MODE);
//            }
//        }
//        need_to_assign_role = false;
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
//            Attacker attacker = this.attackers.get(attacker_index);
//            attacker.setFly_mode(Attacker.FLYING_MODE);
//            float[] dummy_threat_coord = World.assignUAVPortInBase(attacker.getIndex());
//            Threat dummy_threat = new Threat(Threat.UAV_BASE_INDEX, dummy_threat_coord, 0, 0);
//            attacker.setTarget_indicated_by_role(dummy_threat);
//            attacker.setNeed_to_replan(true);
//            attacker.setSpeed(StaticInitConfig.SPEED_OF_ATTACKER_IDLE);
//            attacker.setFly_mode(Attacker.FLYING_MODE);
//            attacker.setHovered_time_step(0);
//        }
    }

    @Override
    public ArrayList<Obstacle> getObstacles() {
        return kb.getObstacles();
    }

    public float getScout_speed() {
        return scout_speed;
    }

    public void setScout_speed(float scout_speed) {
        this.scout_speed = scout_speed;
    }

    @Override
    public ArrayList<Conflict> getConflicts() {
        return kb.getConflicts();
    }

    public boolean isScout_scanned_over() {
        return scout_scanned_over;
    }

    public void setScout_scanned_over(boolean scout_scanned_over) {
        this.scout_scanned_over = scout_scanned_over;
    }

    @Override
    public ArrayList<Threat> getThreats() {
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

    @Override
    public boolean removeObstacle(Obstacle obstacle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeThreat(Threat threat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeConflict(Conflict conflict) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public boolean containsConflict(Conflict conflict) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static KnowledgeInterface getKb() {
        return kb;
    }

    public static void setKb(KnowledgeInterface kb) {
        ControlCenterServices.kb = kb;
    }
    
    
}
