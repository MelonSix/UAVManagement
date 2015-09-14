/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.eventObject;

import org.mars.m2m.demo.model.Obstacle;
import org.mars.m2m.demo.uav.Scout;

/**
 *
 * @author AG BRIGHTER
 */
public class DetectedObstacleEventObject 
{
    private Scout scout;
    private Obstacle obstacle;

    public DetectedObstacleEventObject() {
    }

    public DetectedObstacleEventObject(Scout scout, Obstacle obstacle) {
        this.scout = scout;
        this.obstacle = obstacle;
    }
    
    public Scout getScout() {
        return scout;
    }

    public void setScout(Scout scout) {
        this.scout = scout;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }
    
    
}
