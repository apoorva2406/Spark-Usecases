package com.wincere.analytics.xobject.audit;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FormData")
public class FormData {
	
	ItemGroupData itemGroupData;
	AuditRecord auditRecords;	
	Signature signature;
	String formOID;
	String formRepeatKey;
	@XmlAttribute(name = "FormOID")
	public String getFormOID() {
		return this.formOID;
	}
	
	public void setFormOID(String formOID) {
		this.formOID = formOID;
	}
	
	@XmlAttribute(name = "FormRepeatKey")
	public String getFormRepeatKey() {
		return this.formRepeatKey;
	}
	
	public void setFormRepeatKey(String formRepeatKey) {
		this.formRepeatKey = formRepeatKey;
	}
	
	@XmlElement(name = "ItemGroupData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public ItemGroupData getItemGroupData() {
		return itemGroupData;
	}
	public void setItemGroupData(ItemGroupData itemGroupData) {
		this.itemGroupData = itemGroupData;
	}
	
	@XmlElement(name = "AuditRecord", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public AuditRecord getAuditRecords() {
		return auditRecords;
	}
	public void setAuditRecords(AuditRecord auditRecords) {
		this.auditRecords = auditRecords;
	}
	
	@XmlElement(name = "Signature", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
}