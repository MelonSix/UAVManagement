/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.model;

import java.util.ArrayList;
import org.eclipse.leshan.core.request.BindingMode;

/**
 *
 * @author AG BRIGHTER
 */
public class ReportedLwM2MClient 
{
    private String endpoint;
    private String registrationId;
    private String registrationDate;
    private String address;
    private String smsNumber;
    private String lwM2MmVersion;
    private int lifeTime;
    private BindingMode bindingMode;
    private String rootPath;
    private ArrayList<ObjectLink> objectLinks;
    private boolean secure;

    public ReportedLwM2MClient() {
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getLwM2MmVersion() {
        return lwM2MmVersion;
    }

    public void setLwM2MmVersion(String lwM2MmVersion) {
        this.lwM2MmVersion = lwM2MmVersion;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public BindingMode getBindingMode() {
        return bindingMode;
    }

    public void setBindingMode(BindingMode bindingMode) {
        this.bindingMode = bindingMode;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public ArrayList<ObjectLink> getObjectLinks() {
        return objectLinks;
    }

    public void setObjectLinks(ArrayList<ObjectLink> objectLinks) {
        this.objectLinks = objectLinks;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public String toString() {
        return this.endpoint;
    }
    
    
}
