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
public class AltitudeSensor  extends BaseInstanceEnabler implements DeviceExecution 
{
    private static Logger log = (Logger) LoggerFactory.getLogger(UAVmanager.class);
    
    //resources
    private float sensorValue;
    private float previousAltitude;
    
    public AltitudeSensor() {
        this(0f,0f);
    }

    public AltitudeSensor(float sensorValue, float previousAltitude) {
        this.sensorValue = sensorValue;
        this.previousAltitude = previousAltitude;
    }

    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        log.info("[{}] Execute on Device resource {}", this.getClass().getName() , resourceid);
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
    }
    
    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
        switch(resourceid)
        {
            case 0:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getSensorValue())) );
            case 1:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getPreviousAltitude())) );
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

    public float getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(float sensorValue) {
        this.sensorValue = sensorValue;
    }

    public float getPreviousAltitude() {
        return previousAltitude;
    }

    public void setPreviousAltitude(float previousAltitude) {
        this.previousAltitude = previousAltitude;
    }
    
}
