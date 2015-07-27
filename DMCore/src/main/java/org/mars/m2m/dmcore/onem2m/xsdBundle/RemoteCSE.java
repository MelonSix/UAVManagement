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
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}announceableResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cseType" type="{http://www.onem2m.org/xml/protocols}cseTypeID" minOccurs="0"/&gt;
 *         &lt;element name="pointOfAccess" type="{http://www.onem2m.org/xml/protocols}pOAList" minOccurs="0"/&gt;
 *         &lt;element name="CSEBase" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="CSE-ID" type="{http://www.onem2m.org/xml/protocols}ID"/&gt;
 *         &lt;element name="M2M-Ext-ID" type="{http://www.onem2m.org/xml/protocols}externalID" minOccurs="0"/&gt;
 *         &lt;element name="Trigger-Recipient-ID" type="{http://www.onem2m.org/xml/protocols}triggerRecipientID" minOccurs="0"/&gt;
 *         &lt;element name="requestReachability" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="nodeLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="childResource" type="{http://www.onem2m.org/xml/protocols}childResourceRef" maxOccurs="unbounded"/&gt;
 *           &lt;choice maxOccurs="unbounded"&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}AE"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}container"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}group"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}accessControlPolicy"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}subscription"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}pollingChannel"/&gt;
 *             &lt;element ref="{http://www.onem2m.org/xml/protocols}schedule"/&gt;
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
    "cseType",
    "pointOfAccess",
    "cseBase",
    "cseid",
    "m2MExtID",
    "triggerRecipientID",
    "requestReachability",
    "nodeLink",
    "childResource",
    "aeOrContainerOrGroup"
})
@XmlRootElement(name = "remoteCSE")
public class RemoteCSE
    extends AnnounceableResource
{

    protected BigInteger cseType;
    @XmlList
    @XmlSchemaType(name = "anySimpleType")
    protected List<String> pointOfAccess;
    @XmlElement(name = "CSEBase", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String cseBase;
    @XmlElement(name = "CSE-ID", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String cseid;
    @XmlElement(name = "M2M-Ext-ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String m2MExtID;
    @XmlElement(name = "Trigger-Recipient-ID")
    @XmlSchemaType(name = "unsignedInt")
    protected Long triggerRecipientID;
    protected boolean requestReachability;
    @XmlSchemaType(name = "anyURI")
    protected String nodeLink;
    protected List<ChildResourceRef> childResource;
    @XmlElements({
        @XmlElement(name = "AE", namespace = "http://www.onem2m.org/xml/protocols", type = AE.class),
        @XmlElement(name = "container", namespace = "http://www.onem2m.org/xml/protocols", type = Container.class),
        @XmlElement(name = "group", namespace = "http://www.onem2m.org/xml/protocols", type = Group.class),
        @XmlElement(name = "accessControlPolicy", namespace = "http://www.onem2m.org/xml/protocols", type = AccessControlPolicy.class),
        @XmlElement(name = "subscription", namespace = "http://www.onem2m.org/xml/protocols", type = Subscription.class),
        @XmlElement(name = "pollingChannel", namespace = "http://www.onem2m.org/xml/protocols", type = PollingChannel.class),
        @XmlElement(name = "schedule", namespace = "http://www.onem2m.org/xml/protocols", type = Schedule.class)
    })
    protected List<Resource> aeOrContainerOrGroup;

    /**
     * Gets the value of the cseType property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCseType() {
        return cseType;
    }

    /**
     * Sets the value of the cseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCseType(BigInteger value) {
        this.cseType = value;
    }

    /**
     * Gets the value of the pointOfAccess property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pointOfAccess property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPointOfAccess().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPointOfAccess() {
        if (pointOfAccess == null) {
            pointOfAccess = new ArrayList<String>();
        }
        return this.pointOfAccess;
    }

    /**
     * Gets the value of the cseBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSEBase() {
        return cseBase;
    }

    /**
     * Sets the value of the cseBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSEBase(String value) {
        this.cseBase = value;
    }

    /**
     * Gets the value of the cseid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSEID() {
        return cseid;
    }

    /**
     * Sets the value of the cseid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSEID(String value) {
        this.cseid = value;
    }

    /**
     * Gets the value of the m2MExtID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getM2MExtID() {
        return m2MExtID;
    }

    /**
     * Sets the value of the m2MExtID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setM2MExtID(String value) {
        this.m2MExtID = value;
    }

    /**
     * Gets the value of the triggerRecipientID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTriggerRecipientID() {
        return triggerRecipientID;
    }

    /**
     * Sets the value of the triggerRecipientID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTriggerRecipientID(Long value) {
        this.triggerRecipientID = value;
    }

    /**
     * Gets the value of the requestReachability property.
     * 
     */
    public boolean isRequestReachability() {
        return requestReachability;
    }

    /**
     * Sets the value of the requestReachability property.
     * 
     */
    public void setRequestReachability(boolean value) {
        this.requestReachability = value;
    }

    /**
     * Gets the value of the nodeLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeLink() {
        return nodeLink;
    }

    /**
     * Sets the value of the nodeLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeLink(String value) {
        this.nodeLink = value;
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
     * Gets the value of the aeOrContainerOrGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aeOrContainerOrGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAEOrContainerOrGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AE }
     * {@link Container }
     * {@link Group }
     * {@link AccessControlPolicy }
     * {@link Subscription }
     * {@link PollingChannel }
     * {@link Schedule }
     * 
     * 
     */
    public List<Resource> getAEOrContainerOrGroup() {
        if (aeOrContainerOrGroup == null) {
            aeOrContainerOrGroup = new ArrayList<Resource>();
        }
        return this.aeOrContainerOrGroup;
    }

}