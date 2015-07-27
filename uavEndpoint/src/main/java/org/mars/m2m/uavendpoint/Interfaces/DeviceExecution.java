/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Interfaces;

/**
 *
 * @author AG BRIGHTER
 */
public interface DeviceExecution {
    
    /**
     * Reboot the LWM2M device to restore the device from unexpected firmware failure
     * @param deviceID the id of the device in the UAV
     */
    public void Reboot(int deviceID);
    
    /**
     * Perform factory reset of the LWM2M Device to make the LWM2M Device have the same configuration as at the initial deployment
     * @param deviceID the id of the device in the UAV
     */
    public void FactorReset(int deviceID);
    
    /**
     * Resets the error code of a device
     * @param deviceID the id of the device in the UAV
     */
    public void ResetErrorCode(int deviceID);
    
}
