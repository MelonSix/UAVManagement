/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Zebra Technologies - initial API and implementation
 *******************************************************************************/
package org.mars.m2m.apiExtension;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoAPEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.leshan.client.LwM2mClient;
import org.eclipse.leshan.client.californium.impl.CaliforniumLwM2mClientRequestSender;
import org.eclipse.leshan.client.californium.impl.ObjectResource;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.core.request.UplinkRequest;
import org.eclipse.leshan.core.response.ErrorCallback;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ResponseCallback;
import org.eclipse.leshan.util.Validate;

/**
 * A Lightweight M2M client.
 */
public class LeshanClientExt implements LwM2mClient {

    private final CoapServer clientSideServer;
    private final AtomicBoolean clientServerStarted = new AtomicBoolean(false);
    private CaliforniumLwM2mClientRequestSender requestSender;
    private final List<LwM2mObjectEnabler> objectEnablers;
    private final ArrayList<InterceptRequest> interceptors;
    
    /**
     * Modified to allow starting of server without LWM2M server address specified.
     * The subsequent bootstrap information is then used later to create the {@link CaliforniumLwM2mClientRequestSender} object
     * @param clientAddress
     * @param serverLocal
     * @param objectEnablers 
     */
    public LeshanClientExt(final InetSocketAddress clientAddress,
            final CoapServer serverLocal, final List<LwM2mObjectEnabler> objectEnablers) {
        this.interceptors = new ArrayList<>();

        Validate.notNull(clientAddress);
        Validate.notNull(serverLocal);
        //Validate.notNull(serverAddress);
        Validate.notNull(objectEnablers);
        Validate.notEmpty(objectEnablers);

        final Endpoint endpoint = new CoAPEndpoint(clientAddress);
        serverLocal.addEndpoint(endpoint);

        clientSideServer = serverLocal;

        this.objectEnablers = new ArrayList<>(objectEnablers);
        for (LwM2mObjectEnabler enabler : objectEnablers) {
            if (clientSideServer.getRoot().getChild(Integer.toString(enabler.getId())) != null) {
                throw new IllegalArgumentException("Trying to load Client Object of name '" + enabler.getId()
                        + "' when one was already added.");
            }

            final ObjectResource clientObject = new ObjectResource(enabler);
            clientSideServer.add(clientObject);
        }
        
        /*requestSender = new CaliforniumLwM2mClientRequestSender(serverLocal.getEndpoint(clientAddress), serverAddress,
        this);*/
    }
    
    /**
     * Registers interceptor
     * @param <E>
     * @param interceptor 
     */
    public <E extends InterceptRequest> void addInterceptor(E interceptor)
    {
        if (interceptor!=null) {
            this.interceptors.add(interceptor);
        }
    }
    
    /**
     * de-registers interceptor
     * @param <E>
     * @param interceptor 
     */
    public <E extends InterceptRequest> void removeInterceptor(E interceptor)
    {
        if (interceptor!=null) {
            this.interceptors.remove(interceptor);
        }
    } 
    
    /**
     * Creates a request sender for this client using the specified client and server addresses
     * @param clientAddress Client address
     * @param serverAddress Server address
     */
    public void setRequestSender(InetSocketAddress clientAddress, InetSocketAddress serverAddress)
    {
        requestSender = new CaliforniumLwM2mClientRequestSender(clientSideServer.getEndpoint(clientAddress), serverAddress,
        this);
    }
    
    @Override
    public void start() {
        clientSideServer.start();
        clientServerStarted.set(true);
    }

    @Override
    public void stop() {
        clientSideServer.stop();
        clientServerStarted.set(false);
    }

    @Override
    public <T extends LwM2mResponse> T send(final UplinkRequest<T> request) {
        if (!clientServerStarted.get()) {
            throw new RuntimeException("Internal CoapServer is not started.");
        }
        if(this.interceptors != null && this.interceptors.size()>0)
        {
            for(Iterator<InterceptRequest> it = this.interceptors.iterator(); it.hasNext();)
            {
                InterceptRequest interceptor = it.next();
                interceptor.interceptUplinkRequest(request);
            }
        }
        return requestSender.send(request, null);
    }

    @Override
    public <T extends LwM2mResponse> T send(final UplinkRequest<T> request, long timeout) {
        if (!clientServerStarted.get()) {
            throw new RuntimeException("Internal CoapServer is not started.");
        }
        return requestSender.send(request, timeout);
    }

    @Override
    public <T extends LwM2mResponse> void send(final UplinkRequest<T> request,
            final ResponseCallback<T> responseCallback, final ErrorCallback errorCallback) {
        if (!clientServerStarted.get()) {
            throw new RuntimeException("Internal CoapServer is not started.");
        }
        requestSender.send(request, responseCallback, errorCallback);
    }

    @Override
    public List<LwM2mObjectEnabler> getObjectEnablers() {
        return objectEnablers;
    }
}
