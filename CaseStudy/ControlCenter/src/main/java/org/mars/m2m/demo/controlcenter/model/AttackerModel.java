/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model;

import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.mars.m2m.demo.controlcenter.enums.AttackerType;
import org.mars.m2m.demo.controlcenter.services.OntologyBasedKnowledge;
import org.mars.m2m.demo.controlcenter.util.AttackerUtils;
import org.mars.m2m.demo.controlcenter.util.DistanceUtil;
import org.mars.m2m.demo.controlcenter.util.RectangleUtil;

/**
 *
 * @author AG BRIGHTER
 */
public class AttackerModel implements KnowledgeAwareInterface
{
    private ReportedLwM2MClient client;
    
    //device resources
    private int flightMode;
    private int index;
    private Target target_indicated_by_role;
    private boolean online;
    private float [] centerCoordinates;
    private float remainedEnergy;
    private final float [] uavBaseCenterCoordinates;;
    private float [] uavPositionInBaseStation;
    private boolean attackerLocked;
    private AttackerType attackerType;
    private KnowledgeInterface kb;
    private boolean threatDestroyed;
    private int speed;

    public AttackerModel() {
        this.uavBaseCenterCoordinates = new float[]{60, 60};
        this.kb = new OntologyBasedKnowledge();
    }
    
    /**
     * 
     * @param index
     * @param flightMode
     * @param target
     * @param online
     * @param center_coordinates
     * @param locked
     * @param remained_energy
     * @param attackerType 
     */
    public AttackerModel(int index, int flightMode, Target target, boolean online, float[] center_coordinates, boolean locked,
            float remained_energy, AttackerType attackerType)
    {
        this.index = index;
        this.flightMode = flightMode;
        this.target_indicated_by_role = target;
        this.online = online;
        this.centerCoordinates = center_coordinates;
        this.attackerLocked = locked;
        this.remainedEnergy = remained_energy;
        this.threatDestroyed = true;
        this.attackerType = attackerType;
        this.kb = new OntologyBasedKnowledge();
        this.uavBaseCenterCoordinates = new float[]{60, 60};
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }  

    public int getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(int flightMode) {
        this.flightMode = flightMode;
    }

    public Target getTarget_indicated_by_role() {
        return target_indicated_by_role;
    }

    public void setTarget_indicated_by_role(Target target_indicated_by_role) {
        this.target_indicated_by_role = target_indicated_by_role;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public float[] getCenterCoordinates() {
        return centerCoordinates;
    }

    public void setCenterCoordinates(float[] centerCoordinates) {
        this.centerCoordinates = centerCoordinates;
    }

    public float[] getUavPositionInBaseStation() {
        return uavPositionInBaseStation;
    }

    public void setUavPositionInBaseStation(float[] uavPositionInBaseStation) {
        this.uavPositionInBaseStation = uavPositionInBaseStation;
    }

    public void setClient(ReportedLwM2MClient client) {
        this.client = client;
    }

    public ReportedLwM2MClient getClient() {
        return client;
    }

    public boolean isAttackerLocked() {
        return attackerLocked;
    }

    public void setAttackerLocked(boolean attackerLocked) {
        this.attackerLocked = attackerLocked;
    }

    public float getRemainedEnergy() {
        return remainedEnergy;
    }

    public void setRemainedEnergy(float remainedEnergy) {
        this.remainedEnergy = remainedEnergy;
    }

    public AttackerType getAttackerType() {
        return attackerType;
    }

    public void setAttackerType(AttackerType attackerType) {
        this.attackerType = attackerType;
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
    
    /** parsing the received msgs, The received msgs are parsed into obstacle, threat, or conflict. 
     * And the uav should replan when received new info.
     *
     * @param msg
     */
    private void parseMessage(Message msg) {
        AttackerUtils attackerUtils = new AttackerUtils();
        int msg_type = msg.getMsg_type();
        if (msg_type == Message.CONFLICT_MSG) {
            Conflict conflict = (Conflict) msg;
            attackerUtils.execute.addConflict(conflict, this);
            attackerUtils.update.setReplan(true, this);
        }  
        else if (msg_type == Message.THREAT_MSG) {
            Threat threat = (Threat) msg;
            attackerUtils.execute.addThreat(threat,  this);
            attackerUtils.update.setReplan(true, this);
        }
        else if (msg_type == Message.OBSTACLE_MSG) {
            Obstacle obstacle = (Obstacle) msg;
            attackerUtils.execute.addObstacle(obstacle, this);
            if (this.getTarget_indicated_by_role()== null || !this.isObstacleInTargetMBR(obstacle.getShape().getBounds())) {
                attackerUtils.update.setReplan(true, this);
            }
        }
    }
    
    /** check whether the remained energy of the attacker is enough to destroy the target and return back to the uav base.
     * 
     * @param potential_target
     * @return 
     */
    public boolean isEnduranceCapReachable(Target potential_target) {
        float dist_to_potential_target = DistanceUtil.distanceBetween(centerCoordinates, potential_target.getCoordinates());
        float dist_from_potential_target_to_uav_base = DistanceUtil.distanceBetween(potential_target.getCoordinates(), uavBaseCenterCoordinates);
        float path_parameter = 1.5f;
        if (path_parameter * (dist_to_potential_target + dist_from_potential_target_to_uav_base) > remainedEnergy) {
            return false;
        }
        return true;
    }
   
    /** check whether obstacle is in the rectangle, which is constructed by the current location of this uav and its target location.
     * 
     * @param obstacleMinimumBoundedRectangle
     * @return 
     */
    public boolean isObstacleInTargetMBR(Rectangle obstacleMinimumBoundedRectangle)
    {
        float[] target_coord=this.getTarget_indicated_by_role().getCoordinates();
        Rectangle rect = RectangleUtil.findMBRRect(this.centerCoordinates, target_coord);
        return rect.intersects(obstacleMinimumBoundedRectangle);
    }

    @Override
    public boolean containsObstacle(Obstacle obstacle) {
        return false;//TODO: Handle this operation
    }

    @Override
    public synchronized boolean containsThreat(Threat threat) {
        return this.kb.containsThreat(threat);
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

    /**
     * @return the kb
     */
    public KnowledgeInterface getKb() {
        return kb;
    }

    /**
     * @param kb the kb to set
     */
    public void setKb(KnowledgeInterface kb) {
        this.kb = kb;
    }

    public boolean isThreatDestroyed() {
        return threatDestroyed;
    }

    public void setThreatDestroyed(boolean threatDestroyed) {
        this.threatDestroyed = threatDestroyed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
