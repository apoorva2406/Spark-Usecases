package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Comment")
public class Comment {	
	String commentRepeatKey;
	String transactionType;
	String value;
	
	@XmlElement(name = "CommentRepeatKey", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getCommentRepeatKey() {
		return commentRepeatKey;
	}
	public void setCommentRepeatKey(String commentRepeatKey) {
		this.commentRepeatKey = commentRepeatKey;
	}
	
	@XmlElement(name = "TransactionType", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	@XmlElement(name = "Value", namespace = "http://www.mdsol.com/ns/odm/metadata")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}