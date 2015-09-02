/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.ListenersImpl;

import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistryListener;
import org.mars.m2m.managementserver.core.HandleDeviceReporting;

/**
 *
 * @author AG BRIGHTER
 */

public class ClientRegistryListenerImpl implements ClientRegistryListener 
{
    LeshanServer server;
    public ClientRegistryListenerImpl(LeshanServer server) {
        this.server = server;
    }

    @Override
    public void registered(Client client) 
    {        
        System.out.println("Client registered: "+client.getEndpoint());
        
        //client reporting
        DeviceReporterImpl reporterImpl = new DeviceReporterImpl();
        HandleDeviceReporting deviceReporting = new HandleDeviceReporting();
        deviceReporting.addDeviceReporterListener(reporterImpl);
        deviceReporting.performReporting(client);
        
        //LwM2mResponse response = server.send(client, new ObserveRequest(3303, 0, 5700));
        //System.out.println("Observe response: " + response);
    }

    @Override
    public void updated(Client clientUpdated) {
        
        System.out.println("Client registration updated: "+clientUpdated.getEndpoint());
    }

    @Override
    public void unregistered(Client client) {
                
        System.out.println("Client deregistering: "+client.getEndpoint());
    }
}