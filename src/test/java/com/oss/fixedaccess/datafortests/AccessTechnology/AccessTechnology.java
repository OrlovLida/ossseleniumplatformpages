package com.oss.fixedaccess.datafortests.AccessTechnology;

public class AccessTechnology {

    private String name;
    private String maxDownloadSpeed;
    private String maxUploadSpeed;
    private String mediumType;

    public AccessTechnology(String name, String maxDownloadSpeed, String maxUploadSpeed, String mediumType) {
        this.name = name;
        this.maxDownloadSpeed = maxDownloadSpeed;
        this.maxUploadSpeed = maxUploadSpeed;
        this.mediumType = mediumType;
    }

    public String getName() {
        return name;
    }

    public String getMaxDownloadSpeed() {
        return maxDownloadSpeed;
    }

    public String getMaxUploadSpeed() {
        return maxUploadSpeed;
    }

    public String getMediumType() {
        return mediumType;
    }
}
