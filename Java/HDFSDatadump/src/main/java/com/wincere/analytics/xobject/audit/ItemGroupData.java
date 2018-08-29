package com.wincere.analytics.xobject.audit;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ItemGroupData")
public class ItemGroupData {
	
	ItemData itemData;
	AuditRecord auditRecords;
	String itemGroupOID;
	String  itemGroupRepeatKey;
	String transactionType;
	
	@XmlAttribute(name = "ItemGroupOID")
	public String getItemGroupOID() {
		return this.itemGroupOID;
	}
	public void setItemGroupOID(String itemGroupOID) {
		this.itemGroupOID = itemGroupOID;
	}
	
	@XmlAttribute(name = "ItemGroupRepeatKey")
	public String getItemGroupRepeatKey() {
		return this.itemGroupRepeatKey;
	}
	public void setItemGroupRepeatKey(String itemGroupRepeatKey) {
		this.itemGroupRepeatKey = itemGroupRepeatKey;
	}
	
	@XmlAttribute(name = "TransactionType")
	public String getTransactionType() {
		return this.transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType= transactionType;
	}
	
	@XmlElement(name = "ItemData", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public ItemData getItemData() {
		return itemData;
	}

	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}

	@XmlElement(name = "AuditRecord", namespace = "http://www.cdisc.org/ns/odm/v1.3")
	public AuditRecord getAuditRecords() {
		return auditRecords;
	}

	public void setAuditRecords(AuditRecord auditRecords) {
		this.auditRecords = auditRecords;
	}
}