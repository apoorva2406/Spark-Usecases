package com.wincere.analytics.xobject.audit;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="ODM", namespace = "http://www.cdisc.org/ns/odm/v1.3")
public class ODM {

	@XmlAttribute(name="ODMVersion")
	public String ODMVersion;
	@XmlAttribute(name="FileType")
	public String FileType;
	@XmlAttribute(name="FileOID")
	public String FileOID;
	@XmlAttribute(name="CreationDateTime")
	public String CreationDateTime;
	
	@XmlElement(name = "ClinicalData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	//@XmlElement(name = "ClinicalData")
	public ArrayList<ClinicalData> clinicalData;
}
