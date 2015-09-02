/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.DeviceReporting;

import java.util.EventListener;

/**
 *Handles events of a newly connected/registered client/device
 * @author AG BRIGHTER
 */
public interface DeviceReporter extends EventListener
{

    /**
     *Reports a connected device/LwM2M client 
     * @param data The data to be relayed
     */
    void reportDevice(String data);
}
