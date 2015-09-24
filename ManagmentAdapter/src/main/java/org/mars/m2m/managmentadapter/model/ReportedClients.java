/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

import java.util.ArrayList;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;

/**
 * This class keeps record of all active clients
 * @author AG BRIGHTER
 */
public class ReportedClients 
{
    private static final ArrayList<ReportedLwM2MClient> clients = new ArrayList<>();

    public ReportedClients() {
    }

    public static ArrayList<ReportedLwM2MClient> getClients() {
        return clients;
    }
    
}
