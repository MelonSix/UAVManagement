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
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.util.DmCommons;
import org.mars.m2m.managmentadapter.service.OperationService;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/mgmtAdapter")
public class ManagementOpsResource {
    
    Logger logger = (Logger) LoggerFactory.getLogger(ManagementOpsResource.class);
    Operation operation;

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
    public ResponsePrimitive postRequest(RequestPrimitive requestPrimitive)
    {
        return processRequest(requestPrimitive);
    }
    
    /**
     * Process the request primitive by selecting the appropriate operation to handle the request
     * and delivering the response
     * @param request
     * @return 
     */
    public ResponsePrimitive processRequest(RequestPrimitive request)
    {
        ResponsePrimitive response = null;
        operation = DmCommons.determineOneM2mOperation(request.getOperation());
        OperationService opSvc = new OperationService();
        
        try {
            switch (operation) {
                case CREATE:
                    break;
                case RETRIEVE:
                    response = opSvc.retrieve(request);
                    break;
                case UPDATE:
                    break;
                case DELETE:
                    break;
                case NOTIFY:
                    break;
                default:
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        
        return response;
    }
    
}
