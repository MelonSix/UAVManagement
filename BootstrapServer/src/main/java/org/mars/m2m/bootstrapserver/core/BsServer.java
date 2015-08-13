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
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.mars.m2m.bootstrapserver.core;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoAPEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.leshan.server.bootstrap.BootstrapStore;
import org.eclipse.leshan.server.bootstrap.LwM2mBootstrapServer;
import org.eclipse.leshan.server.californium.impl.BootstrapResource;
import org.eclipse.leshan.server.californium.impl.LwM2mPskStore;
import org.eclipse.leshan.server.californium.impl.SecureEndpoint;
import org.eclipse.leshan.server.security.SecurityStore;
import org.eclipse.leshan.util.Validate;
import org.slf4j.LoggerFactory;

/**
 * 
 * A Lightweight M2M server, serving bootstrap information on /bs. -With modifications
 */
public class BsServer implements LwM2mBootstrapServer {

    private final static ch.qos.logback.classic.Logger LOG = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(BsServer.class);

    /** IANA assigned UDP port for CoAP (so for LWM2M) */
    public static final int PORT = 5683;

    /** IANA assigned UDP port for CoAP with DTLS (so for LWM2M) */
    public static final int PORT_DTLS = 5684;

    private final CoapServer coapServer;

    private final BootstrapStore bsStore;

    private final SecurityStore securityStore;

    public BsServer(BootstrapStore bsStore, SecurityStore securityStore) {
        this(new InetSocketAddress((InetAddress) null, PORT), new InetSocketAddress((InetAddress) null, PORT_DTLS),
                bsStore, securityStore);

    }

    public BsServer(InetSocketAddress localAddress, InetSocketAddress localAddressSecure,
            BootstrapStore bsStore, SecurityStore securityStore) {
        Validate.notNull(bsStore, "bootstrap store must not be null");

        this.bsStore = bsStore;
        this.securityStore = securityStore;
        // init CoAP server
        coapServer = new CoapServer();
        Endpoint endpoint = new CoAPEndpoint(localAddress);
        coapServer.addEndpoint(endpoint);

        // init DTLS server
        DTLSConnector connector = new DTLSConnector(localAddressSecure, null);
        connector.getConfig().setPskStore(new LwM2mPskStore(this.securityStore));

        Endpoint secureEndpoint = new SecureEndpoint(connector);
        coapServer.addEndpoint(secureEndpoint);

        // define /bs ressource
        BsResource bsResource = new BsResource(bsStore);
        coapServer.add(bsResource);
    }

    @Override
    public BootstrapStore getBoostrapStore() {
        return bsStore;
    }

    @Override
    public SecurityStore getSecurityStore() {
        return securityStore;
    }

    /**
     * Starts the server and binds it to the specified port.
     */
    public void start() {
        coapServer.start();
        LOG.info("LW-M2M server started");
    }

    /**
     * Stops the server and unbinds it from assigned ports (can be restarted).
     */
    public void stop() {
        coapServer.stop();
    }

    /**
     * Stops the server and unbinds it from assigned ports.
     */
    public void destroy() {
        coapServer.destroy();
    }
}