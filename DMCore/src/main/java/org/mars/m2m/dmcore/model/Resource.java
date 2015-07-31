/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.model;

/**
 *
 * @author AG BRIGHTER
 */
public class Resource {
    
    private String id;
    private Object value;
    
    public Resource() {
    }

    public Resource(String id, Object value) {
        this.id = id;
        this.value = value;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    
}
