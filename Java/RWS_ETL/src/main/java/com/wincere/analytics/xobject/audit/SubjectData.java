package com.wincere.analytics.xobject.audit;


import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wincere.analytics.xobject.study.Study;

@XmlRootElement(name = "SubjectData")
public class SubjectData {

	
	AuditRecord auditRecord;
	SiteRef siteRef;
	StudyEventData studies;
	String subjectKey;
	String subjectKeyType;
	String subjectName;
	@XmlAttribute(name = "SubjectKey")
	public String getSubjectKey() {
		return this.subjectKey;
	}
	public void setSubjectKey(String subjectKey) {
		this.subjectKey = subjectKey;
	}
	
	@XmlAttribute(name = "SubjectKeyType" , namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getSubjectKeyType(){
		return this.subjectKeyType;
	}
	
	public void setSubjectKeyType(String subjectKeyType){
		this.subjectKeyType = subjectKeyType;
	}
	
	@XmlAttribute(name = "SubjectName" , namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getSubjectName(){
		return this.subjectName;
	}
	
	public void setSubjectName(String subjectName){
		this.subjectName= subjectName;
	}
	
	@XmlElement(name = "AuditRecord", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public AuditRecord getAuditRecord(){
		return auditRecord;
	}
	public void setAuditRecord(AuditRecord auditRecord){
		this.auditRecord = auditRecord;
	}
		
	@XmlElement(name = "SiteRef", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public SiteRef getSiteRef() {
		return siteRef;
	}
	public void setSiteRef(SiteRef siteRef) {
		this.siteRef = siteRef;
	}
	
	@XmlElement(name = "StudyEventData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public StudyEventData getStudies() {
		return studies;
	}
	public void setStudies(StudyEventData studies) {
		this.studies = studies;
	}	
}