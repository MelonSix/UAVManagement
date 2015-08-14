/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.omaObjects;

import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;

/**
 *
 * @author AG BRIGHTER
 */
public class LwM2mServer  extends BaseInstanceEnabler
{
    private int shortId;
    private int lifetime;
    private int defaultMinPeriod;
    private Integer defaultMaxPeriod;
    private Integer disableTimeout;
    private boolean notifIfDisabled;
    private BindingMode binding;
        
    public LwM2mServer() {
    }

    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        return super.execute(resourceid, params); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value) 
    {        
        System.out.println("Write on Server Resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 1:
            setLifetime((Integer) value.getValue().value);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 2:
            setDefaultMinPeriod((Integer) value.getValue().value);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 3:
            setDefaultMaxPeriod((Integer) value.getValue().value);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 5:
            setDisableTimeout((Integer) value.getValue().value);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 6:
            setNotifIfDisabled((Boolean) value.getValue().value);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 7:
            setBinding(BindingMode.valueOf(value.getValue().value.toString()));
            return new LwM2mResponse(ResponseCode.CHANGED);
        default:
            return super.write(resourceid, value);
        }
    }

    @Override
    public ValueResponse read(int resourceid) {
        System.out.println("Read on Server Resource " + resourceid);
        try {
            switch (resourceid) {
                case 0:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newIntegerValue(getShortId())));
                case 1:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newIntegerValue(getLifetime())));
                case 2:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newIntegerValue(getDefaultMinPeriod())));
                case 3:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newIntegerValue(getDefaultMaxPeriod())));
                case 5:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newIntegerValue(getDisableTimeout())));
                case 6:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newBooleanValue(isNotifIfDisabled())));
                case 7:
                    return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                            Value.newStringValue(getBinding().toString())));
                default:
                    return super.read(resourceid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.read(resourceid);
    }
    
    
    //-------------Accessors and Mutators -------------------
    
    public int getShortId() {
        return shortId;
    }

    public LwM2mServer setShortId(int shortId) {
        this.shortId = shortId;
        return this;
    }

    public int getLifetime() {
        return lifetime;
    }

    public LwM2mServer setLifetime(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public int getDefaultMinPeriod() {
        return defaultMinPeriod;
    }

    public LwM2mServer setDefaultMinPeriod(int defaultMinPeriod) {
        this.defaultMinPeriod = defaultMinPeriod;
        return this;
    }

    public Integer getDefaultMaxPeriod() {
        return defaultMaxPeriod;
    }

    public LwM2mServer setDefaultMaxPeriod(Integer defaultMaxPeriod) {
        this.defaultMaxPeriod = defaultMaxPeriod;
        return this;
    }

    public Integer getDisableTimeout() {
        return disableTimeout;
    }

    public LwM2mServer setDisableTimeout(Integer disableTimeout) {
        this.disableTimeout = disableTimeout;
        return this;
    }

    public boolean isNotifIfDisabled() {
        return notifIfDisabled;
    }

    public LwM2mServer setNotifIfDisabled(boolean notifIfDisabled) {
        this.notifIfDisabled = notifIfDisabled;
        return this;
    }

    public BindingMode getBinding() {
        return binding;
    }

    public LwM2mServer setBinding(BindingMode binding) {
        this.binding = binding;
        return this;
    }
    
    
}
