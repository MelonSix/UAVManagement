/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.eventHandling.ListernerImpl;

import javax.ws.rs.core.MediaType;
import org.mars.m2m.demo.controlcenter.appConfig.CC_StaticInitConfig;
import org.mars.m2m.demo.controlcenter.client.ServiceConsumer;
import org.mars.m2m.demo.controlcenter.model.SvcConsumerDetails;
import org.mars.m2m.demo.controlcenter.eventHandling.Listerners.ReflexListener;
import org.mars.m2m.dmcore.model.ObjectLink;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.util.RequestUtil;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;

/**
 *
 * @author AG BRIGHTER
 */
public class SendObservationReflexListenerImpl implements ReflexListener
{

    @Override
    public void sendScoutingPlan(ReportedLwM2MClient device) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendObservationRequest(ReportedLwM2MClient device) 
    {
        ServiceConsumer sc = new ServiceConsumer();
        SvcConsumerDetails consumerDetails = new SvcConsumerDetails();
               
        RequestUtil requestUtil = new RequestUtil();
        
        for(ObjectLink  object : device.getObjectLinks())
        {
            if(object.getObjectId() == 12202 || object.getObjectId()==12206)
            {  
                String endpointURL;
                endpointURL = CC_StaticInitConfig.mgmntServerURL+device.getEndpoint()+"/"+
                                    object.getObjectId()+"/"+object.getObjectInstanceId()+"/0";
                
                consumerDetails.setRequest(requestUtil.
                        getRequestPrimitiveForData(Operation.NOTIFY, endpointURL, CC_StaticInitConfig.ccNotificationServiceURL, ""));


                sc.handlePost(CC_StaticInitConfig.mgmntAdapterURL, consumerDetails.getRequest(), MediaType.APPLICATION_XML);
            }
            
                if(object.getObjectId() == 12207)
                {
                    int[] resourceIDs = {14,15};
                    for(int i=0; i<resourceIDs.length;i++)
                    {
                        String endpointURL;
                        endpointURL = CC_StaticInitConfig.mgmntServerURL+device.getEndpoint()+"/"+
                                            object.getObjectId()+"/"+object.getObjectInstanceId()+"/"+resourceIDs[i];

                        consumerDetails.setRequest(requestUtil.
                                getRequestPrimitiveForData(Operation.NOTIFY, endpointURL, CC_StaticInitConfig.ccNotificationServiceURL, ""));


                        sc.handlePost(CC_StaticInitConfig.mgmntAdapterURL, consumerDetails.getRequest(), MediaType.APPLICATION_XML);
                    }
                }
        }
    }
    
}
