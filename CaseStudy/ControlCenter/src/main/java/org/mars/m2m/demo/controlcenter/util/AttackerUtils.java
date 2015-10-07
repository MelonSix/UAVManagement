/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.callback.AsyncServiceCallback;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.Conflict;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.model.Target;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Content;
import org.mars.m2m.demo.controlcenter.model.endpointModel.ObjectInstance;
import org.mars.m2m.demo.controlcenter.model.endpointModel.ObjectResourceUpdate;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Resource;
import org.mars.m2m.demo.controlcenter.uav.UAVPath;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResourceDataType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.utils.json.jsonarrayparser.JsonArrayUtil;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AttackerUtils 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AttackerUtils.class);
    private static final RequestUtil requestUtil = new RequestUtil();
    private static final Gson gson = new Gson();
    private static final JsonArrayUtil jsonArrayUtil = new JsonArrayUtil();
    public final AttackerUpdate update;
    public final AttackerExecution execute;
    
    public AttackerUtils() {
        this.update = new AttackerUpdate();
        this.execute = new AttackerExecution();
    }
    
    /**
     * Provides the an attacker model that is a virtual operation field attacker
     * @param node
     * @return
     */
    public static AttackerModel getVirtualizedAttacker(DefaultMutableTreeNode node) 
    {
        Object nodeInfo;
        nodeInfo = node.getUserObject();
        ReportedLwM2MClient client = requestUtil.getLwM2MClientFromTreeNode(node);
        AttackerModel attacker = getAttacker(client);
        attacker.setClient(client);
        return attacker;
    }
    
    private static AttackerModel getAttacker(ReportedLwM2MClient client)
    {        
        try {
            String endpointURL = CC_StaticInitConfig.mgmntServerURL + client.getEndpoint() + "/12207/0";
            RequestPrimitive resp = 
                    (RequestPrimitive) requestUtil.sendToEndpoint(CC_StaticInitConfig.ccAddress, endpointURL, 
                            Operation.RETRIEVE, null, RequestPrimitive.class);
            if (resp == null) {
                return null;
            }
            return parseDataToAttacker(resp);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    
    private static AttackerModel parseDataToAttacker(RequestPrimitive req)
    {
        String endpointData = requestUtil.extractRequestData(req);//in JSON format
        return attackerObjectInstanceDataMapping(endpointData);
    }
    
    private static AttackerModel attackerObjectInstanceDataMapping(String instanceDataStr)
    {
        AttackerModel attacker = new AttackerModel();
        ObjectInstance objectInstance = gson.fromJson(instanceDataStr, ObjectInstance.class);
        Content objectInstanceContent = objectInstance.getContent();
        ArrayList<Resource> resources = objectInstanceContent.getResources();
        for(Resource resource : resources)
        {
            try 
            {
                switch(resource.getId()) 
                {
                    case 0:
                        Double pathIndexVal = (double)resource.getValue();
                        if (!pathIndexVal.isNaN()) {
                            attacker.setPathIndex(pathIndexVal.intValue());
                        }
                        break;
                    case 1:
                        attacker.setReplan((boolean) resource.getValue());
                        break;
                    case 2:
                        attacker.setMovedAtLastStep((boolean) resource.getValue());
                        break;
                    case 3://Knowledgebase -- not implemented
                        break;
                    case 4:
                        UAVPath lastpath = gson.fromJson(resource.getValue().toString(), UAVPath.class);
                        attacker.setPathPlannedAtLastStep(lastpath);
                        break;
                    case 5:                        
                        UAVPath historypath = gson.fromJson(resource.getValue().toString(), UAVPath.class);
                        attacker.setPathHistory(historypath);
                        break;
                    case 6:
                        UAVPath currentpath = gson.fromJson(resource.getValue().toString(), UAVPath.class);
                        attacker.setCurrentPath(currentpath);
                        break;
                    case 7:
                        attacker.setHasReplanned((boolean) resource.getValue());
                        break;
                    case 8:
                        Double flightModeVal = (double)resource.getValue();
                        if (!flightModeVal.isNaN()) {
                            attacker.setFlightMode(flightModeVal.intValue());
                        }
                        break;
                    case 9:
                        Double hoveredVal = (double) resource.getValue();
                        if (!hoveredVal.isNaN()) {
                            attacker.setHoveredTimeStep(hoveredVal.intValue());
                        }
                        break;
                    case 10:
                        attacker.setIterationGoal(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break;
                    case 11:
                        Double stuckVal = (double) resource.getValue();
                        if (!stuckVal.isNaN()) {
                            attacker.setStuckTimes(stuckVal.intValue());
                        }
                        break;
                    case 12:
                        Double maxStuckTimesVal = (double) resource.getValue();
                        if (!maxStuckTimesVal.isNaN()) {
                            attacker.setMaximumStuckTimes(maxStuckTimesVal.intValue());
                        }
                        break;
                    case 13:
                        Double indexVal = (double) resource.getValue();
                        if (!indexVal.isNaN()) {
                            attacker.setIndex(indexVal.intValue());
                        }
                        break;    
                    case 14:
                        attacker.setTarget_indicated_by_role(gson.fromJson(resource.getValue().toString(), Target.class));
                        break;                     
                    case 15:
                        attacker.setOnline((boolean) resource.getValue());
                        break;
                    case 16:
                        attacker.setCenterCoordinates(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break; 
                    case 17:
                        Double energyVal = (double) resource.getValue();if (!energyVal.isNaN()) {                            
                            attacker.setRemainedEnergy(energyVal.floatValue());
                        }
                        break;
                    case 18:
                        attacker.setUavBaseCenterCoordinates(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break;
                    case 19: 
                        attacker.setUavPositionInBaseStation(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break;
                    case 20:
                        Double speedVal = (double) resource.getValue();
                        if (!speedVal.isNaN()) {
                            attacker.setSpeed(speedVal.intValue());
                        }
                        break; 
                    case 23:
                        attacker.setAttackerLocked((boolean)resource.getValue());
                        break; 
                    default:
                }
            } catch (Exception e) {
                logger.error("ERROR: {}",e.getMessage());
            }
        }
        return attacker;
    }
    
    public static void updateResource(ReportedLwM2MClient client, int resourceID, ObjectResourceUpdate resourceUpdate)
    {
        if(resourceUpdate != null && client != null)
        {
            String data = gson.toJson(resourceUpdate);
            String endpointUrl = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0/"+resourceID;
            requestUtil.sendToEndpoint(CC_StaticInitConfig.ccAddress, endpointUrl, Operation.UPDATE, data, RequestPrimitive.class);
        }
    }
    
    public static void executeOperationOnResource(ReportedLwM2MClient client, int resourceID, ObjectResourceUpdate resourceUpdate)
    {
        if(resourceUpdate != null && client != null)
        {
            String data = gson.toJson(resourceUpdate);
            String endpointUrl = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0/"+resourceID;
            requestUtil.sendToEndpoint(CC_StaticInitConfig.ccAddress, endpointUrl, Operation.CREATE, data, RequestPrimitive.class);
        }
    }
    
    //Asynchronous methods
    public static void asyncUpdateResource(ReportedLwM2MClient client, int resourceID, ObjectResourceUpdate resourceUpdate, AsyncServiceCallback<Response> callback)
    {
        if(resourceUpdate != null && client != null)
        {
            String data = gson.toJson(resourceUpdate);
            String endpointUrl = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0/"+resourceID;
            System.out.println("asynUpdate: "+data);
            //asynchronous update
            requestUtil.asyncSendToEndpoint(CC_StaticInitConfig.ccAddress, endpointUrl, Operation.UPDATE, data, RequestPrimitive.class, callback);
        }
    }
    
    public static void asyncExecuteOperationOnResource(ReportedLwM2MClient client, int resourceID, ObjectResourceUpdate resourceUpdate,  AsyncServiceCallback<Response> callback)
    {
        if(resourceUpdate != null && client != null)
        {
            String data = gson.toJson(resourceUpdate);
            String endpointUrl = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0/"+resourceID;
            System.out.println("asynExec: "+data);
            requestUtil.asyncSendToEndpoint(CC_StaticInitConfig.ccAddress, endpointUrl, Operation.CREATE, data, RequestPrimitive.class, callback);
        }
    }
    
    /**
     * This class contains static methods for invoking attacker endpoints updates
     */
    public class AttackerUpdate implements AsyncServiceCallback<Response>
    {

        public AttackerUpdate() {
        }
            
        /**
         * Updates Need to replan resource
         * @param replan
         * @param attacker 
         */
        public void setReplan(boolean replan, AttackerModel attacker) 
        {
            System.out.println("Replan set");
            AttackerUtils.asyncUpdateResource(attacker.client, 1, new ObjectResourceUpdate(1, replan, 
                                        ResourceDataType.BOOLEAN.toString()), this);
        }
        
        /**
         * Updates the flight mode
         * @param flightMode
         * @param attacker 
         */
        public void setFlightMode(int flightMode, AttackerModel attacker) 
        {
            AttackerUtils.asyncUpdateResource(attacker.client, 8, new ObjectResourceUpdate(8, flightMode, 
                                            ResourceDataType.INTEGER.toString()), this);
        }
        
        /**
         * Updates the target resource
         * @param target_indicated_by_role
         * @param attacker 
         */
        public void setTarget_indicated_by_role(Target target_indicated_by_role, AttackerModel attacker) {
            AttackerUtils.asyncUpdateResource(attacker.client, 14, new ObjectResourceUpdate(14, target_indicated_by_role, 
                                            ResourceDataType.STRING.toString()), this);
        }
        
        /**
         * Updates the speed resource
         * @param speed
         * @param attacker 
         */
        public void setSpeed(int speed, AttackerModel attacker) 
        {
            AttackerUtils.asyncUpdateResource(attacker.client, 20, new ObjectResourceUpdate(20, speed, 
                                            ResourceDataType.INTEGER.toString()), this);
        }

        @Override
        public void asyncServicePerformed(Response response) {
            logger.info("Attacker update operation performed");
        }
        
    }
    
    /**
     * This class contains static methods for executing procedures on attacker endpoints
     */
    public class AttackerExecution implements AsyncServiceCallback<Response>
    {
         private  Gson gson;
        public AttackerExecution() {
            gson = new Gson();
        }
        
        public void addConflict(Conflict conflict, AttackerModel attacker) {
            AttackerUtils.asyncExecuteOperationOnResource(attacker.client, 3, new ObjectResourceUpdate(3, gson.toJson(conflict), ResourceDataType.STRING.toString()), this);
        }
    
        public void addObstacle(Obstacle obstacle, AttackerModel attacker) {
            AttackerUtils.asyncExecuteOperationOnResource(attacker.client, 21, new ObjectResourceUpdate(21, gson.toJson(obstacle), ResourceDataType.STRING.toString()), this);
        }
    
        public  void addThreat(Threat threat, AttackerModel attacker) {
            AttackerUtils.asyncExecuteOperationOnResource(attacker.client, 22, new ObjectResourceUpdate(22, gson.toJson(threat), ResourceDataType.STRING.toString()), this);
        }

        @Override
        public void asyncServicePerformed(Response response) {
            logger.info("Attacker execution operation performed");
        }
    }
}
