/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.demo.model.Target;
import org.mars.m2m.demo.world.KnowledgeInterface;
import org.mars.m2m.demo.world.OntologyBasedKnowledge;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class UavAttackerDevice extends BaseInstanceEnabler implements DeviceExecution
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UavAttackerDevice.class);
    private static final Gson gson = new Gson();
    private Attacker attacker;
    
    //device resources
    private int pathIndex;
    private boolean replan;
    private boolean movedAtLastStep;
    private KnowledgeInterface kb;
    private UAVPath pathPlannedAtLastStep;
    private UAVPath pathHistory;
    private UAVPath currentPath;
    private boolean hasReplanned;
    private int flightMode;
    private int hoveredTimeStep;
    private float [] iterationGoal;
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
    
    public UavAttackerDevice() 
    {
        this.kb = new OntologyBasedKnowledge();
    }

    @Override
    public ValueResponse read(int resourceid) {
       logger.info("[{}] Read on resource: {}",resourceid);
       //System.out.println("read on resource "+resourceid);
        try {
            switch (resourceid) {
                case 0:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(this.getPathIndex())));
                case 1:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBooleanValue(this.isReplan())));
                case 2:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBooleanValue(this.isMovedAtLastStep())));
                case 3:
                        ByteArrayOutputStream bo = new ByteArrayOutputStream();
                        ObjectOutputStream oo = new ObjectOutputStream(bo);
                        oo.writeObject(kb);
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBinaryValue(bo.toByteArray())));
                case 4:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getPathPlannedAtLastStep()))));
                case 5:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getPathHistory()))));
                case 6:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getPathHistory()))));
                case 7:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBooleanValue(this.isHasReplanned())));
                case 8:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(this.getFlightMode())));
                case 9:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(this.getHoveredTimeStep())));
                case 10:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getIterationGoal()))));
                case 11:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(this.getStuckTimes())));
                case 12:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(this.getMaximumStuckTimes()))); 
                case 13:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(this.getIndex())));    
                case 14:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getTarget_indicated_by_role()))));                     
                case 15:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBooleanValue(this.isOnline()))); 
                case 16:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getCenterCoordinates())))); 
                case 17:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newFloatValue(getRemainedEnergy()))); 
//                case 18:
//                    return new ValueResponse(ResponseCode.CONTENT,
//                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getUavBaseCenterCoordinates())))); 
//                case 19:
//                    return new ValueResponse(ResponseCode.CONTENT,
//                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getUavPositionInBaseStation())))); 
                default:
                    return super.read(resourceid);
            }
        } catch (Exception e) {
            logger.error("ERROR: {}",e.getMessage());
            return super.read(resourceid);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value) {
        System.out.println("Write on UAV Attacker Device Resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 0:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 1:
            System.out.println("Need to replan write");
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 3:
            System.out.println("Knowledegbase write");
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 7:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 8:
            System.out.println("Fly mode write");
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 10:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 12:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 13:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 14:
            System.out.println("target indicated by role write");
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 20:
            System.out.println("Speed write");
            return new LwM2mResponse(ResponseCode.CHANGED);
        default:
            return super.write(resourceid, value);
        }
    }
    
    @Override
    public void Reboot(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FactorReset(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ResetErrorCode(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getPathIndex() {
        if(this.attacker != null)
            this.setPathIndex(attacker.getCurrent_index_of_planned_path());
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public boolean isReplan() {
        if(this.attacker != null)
            this.setReplan(attacker.isReplanned_at_current_time_step());
        return replan;
    }

    public void setReplan(boolean replan) {
        this.replan = replan;
    }

    public boolean isMovedAtLastStep() {
        if(this.attacker != null)
            this.setMovedAtLastStep(attacker.isMoved_at_last_time());
        return movedAtLastStep;
    }

    public void setMovedAtLastStep(boolean movedAtLastStep) {
        this.movedAtLastStep = movedAtLastStep;
    }

    public KnowledgeInterface getKb() {
        if(this.attacker != null)
            this.setKb(attacker.getKb());
        return kb;
    }

    public void setKb(KnowledgeInterface kb) {
        this.kb = kb;
    }

    public UAVPath getPathPlannedAtLastStep() {
        if(this.attacker != null)
            this.setPathPlannedAtLastStep(attacker.getPath_planned_at_last_time_step());
        return pathPlannedAtLastStep;
    }

    public void setPathPlannedAtLastStep(UAVPath pathPlannedAtLastStep) {
        this.pathPlannedAtLastStep = pathPlannedAtLastStep;
    }

    public UAVPath getPathHistory() {
        if(this.attacker != null)
            this.setPathHistory(attacker.getHistory_path());
        return pathHistory;
    }

    public void setPathHistory(UAVPath pathHistory) {
        this.pathHistory = pathHistory;
    }

    public UAVPath getCurrentPath() {
        if(this.attacker != null)
            this.setCurrentPath(attacker.getPath_planned_at_current_time_step());
        return currentPath;
    }

    public void setCurrentPath(UAVPath currentPath) {
        this.currentPath = currentPath;
    }

    public boolean isHasReplanned() {
        if(this.attacker != null)
            this.setHasReplanned(attacker.isReplanned_at_current_time_step());
        return hasReplanned;
    }

    public void setHasReplanned(boolean hasReplanned) {
        this.hasReplanned = hasReplanned;
    }

    public int getFlightMode() {
        if(this.attacker != null)
            this.setFlightMode(attacker.getFly_mode());
        return flightMode;
    }

    public void setFlightMode(int flightMode) {
        this.flightMode = flightMode;
    }

    public int getHoveredTimeStep() {
        if(this.attacker != null)
            this.setHoveredTimeStep(attacker.getHovered_time_step());
        return hoveredTimeStep;
    }

    public void setHoveredTimeStep(int hoveredTimeStep) {
        this.hoveredTimeStep = hoveredTimeStep;
    }

    public float[] getIterationGoal() {
        if(this.attacker != null)
            this.setIterationGoal(attacker.getGoal_for_each_iteration());
        return iterationGoal;
    }

    public void setIterationGoal(float[] iterationGoal) {
        this.iterationGoal = iterationGoal;
    }

    public int getStuckTimes() {
        if(this.attacker != null)
            this.setStuckTimes(attacker.getStucked_times());
        return stuckTimes;
    }

    public void setStuckTimes(int stuckTimes) {
        this.stuckTimes = stuckTimes;
    }

    public int getMaximumStuckTimes() {
        if(this.attacker != null)
            this.setMaximumStuckTimes(attacker.getMax_stucked_times());
        return maximumStuckTimes;
    }

    public void setMaximumStuckTimes(int maximumStuckTimes) {
        this.maximumStuckTimes = maximumStuckTimes;
    }

    public Target getTarget_indicated_by_role() {
        if(this.attacker != null)
            this.setTarget_indicated_by_role(attacker.getTarget_indicated_by_role());
        return target_indicated_by_role;
    }

    public void setTarget_indicated_by_role(Target target_indicated_by_role) {
        this.target_indicated_by_role = target_indicated_by_role;
    }

    public boolean isOnline() {
        if(this.attacker != null)
            this.setOnline(attacker.isVisible());
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSpeed() {
        if(this.attacker != null)
            this.setSpeed(attacker.getSpeed());
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float[] getCenterCoordinates() {
        if(this.attacker != null)
            this.setCenterCoordinates(attacker.getCenter_coordinates());
        return centerCoordinates;
    }

    public void setCenterCoordinates(float[] centerCoordinates) {
        this.centerCoordinates = centerCoordinates;
    }

    public float getRemainedEnergy() {
        if(this.attacker != null)
            this.setRemainedEnergy(attacker.getRemained_energy());
        return remainedEnergy;
    }

    public void setRemainedEnergy(float remainedEnergy) {
        this.remainedEnergy = remainedEnergy;
    }

    /*public float[] getUavBaseCenterCoordinates() {
    if(this.attacker != null)
    this.setUavBaseCenterCoordinates(attacker.getCenter_coordinates());
    return uavBaseCenterCoordinates;
    }
    
    public void setUavBaseCenterCoordinates(float[] uavBaseCenterCoordinates) {
    this.uavBaseCenterCoordinates = uavBaseCenterCoordinates;
    }*/

    /*public float[] getUavPositionInBaseStation() {
    if(this.attacker != null)
    this.setUavPositionInBaseStation(attacker.getPath_planned_at_last_time_step());
    return uavPositionInBaseStation;
    }
    
    public void setUavPositionInBaseStation(float[] uavPositionInBaseStation) {
    this.uavPositionInBaseStation = uavPositionInBaseStation;
    }*/

    public void setAttacker(Attacker attacker) {
        this.attacker = attacker;
    }

    public Attacker getAttacker() {
        return attacker;
    }
    
}
