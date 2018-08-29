package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlAttribute;

public class ProtocolDeviation {

	String transactionType;
	String value;
	String status;
	String code;
	String className;
	String protocolDeviationRepeatKey;
	@XmlAttribute(name = "TransactionType")
	public String getTransactionType() {
		return this.transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType= transactionType;
	}
	
	@XmlAttribute(name = "Value")
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value= value;
	}
	
	@XmlAttribute(name = "Status")
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status= status;
	}
	@XmlAttribute(name = "Code")
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code= code;
	}
	@XmlAttribute(name = "Class")
	public String getClassName() {
		return this.className;
	}
	public void setClassName(String className) {
		this.className= className;
	}
	
	@XmlAttribute(name = "ProtocolDeviationRepeatKey")
	public String getProtocolDeviationRepeatKey() {
		return this.protocolDeviationRepeatKey;
	}
	public void setProtocolDeviationRepeatKey(String protocolDeviationRepeatKey) {
		this.protocolDeviationRepeatKey= protocolDeviationRepeatKey;
	}
}
