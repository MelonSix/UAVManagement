/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.apiExtension;

import org.eclipse.leshan.core.request.UplinkRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;

/**
 *
 * @author AG BRIGHTER
 */
public interface InterceptRequest {
    <T extends LwM2mResponse> void  interceptUplinkRequest(final UplinkRequest<T> request);
}
