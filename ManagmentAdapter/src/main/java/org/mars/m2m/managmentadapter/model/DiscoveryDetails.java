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
    private String objectId; // Made as a string to track IDs that are null although IDs are integers
    private String objectInstanceId;
    private String resourceId;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectInstanceId() {
        return objectInstanceId;
    }

    public void setObjectInstanceId(String objectInstanceId) {
        this.objectInstanceId = objectInstanceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
}
