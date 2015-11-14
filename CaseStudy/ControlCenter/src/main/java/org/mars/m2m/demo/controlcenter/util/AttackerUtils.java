/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.analysis.GraphDatastore;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.callback.AsyncServiceCallback;
import org.mars.m2m.demo.controlcenter.enums.AttackerType;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.Conflict;
import org.mars.m2m.demo.controlcenter.model.Message;
import org.mars.m2m.demo.controlcenter.model.Obstacle;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.model.Target;
import org.mars.m2m.demo.controlcenter.model.Threat;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Content;
import org.mars.m2m.demo.controlcenter.model.endpointModel.ObjectInstance;
import org.mars.m2m.demo.controlcenter.model.endpointModel.ObjectResourceUpdate;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Resource;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.services.MessageHistory;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResourceDataType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
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
    public final AttackerUpdate update;
    public final AttackerExecution execute;
    public static final MessageHistory MESSAGE_HISTORY = new MessageHistory();
    
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
        ReportedLwM2MClient client = requestUtil.getLwM2MClientFromTreeNode(node);
        if (client != null) 
        {
            AttackerModel attacker = getAttacker(client);
            if (attacker != null) {
                attacker.setClient(client);
                return attacker;
            }
            return null;
        }
        else
        {
            logger.error("Cannot get virtualized attacker. A null client was submitted");
            return null;
        }
    }
    
    private synchronized static AttackerModel getAttacker(ReportedLwM2MClient client)
    {        
        try 
        {
            String endpointURL = CC_StaticInitConfig.mgmntServerURL + client.getEndpoint() + "/12207/0";
            RequestPrimitive resp;
            resp = requestUtil.send(CC_StaticInitConfig.ccAddress, endpointURL, 
                    Operation.RETRIEVE, null);
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
        if (!objectInstance.getStatus().contains("NO CONTENT")) 
        {
            Content objectInstanceContent = objectInstance.getContent();
            ArrayList<Resource> resources = objectInstanceContent.getResources();
            for (Resource resource : resources) {
                try {
                    switch (resource.getId()) {
                        case 0:
                            Double flightModeVal = (double) resource.getValue();
                            if (!flightModeVal.isNaN()) {
                                attacker.setFlightMode(flightModeVal.intValue());
                            }
                            break;
                        case 1:
                            Double indexVal = (double) resource.getValue();
                            if (!indexVal.isNaN()) {
                                attacker.setIndex(indexVal.intValue());
                            }
                            break;                        
                        case 2:
                            attacker.setTarget_indicated_by_role(gson.fromJson(resource.getValue().toString(), Target.class));
                            break;                        
                        case 3:
                            attacker.setOnline((boolean) resource.getValue());
                            break;
                        case 4:
                            attacker.setCenterCoordinates(gson.fromJson(resource.getValue().toString(), float[].class));
                            break;                        
                        case 5:                            
                            attacker.setUavPositionInBaseStation(gson.fromJson(resource.getValue().toString(), float[].class));
                            break;                        
                        case 9:
                            attacker.setAttackerLocked((boolean) resource.getValue());
                            break;                        
                        case 12:
                            Double energyVal = (double) resource.getValue();
                            if (!energyVal.isNaN()) {
                                attacker.setRemainedEnergy(energyVal.floatValue());
                            }
                            break;                        
                        case 13:
                            attacker.setAttackerType(AttackerType.valueOf(resource.getValue().toString()));
                            break;
                        case 14:
                            System.out.println("Read threat destroyed status: "+resource.getValue().toString());
                            attacker.setThreatDestroyed((boolean) resource.getValue());
                        default:
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return attacker;
    }
    
    /**
     * Checks and adds a communicated message(Conflict, Threat, or Obstacle) to a message history registry
     * @param endpointID
     * @param message
     * @param registry 
     */
    public static void addToMessageHistory(String endpointID, Message message, Map<String, ArrayList<Message>> registry)
    {
        if(registry.containsKey(endpointID))//if this endpoint already has an entry then add the current message to the existing
        {
            registry.get(endpointID).add(message);
        }
        else //else create an entry and add the current message to the new message history record that has been created
        {
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            registry.put(endpointID, messages);
        }
    }
    
    /**
     * Determines if a threat, obstacle, or conflict has already been communicated to an endpoint
     * @param endpointID
     * @param message
     * @param registry
     * @return 
     */
    public static boolean isMessageInMessageHistory(String endpointID, Message message, Map<String, ArrayList<Message>> registry)
    {
        if(registry.containsKey(endpointID))
        {
            ArrayList<Message> messages = registry.get(endpointID);
            return messages.contains(message);
        }
        else {
            return false;
        }
    }
    
    public static void updateResource(ReportedLwM2MClient client, int resourceID, ObjectResourceUpdate resourceUpdate)
    {
//        if(resourceUpdate != null && client != null)
//        {
//            String data = gson.toJson(resourceUpdate);
//            String endpointUrl = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0/"+resourceID;
//            requestUtil.send(CC_StaticInitConfig.ccAddress, endpointUrl, Operation.UPDATE, data);
//            
//            getDataForAnalysis();
//        }
    }

    public static void getDataForAnalysis() {
        GraphDatastore
                .getMessagesPerSecondData()
                .put(CC_StaticInitConfig.CURRENT_SIMULATION_TIME.get(),
                        CC_StaticInitConfig.TOTAL_MESSAGES_SENT_IN_CURRENT_SIMULATION_TIMESTEP.addAndGet(1));
        CC_StaticInitConfig.TOTAL_MESSAGES_SENT.incrementAndGet();
    }
    
    public static void executeOperationOnResource(ReportedLwM2MClient client, int resourceID, ObjectResourceUpdate resourceUpdate)
    {
        if(resourceUpdate != null && client != null)
        {
            String data = gson.toJson(resourceUpdate);
            String endpointUrl = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0/"+resourceID;
            requestUtil.send(CC_StaticInitConfig.ccAddress, endpointUrl, Operation.CREATE, data);
            
            getDataForAnalysis();
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
            
            getDataForAnalysis();
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
            getDataForAnalysis();
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
            int resourceID = 10;
            AttackerUtils.asyncUpdateResource(attacker.getClient(), resourceID, new ObjectResourceUpdate(resourceID, replan, 
                                        ResourceDataType.BOOLEAN.toString()), new AsyncServiceCallback<Response>() {

                @Override
                public void asyncServicePerformed(Response t) {
                    
                }
            });
        }
        
        /**
         * Updates the flight mode
         * @param flightMode
         * @param attacker 
         */
        public void setFlightMode(final int flightMode, final AttackerModel attacker) 
        {            
            int resourceID = 0;
            AttackerUtils.asyncUpdateResource(attacker.getClient(), resourceID, new ObjectResourceUpdate(resourceID, flightMode, 
                                            ResourceDataType.INTEGER.toString()),  new AsyncServiceCallback<Response>() {

                @Override
                public void asyncServicePerformed(Response t) {
                    
                }
            });
        }
        
        /**
         * Updates the target resource
         * @param target_indicated_by_role
         * @param attacker 
         */
        public void setTarget_indicated_by_role(final Target target_indicated_by_role, final AttackerModel attacker) {
            
            int resourceID = 2;
            AttackerUtils.asyncUpdateResource(attacker.getClient(), resourceID, new ObjectResourceUpdate(resourceID, target_indicated_by_role, 
                                            ResourceDataType.STRING.toString()), new AsyncServiceCallback<Response>() {

                @Override
                public void asyncServicePerformed(Response t) {
                    attacker.setTarget_indicated_by_role(null);
                }
            });
        }
        
        /**
         * Updates the speed resource
         * @param speed
         * @param attacker 
         */
        public void setSpeed(int speed, AttackerModel attacker) 
        {
            int resourceID = 11;
            AttackerUtils.asyncUpdateResource(attacker.getClient(), resourceID, new ObjectResourceUpdate(resourceID, speed, 
                                            ResourceDataType.INTEGER.toString()), new AsyncServiceCallback<Response>() {

                @Override
                public void asyncServicePerformed(Response t) {
                    
                }
            });
        }

        @Override
        public void asyncServicePerformed(Response response) {
            logger.info("Attacker update operation performed");
        }
        
    }
    
    /**
     * This class contains static methods for executing procedures on attacker endpoints
     */
    public class AttackerExecution /*implements AsyncServiceCallback<Response>*/
    {
        private final  Gson gson;
        public AttackerExecution() {
            gson = new Gson();
        }
        
        public void addConflict(final Conflict conflict, final AttackerModel attacker) 
        {
            if(!isMessageInMessageHistory(attacker.getClient().getEndpoint(), conflict, MESSAGE_HISTORY.getCommunicatedConflicts()))
            {
                int resourceID = 7;
                AttackerUtils.asyncExecuteOperationOnResource(attacker.getClient(), resourceID, 
                        new ObjectResourceUpdate(resourceID, gson.toJson(conflict), ResourceDataType.STRING.toString()), 
                        new AsyncServiceCallback<Response>() {

                    @Override
                    public void asyncServicePerformed(Response r) {
                        attacker.addConflict(conflict);
                        addToMessageHistory(attacker.getClient().getEndpoint(), conflict, MESSAGE_HISTORY.getCommunicatedConflicts());
                    }
                });
            }
        }
    
        public void addObstacle(final Obstacle obstacle, final AttackerModel attacker) 
        {
            if (!isMessageInMessageHistory(attacker.getClient().getEndpoint(), obstacle, MESSAGE_HISTORY.getCommunicatedObstacles())) {
                int resourceID = 6;
                if (!attacker.containsObstacle(obstacle)) 
                {
                    attacker.addObstacle(obstacle);
                    AttackerUtils.asyncExecuteOperationOnResource(attacker.getClient(), resourceID,
                            new ObjectResourceUpdate(resourceID, gson.toJson(obstacle), ResourceDataType.STRING.toString()),
                            new AsyncServiceCallback<Response>() {
                                
                                @Override
                                public void asyncServicePerformed(Response r) {
                                    addToMessageHistory(attacker.getClient().getEndpoint(), obstacle, MESSAGE_HISTORY.getCommunicatedObstacles());
                                }
                            });
                }
            }
        }
    
        public void addThreat(final Threat threat, final AttackerModel attacker) 
        {   
//            System.out.println("Sending threat...");
            if (!isMessageInMessageHistory(attacker.getClient().getEndpoint(), threat, MESSAGE_HISTORY.getCommunicatedThreats())) 
            {
//               System.out.println("History condition passed"); 
                int resourceID = 8;
                //checks if this attacker has the capability of destroying this threat before it sends to it
                if (!attacker.containsThreat(threat) && attacker.isThreatDestroyed()) 
                {
                    if(ControlCenterServices.inforshare_algorithm == CC_StaticInitConfig.BROADCAST_INFOSHARE)
                    {
//                    System.out.println("attacker condition passed"); 
                        attacker.addThreat(threat);
    //                    System.out.println("Threat added to attacker kb. No.: "+attacker.getKb().getThreats().size()); 
                        AttackerUtils.asyncExecuteOperationOnResource(attacker.getClient(), resourceID,
                                new ObjectResourceUpdate(resourceID, gson.toJson(threat), ResourceDataType.STRING.toString()),
                                new AsyncServiceCallback<Response>() {

                                    @Override
                                    public void asyncServicePerformed(Response r) {
                                        addToMessageHistory(attacker.getClient().getEndpoint(), threat, MESSAGE_HISTORY.getCommunicatedThreats());
                                    }
                                });
                    }
                    else if(ControlCenterServices.inforshare_algorithm == CC_StaticInitConfig.REGISTER_BASED_INFORSHARE)
                    {
                        if(threat.getThreatType().toString().equals(attacker.getAttackerType().toString()))
                        {
    //                    System.out.println("attacker condition passed"); 
                            attacker.addThreat(threat);
        //                    System.out.println("Threat added to attacker kb. No.: "+attacker.getKb().getThreats().size()); 
                            AttackerUtils.asyncExecuteOperationOnResource(attacker.getClient(), resourceID,
                                    new ObjectResourceUpdate(resourceID, gson.toJson(threat), ResourceDataType.STRING.toString()),
                                    new AsyncServiceCallback<Response>() {

                                        @Override
                                        public void asyncServicePerformed(Response r) {
                                            addToMessageHistory(attacker.getClient().getEndpoint(), threat, MESSAGE_HISTORY.getCommunicatedThreats());
                                        }
                                    });  
                        }

                    }
                }
            }
        }

        /*@Override
        public void asyncServicePerformed(Response response) {
        logger.info("Attacker execution operation performed");
        }*/
    }
}
