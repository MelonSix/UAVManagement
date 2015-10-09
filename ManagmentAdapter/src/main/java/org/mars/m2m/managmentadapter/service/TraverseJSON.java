/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.managmentadapter.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;

/**
 *
 * @author AG BRIGHTER
 */
public class TraverseJSON 
{
    ServiceUtils serviceUtils;
    
    public TraverseJSON() {
        this.serviceUtils = new ServiceUtils();
    }
    
    public void parseJsonToOneM2M(JSONObject jsonObj)
    {
        for(Object key : jsonObj.keySet())
        {
            Container container;
            ContentInstance contentInstance;
            String keyStr = (String)key;
            Object keyvalue = jsonObj.get(keyStr);
             
            //for nested objects iteration
            if (keyvalue instanceof JSONObject)//only one JSONObject
            {
                parseJsonToOneM2M((JSONObject)keyvalue);
            }
            else//when it's an array of one or more JSONObjects
                if(keyvalue instanceof JSONArray && ((JSONArray)keyvalue).get(0) instanceof JSONObject)
                {
                    System.out.println("------------------------------");
                    System.out.println("key: "+ keyStr);
                    parseJsonToOneM2M((JSONArray) keyvalue);  
                    System.out.println("------------------------------");
                }
            else //when it's a value or an array of primitives but not JSONObjects withing array
                {
                    //Print key and value
                    System.out.println("key: "+ keyStr + " value: " + keyvalue);
                }
        }
    }
    
    public void parseJsonToOneM2M(JSONArray jsonArr)
    {
        for(int i=0; i<jsonArr.length(); i++)
        {
            Object jsonObj = jsonArr.get(i);
            //for nested objects iteration
            if (jsonObj instanceof JSONObject)
            {
                parseJsonToOneM2M((JSONObject)jsonObj);
            }
            else
                if(jsonObj instanceof JSONArray)
                { 
                    parseJsonToOneM2M((JSONArray) jsonObj);
                }
        }
    } 
    
    public static void main(String [] args) throws IOException, JAXBException
    {
        InputStream restrm = TraverseJSON.class.getResourceAsStream("/loadedClients.txt");
        StringWriter writer = new StringWriter();
        IOUtils.copy(restrm, writer, "UTF-8");
        String jsontxt = writer.toString();
        TraverseJSON traverse = new TraverseJSON();
        traverse.parseJsonToOneM2M(new JSONArray(jsontxt));
        
//        JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
//        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//for debugging purposes
//        for(Container container : containers)
//            jaxbMarshaller.marshal(container, System.out);//for debugging purposes
    }
}
