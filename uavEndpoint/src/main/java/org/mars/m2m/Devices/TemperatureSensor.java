/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import java.util.Timer;
import java.util.TimerTask;
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
public class TemperatureSensor extends BaseInstanceEnabler implements DeviceExecution
{
    //demo observe
    float tempVal = 0;
    
    /**
    * Default constructor
    */
    public TemperatureSensor() 
    {
        //demo of observing temperature sensor value
        Timer timer = new Timer();
            timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fireResourceChange(5700);
                tempVal = 10+ (float) Math.random()*100;
            }
        }, 1000, 1000);
    }    
    
    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        return super.execute(resourceid, params); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value) {
        return super.write(resourceid, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueResponse read(int resourceid) {
        System.out.println("Read on Device Resource " + resourceid);
        switch (resourceid) {
        case 5601:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newFloatValue(tempVal) ) );
        case 5602:
            return super.read(resourceid);
        case 5700:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newFloatValue(tempVal) ) );
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
    
}
