/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import ch.qos.logback.classic.Logger;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class MissileDispatcher extends BaseInstanceEnabler implements DeviceExecution
{
    private static Logger log = (Logger) LoggerFactory.getLogger(MissileDispatcher.class);
    
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
        log.info("[{}] Execute on Device resource {}", this.getClass().getName() , resourceid);
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value){
        log.info("Write on Device Resource [{}]  value[{}] ", resourceid , value);
        switch(resourceid)
        {
            case 2:
                this.setTargetLatitude(Integer.parseInt(value.getValue().value.toString()));
                fireResourceChange(resourceid);
                return new LwM2mResponse(ResponseCode.CHANGED);
            case 3:
                this.setTargetLongitude(Integer.parseInt(value.getValue().value.toString()));
                fireResourceChange(resourceid);
                return new LwM2mResponse(ResponseCode.CHANGED);
            default:
                return new LwM2mResponse(ResponseCode.BAD_REQUEST);
        }
    }

    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
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
