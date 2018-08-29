package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Query", namespace = "http://www.mdsol.com/ns/odm/metadata")
public class Query {

	String queryRepeatKey;
	String recipient;
	String status;
	String value;
	@XmlAttribute(name = "QueryRepeatKey")
	public String getQueryRepeatKey() {
		return this.queryRepeatKey;
	}
	public void setQueryRepeatKey(String queryRepeatKey) {
		this.queryRepeatKey= queryRepeatKey;
	}
	
	@XmlAttribute(name = "Recipient")
	public String getRecipient() {
		return this.recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient= recipient;
	}
	
	@XmlAttribute(name = "Status")
	public String getStatus() {
		return this.recipient;
	}
	public void setStatus(String status) {
		this.status= status;
	}
	
	@XmlAttribute(name = "Value")
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value= value;
	}
}
