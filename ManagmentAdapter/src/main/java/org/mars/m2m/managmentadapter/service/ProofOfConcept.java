/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResourceType;
import org.mars.m2m.dmcore.onem2m.xsdBundle.MgmtResource;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ObjectFactory;
import org.mars.m2m.dmcore.onem2m.xsdBundle.PrimitiveContent;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.util.dateTime.OneM2mTimestamp;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ProofOfConcept 
{
    Logger logger = (Logger) LoggerFactory.getLogger(ProofOfConcept.class);
    ObjectFactory of;
    
    RequestPrimitive r;
    
    public ProofOfConcept() {
        this.of = new ObjectFactory();
            r = of.createRequestPrimitive();
            r.setOperation(Operation.CREATE.getValue());
            r.setTo("http://127.0.0.1:8090/ms/resource");
            r.setFrom("http://managmentadapter.com/resource");
            r.setRequestIdentifier(UUID.randomUUID().toString());
            r.setResourceType(ResourceType.MGMT_OBJ.getValue());
            r.setName("myObjectName");
            PrimitiveContent primCtnt = new PrimitiveContent();
            primCtnt.getAny().add(new MgmtResource());
            r.setContent(primCtnt);
            r.setOriginatingTimestamp(OneM2mTimestamp.getTimeStamp());
            r.setResultExpirationTimestamp(OneM2mTimestamp.getTimeStamp());
            
    }
    
    public void generateRequestPrimitive()
    {
        try 
        {
            System.out.println("Inside generate request function in proof of concept class");
            JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(r, System.out);
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
    
}
