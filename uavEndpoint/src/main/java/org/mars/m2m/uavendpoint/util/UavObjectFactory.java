/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.util;

import com.google.gson.Gson;
import org.mars.m2m.Devices.AltitudeSensor;
import org.mars.m2m.Devices.MissileDispatcher;
import org.mars.m2m.Devices.TemperatureSensor;
import org.mars.m2m.Devices.ThreatSensor;
import org.mars.m2m.Devices.UAVmanager;
import org.mars.m2m.uavendpoint.Configuration.ThreatType;
import org.mars.m2m.uavendpoint.Model.MinimumBoundingRectangle;

/**
 *
 * @author AG BRIGHTER
 */
public class UavObjectFactory {

    /**
     *
     * @return 
     */
    public AltitudeSensor createAltitudeSensor() {
        return new AltitudeSensor((float) (Math.random() * 10), (float) (Math.random() * 8));
    }
    
    
    public MissileDispatcher createMissileDispatcher()
    {
        return new MissileDispatcher((int) (Math.random()*10), (int) (int) (Math.random()*10),
                (int) (Math.random()*100), (int) (Math.random()*100));
    }

    /**
     *
     * @return 
     */
    public TemperatureSensor createTemperatureSensor() {
        return new TemperatureSensor((float) (Math.random() * 100 / 2), 
                (float) (Math.random() * 6), (float) (Math.random() * 10), (float) (Math.random() * 100));
    }

    public ThreatSensor createThreatSensor() {
        MinimumBoundingRectangle mbr = new MinimumBoundingRectangle(System.currentTimeMillis() * 100, 
                System.currentTimeMillis() * 100 / 6, System.currentTimeMillis() * 100 / 3, System.currentTimeMillis() * 100 / 2);
        Gson gson = new Gson();
        return new ThreatSensor(ThreatType.BIOLOGICAL_WEAPON, (int) System.currentTimeMillis() * 100, 
                (int) System.currentTimeMillis() * 160, gson.toJson(mbr), 300, 400);
    }

    public UAVmanager createUAVmanager() {
        String focusModel = "Chengdu Pterodactyl I (Wing Loong)";
        String origin = "China";
        String manufacturer = "Chengdu Aircraft Industry Group - China";
        String initialYearOfService = "2014";
        float length = 9;
        float width = 14; //.00f;
        float height = 2; //.77f;
        float weight_empty = 0; //.00f;
        float weight_mtow = 1100;
        String powerPlant = "1x Conventionally-powered engine driving a three-bladed propeller";
        float maximumSpeed = 174;
        float maximumRange = 5000;
        float serviceCeiling = 16404;
        float rateOfClimb = 0;
        float payloadCapability = 99; //.79f;
        float cruiseSpeed = 300;
        String launchType = "Catapult launch";
        int maximumFlightTime = 86400;
        float wingspan = 100;
        int operatingTemperature_lowest = 5;
        int operatingTemperature_highest = 80;
        return new UAVmanager(focusModel, origin, manufacturer, initialYearOfService, 
                length, width, height, weight_empty, weight_mtow, powerPlant, maximumSpeed, 
                maximumRange, serviceCeiling, rateOfClimb, payloadCapability, cruiseSpeed, 
                launchType, maximumFlightTime, wingspan, operatingTemperature_lowest, operatingTemperature_highest);
    }
}
