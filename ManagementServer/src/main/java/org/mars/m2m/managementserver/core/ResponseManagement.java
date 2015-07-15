/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.core;

import java.io.IOException;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.mars.m2m.managementserver.json.ConfigGson;

/**
 *
 * @author AG BRIGHTER
 */
public class ResponseManagement {
    
    /**
     * 
     * @param cResponse
     * @return
     * @throws IOException 
     */
    public static String processDeviceResponse(LwM2mResponse cResponse) throws IOException {
        String response = null;
        if (cResponse == null) {
            response = "Request timeout";
        } else {
            response = ConfigGson.getCustomGsonConfig().toJson(cResponse);
        }
        return response;
    }
}
