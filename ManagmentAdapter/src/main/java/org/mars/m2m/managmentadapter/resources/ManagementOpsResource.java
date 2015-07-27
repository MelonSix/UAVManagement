/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/mgmtAdapter")
public class ManagementOpsResource {
    
    Logger logger = (Logger) LoggerFactory.getLogger(ManagementOpsResource.class);

    public ManagementOpsResource() {
    }
    
    /**
     *Accepts requests that are posted to the management adapter
     * @param requestPrimitive The posted request to be processed by the management adapter
     * @return The response associated with this request
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public RequestPrimitive postRequest(RequestPrimitive requestPrimitive)
    {
        String strResponse = null;
        
        return requestPrimitive;
    }
}
