/*
 * Copyright (c) 2015 AG BRIGHTER.
 *
 * Contributors:
 *    AG BRIGHTER - initial API and implementation and/or initial documentation
 */
package org.mars.m2m.managementserver.core;

import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.model.LwM2mModelProvider;
import org.mars.m2m.dmcore.Loader.LwM2mModelLoader;

/**
 *
 * @author AG BRIGHTER
 */
public class CustomObjectModel  implements LwM2mModelProvider  {

    private final LwM2mModel model;
    public CustomObjectModel()
    {
        this.model = LwM2mModelLoader.loadCustomObjectModel("/uavObjectModel.json");
    }
    
    @Override
    public synchronized LwM2mModel getObjectModel(Client client) {
        // same model for all clients
        return model;
    }
    
}
