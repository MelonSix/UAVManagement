/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;

/**
 *
 * @author AG BRIGHTER
 */
public class MissileDispatcher extends BaseInstanceEnabler implements DeviceExecution
{
    private int maximumMissiles;
    private int remainingMissiles;
    private int targetLatitude;
    private int targetLongitude;
    
    public MissileDispatcher() {
        this(0,0,0,0);
    }

    public MissileDispatcher(int maximumMissiles, int remainingMissiles, int targetLatitude, int targetLongitude) {
        this.maximumMissiles = maximumMissiles;
        this.remainingMissiles = remainingMissiles;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
    }        
    
    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        System.out.println("Execute on missile dispatcher device");
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource resource) {
        System.out.println("Write on Missile Resource " + resourceid + " value " + resource);
        switch(resourceid)
        {
            case 2:
                this.setTargetLatitude(Integer.parseInt(resource.getValue().value.toString()));
                return new LwM2mResponse(ResponseCode.CHANGED);
            case 3:
                this.setTargetLongitude(Integer.parseInt(resource.getValue().value.toString()));
                return new LwM2mResponse(ResponseCode.CHANGED);
            default:
                return new LwM2mResponse(ResponseCode.METHOD_NOT_ALLOWED);
        }
    }

    @Override
    public ValueResponse read(int resourceid) {
        System.out.println("Read on Device Resource " + resourceid);
        switch (resourceid) {
        case 0:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
                    Value.newIntegerValue(this.getMaximumMissiles())) );
        case 1:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
                    Value.newIntegerValue(this.getRemainingMissiles())) );
        case 2:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
                    Value.newIntegerValue(this.getTargetLatitude())) );
        case 3:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
                    Value.newIntegerValue(this.getTargetLongitude())) );
        default:
            return super.read(resourceid);
        }
    }

    @Override
    public void Reboot(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FactorReset(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ResetErrorCode(int deviceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getMaximumMissiles() {
        return maximumMissiles;
    }

    public void setMaximumMissiles(int maximumMissiles) {
        this.maximumMissiles = maximumMissiles;
    }

    public int getRemainingMissiles() {
        return remainingMissiles;
    }

    public void setRemainingMissiles(int remainingMissiles) {
        this.remainingMissiles = remainingMissiles;
    }

    public int getTargetLatitude() {
        return targetLatitude;
    }

    public void setTargetLatitude(int targetLatitude) {
        this.targetLatitude = targetLatitude;
    }

    public int getTargetLongitude() {
        return targetLongitude;
    }

    public void setTargetLongitude(int targetLongitude) {
        this.targetLongitude = targetLongitude;
    }
    
    
}
