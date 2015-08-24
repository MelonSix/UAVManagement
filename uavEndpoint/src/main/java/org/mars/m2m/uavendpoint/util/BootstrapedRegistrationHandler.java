/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.util;

import ch.qos.logback.classic.Logger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.leshan.client.californium.impl.ObjectResource;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig;
import org.mars.m2m.uavendpoint.Model.RegisteredClientData;
import org.mars.m2m.uavendpoint.Model.RequiredBootstrapInfo;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mSecurity;
import org.mars.m2m.uavendpoint.omaObjects.OmaLwM2mServer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class BootstrapedRegistrationHandler 
{
    public final Logger LOG = (Logger) LoggerFactory.getLogger(BootstrapedRegistrationHandler.class);

    public BootstrapedRegistrationHandler() {
    }
    
    /**
     * Performs registration using the information from the LWM2M Bootstrap server
     * @param client
     * @param serverConfigs
     * @param securityConfigs
     * @param clientAddress 
     */
    private void registerWithBoostrapInfo(AbstractDevice client, ArrayList<OmaLwM2mServer> serverConfigs, 
            ArrayList<OmaLwM2mSecurity> securityConfigs, final InetSocketAddress clientAddress) 
    {
        final String endpointIdentifier = client.endpointName;
        
        /**
        * LwM2M client registration
        * Treats server and security instances as pairs
        * {(serverConfigs[0],securityConfigs[0]),..,(serverConfigs[n],securityConfigs[n])}
        */
        for (int i = 0; i < serverConfigs.size(); i++) 
        {
            String[] lwm2mServerDetails = securityConfigs.get(i).getUri().split(":");
            
            //first sets the request handler's address and performs registration
            client.client.setRequestSender(clientAddress, new InetSocketAddress(lwm2mServerDetails[1].substring(2), 
                    Integer.parseInt(lwm2mServerDetails[2])));
            client.registrationID = DeviceHelper.register(client.client, endpointIdentifier, securityConfigs.get(i), 
                    serverConfigs.get(i), client.serverHostName, client.serverPort);
            
            if (client.registrationID != null) {
                RegisteredClientData registeredClientData = new RegisteredClientData();
                registeredClientData.setLwM2mSecurity(securityConfigs.get(i));
                registeredClientData.setLwM2mServer(serverConfigs.get(i));
                client.registeredClientDataList.getRegisteredList().put(client.registrationID, registeredClientData);
            }
        }
    }
    
    /**
     * Initiates a bootstrap request for the client and then passes the resulting information to the registration method
     * @param initializer
     * @param coapServer
     * @param clientAddress
     * @param client 
     */
    public void doBsRegistration(ObjectsInitializer initializer, CoapServer coapServer, 
            final InetSocketAddress clientAddress, AbstractDevice client) 
    {
        try 
        {
            List<ObjectEnabler> enablers;
            
            UnmarshalBootstrap bootstrap = new UnmarshalBootstrap();
            BootstrapConfig bootstrapConfig;
            bootstrapConfig = bootstrap.getBootstrapConfig(
                    new RequiredBootstrapInfo(client.endpointName, client.bsAddress, 
                                            client.bsPortnumber, client.client));
            client.security = bootstrapConfig.security;
            client.servers = bootstrapConfig.servers;
            
            ArrayList<OmaLwM2mSecurity> securityConfigs = new ArrayList<>();            
            for (Integer i : client.security.keySet()) {
                OmaLwM2mSecurity lwM2mSecurity;
                lwM2mSecurity = ConvertObject.toLwM2mSecurity(client.security.get(i));
                initializer.setInstancesForObject(0, lwM2mSecurity);
                securityConfigs.add(lwM2mSecurity);
            }
            
            ArrayList<OmaLwM2mServer> serverConfigs = new ArrayList<>();
            for (Integer i : client.servers.keySet()) {
                OmaLwM2mServer lwM2mServer;
                lwM2mServer = ConvertObject.toLwM2mServer(client.servers.get(i));
                initializer.setInstancesForObject(1, lwM2mServer);
                serverConfigs.add(lwM2mServer);
            }
            
            enablers = initializer.create(0, 1);
            List<LwM2mObjectEnabler> objectEnablers = new ArrayList<LwM2mObjectEnabler>(enablers);
            for (LwM2mObjectEnabler enabler : objectEnablers) {
                if (coapServer.getRoot().getChild(Integer.toString(enabler.getId())) != null) {
                    throw new IllegalArgumentException("Trying to load Client Object of name '" + enabler.getId() 
                            + "' when one was already added.");
                }
                final ObjectResource clientObject = new ObjectResource(enabler);
                coapServer.add(clientObject);
            }
            
            //Registration
            registerWithBoostrapInfo(client, serverConfigs, securityConfigs, clientAddress);
            
        }catch(NullPointerException nulEx)
        {
            LOG.error("Error Loading bootstrap information. Registration failed");
        }
        catch (Exception e) {
            LOG.error(e.toString());
        }
    }
    
}
