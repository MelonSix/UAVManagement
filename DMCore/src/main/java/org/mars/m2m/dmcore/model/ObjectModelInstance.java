/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.model;

import java.util.List;

/**
 *
 * @author AG BRIGHTER
 */
public class ObjectModelInstance {

    private String id;
    private List<Resource> resources;
    
    public ObjectModelInstance() {
    }

    public ObjectModelInstance(String id, List<Resource> resources) {
        this.id = id;
        this.resources = resources;
    }   
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
    
    
}
