/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.mars.m2m.demo.model.Conflict;
import org.mars.m2m.demo.model.ObjectResourceModel;
import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.model.Threat;
import org.mars.m2m.demo.world.World;

/**
 *
 * @author AG BRIGHTER
 */
public class UavAttackerDevice extends BaseInstanceEnabler implements DeviceExecution
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UavAttackerDevice.class);
    private static final Gson gson = new Gson();
    private final Attacker attacker;
    
    public UavAttackerDevice() {
        this.attacker = null;
    }
    
    public UavAttackerDevice(Attacker attacker) 
    {
        this.attacker = attacker;
    }

    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        try 
       {
           if (params != null && params.length != 0) 
           {
               ObjectResourceModel dataStr = gson.fromJson(new String(params), ObjectResourceModel.class);
               switch (dataStr.getId()) 
               {
                   case 6:
                       Obstacle obstacle = gson.fromJson(dataStr.getValue().toString(), Obstacle.class);
                       if (!attacker.getKb().containsObstacle(obstacle)) {
                           attacker.addObstacle(obstacle);
                           System.out.println("Exec: Obstacle added: " + dataStr.getValue().toString());
                       }
                       return new LwM2mResponse(ResponseCode.CHANGED);
                   case 7:
                       Conflict conflict = gson.fromJson(dataStr.getValue().toString(), Conflict.class);
                       if (!attacker.getKb().containsConflict(conflict)) {
                           attacker.addConflict(conflict);
                           System.out.println("Exec: Conflict added: " + dataStr.getValue().toString());
                       }
                       return new LwM2mResponse(ResponseCode.CHANGED);
                   case 8:
                       System.out.println("Threat: "+dataStr);
                           Threat threat = gson.fromJson(dataStr.getValue().toString(), Threat.class);
                           if (!attacker.containsThreat(threat)) 
                           {
                               attacker.addThreat(threat);
                               System.out.println("Num of threats: "+attacker.getKb().getThreats().size());
                               System.out.println("Exec: Threat added: " + dataStr.getValue().toString());
                           }
                       return new LwM2mResponse(ResponseCode.CHANGED);
                   default:
                       return new LwM2mResponse(ResponseCode.BAD_REQUEST);
               }
           }
       } catch (Exception e) {
           logger.error(e.getMessage());
       }
        return new LwM2mResponse(ResponseCode.BAD_REQUEST);
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource resource) {
        System.out.println("Write on UAV Attacker Device Resource " + resourceid + " value " + resource);
       switch (resourceid) {
           case 0:
                attacker.setFly_mode(Integer.parseInt(resource.getValue().value.toString()));
                System.out.println("Fly mode write: " + resource.getValue().value.toString());
                return new LwM2mResponse(ResponseCode.CHANGED);
           case 2:               
//                String data = parseValue(resource.getValue().value.toString());
//                Threat target = gson.fromJson(data, Threat.class);
//                attacker.setTarget_indicated_by_role(target);
//                attacker.setLockedToThreat(true);
//                System.out.println("target indicated by role write: " + data);
               return new LwM2mResponse(ResponseCode.CHANGED);
           case 10:
                attacker.setNeed_to_replan(Boolean.valueOf(resource.getValue().value.toString()));
                System.out.println("Need to replan write: " + resource.getValue().value.toString());
                return new LwM2mResponse(ResponseCode.CHANGED);
           case 12:
                attacker.setSpeed(Integer.parseInt(resource.getValue().value.toString()));
                System.out.println("Speed write: " + resource.getValue().value);
                return new LwM2mResponse(ResponseCode.CHANGED);
           default:
               return super.write(resourceid, resource);
       }
    }

    @Override
    public ValueResponse read(int resourceid) {
        //System.out.println("read on resource "+resourceid);
       try {
           switch (resourceid) {
                case 0:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(attacker.getFly_mode())));
                case 1:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newIntegerValue(attacker.getIndex())));  
                case 2:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(attacker.getTarget_indicated_by_role())))); 
                case 3:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBooleanValue(attacker.isVisible()))); 
                case 4:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(attacker.getCenter_coordinates())))); 
                case 5:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newStringValue(gson.toJson(World.assignUAVPortInBase(attacker.getIndex()))))); 
                case 9:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newBooleanValue(attacker.isLockedToThreat())));
                case 12:
                    return new ValueResponse(ResponseCode.CONTENT,
                            new LwM2mResource(resourceid, Value.newFloatValue(attacker.getRemained_energy())));
               default:
                   return super.read(resourceid);
           }
       } catch (Exception e) {
           logger.error("ERROR: {}",e.getMessage());
           return super.read(resourceid);
       }
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
        //regular expresion for getting property names
        String elementPart = "([\\w]*:)";
        
        //regular expression for getting the values 
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
    
        
    
}
