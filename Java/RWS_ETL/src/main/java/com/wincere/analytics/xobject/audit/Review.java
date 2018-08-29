package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlAttribute;

public class Review {

	String groupName;
	String reviewed;
	
	@XmlAttribute(name = "GroupName")
	public String getGroupName() {
		return this.groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName= groupName;
	}
	
	@XmlAttribute(name = "Reviewed")
	public String getReviewed() {
		return this.reviewed;
	}
	public void setReviewed(String reviewed) {
		this.reviewed= reviewed;
	}
}
