/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Devices;

import ch.qos.logback.classic.Logger;
import java.util.LinkedList;
import org.eclipse.leshan.ResponseCode;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ValueResponse;
import org.mars.m2m.uavendpoint.Interfaces.DeviceExecution;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class FlightControl extends BaseInstanceEnabler implements DeviceExecution
{
    private static Logger log = (Logger) LoggerFactory.getLogger(FlightControl.class);
    private LinkedList<Float> move_at_y_coordinate_task;
    private LinkedList<Float> move_at_x_coordinate_task;
    

    public FlightControl() {
        this.move_at_x_coordinate_task = new LinkedList<>();
        this.move_at_y_coordinate_task = new LinkedList<>();
    }

    @Override
    public LwM2mResponse execute(int resourceid, byte[] params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LwM2mResponse write(int resourceid, LwM2mResource value){
        System.out.println("huraaayyy");
        log.info("Write on Device Resource [{}]  value[{}] ", resourceid , value);
        switch(resourceid)
        {
            case 0:                
                return new LwM2mResponse(ResponseCode.CHANGED);
            case 1:
                String [] points = value.getValue().value.toString().split(",");
                for(String point : points)
                {
                    move_at_y_coordinate_task.add(Float.parseFloat(point));
                }
                return new LwM2mResponse(ResponseCode.CHANGED);
            default:
                return new LwM2mResponse(ResponseCode.BAD_REQUEST);
        }
    }

    @Override
    public ValueResponse read(int resourceid) {
        log.info("[{}] Read on resource: {}",this.getClass().getName(),resourceid);
        switch (resourceid) {
//        case 0:
//            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
//                    Value.newIntegerValue(this.getMaximumMissiles())) );
//        case 1:
//            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
//                    Value.newIntegerValue(this.getRemainingMissiles())) );
//        case 2:
//            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
//                    Value.newIntegerValue(this.getTargetLatitude())) );
//        case 3:
//            return new ValueResponse(ResponseCode.CONTENT, new LwM2mResource(resourceid,  
//                    Value.newIntegerValue(this.getTargetLongitude())) );
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

    public LinkedList<Float> getMove_at_x_coordinate_task() {
        return move_at_x_coordinate_task;
    }

    public LinkedList<Float> getMove_at_y_coordinate_task() {
        return move_at_y_coordinate_task;
    }
    
    
}
