/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.DeviceReporting;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import org.mars.m2m.dmcore.json.ConfigGson;
import org.mars.m2m.managmentadapter.client.ServiceConsumer;
import org.mars.m2m.managmentadapter.config.StaticConfigs;
import org.slf4j.LoggerFactory;

public class DeviceReporterImpl implements DeviceReporter 
{
    private final Logger logger = (Logger) LoggerFactory.getLogger(DeviceReporterImpl.class);

    @Override
    public void reportDevice(String data) 
    {
        try 
        {
            ServiceConsumer sc = new ServiceConsumer();
            Gson gson = ConfigGson.getCustomGsonConfig();
            sc.handlePost(StaticConfigs.DEVICE_REPORTING_URL, data, MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            logger.error("{}", e.toString());
        }
    }
    
    
}
