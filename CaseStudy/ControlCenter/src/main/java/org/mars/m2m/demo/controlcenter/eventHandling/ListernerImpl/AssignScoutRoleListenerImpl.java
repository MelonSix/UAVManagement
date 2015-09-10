/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.eventHandling.ListernerImpl;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.LinkedList;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.mars.m2m.demo.controlcenter.appConfig.StaticInitConfig;
import org.mars.m2m.demo.controlcenter.client.ServiceConsumer;
import org.mars.m2m.demo.controlcenter.client.SvcConsumerDetails;
import org.mars.m2m.demo.controlcenter.eventHandling.Listerners.AssignScoutRoleListener;
import org.mars.m2m.demo.controlcenter.model.ObjectResource;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.mars.m2m.demo.controlcenter.services.ControlCenterServices;
import org.mars.m2m.demo.controlcenter.util.RequestUtil;
import org.mars.m2m.dmcore.model.FlightControlWaypoints;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class AssignScoutRoleListenerImpl implements AssignScoutRoleListener
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AssignScoutRoleListenerImpl.class);
        
    public AssignScoutRoleListenerImpl() {
    }
    
    /**
     * Starts a new thread to send the scouting information
     * @param device 
     */
    @Override
    public void sendScoutingPlan(final ReportedLwM2MClient device) 
    {        
        ServiceConsumer sc = new ServiceConsumer();
        SvcConsumerDetails consumerDetails = new SvcConsumerDetails();
        FlightControlWaypoints flightControlWaypoints = new FlightControlWaypoints();
        LinkedList<Float> yCordWaypoints = ControlCenterServices.roleAssignForScouts();
        flightControlWaypoints.setyCordWaypoints(yCordWaypoints);
        
        Gson gson = new Gson();
        ObjectResource resUpdateModel = new ObjectResource();
        resUpdateModel.setId(1);//y waypoints resource ID from object model json file
        resUpdateModel.setDataType("STRING");//datatype of resource from object model json file
        resUpdateModel.setValue(flightControlWaypoints.getYcordsAsString());        
        String data = gson.toJson(resUpdateModel);
        
        RequestUtil requestUtil = new RequestUtil();
        String endpointURL = StaticInitConfig.mgmntServerURL+device.getEndpoint()+"/12204/0/1";
        
        consumerDetails.setRequest(requestUtil.
                getRequestPrimitiveForData(Operation.UPDATE, endpointURL, StaticInitConfig.ccAddress, data));
        try 
        {
            System.out.println("Inside generate request function in proof of concept class");
            JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(consumerDetails.getRequest(), System.out);
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        sc.handlePost(StaticInitConfig.mgmntAdapterURL, consumerDetails.getRequest(), MediaType.APPLICATION_XML);
        logger.info("Scouting info sent: {},to: {}",data,device.getAddress());
        System.out.println("Sending address: "+endpointURL);
    }
    
}

enum TransferProtocol
{
    /**
     * Hyper Text Transfer Protocol
     */
    HTTP("http://"),
    /**
     * Hyper Text Transfer Protocol with Security
     */
    HTTPS("https://"),
    /**
     * Constrained Application Protocol
     */
    CoAP("coap://");
    private final String name;    
    TransferProtocol(String name)
    {
        this.name = name;
    }
}