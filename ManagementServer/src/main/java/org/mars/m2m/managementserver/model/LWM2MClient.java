/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.model;

import java.util.Date;
import org.eclipse.leshan.LinkObject;
import org.eclipse.leshan.core.request.BindingMode;

/**
 *
 * @author AG BRIGHTER
 */
public class LWM2MClient 
{
    private Date registrationDate;

    private int port;

    /*
     * The address of the LWM2M Server's CoAP end point the client used to register.
     */
    private String hostname;
    /**
    * Holds IP address.
    */
    private String address;
    /**
    * Specifies the address family type, for instance, '1' for IPv4
    * addresses, and '2' for IPv6 addresses.
    */
    private int family;

    private long lifeTimeInSec;

    private String smsNumber;

    private String lwM2mVersion;

    private BindingMode bindingMode;

    /**
     * The LWM2M Client's unique endpoint name.
     */
    private String endpoint;

    private String registrationId;

    private LinkObject[] objectLinks;

    /** The location where LWM2M objects are hosted on the device */
    private String rootPath;

    private Date lastUpdate;

    public LWM2MClient() {
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public long getLifeTimeInSec() {
        return lifeTimeInSec;
    }

    public void setLifeTimeInSec(long lifeTimeInSec) {
        this.lifeTimeInSec = lifeTimeInSec;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getLwM2mVersion() {
        return lwM2mVersion;
    }

    public void setLwM2mVersion(String lwM2mVersion) {
        this.lwM2mVersion = lwM2mVersion;
    }

    public BindingMode getBindingMode() {
        return bindingMode;
    }

    public void setBindingMode(BindingMode bindingMode) {
        this.bindingMode = bindingMode;
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

    public LinkObject[] getObjectLinks() {
        return objectLinks;
    }

    public void setObjectLinks(LinkObject[] objectLinks) {
        this.objectLinks = objectLinks;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    
    
}
