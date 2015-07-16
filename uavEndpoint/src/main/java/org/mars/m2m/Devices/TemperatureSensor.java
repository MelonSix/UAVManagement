/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import ch.qos.logback.classic.Logger;
import java.util.Timer;
import java.util.TimerTask;
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
public class TemperatureSensor extends BaseInstanceEnabler implements DeviceExecution
{
     private static Logger log = (Logger) LoggerFactory.getLogger(UAVmanager.class);
     
    //demo observe
    float tempVal = 0;
    
    //resources
    private float minMeasuredValue;
    private float maxMeasuredValue;
    private float minRangeValue;
    private float maxRangeValue;
    private float sensorValue;
    private String sensorUnits = "Deg. Celcius";
    
    /**
    * Default constructor
    */
    public TemperatureSensor() 
    {
        demoObserve();
    }        
    
    /**
     * 
     * @param minMeasuredValue
     * @param maxMeasuredValue
     * @param minRangeValue
     * @param maxRangeValue
     * @param sensorValue 
     */
    public TemperatureSensor(float minMeasuredValue, float maxMeasuredValue,
            float minRangeValue, float maxRangeValue) {
        this.minMeasuredValue = minMeasuredValue;
        this.maxMeasuredValue = maxMeasuredValue;
        this.minRangeValue = minRangeValue;
        this.maxRangeValue = maxRangeValue;
        demoObserve();
    }
    
    
    
    /**
     * For demonstration purposes of the sensor value
     */
    public void demoObserve()
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
        log.info("[{}] Execute on Device resource {}", this.getClass().getName() , resourceid);
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
    }

    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
        switch (resourceid) {
        case 5601:
            return new ValueResponse(ResponseCode.CONTENT, 
                    new LwM2mResource(resourceid,  Value.newFloatValue(this.getMinMeasuredValue()) ) );
        case 5602:
            return new ValueResponse(ResponseCode.CONTENT, 
                    new LwM2mResource(resourceid,  Value.newFloatValue(this.getMaxMeasuredValue()) )) ;
        case 5603:
            return new ValueResponse(ResponseCode.CONTENT, 
                    new LwM2mResource(resourceid,  Value.newFloatValue(this.getMinRangeValue()) ) ); 
        case 5700:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newFloatValue(tempVal) ) );
        case 5701:
            return new ValueResponse(ResponseCode.CONTENT, 
                    new LwM2mResource(resourceid,  Value.newStringValue(this.getSensorUnits()) ) );
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

    public float getTempVal() {
        return tempVal;
    }

    public void setTempVal(float tempVal) {
        this.tempVal = tempVal;
    }

    public float getMinMeasuredValue() {
        return minMeasuredValue;
    }

    public void setMinMeasuredValue(float minMeasuredValue) {
        this.minMeasuredValue = minMeasuredValue;
    }

    public float getMaxMeasuredValue() {
        return maxMeasuredValue;
    }

    public void setMaxMeasuredValue(float maxMeasuredValue) {
        this.maxMeasuredValue = maxMeasuredValue;
    }

    public float getMinRangeValue() {
        return minRangeValue;
    }

    public void setMinRangeValue(float minRangeValue) {
        this.minRangeValue = minRangeValue;
    }

    public float getMaxRangeValue() {
        return maxRangeValue;
    }

    public void setMaxRangeValue(float maxRangeValue) {
        this.maxRangeValue = maxRangeValue;
    }

    public float getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(float sensorValue) {
        this.sensorValue = sensorValue;
    }

    public String getSensorUnits() {
        return sensorUnits;
    }

    public void setSensorUnits(String sensorUnits) {
        this.sensorUnits = sensorUnits;
    }
    
    
}
