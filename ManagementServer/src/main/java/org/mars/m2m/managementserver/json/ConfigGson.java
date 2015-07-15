/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.ResourceModel;
import org.eclipse.leshan.core.model.json.ObjectModelSerializer;
import org.eclipse.leshan.core.model.json.ResourceModelSerializer;
import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.response.LwM2mResponse;

/**
 *
 * @author AG BRIGHTER
 */
public class ConfigGson {
    private static Gson gson;
    
    public static Gson getCustomGsonConfig()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(org.eclipse.leshan.server.client.Client.class, new ClientSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mResponse.class, new ResponseSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeDeserializer());        
        gsonBuilder.registerTypeHierarchyAdapter(ObjectModel.class, new ObjectModelSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(ResourceModel.class, new ResourceModelSerializer());
        gsonBuilder.setDateFormat("();yyyy-MM-dd'T'HH:mm:ssXXX");
        gson = gsonBuilder.create();
        return gson;
    }
}
