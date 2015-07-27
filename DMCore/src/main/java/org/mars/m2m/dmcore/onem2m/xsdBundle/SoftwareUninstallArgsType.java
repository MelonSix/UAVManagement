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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for softwareUninstallArgsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="softwareUninstallArgsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UUID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="executionEnvRef" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="anyArg" type="{http://www.onem2m.org/xml/protocols}anyArgType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "softwareUninstallArgsType", propOrder = {
    "uuid",
    "version",
    "executionEnvRef",
    "anyArg"
})
public class SoftwareUninstallArgsType {

    @XmlElement(name = "UUID", required = true)
    protected String uuid;
    @XmlElement(required = true)
    protected String version;
    @XmlElement(required = true)
    protected String executionEnvRef;
    protected List<AnyArgType> anyArg;

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUUID(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the executionEnvRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExecutionEnvRef() {
        return executionEnvRef;
    }

    /**
     * Sets the value of the executionEnvRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExecutionEnvRef(String value) {
        this.executionEnvRef = value;
    }

    /**
     * Gets the value of the anyArg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anyArg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnyArg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnyArgType }
     * 
     * 
     */
    public List<AnyArgType> getAnyArg() {
        if (anyArg == null) {
            anyArg = new ArrayList<AnyArgType>();
        }
        return this.anyArg;
    }

}
