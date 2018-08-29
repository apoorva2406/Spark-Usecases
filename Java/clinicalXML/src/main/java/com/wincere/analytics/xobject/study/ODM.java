package com.wincere.analytics.xobject.study;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;;

@XmlRootElement(name="ODM")
public class ODM {
		
	@XmlElement(name = "Study")
	ArrayList<Study> studies;

	public ODM(){
		
	}
			
	public ArrayList<Study> getStudies() {
		return studies;
	}

	public void setStudy(ArrayList<Study> studies) {
		this.studies = studies;
	}	
}
