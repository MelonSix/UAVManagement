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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="targetNetwork" type="{http://www.onem2m.org/xml/protocols}listOfM2MID"/&gt;
 *         &lt;element name="minReqVolume" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *         &lt;element name="backOffParameters" type="{http://www.onem2m.org/xml/protocols}backOffParameters"/&gt;
 *         &lt;element name="otherConditions" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="mgmtLink" type="{http://www.onem2m.org/xml/protocols}mgmtLinkRef"/&gt;
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
    "targetNetwork",
    "minReqVolume",
    "backOffParameters",
    "otherConditions",
    "mgmtLink"
})
@XmlRootElement(name = "cmdhNwAccessRule")
public class CmdhNwAccessRule
    extends MgmtResource
{

    @XmlList
    @XmlElement(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected List<String> targetNetwork;
    @XmlElement(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger minReqVolume;
    @XmlList
    @XmlElement(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected List<BigInteger> backOffParameters;
    @XmlElement(required = true)
    protected Object otherConditions;
    @XmlElement(required = true)
    protected MgmtLinkRef mgmtLink;

    /**
     * Gets the value of the targetNetwork property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetNetwork property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetNetwork().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTargetNetwork() {
        if (targetNetwork == null) {
            targetNetwork = new ArrayList<String>();
        }
        return this.targetNetwork;
    }

    /**
     * Gets the value of the minReqVolume property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinReqVolume() {
        return minReqVolume;
    }

    /**
     * Sets the value of the minReqVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinReqVolume(BigInteger value) {
        this.minReqVolume = value;
    }

    /**
     * Gets the value of the backOffParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the backOffParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBackOffParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getBackOffParameters() {
        if (backOffParameters == null) {
            backOffParameters = new ArrayList<BigInteger>();
        }
        return this.backOffParameters;
    }

    /**
     * Gets the value of the otherConditions property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getOtherConditions() {
        return otherConditions;
    }

    /**
     * Sets the value of the otherConditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setOtherConditions(Object value) {
        this.otherConditions = value;
    }

    /**
     * Gets the value of the mgmtLink property.
     * 
     * @return
     *     possible object is
     *     {@link MgmtLinkRef }
     *     
     */
    public MgmtLinkRef getMgmtLink() {
        return mgmtLink;
    }

    /**
     * Sets the value of the mgmtLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link MgmtLinkRef }
     *     
     */
    public void setMgmtLink(MgmtLinkRef value) {
        this.mgmtLink = value;
    }

}
