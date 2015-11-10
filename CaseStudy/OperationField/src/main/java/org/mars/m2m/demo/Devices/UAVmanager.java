/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.Devices;

import ch.qos.logback.classic.Logger;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class UAVmanager extends BaseInstanceEnabler
{
    private static Logger log = (Logger) LoggerFactory.getLogger(UAVmanager.class);
    
    /**
     * Resources
     */
    private String focusModel;
    private String origin;
    private String manufacturer;
    private String initialYearOfService;
    private float length;
    private float width;
    private float height;
    private float weight_empty;
    private float weight_mtow;
    private String powerPlant;
    private float maximumSpeed;
    private float maximumRange;
    private float serviceCeiling;
    private float rateOfClimb;
    private float payloadCapability;
    private float cruiseSpeed;
    private String launchType;
    private int maximumFlightTime;
    private float wingspan;
    private int operatingTemperature_lowest;
    private int operatingTemperature_highest;

    public UAVmanager() {
    }
    
    /**
     *
     * @param focusModel
     * @param origin
     * @param manufacturer
     * @param initialYearOfService
     * @param length
     * @param width
     * @param height
     * @param weight_empty
     * @param weight_mtow
     * @param powerPlant
     * @param maximumSpeed
     * @param maximumRange
     * @param serviceCeiling
     * @param rateOfClimb
     * @param payloadCapability
     * @param cruiseSpeed
     * @param launchType
     * @param maximumFlightTime
     * @param wingspan
     * @param operatingTemperature_lowest
     * @param operatingTemperature_highest
     */
    public UAVmanager(String focusModel, String origin, String manufacturer, 
            String initialYearOfService, float length, float width, float height, 
            float weight_empty, float weight_mtow, String powerPlant, float maximumSpeed, 
            float maximumRange, float serviceCeiling, float rateOfClimb, float payloadCapability, 
            float cruiseSpeed, String launchType, int maximumFlightTime, float wingspan, 
            int operatingTemperature_lowest, int operatingTemperature_highest)
    {
        this.focusModel = focusModel;
        this.origin = origin;
        this.manufacturer = manufacturer;
        this.initialYearOfService = initialYearOfService;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight_empty = weight_empty;
        this.weight_mtow = weight_mtow;
        this.powerPlant = powerPlant;
        this.maximumSpeed = maximumSpeed;
        this.maximumRange = maximumRange;
        this.serviceCeiling = serviceCeiling;
        this.rateOfClimb = rateOfClimb;
        this.payloadCapability = payloadCapability;
        this.cruiseSpeed = cruiseSpeed;
        this.launchType = launchType;
        this.maximumFlightTime = maximumFlightTime;
        this.wingspan = wingspan;
        this.operatingTemperature_lowest = operatingTemperature_lowest;
        this.operatingTemperature_highest = operatingTemperature_highest;
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
        return super.write(resourceid, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
        
        switch(resourceid)
        {
            case 0:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.getFocusModel())) );
            case 1:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.getOrigin())) );
            case 2:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.getManufacturer())));
            case 3:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.getInitialYearOfService())) );
            case 4:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getLength())) );
            case 5:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getWidth())) );
            case 6:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getHeight())) );
            case 7:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getWeight_empty())) );
            case 8:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getWeight_mtow())) );
            case 9:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.getPowerPlant())) );
            case 10:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getMaximumSpeed())) );
            case 11:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getMaximumRange())) );
            case 12:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getServiceCeiling())) );
            case 13:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getRateOfClimb())) );
            case 14:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getPayloadCapability())) );
            case 15:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getCruiseSpeed())) );
            case 16:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.getLaunchType())) );
            case 17:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newIntegerValue(this.getMaximumFlightTime())) );
            case 18:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newFloatValue(this.getWingspan())) );
            case 19:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newIntegerValue(this.getOperatingTemperature_lowest())) );
            case 20:
                return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newIntegerValue(this.getOperatingTemperature_highest())) );
            default:
                return super.read(resourceid);
        }
    }
              
    
    public String getFocusModel() {
        return focusModel;
    }

    public void setFocusModel(String focusModel) {
        this.focusModel = focusModel;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getInitialYearOfService() {
        return initialYearOfService;
    }

    public void setInitialYearOfService(String initialYearOfService) {
        this.initialYearOfService = initialYearOfService;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight_empty() {
        return weight_empty;
    }

    public void setWeight_empty(float weight_empty) {
        this.weight_empty = weight_empty;
    }

    public float getWeight_mtow() {
        return weight_mtow;
    }

    public void setWeight_mtow(float weight_mtow) {
        this.weight_mtow = weight_mtow;
    }

    public String getPowerPlant() {
        return powerPlant;
    }

    public void setPowerPlant(String powerPlant) {
        this.powerPlant = powerPlant;
    }

    public float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(float maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public float getMaximumRange() {
        return maximumRange;
    }

    public void setMaximumRange(float maximumRange) {
        this.maximumRange = maximumRange;
    }

    public float getServiceCeiling() {
        return serviceCeiling;
    }

    public void setServiceCeiling(float serviceCeiling) {
        this.serviceCeiling = serviceCeiling;
    }

    public float getRateOfClimb() {
        return rateOfClimb;
    }

    public void setRateOfClimb(float rateOfClimb) {
        this.rateOfClimb = rateOfClimb;
    }

    public float getPayloadCapability() {
        return payloadCapability;
    }

    public void setPayloadCapability(float payloadCapability) {
        this.payloadCapability = payloadCapability;
    }

    public float getCruiseSpeed() {
        return cruiseSpeed;
    }

    public void setCruiseSpeed(float cruiseSpeed) {
        this.cruiseSpeed = cruiseSpeed;
    }

    public String getLaunchType() {
        return launchType;
    }

    public void setLaunchType(String launchType) {
        this.launchType = launchType;
    }

    public int getMaximumFlightTime() {
        return maximumFlightTime;
    }

    public void setMaximumFlightTime(int maximumFlightTime) {
        this.maximumFlightTime = maximumFlightTime;
    }

    public float getWingspan() {
        return wingspan;
    }

    public void setWingspan(float wingspan) {
        this.wingspan = wingspan;
    }

    public int getOperatingTemperature_lowest() {
        return operatingTemperature_lowest;
    }

    public void setOperatingTemperature_lowest(int operatingTemperature_lowest) {
        this.operatingTemperature_lowest = operatingTemperature_lowest;
    }

    public int getOperatingTemperature_highest() {
        return operatingTemperature_highest;
    }

    public void setOperatingTemperature_highest(int operatingTemperature_highest) {
        this.operatingTemperature_highest = operatingTemperature_highest;
    } 
    
    
}
