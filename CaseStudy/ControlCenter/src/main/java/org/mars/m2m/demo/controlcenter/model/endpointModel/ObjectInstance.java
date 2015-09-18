/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.model.endpointModel;

/**
 *Models the json Data returned on an object instance
 * @author AG BRIGHTER
 */
public class ObjectInstance 
{
    private String status;//indicates whether there's a content or no content
    private Content content;//the content of the instance if any

    public ObjectInstance() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
