/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.eventObject;

import java.util.LinkedList;
import org.mars.m2m.demo.uav.Scout;

/**
 *
 * @author AG BRIGHTER
 */
public class FlightControlEventObject 
{
    private Scout scout;
    private LinkedList<Float> move_at_y_coordinate_task;

    public FlightControlEventObject() {
    }

    public Scout getScout() {
        return scout;
    }

    public LinkedList<Float> getMove_at_y_coordinate_task() {
        return move_at_y_coordinate_task;
    }

    public void setMove_at_y_coordinate_task(LinkedList<Float> move_at_y_coordinate_task) {
        this.move_at_y_coordinate_task = move_at_y_coordinate_task;
    }

    public void setScout(Scout scout) {
        this.scout = scout;
    }
    
    
}
