/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the model for the individual discovery details about a particular object/instance/resource
 * @author BRIGHTER AGYEMANG
 */
public class DiscoveryDetails 
{
    private String url;
    private Map<String, String> attributes;
    private int objectId;
    private int objectInstanceId;
    private int resourceId;
    private String path;

    public DiscoveryDetails() {
        this.attributes = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectInstanceId() {
        return objectInstanceId;
    }

    public void setObjectInstanceId(int objectInstanceId) {
        this.objectInstanceId = objectInstanceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
    
}
