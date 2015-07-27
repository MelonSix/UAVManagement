//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.28 at 11:15:39 AM CST 
//


package org.mars.m2m.dmcore.onem2m.xsdBundle;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for operationResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="operationResult"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="content" type="{http://www.onem2m.org/xml/protocols}primitiveContent" minOccurs="0"/&gt;
 *         &lt;element name="eventCategory" type="{http://www.onem2m.org/xml/protocols}eventCat" minOccurs="0"/&gt;
 *         &lt;element name="from" type="{http://www.onem2m.org/xml/protocols}ID" minOccurs="0"/&gt;
 *         &lt;element name="originatingTimestamp" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="requestIdentifier" type="{http://www.onem2m.org/xml/protocols}requestID"/&gt;
 *         &lt;element name="resultExpirationTimestamp" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *         &lt;element name="to" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="responseStatusCode" type="{http://www.onem2m.org/xml/protocols}responseStatusCode"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operationResult", propOrder = {
    "content",
    "eventCategory",
    "from",
    "originatingTimestamp",
    "requestIdentifier",
    "resultExpirationTimestamp",
    "to",
    "responseStatusCode"
})
public class OperationResult {

    protected PrimitiveContent content;
    @XmlSchemaType(name = "anySimpleType")
    protected String eventCategory;
    @XmlSchemaType(name = "anyURI")
    protected String from;
    protected String originatingTimestamp;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String requestIdentifier;
    protected String resultExpirationTimestamp;
    @XmlSchemaType(name = "anyURI")
    protected String to;
    @XmlElement(required = true)
    protected BigInteger responseStatusCode;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link PrimitiveContent }
     *     
     */
    public PrimitiveContent getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimitiveContent }
     *     
     */
    public void setContent(PrimitiveContent value) {
        this.content = value;
    }

    /**
     * Gets the value of the eventCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventCategory() {
        return eventCategory;
    }

    /**
     * Sets the value of the eventCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventCategory(String value) {
        this.eventCategory = value;
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrom(String value) {
        this.from = value;
    }

    /**
     * Gets the value of the originatingTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginatingTimestamp() {
        return originatingTimestamp;
    }

    /**
     * Sets the value of the originatingTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginatingTimestamp(String value) {
        this.originatingTimestamp = value;
    }

    /**
     * Gets the value of the requestIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    /**
     * Sets the value of the requestIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestIdentifier(String value) {
        this.requestIdentifier = value;
    }

    /**
     * Gets the value of the resultExpirationTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultExpirationTimestamp() {
        return resultExpirationTimestamp;
    }

    /**
     * Sets the value of the resultExpirationTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultExpirationTimestamp(String value) {
        this.resultExpirationTimestamp = value;
    }

    /**
     * Gets the value of the to property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTo(String value) {
        this.to = value;
    }

    /**
     * Gets the value of the responseStatusCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getResponseStatusCode() {
        return responseStatusCode;
    }

    /**
     * Sets the value of the responseStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setResponseStatusCode(BigInteger value) {
        this.responseStatusCode = value;
    }

}
