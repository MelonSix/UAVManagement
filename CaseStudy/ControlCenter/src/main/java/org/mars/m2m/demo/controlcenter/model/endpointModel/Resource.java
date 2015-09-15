/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model.endpointModel;

/**
 *
 * @author AG BRIGHTER
 */
public class Resource 
{
    private int id;
    private Object value;

    public Resource() {
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    
}
