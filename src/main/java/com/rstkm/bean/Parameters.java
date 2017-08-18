package com.rstkm.bean;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "parameters", propOrder = {"numericParameters", "stringParameters"})
public class Parameters {
    @XmlAttribute
    private String method;
    private List<Parameter> numericParameters;
    private List<Parameter> stringParameters;

    public Parameters() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @XmlElementWrapper(name = "numeric_parameters")
    @XmlElement(name = "parameter")
    private List<Parameter> getNumericParameters() {
        return numericParameters;
    }

    public void setNumericParameters(List<Parameter> numericParameters) {
        this.numericParameters = numericParameters;
    }

    @XmlElementWrapper(name = "string_parameters")
    @XmlElement(name = "parameter")
    private List<Parameter> getStringParameters() {
        return stringParameters;
    }

    public void setStringParameters(List<Parameter> stringParameters) {
        this.stringParameters = stringParameters;
    }
}
