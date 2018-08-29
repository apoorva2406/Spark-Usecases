package com.wincere.analytics.xobject.study;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "study")
public class Study {		
	
	@XmlAttribute(name="OID")
	String oID;
		
	@XmlElement(name = "GlobalVariables")
	public GlobalVariables getGlobalVariables() {
		return globalVariables;
	}

	public void setGlobalVariables(GlobalVariables globalVariables) {
		this.globalVariables = globalVariables;
	}

	
	GlobalVariables globalVariables;
	
	public Study(){		
	}
	
	
	public String getOID() {
		return oID;
	}

	public void setOID(String oID) {
		oID = oID;
	}	
}
