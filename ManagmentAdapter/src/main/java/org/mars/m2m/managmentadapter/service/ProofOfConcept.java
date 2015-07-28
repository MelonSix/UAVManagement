/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.RequestStatusCode;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResourceType;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.StdEventCats;
import org.mars.m2m.dmcore.onem2m.xsdBundle.MgmtResource;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.mars.m2m.dmcore.util.DmCommons;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ProofOfConcept 
{
    Logger logger = (Logger) LoggerFactory.getLogger(ProofOfConcept.class);
    ObjectFactory of;
    
    RequestPrimitive req;
    ResponsePrimitive resp;
    
    public ProofOfConcept() 
    {
        this.of = new ObjectFactory();
        
            req = of.createRequestPrimitive();
            resp = of.createResponsePrimitive();
            
            req.setOperation(Operation.CREATE.getValue());
            req.setTo("http://127.0.0.1:8090/ms/resource");
            req.setFrom("http://managmentadapter.com/resource");
            req.setRequestIdentifier(DmCommons.generateID());
            req.setResourceType(ResourceType.MGMT_OBJ.getValue());
            req.setName("myObjectName");
            PrimitiveContent primCtnt = new PrimitiveContent();
            primCtnt.getAny().add(new MgmtResource());
            req.setContent(primCtnt);
            req.setOriginatingTimestamp(DmCommons.getOneM2mTimeStamp());
            req.setResultExpirationTimestamp(DmCommons.getOneM2mTimeStamp());
            
            resp.setResponseStatusCode(RequestStatusCode.CHANGED.getValue());
            resp.setRequestIdentifier(DmCommons.generateID());
            resp.setContent(primCtnt);
            resp.setTo("http://127.0.0l.1/resource");
            resp.setFrom("http://managementadpater.com/resource");
            resp.setOriginatingTimestamp(DmCommons.getOneM2mTimeStamp());
            resp.setResultExpirationTimestamp(DmCommons.getOneM2mTimeStamp());
            resp.setEventCategory(StdEventCats.DEFAULT.name());
    }
    
    public void generateRequestPrimitive()
    {
        try 
        {
            System.out.println("Inside generate request function in proof of concept class");
            JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(req, System.out);
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
    
    public void generateResponsePrimitive()
    {
        try 
        {
            ByteArrayOutputStream bt = new ByteArrayOutputStream();
            System.out.println("Inside generate response function in proof of concept class");
            JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(resp, System.out);
            //return new String(bt.toByteArray());
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
            //return null;
        }
    }

    public RequestPrimitive getReq() {
        return req;
    }

    public void setReq(RequestPrimitive req) {
        this.req = req;
    }

    public ResponsePrimitive getResp() {
        return resp;
    }

    public void setResp(ResponsePrimitive resp) {
        this.resp = resp;
    }
    
}
