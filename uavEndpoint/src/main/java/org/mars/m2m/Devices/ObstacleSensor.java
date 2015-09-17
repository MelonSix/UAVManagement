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
public class ObstacleSensor  extends BaseInstanceEnabler implements DeviceExecution
{
    private static final Logger log = (Logger) LoggerFactory.getLogger(ObstacleSensor.class);
    
    /*private int index;
    private Polygon shape;
    private Rectangle mbr;*/
    
    //resource
    private String obstacleInJson="";

    public ObstacleSensor() 
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
        return super.execute(resourceid, params); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value) {
        return super.write(resourceid, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
        switch (resourceid) 
        {
        case 0:
            return new ValueResponse(ResponseCode.CONTENT, 
                        new LwM2mResource(resourceid,  Value.newStringValue(this.obstacleInJson)) );
            //<editor-fold defaultstate="collapsed" desc="Other resources">
            /*case 1:
            return new ValueResponse(ResponseCode.CONTENT,
            new LwM2mResource(resourceid,  Value.newStringValue(gson.toJson(this.getShape()))) );
            case 2:
            return new ValueResponse(ResponseCode.CONTENT,
            new LwM2mResource(resourceid,  Value.newStringValue(gson.toJson(this.getMbr()))) );*/
//</editor-fold>
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

    public void setObstacleInJson(String obstacleInJson) {
        this.obstacleInJson = obstacleInJson;
        fireResourceChange(0);
    }

    public String getObstacleInJson() {
        return obstacleInJson;
    }

    
    
}
