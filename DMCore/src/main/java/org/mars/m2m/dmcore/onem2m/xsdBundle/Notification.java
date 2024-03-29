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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for notification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="notification"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="notificationEvent" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="representation" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *                   &lt;element name="resourceStatus" type="{http://www.onem2m.org/xml/protocols}resourceStatus" minOccurs="0"/&gt;
 *                   &lt;element name="operationMonitor" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="operation" type="{http://www.onem2m.org/xml/protocols}operation" minOccurs="0"/&gt;
 *                             &lt;element name="originator" type="{http://www.onem2m.org/xml/protocols}ID" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="verificationRequest" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="subscriptionDeletion" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="subscriptionReference" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="creator" type="{http://www.onem2m.org/xml/protocols}ID" minOccurs="0"/&gt;
 *         &lt;element name="notificationForwardingURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notification", propOrder = {
    "notificationEvent",
    "verificationRequest",
    "subscriptionDeletion",
    "subscriptionReference",
    "creator",
    "notificationForwardingURI"
})
public class Notification {

    protected Notification.NotificationEvent notificationEvent;
    protected Boolean verificationRequest;
    protected Boolean subscriptionDeletion;
    @XmlSchemaType(name = "anyURI")
    protected String subscriptionReference;
    @XmlSchemaType(name = "anyURI")
    protected String creator;
    @XmlSchemaType(name = "anyURI")
    protected String notificationForwardingURI;

    /**
     * Gets the value of the notificationEvent property.
     * 
     * @return
     *     possible object is
     *     {@link Notification.NotificationEvent }
     *     
     */
    public Notification.NotificationEvent getNotificationEvent() {
        return notificationEvent;
    }

    /**
     * Sets the value of the notificationEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notification.NotificationEvent }
     *     
     */
    public void setNotificationEvent(Notification.NotificationEvent value) {
        this.notificationEvent = value;
    }

    /**
     * Gets the value of the verificationRequest property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVerificationRequest() {
        return verificationRequest;
    }

    /**
     * Sets the value of the verificationRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVerificationRequest(Boolean value) {
        this.verificationRequest = value;
    }

    /**
     * Gets the value of the subscriptionDeletion property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSubscriptionDeletion() {
        return subscriptionDeletion;
    }

    /**
     * Sets the value of the subscriptionDeletion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubscriptionDeletion(Boolean value) {
        this.subscriptionDeletion = value;
    }

    /**
     * Gets the value of the subscriptionReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscriptionReference() {
        return subscriptionReference;
    }

    /**
     * Sets the value of the subscriptionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubscriptionReference(String value) {
        this.subscriptionReference = value;
    }

    /**
     * Gets the value of the creator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreator(String value) {
        this.creator = value;
    }

    /**
     * Gets the value of the notificationForwardingURI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationForwardingURI() {
        return notificationForwardingURI;
    }

    /**
     * Sets the value of the notificationForwardingURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationForwardingURI(String value) {
        this.notificationForwardingURI = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="representation" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
     *         &lt;element name="resourceStatus" type="{http://www.onem2m.org/xml/protocols}resourceStatus" minOccurs="0"/&gt;
     *         &lt;element name="operationMonitor" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="operation" type="{http://www.onem2m.org/xml/protocols}operation" minOccurs="0"/&gt;
     *                   &lt;element name="originator" type="{http://www.onem2m.org/xml/protocols}ID" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "representation",
        "resourceStatus",
        "operationMonitor"
    })
    public static class NotificationEvent {

        protected Object representation;
        protected BigInteger resourceStatus;
        protected Notification.NotificationEvent.OperationMonitor operationMonitor;

        /**
         * Gets the value of the representation property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getRepresentation() {
            return representation;
        }

        /**
         * Sets the value of the representation property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setRepresentation(Object value) {
            this.representation = value;
        }

        /**
         * Gets the value of the resourceStatus property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getResourceStatus() {
            return resourceStatus;
        }

        /**
         * Sets the value of the resourceStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setResourceStatus(BigInteger value) {
            this.resourceStatus = value;
        }

        /**
         * Gets the value of the operationMonitor property.
         * 
         * @return
         *     possible object is
         *     {@link Notification.NotificationEvent.OperationMonitor }
         *     
         */
        public Notification.NotificationEvent.OperationMonitor getOperationMonitor() {
            return operationMonitor;
        }

        /**
         * Sets the value of the operationMonitor property.
         * 
         * @param value
         *     allowed object is
         *     {@link Notification.NotificationEvent.OperationMonitor }
         *     
         */
        public void setOperationMonitor(Notification.NotificationEvent.OperationMonitor value) {
            this.operationMonitor = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="operation" type="{http://www.onem2m.org/xml/protocols}operation" minOccurs="0"/&gt;
         *         &lt;element name="originator" type="{http://www.onem2m.org/xml/protocols}ID" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "operation",
            "originator"
        })
        public static class OperationMonitor {

            protected BigInteger operation;
            @XmlSchemaType(name = "anyURI")
            protected String originator;

            /**
             * Gets the value of the operation property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getOperation() {
                return operation;
            }

            /**
             * Sets the value of the operation property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setOperation(BigInteger value) {
                this.operation = value;
            }

            /**
             * Gets the value of the originator property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOriginator() {
                return originator;
            }

            /**
             * Sets the value of the originator property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOriginator(String value) {
                this.originator = value;
            }

        }

    }

}
