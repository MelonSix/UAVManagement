/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.model;

import java.util.HashMap;

/**
 *
 * @author AG BRIGHTER
 */
public class ObjectLink
{
    private String url;
    private HashMap<String,String> attributes;
    private int objectId;
    private int objectInstanceId;

    public ObjectLink() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getAttributes() {
        if(this.attributes == null)
            this.attributes = new HashMap<>();
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
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

    @Override
    public String toString() {
        return this.url;
    }
    
    
}
