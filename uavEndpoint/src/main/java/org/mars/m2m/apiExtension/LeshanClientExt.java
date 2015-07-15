/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.apiExtension;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoAPEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.impl.CaliforniumLwM2mClientRequestSender;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.util.Validate;

/**
 *
 * @author AG BRIGHTER
 */
public class LeshanClientExt extends LeshanClient {
    
    private final CoapServer clientSideServer;
    private final AtomicBoolean clientServerStarted = new AtomicBoolean(false);
    private final CaliforniumLwM2mClientRequestSender requestSender;
    private final List<LwM2mObjectEnabler> objectEnablers;
    
        
    public LeshanClientExt(final InetSocketAddress serverAddress, final List<LwM2mObjectEnabler> objectEnablers) {
        this(new InetSocketAddress("0", 0), serverAddress, new CoapServer(), objectEnablers);
        //super(serverAddress, objectEnablers);
    }

    public LeshanClientExt(final InetSocketAddress clientAddress, final InetSocketAddress serverAddress,
            final List<LwM2mObjectEnabler> objectEnablers) {
        this(clientAddress, serverAddress, new CoapServer(), objectEnablers);
    }

    public LeshanClientExt(final InetSocketAddress clientAddress, final InetSocketAddress serverAddress,
            final CoapServer serverLocal, final List<LwM2mObjectEnabler> objectEnablers) 
    {
        super(serverAddress, objectEnablers);
        
        Validate.notNull(clientAddress);
        Validate.notNull(serverLocal);
        Validate.notNull(serverAddress);
        Validate.notNull(objectEnablers);
        Validate.notEmpty(objectEnablers);

        final Endpoint endpoint = new CoAPEndpoint(clientAddress);
        serverLocal.addEndpoint(endpoint);

        clientSideServer = serverLocal;
        clientSideServer.setMessageDeliverer(new ServerMessageDelivererExt(serverLocal.getRoot()));

        this.objectEnablers = new ArrayList<>(objectEnablers);
        for (LwM2mObjectEnabler enabler : objectEnablers) {
            if (clientSideServer.getRoot().getChild(Integer.toString(enabler.getId())) != null) {
                throw new IllegalArgumentException("Trying to load Client Object of name '" + enabler.getId()
                        + "' when one was already added.");
            }

            final ObjectResourcePatch clientObject = new ObjectResourcePatch(enabler);
            clientSideServer.add(clientObject);
        }

        requestSender = new CaliforniumLwM2mClientRequestSender(serverLocal.getEndpoint(clientAddress), serverAddress,
                this);
    }
    
}
