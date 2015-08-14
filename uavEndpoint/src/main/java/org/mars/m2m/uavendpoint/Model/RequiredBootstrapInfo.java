/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

import org.eclipse.leshan.client.LwM2mClient;

/**
 *
 * @author AG BRIGHTER
 */
public class RequiredBootstrapInfo 
{
    private String endpoint;
    private String serverAddr;
    private int serverPortnum;
    private LwM2mClient lwM2mClient; 

    public RequiredBootstrapInfo() {
        this(null, null, 0, null);
    }
    
    public RequiredBootstrapInfo(String endpoint, String serverAddr, int serverPortnum, LwM2mClient lwM2mClient) {
        this.endpoint = endpoint;
        this.serverAddr = serverAddr;
        this.serverPortnum = serverPortnum;
        this.lwM2mClient = lwM2mClient;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public int getServerPortnum() {
        return serverPortnum;
    }

    public void setServerPortnum(int serverPortnum) {
        this.serverPortnum = serverPortnum;
    }

    public LwM2mClient getLwM2mClient() {
        return lwM2mClient;
    }

    public void setLwM2mClient(LwM2mClient lwM2mClient) {
        this.lwM2mClient = lwM2mClient;
    }

    
    
    
}
