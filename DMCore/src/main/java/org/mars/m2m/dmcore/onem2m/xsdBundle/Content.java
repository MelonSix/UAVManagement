/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.onem2m.xsdBundle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AG BRIGHTER
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Content {
    
    protected String returnedContent;

    public Content() {
    }

    public String getReturnedContent() {
        return returnedContent;
    }

    public void setReturnedContent(String returnedContent) {
        this.returnedContent = returnedContent;
    }
}
