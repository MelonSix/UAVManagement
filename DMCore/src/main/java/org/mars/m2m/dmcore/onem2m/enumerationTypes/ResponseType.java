/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.onem2m.enumerationTypes;

/**
 *
 * @author AG BRIGHTER
 * Used for rt parameter in request and operation attribute in &lt;request&gt; resource
 */
public enum ResponseType {
    NON_BLOCKING_REQUEST_SYNCH(1),
    NON_BLOCKING_REQUEST_ASYNCH(2),
    BLOCKING_REQUEST(3);
    
    private final int value;

    private ResponseType(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
    
    
}
