/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.Loader;

import ch.qos.logback.classic.Logger;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class LwM2mModelLoader 
{    
    public static Logger log = (Logger) LoggerFactory.getLogger(LwM2mModelLoader.class);
    
    public LwM2mModelLoader() {
    }
                    
    /**
     * Loads the Json file that contains the custom object model
     * @param objectModelStr the filename with its extension e.g. myObjectModel.json
     * @return The LwM2M model created from the object model file
     */
    public static LwM2mModel loadCustomObjectModel(String objectModelStr) 
    {
        InputStream myJsonStr;
        myJsonStr = LwM2mModelLoader.class.getResourceAsStream(objectModelStr);
        LwM2mModel customModel;
        HashMap<Integer, ObjectModel> map = null;
        if(myJsonStr != null)
        {            
            List<ObjectModel> objectModels = ObjectLoader.loadJsonStream(myJsonStr);
            map = new HashMap<>();
            for (ObjectModel objectModel : objectModels) {
                map.put(objectModel.id, objectModel);
            }
            log.info("file {} successfully unmarshalled",objectModelStr);
        }
        else
        {
            try {
                throw new Exception("Error loading json file");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        customModel = new LwM2mModel(map);
        return customModel;
    } 
    
}
