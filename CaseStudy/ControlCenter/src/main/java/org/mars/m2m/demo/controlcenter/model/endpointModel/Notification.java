/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model.endpointModel;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Stores the data from a notification
 * @author AG BRIGHTER
 */
@XmlRootElement
public class Notification 
{
    private String path;
    private Resource content;

    public Notification() {
    }

    public void setContent(Resource content) {
        this.content = content;
    }

    public Resource getContent() {
        return content;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
    @XmlTransient
    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
