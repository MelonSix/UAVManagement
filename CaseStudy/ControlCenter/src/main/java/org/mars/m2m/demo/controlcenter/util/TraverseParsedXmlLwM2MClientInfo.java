/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Container;
import org.mars.m2m.dmcore.onem2m.xsdBundle.ContentInstance;
import org.mars.m2m.dmcore.onem2m.xsdBundle.Resource;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.leshan.core.request.BindingMode;
import org.mars.m2m.dmcore.model.ObjectLink;

/**
 * This class is used to process the LwM2M client details that reported to the control center
 * @author AG BRIGHTER
 */
public class TraverseParsedXmlLwM2MClientInfo
{
    //helps in collating object links of an endpoint
    ObjectLink objectLink;
    
    public TraverseParsedXmlLwM2MClientInfo() {
    }
    
    /**
     * Reports all processed LwM2M Clients
     * @param clients_container The <code>Container</code> resource that contains 
     * all the clients and their details
     * @return A list of LwM2M Clients
     */
    public ArrayList<ReportedLwM2MClient> getReportedLwM2MClients(Container clients_container)
    {
        ArrayList<ReportedLwM2MClient> lwM2MClients;
        lwM2MClients = new ArrayList<>();
        for(Resource resource : clients_container.getContentInstanceOrContainerOrSubscription())
        {
            //Level 1 : All connected LwM2M Clients
            if(resource instanceof Container)
            {
                Container container = (Container) resource;
                ReportedLwM2MClient client = new ReportedLwM2MClient();
                evalContainer(container, client);
                lwM2MClients.add(client);
            }
        }
        return lwM2MClients;
    }
    
    /**
     * Evaluates a {@link Container} instance to find all the {@link ContentInstance}
     * object of it
     * @param container
     * @param client 
     */
    private void evalContainer(Container container, ReportedLwM2MClient client)
    {
        for(Resource res : container.getContentInstanceOrContainerOrSubscription())
        {
            if(res instanceof ContentInstance)
            {
                ContentInstance ci = (ContentInstance) res;
                String data = ci.getContent().toString();
                determineClientProperty(data, client);
            }
            else
                if(res instanceof Container)
                {
                    evalContainer((Container) res, client);
                }
        }
    }
    
    /**
     * Gets the extracted value within a {@link ContentInstance} object
     * and uses a regular expression to extract the property name of the JSON
     * data
     * @param data
     * @param client 
     */
    private void determineClientProperty(String data, ReportedLwM2MClient client)
    {
        try 
        {
            //regular expresion for getting property names
            String elementPart = "((\"?([\\D*[^{}]]\\w+)\"?):)";
            String valuePart = "(:.*[^{}])";
            Pattern elPattern = Pattern.compile(elementPart);
            Matcher elMatcher = elPattern.matcher(data);
            Matcher valMatcher = Pattern.compile(valuePart).matcher(data);
            if (elMatcher.find()) {
                String propName = elMatcher.group();
                propName = StringUtils.remove(propName, ":");//cleaning
                propName = StringUtils.remove(propName, "\"");
                if (valMatcher.find()) {
                    String value = valMatcher.group();
                    value = StringUtils.remove(value, ":");//cleaning
                    value = StringUtils.remove(value, "\"");
                    setClientProperty(propName, value, client);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets a property of the client being processed as specified by the propName
     * The {@link ObjectLink} object of a client as built in this method is designed 
     * upon the analysis of the default presentation of a client's details
     * @param propName
     * @param client 
     */
    private void setClientProperty(String propName, String value, ReportedLwM2MClient client)
    {
//        System.out.println(propName+" "+value);
        switch(propName)
        {
            case "endpoint":
                client.setEndpoint(value);
                break;
            case "registrationId":
                client.setRegistrationId(value);
                break;
            case "registrationDate":
                client.setRegistrationDate(value);
                break;
            case "address":
                client.setAddress(value);
                break;
            case "smsNumber":
                client.setSmsNumber(value);
                break;
            case "lwM2MmVersion":
                client.setLwM2MmVersion(value);
                break;
            case "lifeTime":
                client.setLifeTime(Integer.parseInt(value));
                break;
            case "bindingMode":
                client.setBindingMode(BindingMode.valueOf(value));
                break;
            case "rootPath":
                client.setRootPath(value);
                break;
            case "url":
                if(objectLink != null)
                    objectLink.setUrl(value);
                break;
            case "rt":
                if(objectLink != null)
                    objectLink.getAttributes().put(propName, value);
                break;
            case "objectId":
                if(objectLink != null)
                {
                    objectLink.setUrl(value);
                    client.getObjectLinks().add(objectLink);
                    objectLink = null;
                }
                break;
            case "objectInstanceId":
                objectLink = new ObjectLink();
                objectLink.setObjectInstanceId(Integer.parseInt(value));
                break;
            case "secure":
                client.setSecure(Boolean.getBoolean(value));
                break;
            default:
                System.out.println(propName+" "+value);
        }
    }
}
