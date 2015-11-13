/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.enumeration;

/**
 *
 * @author AG BRIGHTER
 */
public enum NotificationType
{
    OBSTACLE("obstacle"),
    THREAT("threat"),
    CONFLICT("conflict"),
    ATTACKER_THREAT_STATUS("isThreatDestroyed"),
    DESTROYED_THREAT_INDEX("destroyedThreat"),
    INVALID("invalid");
    private final String name;

    private NotificationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}