/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.enums;

/**
 * Target or threat types
 * @author AG BRIGHTER
 */
public enum ThreatType {
    TYPE0(0),
    TYPE1(1),
    TYPE2(2),
    TYPE3(3),
    TYPE4(4),
    TYPE5(5),
    DUMMY(-1);
    
    final int type;

    private ThreatType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
    
}
