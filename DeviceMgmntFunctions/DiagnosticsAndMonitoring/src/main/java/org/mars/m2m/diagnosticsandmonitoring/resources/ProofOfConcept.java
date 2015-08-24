/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.diagnosticsandmonitoring.resources;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/demoLogic")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProofOfConcept {
    private static ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProofOfConcept.class);
    
    public ProofOfConcept() {
    }
    
    @POST
    public Response getNotified(@Context HttpServletRequest req, @Context UriInfo uriInfo)
    {
        String content="[]";
        try {
            content = IOUtils.toString(req.getInputStream());
        } catch (IOException ex) {
            log.error(ex.toString());
        }
        System.out.println("From Diagnostics and Monitoring: "+content);
       return Response.ok(content).build();
    }
    
}
