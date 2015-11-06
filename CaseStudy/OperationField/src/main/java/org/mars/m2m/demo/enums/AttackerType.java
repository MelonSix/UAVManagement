/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.enums;

/**
 * Attacker UAV types
 * @author AG BRIGHTER
 */
public enum AttackerType {
    TYPE1("type1"),
    TYPE2("type2"),
    TYPE3("type3"),
    TYPE4("type4"),
    TYPE5("type5"),
    TYPE6("type6");
    
    final String name;

    private AttackerType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
