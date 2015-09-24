/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.DeviceReporting;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import javax.ws.rs.core.Response;
import org.mars.m2m.dmcore.json.ConfigGson;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.managmentadapter.callback.AsyncServiceCallback;
import org.mars.m2m.managmentadapter.model.ReportedClients;
import org.slf4j.LoggerFactory;

public class DeviceReporterImpl implements DeviceReporter, AsyncServiceCallback<Response> 
{
    private final Logger logger = (Logger) LoggerFactory.getLogger(DeviceReporterImpl.class);
    private static int reportCounter=0;

    @Override
    public void reportDevice(String data) 
    {
        try 
        {
            synchronized(ReportedClients.class)
            {
                Gson gson = ConfigGson.getCustomGsonConfig();
                ReportedLwM2MClient client = gson.fromJson(data, ReportedLwM2MClient.class);
                if(!ReportedClients.getClients().contains(client))
                {
                    ReportedClients.getClients().add(client);
                }
                //Automatically sends a reported client to a specified URL
                //AsyncServiceConsumer sc = new AsyncServiceConsumer();
                //sc.handlePost(StaticConfigs.DEVICE_REPORTING_URL, data, MediaType.APPLICATION_JSON, this);
            }
        } catch (Exception e) {
            logger.error("{}", e.toString());
        }
    }

    @Override
    public void asyncServicePerformed(Response response) {
        System.out.println("Number of devices reported: "+(++reportCounter));
    }
    
    
}
