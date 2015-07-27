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
public enum DiscResType {
    HIERARCHICAL(1),
    NON_HIERARCHICAL(2),
    CSEID_PLUS_RESOURCEID(3);
    
    private final int value;

    private DiscResType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    
}
