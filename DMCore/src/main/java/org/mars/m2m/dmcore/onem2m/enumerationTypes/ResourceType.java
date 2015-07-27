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
public enum ResourceType {
    ACCESS_CONTROL_POLICY(1),
    AE(2),
    CONTAINER(3),
    CONTENT_INSTANCE(4),
    CSE_BASE(5),
    DELIVERY(6),
    EVENT_CONFIG(7),
    EXEC_INSTANCE(8),
    GROUP(9),
    LOCATION_POLICY(10),
    M2M_SERVICE_SUBSCRIPTION_PROFILE(11),
    MGMT_CMD(12),
    MGMT_OBJ(13),
    NODE(14),
    POLLING_CHANNEL(15),
    REMOTE_CSE(16),
    REQUEST(17),
    SCHEDULE(18),
    SERVICE_SUBSCRIBED_APP_RULE(19),
    SERVICE_SUBSCRIBED_NODE(20),
    STATS_COLLECT(21),
    STATS_CONFIG(22),
    SUBSCRIPTION(23),
    ACCESS_CONTROL_POLICY_ANNC(10001),
    AE_ANNC(10002),
    CONTAINER_ANNC(10003),
    CONTENT_INSTANCE_ANNC(10004),
    GROUP_ANNC(10009),
    LOCATION_POLICY_ANNC(10010),
    MGMT_OBJ_ANNC(10013),
    NODE_ANNC(10014),
    REMOTE_CSE_ANNC(10016),
    SCHEDULE_ANNC(10018);
    
    private final BigInteger value;

    private ResourceType(long val) {
        this.value = BigInteger.valueOf(val);
    }

    public BigInteger getValue() {
        return value;
    }
       
}
