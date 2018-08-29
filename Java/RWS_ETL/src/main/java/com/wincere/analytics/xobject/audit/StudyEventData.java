package com.wincere.analytics.xobject.audit;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StudyEventData")
public class StudyEventData {

	FormData formData;		
	AuditRecord auditRecords;
	String studyEventOID;
	String studyEventRepeatKey;
	
	@XmlAttribute(name = "StudyEventOID")
	public String getStudyEventOID() {
		return this.studyEventOID;
	}
	public void setStudyEventOID(String studyEventOID) {
		this.studyEventOID = studyEventOID;
	}
	
	@XmlAttribute(name = "StudyEventRepeatKey")
	public String getStudyEventRepeatKey() {
		return this.studyEventRepeatKey;
	}
	
	public void setStudyEventRepeatKey(String studyEventRepeatKey) {
		this.studyEventRepeatKey = studyEventRepeatKey;
	}
	
	@XmlElement(name = "FormData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public FormData getFormData() {
		return this.formData;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}

	@XmlElement(name = "AuditRecord", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public AuditRecord getAuditRecords() {
		return auditRecords;
	}

	public void setAuditRecords(AuditRecord auditRecords) {
		this.auditRecords = auditRecords;
	}	
}