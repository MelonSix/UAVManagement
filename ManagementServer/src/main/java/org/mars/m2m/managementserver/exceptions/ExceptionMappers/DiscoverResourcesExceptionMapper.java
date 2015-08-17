/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.exceptions.ExceptionMappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.mars.m2m.managementserver.exceptions.DiscoverResourcesException;
import org.mars.m2m.managementserver.model.ErrorMessage;

/**
 *
 * @author AG BRIGHTER
 */
@Provider
public class DiscoverResourcesExceptionMapper implements ExceptionMapper<DiscoverResourcesException> {

    /**
     *
     * @param exception
     * @return
     */
    @Override
    public Response toResponse(DiscoverResourcesException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 404, "www.exceptionLinkHere.com");
        return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
    }
    
}
