/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources.subResources;

import ch.qos.logback.classic.Logger;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mars.m2m.managmentadapter.DeviceReporting.DeviceReporterImpl;
import org.mars.m2m.managmentadapter.DeviceReporting.HandleDeviceReporting;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceReporting 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DeviceReporting.class);
    
    public DeviceReporting() {
    }
    
    /**
     * Receives the device data which is reported and forwards it
     * to the designated URL in <code>StaticConfigs</code> class
     * @param data The received data
     * @param asyncResponse
     */
    @POST
    public void receiveNewDeviceReport(final String data, @Suspended final AsyncResponse asyncResponse)
    {
        setAsyncResponseProperties(asyncResponse);
        asyncResponse.resume(Response.ok().build());
        
        //thread for each request
        new Thread(new Runnable() 
        {

            @Override
            public void run()
            {
                DeviceReporterImpl reporterImpl = new DeviceReporterImpl();
                HandleDeviceReporting reporting = new HandleDeviceReporting();
                reporting.addDeviceReporterListener(reporterImpl);
                reporting.performReporting(data);
            }
        }).start();         
    }

    private void setAsyncResponseProperties(final AsyncResponse asyncResponse) {
        //async properties
        asyncResponse.setTimeoutHandler(new TimeoutHandler() { 
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
            }
        });
        asyncResponse.setTimeout(60, TimeUnit.SECONDS);
        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable != null) 
                {
                    logger.error("Error reporting device to client");
                }
            }            
        });
        asyncResponse.register(new ConnectionCallback() {

            @Override
            public void onDisconnect(AsyncResponse disconnected) {
                logger.error("Client could not be contacted.");
            }
        });
    }
}
