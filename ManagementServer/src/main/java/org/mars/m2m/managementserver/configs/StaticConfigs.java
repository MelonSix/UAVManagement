/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managementserver.configs;

/**
 * Keeps all static configuration details of the management server
 * @author AG BRIGHTER
 */
public class StaticConfigs 
{
    /**
     * The address of where a new client should be reported to
     * <br/>
     * E.g. Management Adapter
     */
    public static final String DEVICE_REPORTING_URL = "http://localhost:8070/ma/mgmtAdapter/processDeviceReporting";
}
