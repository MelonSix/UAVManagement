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
public enum StdEventCats {
    DEFAULT(1),
    IMMEDIATE(2),
    BESTEFFORT(3),
    LATEST(4);
    
    private final int value;

    private StdEventCats(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
     
    
}
