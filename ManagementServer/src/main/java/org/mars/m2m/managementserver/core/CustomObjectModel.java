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

/**
 *
 * @author AG BRIGHTER
 */
public class CustomObjectModel  implements LwM2mModelProvider  {

    private final LwM2mModel model;
    public CustomObjectModel()
    {
    InputStream myJsonStr;
        myJsonStr = this.getClass().getResourceAsStream("/uavObjectModel.json");
        LwM2mModel customModel;
        HashMap<Integer, ObjectModel> map = null;
        if(myJsonStr != null)
        {
            List<ObjectModel> objectModels = ObjectLoader.loadJsonStream(myJsonStr);
            map = new HashMap<>();
            for (ObjectModel objectModel : objectModels) {
                map.put(objectModel.id, objectModel);
            }
        }
        else
        {
            try {
                throw new Exception("Error loading json file");
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, null, ex);
            }
        }
        customModel = new LwM2mModel(map);
        this.model = customModel;
    }
    @Override
    public LwM2mModel getObjectModel(Client client) {
        // same model for all clients
        return model;
    }
    
}
