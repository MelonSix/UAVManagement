/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AG BRIGHTER
 */
public class RegisteredClientDataList 
{
    private Map<String, RegisteredClientData> registeredList;

    public RegisteredClientDataList() {
        registeredList = new HashMap<>();
    }

    public Map<String, RegisteredClientData> getRegisteredList() {
        return registeredList;
    }
        
}
