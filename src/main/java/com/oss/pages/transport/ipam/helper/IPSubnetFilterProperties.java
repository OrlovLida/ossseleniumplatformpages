package com.oss.pages.transport.ipam.helper;

public class IPSubnetFilterProperties {
    private String startIp;
    private String endIp;
    private String operator;
    private String maskLength;

    public IPSubnetFilterProperties(String startIp, String endIp, String operator, String maskLength) {
        this.startIp = startIp;
        this.endIp = endIp;
        this.operator = operator;
        this.maskLength = maskLength;
    }

    public IPSubnetFilterProperties(String startIp, String endIp) {
        this.startIp = startIp;
        this.endIp = endIp;
    }

    public String getStartIp() {
        return startIp;
    }

    public void setStartIp(String startIp) {
        this.startIp = startIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMaskLength() {
        return maskLength;
    }

    public void setMaskLength(String maskLength) {
        this.maskLength = maskLength;
    }
}
