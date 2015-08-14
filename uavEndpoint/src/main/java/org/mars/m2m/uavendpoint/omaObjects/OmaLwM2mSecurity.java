/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.omaObjects;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.server.bootstrap.SecurityMode;
import org.eclipse.leshan.server.bootstrap.SmsSecurityMode;

/**
 *
 * @author AG BRIGHTER
 */
public class OmaLwM2mSecurity extends BaseInstanceEnabler
{
    private String uri;
    private boolean bootstrapServer;
    private SecurityMode securityMode;
    private byte[] publicKeyOrId;
    private byte[] serverPublicKeyOrId;
    private byte[] secretKey;
    private SmsSecurityMode smsSecurityMode;
    private byte[] smsBindingKeyParam;
    private byte[] smsBindingKeySecret;
    private String serverSmsNumber;
    private Integer serverId;
    private int clientOldOffTime;
    
    public OmaLwM2mSecurity() {
    }

    public String getUri() {
        return uri;
    }

    public OmaLwM2mSecurity setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public boolean isBootstrapServer() {
        return bootstrapServer;
    }

    public OmaLwM2mSecurity setBootstrapServer(boolean bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
        return this;
    }

    public SecurityMode getSecurityMode() {
        return securityMode;
    }

    public OmaLwM2mSecurity setSecurityMode(SecurityMode securityMode) {
        this.securityMode = securityMode;
        return this;
    }

    public byte[] getPublicKeyOrId() {
        return publicKeyOrId;
    }

    public OmaLwM2mSecurity setPublicKeyOrId(byte[] publicKeyOrId) {
        this.publicKeyOrId = publicKeyOrId;
        return this;
    }

    public byte[] getServerPublicKeyOrId() {
        return serverPublicKeyOrId;
    }

    public OmaLwM2mSecurity setServerPublicKeyOrId(byte[] serverPublicKeyOrId) {
        this.serverPublicKeyOrId = serverPublicKeyOrId;
        return this;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public OmaLwM2mSecurity setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public SmsSecurityMode getSmsSecurityMode() {
        return smsSecurityMode;
    }

    public OmaLwM2mSecurity setSmsSecurityMode(SmsSecurityMode smsSecurityMode) {
        this.smsSecurityMode = smsSecurityMode;
        return this;
    }

    public byte[] getSmsBindingKeyParam() {
        return smsBindingKeyParam;
    }

    public OmaLwM2mSecurity setSmsBindingKeyParam(byte[] smsBindingKeyParam) {
        this.smsBindingKeyParam = smsBindingKeyParam;
        return this;
    }

    public byte[] getSmsBindingKeySecret() {
        return smsBindingKeySecret;
    }

    public OmaLwM2mSecurity setSmsBindingKeySecret(byte[] smsBindingKeySecret) {
        this.smsBindingKeySecret = smsBindingKeySecret;
        return this;
    }

    public String getServerSmsNumber() {
        return serverSmsNumber;
    }

    public OmaLwM2mSecurity setServerSmsNumber(String serverSmsNumber) {
        this.serverSmsNumber = serverSmsNumber;
        return this;
    }

    public Integer getServerId() {
        return serverId;
    }

    public OmaLwM2mSecurity setServerId(Integer serverId) {
        this.serverId = serverId;
        return this;
    }

    public int getClientOldOffTime() {
        return clientOldOffTime;
    }

    public OmaLwM2mSecurity setClientOldOffTime(int clientOldOffTime) {
        this.clientOldOffTime = clientOldOffTime;
        return this;
    }
}
