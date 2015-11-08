/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model;

import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import java.awt.Rectangle;
import org.mars.m2m.demo.controlcenter.enums.AttackerType;
import org.mars.m2m.demo.controlcenter.util.AttackerUtils;
import org.mars.m2m.demo.controlcenter.util.DistanceUtil;
import org.mars.m2m.demo.controlcenter.util.RectangleUtil;

/**
 *
 * @author AG BRIGHTER
 */
public class AttackerModel 
{
    public ReportedLwM2MClient client;
    
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

    public AttackerModel() {
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
        } else if (msg_type == Message.OBSTACLE_MSG) {
            Obstacle obstacle = (Obstacle) msg;
            attackerUtils.execute.addObstacle(obstacle, this);
            if (this.getTarget_indicated_by_role()== null || !this.isObstacleInTargetMBR(obstacle.getShape().getBounds())) {
                attackerUtils.update.setReplan(true, this);
            }
        } else if (msg_type == Message.THREAT_MSG) {
            Threat threat = (Threat) msg;
            attackerUtils.execute.addThreat(threat,  this);
            attackerUtils.update.setReplan(true, this);
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

    public boolean containsObstacle(Obstacle obstacle) {
        return false;//TODO: Handle this operation
    }
}
