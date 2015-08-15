/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.model;

import java.util.ArrayList;

/**
 *This class is the model for the array of {@link DiscoveryDetails} data from an endpoint
 * @author BRIGHTER AGYEMANG
 */
public class DiscoveryList 
{
    private ArrayList<DiscoveryDetails> data;

    public DiscoveryList() {
        this.data = new ArrayList<>();
    }

    public ArrayList<DiscoveryDetails> getData() {
        return data;
    }

    public void setData(ArrayList<DiscoveryDetails> data) {
        this.data = data;
    }
    
}
