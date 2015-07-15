/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
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
public class ThreatSensor extends BaseInstanceEnabler implements DeviceExecution
{
    float myVal = (float) Math.random();//testing

    /**
    * Default constructor
    */
    public ThreatSensor() {
        // TODO Set device informatin here
        /**E.g
         * private final String manufacturelModel = "EclipseCon Tuto Client";
        private final String modelNumber = "2015";
        private final String serialNumber = "leshan-client-001";
        private final BindingMode bindingModel = BindingMode.U;
         */
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
            @Override
            public void run() {
               // fireResourceChange(5700);
                myVal = (float) Math.random();
            }
        }, 1000, 1000);
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
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 14:            
            return new LwM2mResponse(ResponseCode.CHANGED);
        case 15:
            return new LwM2mResponse(ResponseCode.CHANGED);
        default:
            return super.write(resourceid, value);
        }
    }

    @Override
    public ValueResponse read(int resourceid) {
        System.out.println("Read on Device Resource " + resourceid);
        switch (resourceid) {
        case 0:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newStringValue(this.objectModel.name)) );
        case 1:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newIntegerValue((int)myVal)) );
        case 2:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newIntegerValue((int)myVal)) );
        case 3:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newStringValue((UUID.randomUUID().toString()))) );
        case 4:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newFloatValue(myVal)) );
        case 5:
            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  Value.newFloatValue(myVal)) );
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

}
