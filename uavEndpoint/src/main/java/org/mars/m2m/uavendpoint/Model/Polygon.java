/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

/**
 *POJO class for handling {@link Polygon} instance data
 * @author AG BRIGHTER
 */
public class Polygon 
{
    /**
     * The total number of points.  
     */
    public int npoints;

    /**
     * The array of X coordinates.  
     */
    public int xpoints[];

    /**
     * The array of Y coordinates.  
     */
    public int ypoints[];

    /**
     * The bounds of this {@code Polygon}.0
     */
    private Rectangle bounds;

    public Polygon() {
    }

    public int getNpoints() {
        return npoints;
    }

    public void setNpoints(int npoints) {
        this.npoints = npoints;
    }

    public int[] getXpoints() {
        return xpoints;
    }

    public void setXpoints(int[] xpoints) {
        this.xpoints = xpoints;
    }

    public int[] getYpoints() {
        return ypoints;
    }

    public void setYpoints(int[] ypoints) {
        this.ypoints = ypoints;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
    
    
}
