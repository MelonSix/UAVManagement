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
public class ThreatSensorInCaseStudy extends BaseInstanceEnabler implements DeviceExecution
{
    private static Logger log = (Logger) LoggerFactory.getLogger(ThreatSensorInCaseStudy.class);
        
    //Resource
    private String threatAsJson="";
    
    float myVal = (float) Math.random();//testing

    /**
    * Default constructor
    */
    public ThreatSensorInCaseStudy() 
    {
        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        
        @Override
        public void run() {
        fireResourceChange(0);
        }
        }, 500, 500);*/
    }    
    
    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        return super.execute(resourceid, params);
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
                        new LwM2mResource(resourceid,  Value.newStringValue(this.threatAsJson)) );
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

    public String getThreatAsJson() {
        return threatAsJson;
    }

    public void setThreatInJson(String threatAsJson) {
        this.threatAsJson = threatAsJson;
        fireResourceChange(0);
    }
    
    
}
