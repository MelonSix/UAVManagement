/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model;

import org.mars.m2m.demo.controlcenter.util.DistanceUtil;

/**
 *
 * @author AG BRIGHTER
 */
public class Attacker 
{
    //device resources
    private int pathIndex;
    private boolean replan;
    private boolean movedAtLastStep;
    /*private KnowledgeInterface kb;*/
    private String pathPlannedAtLastStep;
    private String pathHistory;
    private String currentPath;
    private boolean hasReplanned;
    private int flightMode;
    private int hoveredTimeStep;
    private String iterationGoal;
    private int stuckTimes;
    private int maximumStuckTimes;
    private int index;
    private Target target_indicated_by_role;
    private boolean online;
    private int speed;
    private float [] centerCoordinates;
    private float remainedEnergy;
    private float [] uavBaseCenterCoordinates;
    private float [] uavPositionInBaseStation;

    public Attacker() {
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public boolean isReplan() {
        return replan;
    }

    public void setReplan(boolean replan) {
        this.replan = replan;
    }

    public boolean isMovedAtLastStep() {
        return movedAtLastStep;
    }

    public void setMovedAtLastStep(boolean movedAtLastStep) {
        this.movedAtLastStep = movedAtLastStep;
    }

    public String getPathPlannedAtLastStep() {
        return pathPlannedAtLastStep;
    }

    public void setPathPlannedAtLastStep(String pathPlannedAtLastStep) {
        this.pathPlannedAtLastStep = pathPlannedAtLastStep;
    }

    public String getPathHistory() {
        return pathHistory;
    }

    public void setPathHistory(String pathHistory) {
        this.pathHistory = pathHistory;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public boolean isHasReplanned() {
        return hasReplanned;
    }

    public void setHasReplanned(boolean hasReplanned) {
        this.hasReplanned = hasReplanned;
    }

    public int getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(int flightMode) {
        this.flightMode = flightMode;
    }

    public int getHoveredTimeStep() {
        return hoveredTimeStep;
    }

    public void setHoveredTimeStep(int hoveredTimeStep) {
        this.hoveredTimeStep = hoveredTimeStep;
    }

    public String getIterationGoal() {
        return iterationGoal;
    }

    public void setIterationGoal(String iterationGoal) {
        this.iterationGoal = iterationGoal;
    }

    public int getStuckTimes() {
        return stuckTimes;
    }

    public void setStuckTimes(int stuckTimes) {
        this.stuckTimes = stuckTimes;
    }

    public int getMaximumStuckTimes() {
        return maximumStuckTimes;
    }

    public void setMaximumStuckTimes(int maximumStuckTimes) {
        this.maximumStuckTimes = maximumStuckTimes;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float[] getCenterCoordinates() {
        return centerCoordinates;
    }

    public void setCenterCoordinates(float[] centerCoordinates) {
        this.centerCoordinates = centerCoordinates;
    }

    public float getRemainedEnergy() {
        return remainedEnergy;
    }

    public void setRemainedEnergy(float remainedEnergy) {
        this.remainedEnergy = remainedEnergy;
    }

    public float[] getUavBaseCenterCoordinates() {
        return uavBaseCenterCoordinates;
    }

    public void setUavBaseCenterCoordinates(float[] uavBaseCenterCoordinates) {
        this.uavBaseCenterCoordinates = uavBaseCenterCoordinates;
    }

    public float[] getUavPositionInBaseStation() {
        return uavPositionInBaseStation;
    }

    public void setUavPositionInBaseStation(float[] uavPositionInBaseStation) {
        this.uavPositionInBaseStation = uavPositionInBaseStation;
    }
    
    
    
    /** check whether the remained energy of the attacker is enough to destory the target and return back to the uav base.
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
}
