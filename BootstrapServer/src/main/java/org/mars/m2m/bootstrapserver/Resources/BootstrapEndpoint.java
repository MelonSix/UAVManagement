/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.bootstrapserver.Resources;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    public BootstrapEndpoint() {
    }
    
    @Path("/{endpoint}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBootstrapConfigs(@PathParam("endpoint") String endpoint)
    {
        return Response.ok(endpoint).build();
    }
    
}
