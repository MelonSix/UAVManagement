/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import ch.qos.logback.classic.Logger;
import org.mars.m2m.dmcore.onem2m.xsdBundle.RequestPrimitive;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ResponsePrimitive;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class OperationService 
{
    Logger logger = (Logger) LoggerFactory.getLogger(OperationService.class);
    ResponsePrimitive response = null;

    public OperationService() {
    }
    
    public ResponsePrimitive create(RequestPrimitive request)
    {
        return response;
    }
    
    public ResponsePrimitive retrieve(RequestPrimitive request)
    {
//        ProofOfConcept pr = new ProofOfConcept();
//        response = pr.getResp();
//        response.setRequestIdentifier(request.getRequestIdentifier());
        return response;
    }
    
    public ResponsePrimitive update(RequestPrimitive request)
    {
         return response;
    }
    
    
    public ResponsePrimitive delete(RequestPrimitive request)
    {
         return response;
    }
    
    
    public ResponsePrimitive notify(RequestPrimitive request)
    {
         return response;
    }
}
