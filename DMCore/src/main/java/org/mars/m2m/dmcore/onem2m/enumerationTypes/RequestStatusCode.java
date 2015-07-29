/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.onem2m.enumerationTypes;

import java.math.BigInteger;

/**
 *
 * @author AG BRIGHTER
 */
public enum RequestStatusCode {
    CREATED(2000),
    DELETED(2001),
    CHANGED(2004),
    BAD_REQUEST(4000),
    NOT_FOUND(4004),
    OPERATION_NOT_ALLOWED(4005),
    REQUEST_TIMEOUT(4008),
    SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE(4101),
    CONTENTS_UNACCEPTABLE(4102),
    ACCESS_DENIED(4103),
    GROUP_REQUEST_IDENTIFIER_EXISTS(4104),
    CONFLICT(4105),
    INTERNAL_SERVER_ERROR(5000),
    NOT_IMPLEMENTED(5001),
    TARGET_NOT_REACHABLE(5103),
    NO_PRIVILEGE(5105),
    ALREADY_EXISTS(5106),
    TARGET_NOT_SUBSCRIBABLE(5203),
    SUBSCRIPTION_VERIFICATION_INITIATION_FAILED(5204),
    SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE(5205),
    NON_BLOCKING_REQUEST_NOT_SUPPORTED(5206),
    EXTERNAL_OBJECT_NOT_REACHABLE(6003),
    EXTERNAL_OBJECT_NOT_FOUND(6005)
    /*
    *TODO: TS-004 status codes here
    */
    ;
    
    private final BigInteger value;

    private RequestStatusCode(int val) {
        this.value = BigInteger.valueOf(val);
    }

    public BigInteger getValue() {
        return value;
    }
    
    
}
