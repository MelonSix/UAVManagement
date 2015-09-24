/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources.subResources;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.managmentadapter.model.ReportedClients;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
public class ConnectedClients 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ConnectedClients.class);

    public ConnectedClients() {
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    public void getConnectedClients(@Suspended final AsyncResponse asyncResponse)
    {
        //async properties
        asyncResponse.setTimeoutHandler(new TimeoutHandler() { 
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Client Operation time out.").build());
                logger.error("Client Operation time out");
            }
        });
        asyncResponse.setTimeout(60, TimeUnit.SECONDS);
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) 
                {
                    logger.error("Error writing to client");
                }
            }            
        });
        asyncResponse.register(new ConnectionCallback() {

            @Override
            public void onDisconnect(AsyncResponse disconnected) {
                logger.error("Client could not be contacted.");
            }
        });
        
        //thread for each request
        new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    Gson gson = new Gson();
                    synchronized(ReportedClients.class)
                    {
                        String data = gson.toJson(ReportedClients.getClients());
                        System.out.println(data);
                        asyncResponse.resume(data);
                    }
                } catch (Exception ex) {
                    logger.error(ex.toString());
                }                
            }
        }).start();    
    }
}
