//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.28 at 11:15:39 AM CST 
//


package org.mars.m2m.dmcore.onem2m.xsdBundle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for announcedMgmtResource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="announcedMgmtResource"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}announcedResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="mgmtDefinition" type="{http://www.onem2m.org/xml/protocols}mgmtDefinition"/&gt;
 *         &lt;element name="objectIDs" type="{http://www.onem2m.org/xml/protocols}listOfURIs" minOccurs="0"/&gt;
 *         &lt;element name="objectPaths" type="{http://www.onem2m.org/xml/protocols}listOfURIs" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "announcedMgmtResource", propOrder = {
    "mgmtDefinition",
    "objectIDs",
    "objectPaths",
    "description"
})
@XmlSeeAlso({
    EventLogAnnc.class,
    RebootAnnc.class,
    DeviceCapabilityAnnc.class,
    DeviceInfoAnnc.class,
    SoftwareAnnc.class,
    FirmwareAnnc.class,
    MemoryAnnc.class,
    BatteryAnnc.class,
    AreaNwkInfoAnnc.class,
    AreaNwkDeviceInfoAnnc.class
})
public class AnnouncedMgmtResource
    extends AnnouncedResource
{

    @XmlElement(required = true)
    protected BigInteger mgmtDefinition;
    @XmlList
    @XmlSchemaType(name = "anySimpleType")
    protected List<String> objectIDs;
    @XmlList
    @XmlSchemaType(name = "anySimpleType")
    protected List<String> objectPaths;
    protected String description;

    /**
     * Gets the value of the mgmtDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMgmtDefinition() {
        return mgmtDefinition;
    }

    /**
     * Sets the value of the mgmtDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMgmtDefinition(BigInteger value) {
        this.mgmtDefinition = value;
    }

    /**
     * Gets the value of the objectIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getObjectIDs() {
        if (objectIDs == null) {
            objectIDs = new ArrayList<String>();
        }
        return this.objectIDs;
    }

    /**
     * Gets the value of the objectPaths property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectPaths property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectPaths().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getObjectPaths() {
        if (objectPaths == null) {
            objectPaths = new ArrayList<String>();
        }
        return this.objectPaths;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}