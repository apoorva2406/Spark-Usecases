package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "ClinicalData")
@XmlRootElement(name = "ClinicalData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
public class ClinicalData {
	
	SubjectData subjectData;
	String AuditSubCategoryName;
	String studyOID;
	String metaDataVersionOID;
	@XmlAttribute(name = "StudyOID")
	public String getStudyOID() {
		return this.studyOID;
	}
	public void setStudyOID(String studyOID) {
		this.studyOID = studyOID;
	}
	
	@XmlAttribute(name = "MetaDataVersionOID")
	public String getMetaDataVersionOID(){
		return this.metaDataVersionOID;
	}
	public void setMetaDataVersionOID(String metaDataVersionOID){
		this.metaDataVersionOID=metaDataVersionOID;
	}
	
	@XmlAttribute(name = "AuditSubCategoryName" , namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getAuditSubCategoryName() {
		return AuditSubCategoryName;
	}
	public void setAuditSubCategoryName(String auditSubCategoryName) {
		AuditSubCategoryName = auditSubCategoryName;
	}
	
	@XmlElement(name = "SubjectData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	//@XmlElement(name = "SubjectData")
	public SubjectData getSubjectData(){
		return subjectData;
	}
	public void setSubjectData(SubjectData subjectData){
		this.subjectData = subjectData;
	}
}