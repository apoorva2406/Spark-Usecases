package com.wincere.analytics.xobject.audit;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {
 
    @XmlAttribute
    public int id;
     
    public String firstName;
     
    public String lastName;
     
}