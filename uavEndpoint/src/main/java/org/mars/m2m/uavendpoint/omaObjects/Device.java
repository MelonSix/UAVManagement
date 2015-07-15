/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.omaObjects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.uavendpoint.Configuration.UAVConfiguration;

/**
 *
 * @author AG BRIGHTER
 */
public class Device extends BaseInstanceEnabler {


    protected String utcOffset = new SimpleDateFormat("X").format(Calendar.getInstance().getTime());
    protected UAVConfiguration uavConfig;
    
    protected String timeZone = TimeZone.getDefault().getID();
    
    protected int deviceId;
    protected String deviceName;
    
    protected String Manufacturer;
    protected String DeviceType;
    protected String ModelNumber;
    protected String serialNumber;
    protected String hardwareVersion;
    protected String firwareVersion;
    protected String softwareVersion;
    protected int availablePowerSrc;
    protected int powerSrcVolt;
    protected int powerSrcCur;
    protected int batteryLevel;
    protected int batteryStatus;
    protected int freeMemory;
    protected int totalMemory; 
    protected int errorCode=0;
    protected String supportedBinding="U";

    public Device() {
        // notify new date each 5 second
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fireResourceChange(13);
            }
        }, 5000, 5000);
    }
    
    @Override
    public ValueResponse read(int resourceid) 
    {
        System.out.println("Read on Device Resource " + resourceid);
        switch (resourceid) {
        case 0:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getManufacturer())));
        case 1:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getModelNumber())));
        case 2:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getSerialNumber())));
        case 3:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getFirmwareVersion())));
        case 9:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newIntegerValue(getBatteryLevel())));
        case 10:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newIntegerValue(getMemoryFree())));
        case 11:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    new Value<?>[] { Value.newIntegerValue(getErrorCode()) }));
        case 13:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newDateValue(getCurrentTime())));
        case 14:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getUtcOffset())));
        case 15:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getTimezone())));
        case 16:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,
                    Value.newStringValue(getSupportedBinding())));
        default:
            return super.read(resourceid);
        }
    }

    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        System.out.println("Execute on Device resource " + resourceid);
        if (params != null && params.length != 0)
            System.out.println("\t params " + new String(params));
        return new LwM2mResponse(ResponseCode.CHANGED);
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value) {
        System.out.println("Write on Device Resource " + resourceid + " value " + value);
        switch (resourceid) {
        case 13:
            return new LwM2mResponse(ResponseCode.NOT_FOUND);
        case 14:
            setUtcOffset((String) value.getValue().value);
            fireResourceChange(resourceid);
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 15:
            setTimezone((String) value.getValue().value);
            fireResourceChange(resourceid);
            return new LwM2mResponse(ResponseCode.CHANGED);
        default:
            return super.write(resourceid, value);
        }
    }

    protected String getManufacturer() {
        return "Leshan Example Device";
    }

    protected String getModelNumber() {
        return "Model 500";
    }

    protected String getSerialNumber() {
        return "LT-500-000-0001";
    }

    protected String getFirmwareVersion() {
        return "1.0.0";
    }

    protected int getErrorCode() {
        return 0;
    }

    protected int getBatteryLevel() {
        final Random rand = new Random();
        return rand.nextInt(100);
    }

    protected int getMemoryFree() {
        final Random rand = new Random();
        return rand.nextInt(50) + 114;
    }

    protected Date getCurrentTime() {
        return new Date();
    }

    protected String getUtcOffset() {
        return utcOffset;
    }

    protected void setUtcOffset(String t) {
        utcOffset = t;
    }

    protected String getTimezone() {
        return timeZone;
    }

    protected void setTimezone(String t) {
        timeZone = t;
    }

    private String getSupportedBinding() {
        return "U";
    }

    protected UAVConfiguration getUavConfig() {
        return uavConfig;
    }

    protected void setUavConfig(UAVConfiguration uavConfig) {
        this.uavConfig = uavConfig;
    }

    protected String getTimeZone() {
        return timeZone;
    }

    protected void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getDeviceId() {
        return deviceId;
    }

    protected void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    protected String getDeviceName() {
        return deviceName;
    }

    protected void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    protected String getDeviceType() {
        return DeviceType;
    }

    protected void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    protected String getHardwareVersion() {
        return hardwareVersion;
    }

    protected void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    protected String getFirwareVersion() {
        return firwareVersion;
    }

    protected void setFirwareVersion(String firwareVersion) {
        this.firwareVersion = firwareVersion;
    }

    protected String getSoftwareVersion() {
        return softwareVersion;
    }

    protected void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    protected int getAvailablePowerSrc() {
        return availablePowerSrc;
    }

    protected void setAvailablePowerSrc(int availablePowerSrc) {
        this.availablePowerSrc = availablePowerSrc;
    }

    protected int getPowerSrcVolt() {
        return powerSrcVolt;
    }

    protected void setPowerSrcVolt(int powerSrcVolt) {
        this.powerSrcVolt = powerSrcVolt;
    }

    protected int getPowerSrcCur() {
        return powerSrcCur;
    }

    protected void setPowerSrcCur(int powerSrcCur) {
        this.powerSrcCur = powerSrcCur;
    }

    protected int getBatteryStatus() {
        return batteryStatus;
    }

    protected void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    protected int getFreeMemory() {
        return freeMemory;
    }

    protected void setFreeMemory(int freeMemory) {
        this.freeMemory = freeMemory;
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }
}
