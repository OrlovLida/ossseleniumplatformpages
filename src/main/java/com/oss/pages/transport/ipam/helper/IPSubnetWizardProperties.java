package com.oss.pages.transport.ipam.helper;

public class IPSubnetWizardProperties {
    private String subnetType;
    private String status;
    private String role;
    private String description;

    public IPSubnetWizardProperties(String subnetType){
        this.subnetType = subnetType;
    }

    public IPSubnetWizardProperties(String subnetType, String status, String role, String description){
        this.subnetType = subnetType;
        this.status = status;
        this.role = role;
        this.description = description;
    }

    public IPSubnetWizardProperties(String subnetType, String role, String description){
        this.subnetType = subnetType;
        this.role = role;
        this.description = description;
    }

    public IPSubnetWizardProperties(String subnetType, String role){
        this.subnetType = subnetType;
        this.role = role;
    }

    public String getSubnetType() {
        return subnetType;
    }

    public void setSubnetType(String subnetType) {
        this.subnetType = subnetType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
