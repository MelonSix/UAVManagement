/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.onem2m.enumerationTypes;

/**
 *
 * @author AG BRIGHTER
 */
public enum ResultContent {
    NOTHING(0),
    ATTRIBUTES(1),
    HIERARCHICAL_ADDRESS(2),
    HIERARCHICAL_ADDRESS_PLUS_ATTRIBUTES(3),
    ATTRIBUTES_PLUS_CHILD_RESOURCES(4),
    ATTRIBUTES_PLUS_CHILD_RESOURCE_REFERENCES(5),
    CHILD_RESOURCE_REFERENCES(6),
    ORIGINAL_RESOURCE(7);
    
    private int value;
    
    ResultContent(int val)
    {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
    
}
