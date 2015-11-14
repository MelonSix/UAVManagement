/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.core;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.callback.AsyncServiceCallback;
import org.mars.m2m.demo.controlcenter.client.AsyncServiceConsumer;
import org.mars.m2m.demo.controlcenter.services.ControlCenterReflexes;
import org.mars.m2m.demo.controlcenter.services.NewDeviceServices;
import org.mars.m2m.demo.controlcenter.services.ReadAttackers;
import org.mars.m2m.demo.controlcenter.ui.AnimationPanel;
import org.mars.m2m.demo.controlcenter.util.AttackerUtils;
import org.mars.m2m.demo.controlcenter.util.TraverseParsedXmlLwM2MClientInfo;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
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
            JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream bi = new ByteArrayInputStream(data.getBytes());
            Container container_forLwM2Mclients = (Container) unmarshaller.unmarshal(bi);
            System.out.println("Number of LwM2M clients: "+container_forLwM2Mclients.getContentInstanceOrContainerOrSubscription().size());
            TraverseParsedXmlLwM2MClientInfo traverseContainer = new TraverseParsedXmlLwM2MClientInfo();
            final ArrayList<ReportedLwM2MClient> reportedLwM2MClients = traverseContainer.getReportedLwM2MClients(container_forLwM2Mclients);
           
            connectedDevices = newDeviceServices.addNewClientsOnDemand(reportedLwM2MClients);
                
            try {
                Runnable runnable;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (ReportedLwM2MClient device : reportedLwM2MClients) {
                            reflex.observationRequestReflex(device);
                        }
                        
                        ReadAttackers.readAttackerResources();
                    }
                };
                Thread t = new Thread(runnable);
                t.start();
                t.join();
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }
//            ExecutorService executors = Executors.newCachedThreadPool();
//            Future<Void> submit = executors.submit(new Callable<Void>() {
//                
//                @Override
//                public Void call() throws Exception {
//                    
//                    return null;
//                }
//                
//            });
            reflex.scoutingWaypointsReflex(connectedDevices);           
            
            if(connectedDevices != null && connectedDevices.size() > 0 && animationPanel != null)
            {
//               animationPanel.start();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
    
}
