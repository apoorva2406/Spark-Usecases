package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlAttribute;

public class UserRef {

	String userOID;
	
	@XmlAttribute(name = "UserOID")
	public String getUserOID() {
		return this.userOID;
	}
	public void setUserOID(String userOID) {
		this.userOID = userOID;
	}
}
