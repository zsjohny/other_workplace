package com.reliable.controller.form

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "text")
public class UserForm implements Serializable {

    @XmlAttribute
    private String name

    @XmlAttribute
    private Integer age


}