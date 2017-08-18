package com.rstkm.bean;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "request_detail")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {"clientInfo", "parameters"})
public class RequestDetail {
    @XmlElement(name = "client_info", required = true)
    private ClientInfo clientInfo;
    @XmlElement(name = "parameters", required = true)
    private Parameters parameters;

    public RequestDetail() {
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
}
