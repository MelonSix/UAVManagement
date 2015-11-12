/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.services;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.eventHandling.caller.ReflexListenerImplCaller;
import org.mars.m2m.demo.controlcenter.eventHandling.ListernerImpl.AssignScoutRoleReflexListenerImpl;
import org.mars.m2m.demo.controlcenter.eventHandling.ListernerImpl.SendObservationReflexListenerImpl;
import org.mars.m2m.dmcore.model.ObjectLink;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.util.UavUtil;
import org.slf4j.LoggerFactory;

/**
 *This class handles operations that are automatically sent back to the endpoints
 * upon the triggering of an event
 * @author AG BRIGHTER
 */
public class ControlCenterReflexes 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterReflexes.class);    
    private final UavUtil uavUtil;
    private final ExecutorService  executor;
    private final ArrayList<String> existingObservations;
    
    public ControlCenterReflexes() {
        this.existingObservations = new ArrayList<>();
        executor = Executors.newFixedThreadPool(5);
        this.uavUtil = new UavUtil();
    }
    
    /**Automatic scouting information forwarding
     * Gets the scouts and their devices and then selects the flight control device of each
     * then sends the coordinates to the UAV
     * @param connectedDevices 
     */
    
    public void scoutingWaypointsReflex(final ArrayList<ReportedLwM2MClient> connectedDevices)
    {
        CC_StaticInitConfig.currentScoutIndex.set(0);
        final ArrayList<String> configuredScouts = new ArrayList<>();
        //gets all scouts and their respective onboard devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> scouts = 
                uavUtil.getUAVAndOnboardDevices(uavUtil.getConnectedDevicesByCategory(connectedDevices, "scout"));
        
        for(String scout : scouts.keySet())
        {              
            if(!configuredScouts.contains(scout))
            {            
                ArrayList<ReportedLwM2MClient> onboardDevices = scouts.get(scout);
                ReportedLwM2MClient flightControl = null;

                for(ReportedLwM2MClient device : onboardDevices)
                {
                    ArrayList<ObjectLink> objectLinks = device.getObjectLinks();
                    for(ObjectLink obj : objectLinks)
                    {                   

                        //flight control object ID in json object model file is 12204
                        if(obj.getObjectId() == 12204)
                        {
                            flightControl = device;
                        }
                        if(flightControl != null)
                            break;//device found
                    }
                    if(flightControl != null)
                        break;//device found
                }
            
                //if flight control device has been found
                if(flightControl != null)
                {
                    WorkerThread thread = new WorkerThread(flightControl, ThreadOperation.SCOUTING_REFLEX);
                    executor.execute(thread);
                    configuredScouts.add(scout);
                }
            }
        }
    }
    
    /**Manual scouting information forwarding
     * Gets the scouts and their devices and then selects the flight control device of each
     * then sends the coordinates to the UAV
     * @param connectedDevices 
     */
    public synchronized void onDemandScoutingWaypointsReflex(final ArrayList<ReportedLwM2MClient> connectedDevices)
    {
        //gets all scouts and their respective onboard devices
        TreeMap<String, ArrayList<ReportedLwM2MClient>> scouts = 
                uavUtil.getUAVAndOnboardDevices(uavUtil.getConnectedDevicesByCategory(connectedDevices, "scout"));
        
        for(String scout : scouts.keySet())
        {            
            ArrayList<ReportedLwM2MClient> onboardDevices = scouts.get(scout);
            ReportedLwM2MClient flightControl = null;

            for(ReportedLwM2MClient device : onboardDevices)
            {
                ArrayList<ObjectLink> objectLinks = device.getObjectLinks();
                for(ObjectLink obj : objectLinks)
                {                   

                    //flight control object ID in json object model file is 12204
                    if(obj.getObjectId() == 12204)
                    {
                        flightControl = device;
                    }
                    if(flightControl != null)
                        break;//device found
                }
                if(flightControl != null)
                    break;//device found
            }

            //if flight control device has been found
            if(flightControl != null)
            {
                WorkerThread thread = new WorkerThread(flightControl, ThreadOperation.SCOUTING_REFLEX);
                executor.execute(thread);
            }
        }
    }
    
    /**
     * Performs observation requests on the threat and obstacle resources
     * @param device The connected device/client
     */
    public void observationRequestReflex(ReportedLwM2MClient device)
    {
        ArrayList<ObjectLink> objectLinks = device.getObjectLinks();
        for (ObjectLink obj : objectLinks) 
        {                        
            /**
             * 12202 -> Threat sensor
             * 12206 -> Obstacle sensor
             * 12207 -> UAV Attacker Device
             */
            if (obj.getObjectId() == 12202 || obj.getObjectId() == 12206 || obj.getObjectId() == 12207) 
            {
                WorkerThread thread = new WorkerThread(device, ThreadOperation.OBSERVATION_REFLEX);
                executor.execute(thread);
            }
        }
    }
}

//<editor-fold defaultstate="collapsed" desc="Worker Thread for sending scouting plan and other control center reflexes">
class WorkerThread implements Runnable
{
    private final ReportedLwM2MClient device;
    private final ThreadOperation threadOperation;
    private ReflexListenerImplCaller reflexListenerCaller;
    
    public WorkerThread(ReportedLwM2MClient device, ThreadOperation threadOperation) {
        this.device = device;
        this.threadOperation = threadOperation;
    }
    
    @Override
    public void run() 
    {   
        reflexListenerCaller = new ReflexListenerImplCaller();
        switch(this.threadOperation)
        {
            case SCOUTING_REFLEX:                                
                                reflexListenerCaller.addReflexListener(new AssignScoutRoleReflexListenerImpl());
                                reflexListenerCaller.assignScout(device);
                                break;
            case OBSERVATION_REFLEX:
                                reflexListenerCaller.addReflexListener(new SendObservationReflexListenerImpl());
                                reflexListenerCaller.sendObservationRequest(device);
                                break;
            default:
                
        }
    }
    
}
//</editor-fold>

/**
 * Indicates which thread operation to be done
 * <br/>
 * For convenience
 * @author AG BRIGHTER
 */
//<editor-fold defaultstate="collapsed" desc="For indicating thread operation">
enum ThreadOperation
{
    SCOUTING_REFLEX("scouting_reflex"),
    OBSERVATION_REFLEX("observation_reflex");
    private final String operation;
    
    private ThreadOperation(String operation) {
        this.operation = operation;
    }
    
    public String getOperation() {
        return operation;
    }
    
    @Override
    public String toString() {
        return this.operation;
    }
    
}
//</editor-fold>
