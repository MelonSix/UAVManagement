//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.28 at 11:15:39 AM CST 
//


package org.mars.m2m.dmcore.onem2m.xsdBundle;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for announceableResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="announceableResource"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}regularResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="announceTo" type="{http://www.onem2m.org/xml/protocols}listOfURIs" minOccurs="0"/&gt;
 *         &lt;element name="announcedAttribute" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;list itemType="{http://www.w3.org/2001/XMLSchema}token" /&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "announceableResource", propOrder = {
    "announceTo",
    "announcedAttribute"
})
@XmlSeeAlso({
    RemoteCSE.class,
    LocationPolicy.class,
    NodeAnnc.class,
    Node.class,
    AE.class,
    Group.class,
    Container.class,
    MgmtResource.class
})
public class AnnounceableResource
    extends RegularResource
{

    @XmlList
    @XmlSchemaType(name = "anySimpleType")
    protected List<String> announceTo;
    @XmlList
    protected List<String> announcedAttribute;

    /**
     * Gets the value of the announceTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the announceTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnounceTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAnnounceTo() {
        if (announceTo == null) {
            announceTo = new ArrayList<String>();
        }
        return this.announceTo;
    }

    /**
     * Gets the value of the announcedAttribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the announcedAttribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnouncedAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAnnouncedAttribute() {
        if (announcedAttribute == null) {
            announcedAttribute = new ArrayList<String>();
        }
        return this.announcedAttribute;
    }

}
