/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.ArrayList;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.model.AttackerModel;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.model.Target;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Content;
import org.mars.m2m.demo.controlcenter.model.endpointModel.ObjectInstance;
import org.mars.m2m.demo.controlcenter.model.endpointModel.Resource;
import org.mars.m2m.demo.controlcenter.uav.UAVPath;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
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
    
    public AttackerUtils() {
    }
    
    public static AttackerModel getAttacker(ReportedLwM2MClient client)
    {        
        String endpointURL = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0";
        RequestPrimitive resp = (RequestPrimitive) requestUtil.sendToEndpoint(client, endpointURL, Operation.RETRIEVE, null);
        if(resp==null) return null;
        return parseDataToAttacker(resp);
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
                        attacker.setPathIndex((int) resource.getValue());
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
                        attacker.setFlightMode((int) resource.getValue());
                        break;
                    case 9:
                        attacker.setHoveredTimeStep((int) resource.getValue());
                        break;
                    case 10:
                        attacker.setIterationGoal(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break;
                    case 11:
                        attacker.setStuckTimes((int)resource.getValue());
                        break;
                    case 12:
                        attacker.setMaximumStuckTimes((int) resource.getValue());
                        break;
                    case 13:
                        attacker.setIndex((int) resource.getValue());
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
                        attacker.setRemainedEnergy((float) resource.getValue());
                        break;
                    case 18:
                        attacker.setUavBaseCenterCoordinates(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break;
                    case 19: 
                        attacker.setUavPositionInBaseStation(jsonArrayUtil.getFloatArray(resource.getValue().toString()));
                        break;
                    default:
                }
            } catch (Exception e) {
                logger.error("ERROR: {}",e.getMessage());
            }
        }
        return attacker;
    }
}
