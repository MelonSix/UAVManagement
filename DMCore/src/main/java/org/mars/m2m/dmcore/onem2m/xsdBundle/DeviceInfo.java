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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}mgmtResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="deviceLabel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="manufacturer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="model" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="deviceType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fwVersion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="swVersion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="hwVersion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="childResource" type="{http://www.onem2m.org/xml/protocols}childResourceRef" maxOccurs="unbounded"/&gt;
 *           &lt;element ref="{http://www.onem2m.org/xml/protocols}subscription" maxOccurs="unbounded"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "deviceLabel",
    "manufacturer",
    "model",
    "deviceType",
    "fwVersion",
    "swVersion",
    "hwVersion",
    "childResource",
    "subscription"
})
@XmlRootElement(name = "deviceInfo")
public class DeviceInfo
    extends MgmtResource
{

    @XmlElement(required = true)
    protected String deviceLabel;
    @XmlElement(required = true)
    protected String manufacturer;
    @XmlElement(required = true)
    protected String model;
    @XmlElement(required = true)
    protected String deviceType;
    @XmlElement(required = true)
    protected String fwVersion;
    @XmlElement(required = true)
    protected String swVersion;
    @XmlElement(required = true)
    protected String hwVersion;
    protected List<ChildResourceRef> childResource;
    @XmlElement(namespace = "http://www.onem2m.org/xml/protocols")
    protected List<Subscription> subscription;

    /**
     * Gets the value of the deviceLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceLabel() {
        return deviceLabel;
    }

    /**
     * Sets the value of the deviceLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceLabel(String value) {
        this.deviceLabel = value;
    }

    /**
     * Gets the value of the manufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the deviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the value of the deviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceType(String value) {
        this.deviceType = value;
    }

    /**
     * Gets the value of the fwVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFwVersion() {
        return fwVersion;
    }

    /**
     * Sets the value of the fwVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFwVersion(String value) {
        this.fwVersion = value;
    }

    /**
     * Gets the value of the swVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSwVersion() {
        return swVersion;
    }

    /**
     * Sets the value of the swVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSwVersion(String value) {
        this.swVersion = value;
    }

    /**
     * Gets the value of the hwVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHwVersion() {
        return hwVersion;
    }

    /**
     * Sets the value of the hwVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHwVersion(String value) {
        this.hwVersion = value;
    }

    /**
     * Gets the value of the childResource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the childResource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChildResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChildResourceRef }
     * 
     * 
     */
    public List<ChildResourceRef> getChildResource() {
        if (childResource == null) {
            childResource = new ArrayList<ChildResourceRef>();
        }
        return this.childResource;
    }

    /**
     * Gets the value of the subscription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subscription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubscription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subscription }
     * 
     * 
     */
    public List<Subscription> getSubscription() {
        if (subscription == null) {
            subscription = new ArrayList<Subscription>();
        }
        return this.subscription;
    }

}
