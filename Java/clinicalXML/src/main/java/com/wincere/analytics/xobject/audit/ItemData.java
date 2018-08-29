package com.wincere.analytics.xobject.audit;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ItemData")
public class ItemData {
	
	AuditRecord auditRecords;		
	Query query;
	Comment comment;
	Review review;	
	Signature signature;	
	// Might Need to have MeasurementUnitRef class.	
	String measurementUnitRef;	
	// Need to have ProtocolDeviation class.	
	ProtocolDeviation protocolDeviation;
	String itemOID;
	String transactionType;
	String value;
	String verify;
	String lock;
	
	
	
	@XmlAttribute(name = "ItemOID")
	public String getItemOID() {
		return this.itemOID;
	}
	public void setItemOID(String itemOID) {
		this.itemOID= itemOID;
	}
	
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
	
	@XmlAttribute(name = "Verify" , namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getVerify() {
		return this.verify;
	}
	public void setVerify(String verify) {
		this.verify= verify;
	}
	
	@XmlAttribute(name = "Lock" , namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getLock() {
		return this.lock;
	}
	public void setLock(String lock) {
		this.lock=lock;
	}
	
	
	@XmlElement(name = "AuditRecord", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public AuditRecord getAuditRecords() {
		return auditRecords;
	}
	
	public void setAuditRecords(AuditRecord auditRecords) {
		this.auditRecords = auditRecords;
	}

	
	@XmlElement(name = "Query", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	
	@XmlElement(name = "Comment", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@XmlElement(name = "Review", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	@XmlElement(name = "Signature", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	@XmlElement(name = "MeasurementUnitRef", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public String getMeasurementUnitRef() {
		return measurementUnitRef;
	}

	public void setMeasurementUnitRef(String measurementUnitRef) {
		this.measurementUnitRef = measurementUnitRef;
	}

	@XmlElement(name = "ProtocolDeviation", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public ProtocolDeviation getProtocolDeviation() {
		return protocolDeviation;
	}

	public void setProtocolDeviation(ProtocolDeviation protocolDeviation) {
		this.protocolDeviation = protocolDeviation;
	}	
}