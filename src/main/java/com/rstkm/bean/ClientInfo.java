package com.rstkm.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "client-info", propOrder = {"ipAddress", "userAgent"})
public class ClientInfo {

    @XmlElement(name = "ip-address")
    private String ipAddress;
    @XmlElement(name = "user-agent")
    private String userAgent;

    public ClientInfo() {
    }

    private String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
