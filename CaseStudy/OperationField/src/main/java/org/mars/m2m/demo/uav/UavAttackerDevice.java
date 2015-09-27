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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.apache.commons.lang3.StringUtils;
import org.mars.m2m.demo.model.Conflict;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.Threat;

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
    public synchronized ValueResponse read(int resourceid) {
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
    public synchronized LwM2mResponse write(int resourceid, LwM2mResource resource) {
        System.out.println("Write on UAV Attacker Device Resource " + resourceid + " value " + resource);
        switch (resourceid) {
        case 0:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 1:
            setReplan(Boolean.valueOf(resource.getValue().value.toString()));
            System.out.println("Need to replan write: "+resource.getValue().value.toString());
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 3:
            Conflict conflict = gson.fromJson(parseValue(resource.getValue().value.toString()), Conflict.class);
            addConflict(conflict);
            System.out.println("Knowledegbase write: "+resource.getValue().value.toString());
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 7:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 8:
            setFlightMode(Integer.parseInt(resource.getValue().value.toString()));
            System.out.println("Fly mode write: "+resource.getValue().value.toString());
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 10:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 12:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 13:
            return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        case 14:
            String data = parseValue(resource.getValue().value.toString());
            Threat target = gson.fromJson(data, Threat.class);
            setTarget_indicated_by_role(target);
            System.out.println("target indicated by role write: "+data);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 20:
            setSpeed(Integer.parseInt(resource.getValue().value.toString()));
            System.out.println("Speed write: "+resource.getValue().value);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 21:
            Obstacle obstacle = gson.fromJson(parseValue(resource.getValue().value.toString()), Obstacle.class);
            addObstacle(obstacle);
            System.out.println("Obstacle write: "+resource.getValue().value.toString());
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 22:
            Threat threat = gson.fromJson(resource.getValue().value.toString(), Threat.class);
            addThreat(threat);
            System.out.println("Threat write: "+resource.getValue().value.toString());
            return new LwM2mResponse(ResponseCode.CHANGED);
        default:
            return super.write(resourceid, resource);
        }
    }

    @Override
    public synchronized LwM2mResponse execute(int resourceid, byte[] params) {
        System.out.printf("[{}] Execute on Attacker resource {}\n", this.getClass().getName() , resourceid);
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
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
        this.attacker.setCurrent_index_of_planned_path(pathIndex);
    }

    public boolean isReplan() {
        if(this.attacker != null)
            this.setReplan(attacker.isReplanned_at_current_time_step());
        return replan;
    }

    public void setReplan(boolean replan) {
        this.replan = replan;
        this.attacker.setNeed_to_replan(replan);
    }

    public boolean isMovedAtLastStep() {
        if(this.attacker != null)
            this.setMovedAtLastStep(attacker.isMoved_at_last_time());
        return movedAtLastStep;
    }

    public void setMovedAtLastStep(boolean movedAtLastStep) {
        this.movedAtLastStep = movedAtLastStep;
        this.attacker.setMoved_at_last_time(movedAtLastStep);
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
        this.attacker.setPath_planned_at_last_time_step(pathPlannedAtLastStep);
    }

    public UAVPath getPathHistory() {
        if(this.attacker != null)
            this.setPathHistory(attacker.getHistory_path());
        return pathHistory;
    }

    public void setPathHistory(UAVPath pathHistory) {
        this.pathHistory = pathHistory;
        this.attacker.setHistory_path(pathHistory);
    }

    public UAVPath getCurrentPath() {
        if(this.attacker != null)
            this.setCurrentPath(attacker.getPath_planned_at_current_time_step());
        return currentPath;
    }

    public void setCurrentPath(UAVPath currentPath) {
        this.currentPath = currentPath;
        this.attacker.setPath_planned_at_current_time_step(currentPath);
    }

    public boolean isHasReplanned() {
        if(this.attacker != null)
            this.setHasReplanned(attacker.isReplanned_at_current_time_step());
        return hasReplanned;
    }

    public void setHasReplanned(boolean hasReplanned) {
        this.hasReplanned = hasReplanned;
        this.attacker.setReplanned_at_current_time_step(hasReplanned);
    }

    public int getFlightMode() {
        if(this.attacker != null)
            this.setFlightMode(attacker.getFly_mode());
        return flightMode;
    }

    public void setFlightMode(int flightMode) {
        this.flightMode = flightMode;
        this.attacker.setFly_mode(flightMode);
    }

    public int getHoveredTimeStep() {
        if(this.attacker != null)
            this.setHoveredTimeStep(attacker.getHovered_time_step());
        return hoveredTimeStep;
    }

    public void setHoveredTimeStep(int hoveredTimeStep) {
        this.hoveredTimeStep = hoveredTimeStep;
        this.attacker.setHovered_time_step(hoveredTimeStep);
    }

    public float[] getIterationGoal() {
        if(this.attacker != null)
            this.setIterationGoal(attacker.getGoal_for_each_iteration());
        return iterationGoal;
    }

    public void setIterationGoal(float[] iterationGoal) {
        this.iterationGoal = iterationGoal;
        this.attacker.setGoal_for_each_iteration(iterationGoal);
    }

    public int getStuckTimes() {
        if(this.attacker != null)
            this.setStuckTimes(attacker.getStucked_times());
        return stuckTimes;
    }

    public void setStuckTimes(int stuckTimes) {
        this.stuckTimes = stuckTimes;
        this.attacker.setStucked_times(stuckTimes);
    }

    public int getMaximumStuckTimes() {
        if(this.attacker != null)
            this.setMaximumStuckTimes(attacker.getMax_stucked_times());
        return maximumStuckTimes;
    }

    public void setMaximumStuckTimes(int maximumStuckTimes) {
        this.maximumStuckTimes = maximumStuckTimes;
        this.attacker.setMax_stucked_times(maximumStuckTimes);
    }

    public Target getTarget_indicated_by_role() {
        if(this.attacker != null)
            this.setTarget_indicated_by_role(attacker.getTarget_indicated_by_role());
        return target_indicated_by_role;
    }

    public void setTarget_indicated_by_role(Target target_indicated_by_role) {
        if (this.target_indicated_by_role == target_indicated_by_role) {
            return;
        }
        this.target_indicated_by_role = target_indicated_by_role;
        this.attacker.setTarget_indicated_by_role(target_indicated_by_role);
    }

    public boolean isOnline() {
        if(this.attacker != null)
            this.setOnline(attacker.isVisible());
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
        this.attacker.setVisible(online);
    }

    public int getSpeed() {
        if(this.attacker != null)
            this.setSpeed(attacker.getSpeed());
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        this.attacker.setSpeed(speed);
    }

    public float[] getCenterCoordinates() {
        if(this.attacker != null)
            this.setCenterCoordinates(attacker.getCenter_coordinates());
        return centerCoordinates;
    }

    public void setCenterCoordinates(float[] centerCoordinates) {
        this.centerCoordinates = centerCoordinates;
        this.attacker.setCenter_coordinates(centerCoordinates);
    }

    public float getRemainedEnergy() {
        if(this.attacker != null)
            this.setRemainedEnergy(attacker.getRemained_energy());
        return remainedEnergy;
    }

    public void setRemainedEnergy(float remainedEnergy) {
        this.remainedEnergy = remainedEnergy;
        this.attacker.setRemained_energy(remainedEnergy);
    }
    
    public void addObstacle(Obstacle obs) {
        if (!this.attacker.kb.containsObstacle(obs)) {
            this.attacker.addObstacle(obs);
        }
    }

    public void addConflict(Conflict conflict) {
        this.attacker.addConflict(conflict);
    }

    public void addThreat(Threat threat) {
        ArrayList<Threat> threats = this.attacker.getThreats();
        for (int i = 0; i < threats.size(); i++) 
        {
            Threat current_threat = threats.get(i);
            if (threat.getIndex() == current_threat.getIndex()) {
                this.attacker.kb.removeThreat(current_threat);
                this.addThreat(threat);
                if (this.attacker.target_indicated_by_role != null && threat.getIndex() == this.attacker.target_indicated_by_role.getIndex()) {
                    this.attacker.target_indicated_by_role = threat;
                }
                return;
            }
        }
        if (this.attacker.target_indicated_by_role != null && threat.getIndex() == this.attacker.target_indicated_by_role.getIndex()) {
            this.attacker.target_indicated_by_role = threat;
        }
        this.attacker.addThreat(threat);
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
    
    /**
     * Converts the received data of a complex member variable to a JSON string     * 
     * @param data
     * @return 
     */
    private String parseValue(String data)
    {
        StringBuilder jsonData = new StringBuilder();
        jsonData.append("{");
            data = StringUtils.removeStart(data, "{");
            data = StringUtils.removeEnd(data, "}");
            data = StringUtils.replace(data, "=", ":");
        jsonData.append(applyRegEx(data));
        jsonData.append("}");
        return jsonData.toString();
    }
    
    /**
     * This method is used to help in converting a complex object's data into a
     * JSON string
     * @param data The submitted data as a string
     * @return The JSON equivalent of the submitted string
     */
    private String applyRegEx(String data)
    {
        //regular expresion for getting value names
        String elementPart = "([\\w]*:)";
        
        //regular expression for gettng the values 
        String elValuePart = "(:)([\\w]*)";
        
        //apply regular expressions to patterns
        Pattern elPattern = Pattern.compile(elementPart);
        Pattern valPattern = Pattern.compile(elValuePart);
        
        //get matchers to use patterns to match the data/String
        Matcher elMatcher = elPattern.matcher(data);
        Matcher vMatcher = valPattern.matcher(data);
        while(elMatcher.find())//handles value part
        {   
            String element = elMatcher.group();
            data = StringUtils.replaceOnce(data, element, "\""+element.replace(":", "\":"));
        }
        while (vMatcher.find( ))//handles the value part
        {
           String value = vMatcher.group();
           value = StringUtils.remove(value, ":");
           if(!StringUtils.isNumeric(value) && //not a number
                   !StringUtils.equals(value, "true") && //not a boolean value
                   !StringUtils.equals(value, "false") )//not a boolean value
           {
               if(StringUtils.isEmpty(value))
                   data = data.replace(":,", ":null,");
               else
                   data = StringUtils.replaceOnce(data, value, "\""+value+"\"");
           }
        } 
        return data;
    }
    
        
    
}
