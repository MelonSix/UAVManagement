/*
 * Copyright (c) 2015 AG BRIGHTER.
 *
 * Contributors:
 *    AG BRIGHTER - initial API and implementation and/or initial documentation
 */
package org.mars.m2m.managementserver.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
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
    public LwM2mModel getObjectModel(Client client) {
        // same model for all clients
        return model;
    }
    
}
