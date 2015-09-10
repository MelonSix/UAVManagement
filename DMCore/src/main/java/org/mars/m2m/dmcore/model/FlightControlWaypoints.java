/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.model;

import java.util.LinkedList;

/**
 *
 * @author AG BRIGHTER
 */
public class FlightControlWaypoints 
{
    private LinkedList<Float> yCordWaypoints;
    private LinkedList<Float> xCordWaypoints;

    public FlightControlWaypoints() {
        this.xCordWaypoints = new LinkedList<>();
        this.yCordWaypoints = new LinkedList<>();
    }
    
    public String getYcordsAsString()
    {
        StringBuilder builder = new StringBuilder();
        int i=0;
        for(; i < yCordWaypoints.size()-1; i++)
        {
            builder.append(yCordWaypoints.get(i)).append(",");
        }
        builder.append(yCordWaypoints.get(i));//avoids putting comma after the last item
        
        return builder.toString();
    }
    
    public String getXcordsAsString()
    {
        StringBuilder builder = new StringBuilder();
        int i=0;
        for(; i < xCordWaypoints.size()-1; i++)
        {
            builder.append(xCordWaypoints.get(i)).append(",");
        }
        builder.append(xCordWaypoints.get(i));//avoids putting comma after the last item
        
        return builder.toString();
    }

    public LinkedList<Float> getxCordWaypoints() {
        return xCordWaypoints;
    }

    public LinkedList<Float> getyCordWaypoints() {
        return yCordWaypoints;
    }

    public void setxCordWaypoints(LinkedList<Float> xCordWaypoints) {
        this.xCordWaypoints = xCordWaypoints;
    }

    public void setyCordWaypoints(LinkedList<Float> yCordWaypoints) {
        this.yCordWaypoints = yCordWaypoints;
    }
    
    
}
