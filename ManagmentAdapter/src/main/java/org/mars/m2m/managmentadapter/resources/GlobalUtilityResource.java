/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.resources;

import ch.qos.logback.classic.Logger;
import javax.ws.rs.Path;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
@Path("/globalutils")
public class GlobalUtilityResource 
{
    
    Logger logger = (Logger) LoggerFactory.getLogger(GlobalUtilityResource.class);
    
    public GlobalUtilityResource() {
    }
    
    
}
