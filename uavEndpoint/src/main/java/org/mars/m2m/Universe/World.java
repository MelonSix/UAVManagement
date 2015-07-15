/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.Universe;

import org.mars.m2m.uavendpoint.UavType.MilitaryUAV;

/**
 *
 * @author AG BRIGHTER
 */
public class World {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * Military UAV
         */
        MilitaryUAV milUav = new MilitaryUAV();
        
        Thread milThread = new Thread(milUav);
        milThread.start();        
    }
    
}
