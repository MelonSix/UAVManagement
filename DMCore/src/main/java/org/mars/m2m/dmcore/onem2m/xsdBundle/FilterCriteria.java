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
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for filterCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="filterCriteria"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="createdBefore" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="createdAfter" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="modifiedSince" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="unmodifiedSince" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="stateTagSmaller" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="stateTagBigger" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="expireBefore" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="expireAfter" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="labels" type="{http://www.onem2m.org/xml/protocols}labels" minOccurs="0"/&gt;
 *         &lt;element name="resourceType" type="{http://www.onem2m.org/xml/protocols}resourceType" minOccurs="0"/&gt;
 *         &lt;element name="sizeAbove" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *         &lt;element name="sizeBelow" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="contentType" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="attribute" type="{http://www.onem2m.org/xml/protocols}attribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="filterUsage" type="{http://www.onem2m.org/xml/protocols}filterUsage" minOccurs="0"/&gt;
 *         &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filterCriteria", propOrder = {
    "createdBefore",
    "createdAfter",
    "modifiedSince",
    "unmodifiedSince",
    "stateTagSmaller",
    "stateTagBigger",
    "expireBefore",
    "expireAfter",
    "labels",
    "resourceType",
    "sizeAbove",
    "sizeBelow",
    "contentType",
    "attribute",
    "filterUsage",
    "limit"
})
public class FilterCriteria {

    protected String createdBefore;
    protected String createdAfter;
    protected String modifiedSince;
    protected String unmodifiedSince;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger stateTagSmaller;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger stateTagBigger;
    protected String expireBefore;
    protected String expireAfter;
    @XmlList
    @XmlSchemaType(name = "anySimpleType")
    protected List<String> labels;
    protected BigInteger resourceType;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger sizeAbove;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger sizeBelow;
    protected List<String> contentType;
    protected List<Attribute> attribute;
    protected BigInteger filterUsage;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger limit;

    /**
     * Gets the value of the createdBefore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBefore() {
        return createdBefore;
    }

    /**
     * Sets the value of the createdBefore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBefore(String value) {
        this.createdBefore = value;
    }

    /**
     * Gets the value of the createdAfter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedAfter() {
        return createdAfter;
    }

    /**
     * Sets the value of the createdAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedAfter(String value) {
        this.createdAfter = value;
    }

    /**
     * Gets the value of the modifiedSince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedSince() {
        return modifiedSince;
    }

    /**
     * Sets the value of the modifiedSince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedSince(String value) {
        this.modifiedSince = value;
    }

    /**
     * Gets the value of the unmodifiedSince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnmodifiedSince() {
        return unmodifiedSince;
    }

    /**
     * Sets the value of the unmodifiedSince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnmodifiedSince(String value) {
        this.unmodifiedSince = value;
    }

    /**
     * Gets the value of the stateTagSmaller property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStateTagSmaller() {
        return stateTagSmaller;
    }

    /**
     * Sets the value of the stateTagSmaller property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStateTagSmaller(BigInteger value) {
        this.stateTagSmaller = value;
    }

    /**
     * Gets the value of the stateTagBigger property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStateTagBigger() {
        return stateTagBigger;
    }

    /**
     * Sets the value of the stateTagBigger property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStateTagBigger(BigInteger value) {
        this.stateTagBigger = value;
    }

    /**
     * Gets the value of the expireBefore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpireBefore() {
        return expireBefore;
    }

    /**
     * Sets the value of the expireBefore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpireBefore(String value) {
        this.expireBefore = value;
    }

    /**
     * Gets the value of the expireAfter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpireAfter() {
        return expireAfter;
    }

    /**
     * Sets the value of the expireAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpireAfter(String value) {
        this.expireAfter = value;
    }

    /**
     * Gets the value of the labels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the labels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLabels() {
        if (labels == null) {
            labels = new ArrayList<String>();
        }
        return this.labels;
    }

    /**
     * Gets the value of the resourceType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getResourceType() {
        return resourceType;
    }

    /**
     * Sets the value of the resourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setResourceType(BigInteger value) {
        this.resourceType = value;
    }

    /**
     * Gets the value of the sizeAbove property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSizeAbove() {
        return sizeAbove;
    }

    /**
     * Sets the value of the sizeAbove property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSizeAbove(BigInteger value) {
        this.sizeAbove = value;
    }

    /**
     * Gets the value of the sizeBelow property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSizeBelow() {
        return sizeBelow;
    }

    /**
     * Sets the value of the sizeBelow property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSizeBelow(BigInteger value) {
        this.sizeBelow = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getContentType() {
        if (contentType == null) {
            contentType = new ArrayList<String>();
        }
        return this.contentType;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attribute }
     * 
     * 
     */
    public List<Attribute> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<Attribute>();
        }
        return this.attribute;
    }

    /**
     * Gets the value of the filterUsage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFilterUsage() {
        return filterUsage;
    }

    /**
     * Sets the value of the filterUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFilterUsage(BigInteger value) {
        this.filterUsage = value;
    }

    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLimit(BigInteger value) {
        this.limit = value;
    }

}
