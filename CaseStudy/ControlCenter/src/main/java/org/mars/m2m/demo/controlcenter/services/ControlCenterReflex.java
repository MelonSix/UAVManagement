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
import org.mars.m2m.demo.controlcenter.eventHandling.CallbackImpl.AssignScout;
import org.mars.m2m.demo.controlcenter.eventHandling.ListernerImpl.AssignScoutRoleListenerImpl;
import org.mars.m2m.demo.controlcenter.model.ObjectLink;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.util.UavUtil;
import org.slf4j.LoggerFactory;

/**
 *This class handles operations that are automatically sent back to the endpoints
 * upon the triggering of an event
 * @author AG BRIGHTER
 */
public class ControlCenterReflex 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ControlCenterReflex.class);    
    private final UavUtil uavUtil;
    private final ExecutorService  executor;
    
    public ControlCenterReflex() {
        executor = Executors.newFixedThreadPool(5);
        this.uavUtil = new UavUtil();
    }
    
    /**
     * Gets the scouts and their devices and then selects the flight control device of each
     * then sends the coordinates to the UAV
     * @param connectedDevices 
     */
    public void scoutingWaypointsReflex(final ArrayList<ReportedLwM2MClient> connectedDevices)
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
                        System.out.println("Object id: "+obj.getObjectId());
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
                WorkerThread thread = new WorkerThread(flightControl);
                executor.execute(thread);
            }
        }
    }
}

class WorkerThread implements Runnable
{
    private ReportedLwM2MClient device;
    public WorkerThread(ReportedLwM2MClient device) {
        this.device = device;
    }
    
    @Override
    public void run() {
        AssignScout assignScout = new AssignScout();
        assignScout.addAssignScoutRoleListener(new AssignScoutRoleListenerImpl());
        assignScout.assignScout(device);
    }
    
}
