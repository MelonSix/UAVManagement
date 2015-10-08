/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.core;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import javax.ws.rs.core.Response;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.callback.AsyncServiceCallback;
import org.mars.m2m.demo.controlcenter.client.AsyncServiceConsumer;
import org.mars.m2m.demo.controlcenter.services.ControlCenterReflexes;
import org.mars.m2m.demo.controlcenter.services.NewDeviceServices;
import org.mars.m2m.demo.controlcenter.ui.AnimationPanel;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.slf4j.LoggerFactory;

/**
 * Loads connected devices at the MA and use that to populate the JTree in the UI
 * @author AG BRIGHTER
 */
public class LoadUAVs implements AsyncServiceCallback<Response>
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(LoadUAVs.class);
    private final NewDeviceServices newDeviceServices;
    private ArrayList<ReportedLwM2MClient> connectedDevices;
    private final ControlCenterReflexes reflex;
    private AnimationPanel animationPanel;
    
    public LoadUAVs() {
        this.reflex = new ControlCenterReflexes();
        this.newDeviceServices = new NewDeviceServices();
    }
    
    public void loadMgmntAdpterClients(AnimationPanel ui)
    {
        this.animationPanel = ui;
        AsyncServiceConsumer serviceConsumer = new AsyncServiceConsumer();
        serviceConsumer.handleGet(CC_StaticInitConfig.mgmntAdapterGetClientsURL, this);
    }

    @Override
    public void asyncServicePerformed(Response response) {
        try 
        {
            Gson gson = new Gson();
            String data = response.readEntity(String.class);
            ReportedLwM2MClient[] clients = gson.fromJson(data, ReportedLwM2MClient[].class);
            
            connectedDevices = newDeviceServices.addNewClientsOnDemand(new ArrayList<>(Arrays.asList(clients)));
                
            //reflex operations to endpoints
            try 
            {        
                Runnable runnable;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        reflex.scoutingWaypointsReflex(connectedDevices);
                    }
                };
                Thread t = new Thread(runnable);
                t.start();
                t.join();
            } catch (InterruptedException e) {
            }
            for (ReportedLwM2MClient device : clients) {
                reflex.observationRequestReflex(device);
            }
            
            if(connectedDevices != null && connectedDevices.size() > 0 && animationPanel != null)
            {
               animationPanel.start();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
