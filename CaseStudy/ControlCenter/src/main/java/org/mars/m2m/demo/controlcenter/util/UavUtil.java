/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.TreeMap;
import org.mars.m2m.demo.controlcenter.model.ReportedLwM2MClient;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class UavUtil 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UavUtil.class);

    public UavUtil() {
    }
    
    /**
     * Identifies an instance of a UAV and the devices/objects mounted on the UAV.
     * This method base on the device identifier nomenclature e.g. scout1-xxxx-xxxx-xxxx... to identify a UAV instance.
     * It splits the first part of the ID, which denotes the UAV instance that a device is mounted onto, and uses that to group all
     * devices with the same UAV instance as devices mounted on the UAV instance.
     * @param connectedUavDevices Connected devices reported to the control center.
     * @return Mapping of a group of devices to a particular UAV instance
     */
    public TreeMap<String, ArrayList<ReportedLwM2MClient>> getUAVAndOnboardDevices(ArrayList<ReportedLwM2MClient> connectedUavDevices) {
        TreeMap<String, ArrayList<ReportedLwM2MClient>> uavWithOnboardDevices;
        uavWithOnboardDevices = new TreeMap<>();
        for (ReportedLwM2MClient reportedDevice : connectedUavDevices) {
            String uavLabel = reportedDevice.getEndpoint().split("-")[0];
            if (uavWithOnboardDevices.containsKey(uavLabel)) {
                ArrayList<ReportedLwM2MClient> devices = uavWithOnboardDevices.get(uavLabel);
                devices.add(reportedDevice);
            } else {
                ArrayList<ReportedLwM2MClient> devices = new ArrayList<>();
                devices.add(reportedDevice);
                uavWithOnboardDevices.put(uavLabel, devices);
            }
        }
        return uavWithOnboardDevices;
    }

    /**
     * This method is used for categorization purposes. It groups the reported devices into UAV kinds
     * @param connectedDevices The reported devices/LwM2M clients
     * @param uavType The UAV kind to be used for the categorization (e.g. scout/attacker)
     * @return The list of connected devices which are of the specified <code>uavType</code>
     */
    public ArrayList<ReportedLwM2MClient> getConnectedDevicesByCategory(ArrayList<ReportedLwM2MClient> connectedDevices, String uavType) {
        ArrayList<ReportedLwM2MClient> conDevices;
        conDevices = new ArrayList<>();
        for (ReportedLwM2MClient scoutDevice : connectedDevices) {
            String endpointname = scoutDevice.getEndpoint();
            if (endpointname.toLowerCase().contains(uavType)) {
                conDevices.add(scoutDevice);
            }
        }
        return conDevices;
    }
    
}
