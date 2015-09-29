/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.model;

/**
 * Models the structure for containing the data for an update operation on a resource
 * @author AG BRIGHTER
 */
public class ObjectResourceModel {
    
    /**
     * The resource ID in this class is used for determining the appropriate datatype
     * to use for the corresponding value.
     */
    private int id;
    
    private Object value;
    
    private String dataType;

    public ObjectResourceModel() {
    }

    public ObjectResourceModel(int id, Object value) {
        this.id = id;
        this.value = value;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    
}
