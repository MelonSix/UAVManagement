/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model.endpointModel;

import java.util.ArrayList;

/**
 *Models the json Data returned on an object instance's content
 * @author AG BRIGHTER
 */
public class Content 
{
    private int id;//instance id
    private ArrayList<Resource> resources;//resources of the instance

    public Content() {
        this.resources = new ArrayList();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }
    
}
