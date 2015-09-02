/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.Listeners;

import java.util.EventListener;
import org.eclipse.leshan.server.client.Client;

/**
 *Handles events of a newly connected/registered client/device
 * @author AG BRIGHTER
 */
public interface DeviceReporter extends EventListener
{

    /**
     *Reports a connected device/LwM2M client
     * @param c The client to be reported 
     */
    void reportDevice(Client c);
}
