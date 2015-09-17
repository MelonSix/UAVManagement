/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.model.Attacker;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
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
    
    public AttackerUtils() {
    }
    
    public static Attacker getAttacker(ReportedLwM2MClient client)
    {        
        String endpointURL = CC_StaticInitConfig.mgmntServerURL+client.getEndpoint()+"/12207/0";
        RequestPrimitive resp = (RequestPrimitive) requestUtil.sendToEndpoint(client, endpointURL, Operation.RETRIEVE, null);
        if(resp==null) return null;
        return parseDataToAttacker(resp);
    }
    
    private static Attacker parseDataToAttacker(RequestPrimitive req)
    {
        String endpointData = requestUtil.extractRequestData(req);
        Gson gson = new Gson();
        return new Attacker();
    }
    
}
