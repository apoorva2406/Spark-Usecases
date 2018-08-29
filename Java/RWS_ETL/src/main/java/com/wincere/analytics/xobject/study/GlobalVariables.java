package com.wincere.analytics.xobject.study;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GlobalVariables")
public class GlobalVariables {

	String studyName;
	String studyDescription;
	String protocolName;
	@XmlElement(name = "StudyName")
	public String getStudyName() {
		return studyName;
	}
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	@XmlElement(name = "StudyDescription")
	public String getStudyDescription() {
		return studyDescription;
	}
	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}
	@XmlElement(name = "ProtocolName")
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	
}
