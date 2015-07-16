/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

/**
 *
 * @author AG BRIGHTER
 */
public class MinimumBoundingRectangle {
    
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;
    
    public MinimumBoundingRectangle() {
    }
    
    /**
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY 
     */
    public MinimumBoundingRectangle(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }   
    
    
}
