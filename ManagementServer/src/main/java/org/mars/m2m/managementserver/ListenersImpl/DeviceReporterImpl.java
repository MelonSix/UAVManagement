/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.ListenersImpl;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.leshan.server.client.Client;
import org.mars.m2m.dmcore.json.ConfigGson;
import org.mars.m2m.managementserver.Listeners.DeviceReporter;
import org.mars.m2m.managementserver.callback.AsyncServiceCallback;
import org.mars.m2m.managementserver.client.AsyncServiceConsumer;
import org.mars.m2m.managementserver.configs.StaticConfigs;
import org.mars.m2m.managementserver.model.LWM2MClient;
import org.slf4j.LoggerFactory;

public class DeviceReporterImpl implements DeviceReporter, AsyncServiceCallback<Response> 
{
    private final Logger logger = (Logger) LoggerFactory.getLogger(DeviceReporterImpl.class);
    public static int counter=0;

    @Override
    public void reportDevice(Client c) 
    {
        try 
        {
            AsyncServiceConsumer sc = new AsyncServiceConsumer();
            //LWM2MClient client = CloneClientToModel(c);
            Gson gson = ConfigGson.getCustomGsonConfig();
            sc.handlePost(StaticConfigs.DEVICE_REPORTING_URL, gson.toJson(c), MediaType.APPLICATION_JSON, this);
        } catch (Exception e) {
            logger.error("{}", e.toString());
        }
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="Perpares the client for parsing">
    /**
     * Copies the details of the client into a model for parsing as a POJO
     * @param c The LwM2M client
     */
    private LWM2MClient CloneClientToModel(Client c)
    {
        LWM2MClient lwmmc = new LWM2MClient();
        lwmmc.setRegistrationDate(c.getRegistrationDate());
        lwmmc.setAddress(new String(c.getRegistrationEndpointAddress().getAddress().getAddress()));
        lwmmc.setHostname(c.getRegistrationEndpointAddress().getHostName());
        lwmmc.setPort(c.getPort());
        lwmmc.setLifeTimeInSec(c.getLifeTimeInSec());
        lwmmc.setSmsNumber(c.getSmsNumber());
        lwmmc.setLwM2mVersion(c.getLwM2mVersion());
        lwmmc.setBindingMode(c.getBindingMode());
        lwmmc.setEndpoint(c.getEndpoint());
        lwmmc.setRegistrationId(c.getRegistrationId());
        lwmmc.setObjectLinks(c.getObjectLinks());
        lwmmc.setRootPath(c.getRootPath());
        lwmmc.setLastUpdate(c.getLastUpdate());
        return lwmmc;
    }
//</editor-fold>

    @Override
    public void asyncServicePerformed(Response t) {
        System.out.println("Num of reported devices: "+(++counter));
    }
    
}
