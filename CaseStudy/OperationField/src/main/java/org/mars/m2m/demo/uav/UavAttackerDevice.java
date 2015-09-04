/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.uav;

import ch.qos.logback.classic.Logger;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.mars.m2m.demo.world.KnowledgeInterface;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class UavAttackerDevice extends BaseInstanceEnabler implements DeviceExecution
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UavAttackerDevice.class);
    
    //device resources
    private volatile UAVPath path_planned_at_current_time_step;
    private int current_index_of_planned_path = 0; //index of waypoint
    private UAVPath path_planned_at_last_time_step;//the total path planned lately.
    private UAVPath history_path;//the history path of the attacker
    private boolean need_to_replan = true;
    private boolean replanned_at_current_time_step = false;
    private boolean moved_at_last_time = false;
    

    //Knowledgebase
    private KnowledgeInterface kb;

    public UavAttackerDevice() 
    {
    }

    @Override
    public void Reboot(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FactorReset(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ResetErrorCode(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UAVPath getPath_planned_at_current_time_step() {
        return path_planned_at_current_time_step;
    }

    public void setPath_planned_at_current_time_step(UAVPath path_planned_at_current_time_step) {
        this.path_planned_at_current_time_step = path_planned_at_current_time_step;
    }

    public int getCurrent_index_of_planned_path() {
        return current_index_of_planned_path;
    }

    public void setCurrent_index_of_planned_path(int current_index_of_planned_path) {
        this.current_index_of_planned_path = current_index_of_planned_path;
    }

    public UAVPath getPath_planned_at_last_time_step() {
        return path_planned_at_last_time_step;
    }

    public void setPath_planned_at_last_time_step(UAVPath path_planned_at_last_time_step) {
        this.path_planned_at_last_time_step = path_planned_at_last_time_step;
    }

    public UAVPath getHistory_path() {
        return history_path;
    }

    public void setHistory_path(UAVPath history_path) {
        this.history_path = history_path;
    }

    public boolean isNeed_to_replan() {
        return need_to_replan;
    }

    public void setNeed_to_replan(boolean need_to_replan) {
        this.need_to_replan = need_to_replan;
    }

    public boolean isReplanned_at_current_time_step() {
        return replanned_at_current_time_step;
    }

    public void setReplanned_at_current_time_step(boolean replanned_at_current_time_step) {
        this.replanned_at_current_time_step = replanned_at_current_time_step;
    }

    public boolean isMoved_at_last_time() {
        return moved_at_last_time;
    }

    public void setMoved_at_last_time(boolean moved_at_last_time) {
        this.moved_at_last_time = moved_at_last_time;
    }

    public KnowledgeInterface getKb() {
        return kb;
    }

    public void setKb(KnowledgeInterface kb) {
        this.kb = kb;
    }
}
