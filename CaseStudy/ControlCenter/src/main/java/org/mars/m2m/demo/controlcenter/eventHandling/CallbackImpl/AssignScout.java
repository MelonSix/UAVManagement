/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.eventHandling.CallbackImpl;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import org.mars.m2m.demo.controlcenter.eventHandling.Listerners.AssignScoutRoleListener;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AssignScout 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssignScout.class);
    private ArrayList<AssignScoutRoleListener> listeners;
    
    public AssignScout() {
        this.listeners = new ArrayList<>();
    }
    
    public void addAssignScoutRoleListener(AssignScoutRoleListener scoutRoleListener)
    {
        if(this.listeners != null && !this.listeners.contains(scoutRoleListener))
        {
            this.listeners.add(scoutRoleListener);
        }
    }
    
    public void removeAssignScoutRoleListener(AssignScoutRoleListener scoutRoleListener)
    {
        if(this.listeners != null && this.listeners.contains(scoutRoleListener))
        {
            this.listeners.remove(scoutRoleListener);
        }
    }
    
    public void assignScout(final ReportedLwM2MClient device)
    {
        if(this.listeners != null)
        {
            for(AssignScoutRoleListener listener : this.listeners)
            {
                listener.sendScoutingPlan(device);
            }
        }
    }
}
