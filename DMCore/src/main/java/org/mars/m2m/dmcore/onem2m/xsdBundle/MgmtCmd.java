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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}regularResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cmdType" type="{http://www.onem2m.org/xml/protocols}cmdType"/&gt;
 *         &lt;element name="execReqArgs" type="{http://www.onem2m.org/xml/protocols}execReqArgsListType" minOccurs="0"/&gt;
 *         &lt;element name="execEnable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="execTarget" type="{http://www.onem2m.org/xml/protocols}nodeID"/&gt;
 *         &lt;element name="execMode" type="{http://www.onem2m.org/xml/protocols}execModeType" minOccurs="0"/&gt;
 *         &lt;element name="execFrequency" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/&gt;
 *         &lt;element name="execDelay" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/&gt;
 *         &lt;element name="execNumber" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="childResource" type="{http://www.onem2m.org/xml/protocols}childResourceRef" maxOccurs="unbounded"/&gt;
 *           &lt;choice maxOccurs="unbounded"&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}execInstance"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}subscription"/&gt;
 *           &lt;/choice&gt;
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
    "description",
    "cmdType",
    "execReqArgs",
    "execEnable",
    "execTarget",
    "execMode",
    "execFrequency",
    "execDelay",
    "execNumber",
    "childResource",
    "execInstanceOrSubscription"
})
@XmlRootElement(name = "mgmtCmd")
public class MgmtCmd
    extends RegularResource
{

    protected String description;
    @XmlElement(required = true)
    protected BigInteger cmdType;
    protected ExecReqArgsListType execReqArgs;
    protected boolean execEnable;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String execTarget;
    protected BigInteger execMode;
    protected Duration execFrequency;
    protected Duration execDelay;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger execNumber;
    protected List<ChildResourceRef> childResource;
    @XmlElements({
        @XmlElement(name = "execInstance", namespace = "http://www.onem2m.org/xml/protocols", type = ExecInstance.class),
        @XmlElement(name = "subscription", namespace = "http://www.onem2m.org/xml/protocols", type = Subscription.class)
    })
    protected List<RegularResource> execInstanceOrSubscription;

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

    /**
     * Gets the value of the cmdType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCmdType() {
        return cmdType;
    }

    /**
     * Sets the value of the cmdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCmdType(BigInteger value) {
        this.cmdType = value;
    }

    /**
     * Gets the value of the execReqArgs property.
     * 
     * @return
     *     possible object is
     *     {@link ExecReqArgsListType }
     *     
     */
    public ExecReqArgsListType getExecReqArgs() {
        return execReqArgs;
    }

    /**
     * Sets the value of the execReqArgs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecReqArgsListType }
     *     
     */
    public void setExecReqArgs(ExecReqArgsListType value) {
        this.execReqArgs = value;
    }

    /**
     * Gets the value of the execEnable property.
     * 
     */
    public boolean isExecEnable() {
        return execEnable;
    }

    /**
     * Sets the value of the execEnable property.
     * 
     */
    public void setExecEnable(boolean value) {
        this.execEnable = value;
    }

    /**
     * Gets the value of the execTarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExecTarget() {
        return execTarget;
    }

    /**
     * Sets the value of the execTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExecTarget(String value) {
        this.execTarget = value;
    }

    /**
     * Gets the value of the execMode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExecMode() {
        return execMode;
    }

    /**
     * Sets the value of the execMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExecMode(BigInteger value) {
        this.execMode = value;
    }

    /**
     * Gets the value of the execFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getExecFrequency() {
        return execFrequency;
    }

    /**
     * Sets the value of the execFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setExecFrequency(Duration value) {
        this.execFrequency = value;
    }

    /**
     * Gets the value of the execDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getExecDelay() {
        return execDelay;
    }

    /**
     * Sets the value of the execDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setExecDelay(Duration value) {
        this.execDelay = value;
    }

    /**
     * Gets the value of the execNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExecNumber() {
        return execNumber;
    }

    /**
     * Sets the value of the execNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExecNumber(BigInteger value) {
        this.execNumber = value;
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
     * Gets the value of the execInstanceOrSubscription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the execInstanceOrSubscription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExecInstanceOrSubscription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecInstance }
     * {@link Subscription }
     * 
     * 
     */
    public List<RegularResource> getExecInstanceOrSubscription() {
        if (execInstanceOrSubscription == null) {
            execInstanceOrSubscription = new ArrayList<RegularResource>();
        }
        return this.execInstanceOrSubscription;
    }

}
