/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

import ch.qos.logback.classic.Logger;
//import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.HashMap;
import java.util.Map;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.slf4j.LoggerFactory;

/**
 *
 * @author BRIGHTER AGYEMANG
 */
public class SvcConsumerDetails 
{
    Logger logger = (Logger) LoggerFactory.getLogger(SvcConsumerDetails.class);
    
    RequestPrimitive request;
    Map<String, String> headerData;
    Map<String, Object> formData;

    public SvcConsumerDetails() {
        this.request = null;
        this.headerData = new HashMap<>();
        this.formData = new HashMap<>();
    }    

    public RequestPrimitive getRequest() {
        return request;
    }

    public void setRequest(RequestPrimitive request) {
        this.request = request;
    }

    public Map<String, String> getHeaderData() {
        return headerData;
    }

    public void setHeaderData(Map<String, String> headerData) {
        this.headerData = headerData;
    }    

    public Map<String, Object> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

    
}
