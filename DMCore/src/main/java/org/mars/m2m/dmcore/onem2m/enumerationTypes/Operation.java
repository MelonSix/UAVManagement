/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.onem2m.enumerationTypes;

import java.math.BigInteger;

/**
 *
 * @author AG BRIGHTER
 */
public enum Operation {
    CREATE(1),
    RETRIEVE(2),
    UPDATE(3),
    DELETE(4),
    NOTIFY(5);
    
    private final BigInteger value;
    
    Operation(long opVal)
    {
        this.value = BigInteger.valueOf(opVal);
    } 

    public BigInteger getValue() {
        return value;
    }     
    
}
