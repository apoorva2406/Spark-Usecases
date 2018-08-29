package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlAttribute;

public class LocationRef {

	String locationOID;
	
	@XmlAttribute(name = "LocationOID")
	public String getLocationOID() {
		return this.locationOID;
	}
	public void setLocationOID(String locationOID) {
		this.locationOID = locationOID;
	}
}
