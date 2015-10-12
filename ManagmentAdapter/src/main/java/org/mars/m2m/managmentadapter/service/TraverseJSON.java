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
    
    public ArrayList<Container> parseJsonToOneM2M(JSONObject jsonObj)
    {
        ArrayList<Container> containers = new ArrayList<>();
        for(Object key : jsonObj.keySet())
        {
            String keyStr = (String)key;
            Object keyvalue = jsonObj.get(keyStr);
             
//                    System.out.println("key: "+ keyStr + " value: " + keyvalue);
            //for nested objects iteration
            if (keyvalue instanceof JSONObject)//only one JSONObject
            {
                ContentInstance ci = serviceUtils.getContentInstance(keyStr);
                Container co = serviceUtils.getContainer(ci);
                ArrayList<Container> innerCtnr = parseJsonToOneM2M((JSONObject)keyvalue); 
                co.getContentInstanceOrContainerOrSubscription().addAll(innerCtnr);
                containers.add(co);
            }
            else//when it's an array of one or more JSONObjects
                if(keyvalue instanceof JSONArray && ((JSONArray)keyvalue).get(0) instanceof JSONObject)
                {
                    ContentInstance ci = serviceUtils.getContentInstance(keyStr);
                    Container co = serviceUtils.getContainer(ci);
                    //System.out.println("------------------------------");
                    //System.out.println("key: "+ keyStr);
                    ArrayList<Container> arrContainers = parseJsonToOneM2M((JSONArray) keyvalue);  
                    co.getContentInstanceOrContainerOrSubscription().addAll(arrContainers);
                    //System.out.println("------------------------------");
                    containers.add(co);
                }
            else //when it's a value or an array of primitives but not JSONObjects withing array
                {
                    ContentInstance ci = serviceUtils.getContentInstance(new JSONObject().put(keyStr, keyvalue).toString());
                    Container co = serviceUtils.getContainer(ci);
                    containers.add(co);
                    //Print key and value
                    //System.out.println("key: "+ keyStr + " value: " + keyvalue);
                }
        }
        return containers;
    }
    
    public ArrayList<Container> parseJsonToOneM2M(JSONArray jsonArr)
    {
        ArrayList<Container> containers_arr = new ArrayList<>();
        for(int i=0; i<jsonArr.length(); i++)
        {
            ArrayList<Container> containers = new ArrayList<>();
            Object jsonObj = jsonArr.get(i);
            //for nested objects iteration
            if (jsonObj instanceof JSONObject)
            {
                ArrayList<Container> _co = parseJsonToOneM2M((JSONObject)jsonObj);
                containers.addAll(_co);
            }
            else
                if(jsonObj instanceof JSONArray)
                { 
                    parseJsonToOneM2M((JSONArray) jsonObj);
                }
            Container ctnr = new Container();
            ctnr.getContentInstanceOrContainerOrSubscription().addAll(containers);
            containers_arr.add(ctnr);
        }
        return containers_arr;
    } 
    
    /**
     * FOR DEBUGGING PURPOSES - TO BE REMOVED
     * @param args
     * @throws IOException
     * @throws JAXBException 
     */
    public static void main(String [] args) throws IOException, JAXBException
    {
        InputStream restrm = TraverseJSON.class.getResourceAsStream("/loadedClients.txt");
        StringWriter writer = new StringWriter();
        IOUtils.copy(restrm, writer, "UTF-8");
        String jsontxt = writer.toString();
        TraverseJSON traverse = new TraverseJSON();
        ArrayList<Container> clientCtnrs = traverse.parseJsonToOneM2M(new JSONArray(jsontxt));
        ServiceUtils utils = new ServiceUtils();
        Container container = utils.getContainer(clientCtnrs);
        
        JAXBContext jaxbContext = JAXBContext.newInstance("org.mars.m2m.dmcore.onem2m.xsdBundle");
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//for debugging purposes
        jaxbMarshaller.marshal(container, System.out);//for debugging purposes
    }
}
