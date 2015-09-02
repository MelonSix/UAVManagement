/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;
import org.mars.m2m.uavendpoint.Model.Rectangle;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ThreatSensor extends BaseInstanceEnabler implements DeviceExecution
{
    private static Logger log = (Logger) LoggerFactory.getLogger(ThreatSensor.class);
    
    //Resources
    private int threatType;
    private int threatPosition_latitude;
    private int threatPosition_longitude;
    private Rectangle threatMinimumBoundedRectangle;
    private float threatWidth;
    private float threatHeight;
    private Gson gson;
    
    float myVal = (float) Math.random();//testing

    /**
    * Default constructor
    */
    public ThreatSensor() 
    {
        this.gson = new Gson();
    }

    /**
     *
     * @param threatType
     * @param threatPosition_latitude
     * @param threatPosition_longitude
     * @param threatMinimumBoundedRectangle
     * @param threatWidth
     * @param threatHeight
     */
    public ThreatSensor(int threatType, int threatPosition_latitude, 
            int threatPosition_longitude, String threatMinimumBoundedRectangle, 
            float threatWidth, float threatHeight) 
    {
        this.gson = new Gson();
        this.threatType = threatType;
        this.threatPosition_latitude = threatPosition_latitude;
        this.threatPosition_longitude = threatPosition_longitude;
        this.threatMinimumBoundedRectangle = gson.fromJson(threatMinimumBoundedRectangle, Rectangle.class);
        this.threatWidth = threatWidth;
        this.threatHeight = threatHeight;
    }   
    
    
    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        log.info("[{}] Execute on Device resource {}", this.getClass().getName() , resourceid);
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value) {
        return super.write(resourceid, value);
    }

    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
        switch (resourceid) {
        case 0:
            return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newIntegerValue(this.getThreatType())) );
        case 1:
            return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newIntegerValue(this.getThreatPosition_latitude())) );
        case 2:
            return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newIntegerValue(this.getThreatPosition_longitude())) );
        case 3:
            return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(gson.toJson(this.getThreatMinimumBoundedRectangle()))) );
        case 4:
            return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getThreatWidth())) );
        case 5:
            return new ValueResponse(ResponseCode.CONTENT,
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getThreatHeight())) );
        default:
            return super.read(resourceid);
        }
    } 

    @Override
    public void Reboot(int deviceID) {
       System.out.println("Reboot triggered");
    }

    @Override
    public void FactorReset(int deviceID) {
        System.out.println("factory reset triggered");
    }

    @Override
    public void ResetErrorCode(int deviceID) {
        System.out.println("Device error code reset triggered");
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        ThreatSensor.log = log;
    }

    public int getThreatType() {
        return threatType;
    }

    public void setThreatType(int threatType) {
        this.threatType = threatType;
    }

    public int getThreatPosition_latitude() {
        return threatPosition_latitude;
    }

    public void setThreatPosition_latitude(int threatPosition_latitude) {
        this.threatPosition_latitude = threatPosition_latitude;
    }

    public int getThreatPosition_longitude() {
        return threatPosition_longitude;
    }

    public void setThreatPosition_longitude(int threatPosition_longitude) {
        this.threatPosition_longitude = threatPosition_longitude;
    }

    public Rectangle getThreatMinimumBoundedRectangle() {
        return threatMinimumBoundedRectangle;
    }

    public void setThreatMinimumBoundedRectangle(String bounds) {
        this.threatMinimumBoundedRectangle = gson.fromJson(bounds, Rectangle.class);
    }

    public float getThreatWidth() {
        return threatWidth;
    }

    public void setThreatWidth(float threatWidth) {
        this.threatWidth = threatWidth;
    }

    public float getThreatHeight() {
        return threatHeight;
    }

    public void setThreatHeight(float threatHeight) {
        this.threatHeight = threatHeight;
    }

    public float getMyVal() {
        return myVal;
    }

    public void setMyVal(float myVal) {
        this.myVal = myVal;
    }

    
}
