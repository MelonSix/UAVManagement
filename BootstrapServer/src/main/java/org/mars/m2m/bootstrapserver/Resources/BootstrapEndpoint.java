/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.bootstrapserver.Resources;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.leshan.server.security.SecurityStore;
import org.mars.m2m.bootstrapserver.Services.BootstrapInfo;
import org.mars.m2m.bootstrapserver.Services.BootstrapStoreImpl;
import org.mars.m2m.bootstrapserver.Services.ConfigurationChecker;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
@Path("/")
public class BootstrapEndpoint 
{
    /**
     *Logback Logger for logging services
     */
    Logger logger = (Logger) LoggerFactory.getLogger(BootstrapEndpoint.class);
    
    BootstrapStoreImpl bsStore;
    SecurityStore securityStore;

    public BootstrapEndpoint(BootstrapStoreImpl bstrapStore, SecurityStore securityStore) {
        this.bsStore = bstrapStore;
        this.securityStore = securityStore;
    }
    /**
     * 
     * @param endpoint
     * @return 
     */
    @Path("/{endpoint}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBootstrapConfigs(@PathParam("endpoint") String endpoint)
    {
        
        try 
        {
            BootstrapInfo bootstrapInfo = new BootstrapInfo();
            bsStore = bootstrapInfo.setBootstrap(bsStore);
            return Response.ok(bsStore.getBootstrapConfigs()).build();
        } catch (ConfigurationChecker.ConfigurationException ex) {
            logger.error(ex.toString());
            return Response.serverError().build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setBootstrapInfo()
    {
        return null;
    }
}
