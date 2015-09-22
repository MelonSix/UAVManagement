/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model.endpointModel;

/**
 * Models the structure for containing the data for an update operation on a resource
 * @author AG BRIGHTER
 */
public class ObjectResourceUpdate {
    
    /**
     * The resource ID in this class is used for determining the appropriate datatype
     * to use for the corresponding value.
     */
    private int id;
    
    private Object value;
    
    private String dataType;

    public ObjectResourceUpdate() {
    }

    public ObjectResourceUpdate(int id, Object value, String dataType) {
        this.id = id;
        this.value = value;
        this.dataType = dataType;
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
