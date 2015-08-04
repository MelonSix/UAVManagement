/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources.subResources;

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
import org.mars.m2m.managmentadapter.Notification.NotificationListenerImpl;
import org.mars.m2m.managmentadapter.model.NotificationRegistry;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource 
{
    private ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(NotificationResource.class);
    
    public NotificationResource() {
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processReceivedObservation(@Context HttpServletRequest req, @Context UriInfo uriInfo)
    {
        try 
        {
            NotificationListenerImpl listenerImpl = new NotificationListenerImpl();
            String content = IOUtils.toString(req.getInputStream());
            listenerImpl.sendNotification(new NotificationRegistry());
            System.out.println(content);
            return Response.ok().build();
        } 
        catch (IOException ex) 
        {
           log.error(ex.toString());
           return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
