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
    
    //device resources
    private int pathIndex;
    private boolean replan;
    private boolean movedAtLastStep;
    private KnowledgeInterface kb;
    private String pathPlannedAtLastStep="";
    private String pathHistory="";
    private String currentPath="";
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
       System.out.println("read on resource "+resourceid);
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
                            new LwM2mResource(resourceid, Value.newStringValue(this.getPathPlannedAtLastStep())));
                case 5:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(this.getPathHistory())));
                case 6:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(this.getCurrentPath())));
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
                case 18:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getUavBaseCenterCoordinates())))); 
                case 19:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(this.getUavPositionInBaseStation())))); 
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
        return super.write(resourceid, value); //To change body of generated methods, choose Tools | Templates.
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

    public KnowledgeInterface getKb() {
        return kb;
    }

    public void setKb(KnowledgeInterface kb) {
        this.kb = kb;
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

    public float[] getIterationGoal() {
        return iterationGoal;
    }

    public void setIterationGoal(float[] iterationGoal) {
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
    
}
