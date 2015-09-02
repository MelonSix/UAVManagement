/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

/**
 *POJO class for handling {@link Rectangle} instance data
 * @author AG BRIGHTER
 */
public class Rectangle 
{
    /**
     * The X coordinate of the upper-left corner of the <code>Rectangle</code>.
     *
     */
    public int x;

    /**
     * The Y coordinate of the upper-left corner of the <code>Rectangle</code>.
     *
     */
    public int y;

    /**
     * The width of the <code>Rectangle</code>.
     */
    public int width;

    /**
     * The height of the <code>Rectangle</code>.
     */
    public int height;

    public Rectangle() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    
}
