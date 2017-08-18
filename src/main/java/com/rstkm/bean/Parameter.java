package com.rstkm.bean;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.NONE)
public class Parameter {
    @XmlAttribute(name = "name")
    private String name;
    @XmlValue
    private String parameter;

    public Parameter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
