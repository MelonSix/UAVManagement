/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.onem2m.enumerationTypes;

/**
 * Enumeration of the different data types for LwM2M endpoint resource values.
 * 
 * @author AG BRIGHTER
 */

public enum ResourceDataType
{
    INTEGER("INTEGER"),
    STRING("STRING"),
    BOOLEAN("BOOLEAN"),
    OPAQUE("OPAQUE"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    TIME("TIME"),
    LONG("LONG");
    private final String name;

    private ResourceDataType(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
    
}