package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Signature")
@XmlType(namespace="http://www.cdisc.org/ns/odm/v1.3")
public class Signature {

	String userRef;
	String locationRef;
	String signatureRef;
	String dateTimeStamp;
	
	@XmlElement(name = "UserRef", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getUserRef() {
		return userRef;
	}
	public void setUserRef(String userRef) {
		this.userRef = userRef;
	}
	@XmlElement(name = "LocationRef", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getLocationRef() {
		return locationRef;
	}
	public void setLocationRef(String locationRef) {
		this.locationRef = locationRef;
	}
	@XmlElement(name = "SignatureRef", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getSignatureRef() {
		return signatureRef;
	}
	public void setSignatureRef(String signatureRef) {
		this.signatureRef = signatureRef;
	}
	@XmlElement(name = "DateTimeStamp", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getDateTimeStamp() {
		return dateTimeStamp;
	}
	public void setDateTimeStamp(String dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}
}
