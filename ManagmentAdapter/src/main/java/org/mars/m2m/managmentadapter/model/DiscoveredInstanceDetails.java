/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class DiscoveredInstanceDetails 
{
    private String url;
    private String objectId; // Made as a string to track IDs that are null although IDs are integers
    private String objectInstanceId;
    private String path;

    public DiscoveredInstanceDetails() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
