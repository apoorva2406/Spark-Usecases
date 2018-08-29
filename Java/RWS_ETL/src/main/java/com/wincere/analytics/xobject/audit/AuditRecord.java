package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "AuditRecord")
@XmlType(namespace="http://www.cdisc.org/ns/odm/v1.3")
public class AuditRecord {
	
	UserRef userRef;
	LocationRef locationRef;
	String dateTimeStamp;
	String reasonForChange;	
	String sourceID;	
	
	@XmlElement(name = "UserRef", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public UserRef getUserRef() {
		return userRef;
	}

	public void setUserRef(UserRef userRef) {
		this.userRef = userRef;
	}
	
	@XmlElement(name = "LocationRef", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public LocationRef getLocationRef() {
		return locationRef;
	}

	public void setLocationRef(LocationRef locationRef) {
		this.locationRef = locationRef;
	}
	
	@XmlElement(name = "DateTimeStamp", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getDateTimeStamp() {
		return dateTimeStamp;
	}

	public void setDateTimeStamp(String dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}
	
	@XmlElement(name = "ReasonForChange", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getReasonForChange() {
		return reasonForChange;
	}
	
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}
	
	@XmlElement(name = "SourceID", namespace="http://www.cdisc.org/ns/odm/v1.3")
	public String getSourceID() {
		return sourceID;
	}
	
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}			
}
