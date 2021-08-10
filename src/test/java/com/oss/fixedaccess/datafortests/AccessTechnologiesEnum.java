package com.oss.fixedaccess.datafortests;


public enum AccessTechnologiesEnum {

    GPON("GPON", "2547.7", "1273,8", "", "Fiber"),
    ONE_G_EPON("1G-EPON", "1280.0", "1280.0", "1280.0", "Fiber"),
    TEN_G_EPON("10G-EPON", "10560.0", "1280.0", "", "Fiber"),
    TEN_G_ONE_G_EPON("10G-1G-EPON", "10560.0", "10560.0", "10560.0", "Fiber"),
    VDSL("VDSL", "55.0", "3", "", "Pair"),
    VDSL_TWO("VDSL2", "40.0", "3", "", "Pair"),
    ADSL("ADSL", "8.0", "1.8", "", "Pair"),
    ADSL_TWO("ADSL2", "12.0", "3.5", "", "Pair"),
    ADSL_TWO_PLUS("ADSL2+", "10.0", "2", "", "Pair"),
    DOCSIS_TWO_POINT_ZERO("DOCSIS 2.0", "40.0", "30.0", "", "Coax"),
    DOCSIS_THREE_POINT_ZERO("DOCSIS 3.0", "1280.0", "200", "", "Coax"),
    DOCSIS_THREE_POINT_ONE("DOCSIS 3.1", "10000.0", "2000", "", "Coax");

    private final String name;
    private final String downloadSpeed;
    private final String uploadSpeed;
    private final String maxBandwidth;
    private final String supportedMediumType;

    AccessTechnologiesEnum(String name, String downloadSpeed, String uploadSpeed, String maxBandwidth, String supportedMediumType) {
        this.name = name;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.maxBandwidth = maxBandwidth;
        this.supportedMediumType = supportedMediumType;
    }

    public String getName() {
        return name;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public String getUploadSpeed() {
        return uploadSpeed;
    }

    public String getMaxBandwidth() {
        return maxBandwidth;
    }

    public String getSupportedMediumType() {
        return supportedMediumType;
    }
}
