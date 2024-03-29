package com.oss.untils;

/**
 * @author Patryk Gorczynski
 */
public class Constants {

    //params
    public static final String ID = "id";
    public static final String IDS = "ids";
    public static final String CELL_ID = "cell_id";
    public static final String PROJECT_ID = "project_id";
    public static final String PERSPECTIVE = "perspective";
    public static final String LIVE = "LIVE";
    public static final String PLAN = "PLAN";
    public static final String GENERATE_CONTROLLER_ID = "generateControllerId";
    public static final String GENERATE_BASE_STATION_ID = "generateBaseStationId";
    public static final String GENERATE_CELL_ID = "generateCellId";
    public static final String SHOULD_CREATE_MISSING_ADDRESS_ITEMS = "shouldCreateMissingAddressItems";
    public static final String RSQL = "rsql";
    public static final String TYPES = "types";
    public static final String NAME_PARAM = "Name";
    public static final String PORT_ID = "portId";
    public static final String DEVICE_ID = "deviceId";
    //models
    //Generic
    public static final String GENERIC_BSC_MODEL = "Generic BSC";
    public static final String GENERIC_RNCMODEL = "Generic RNC";
    public static final String GENERIC_BTS_MODEL = "Generic BTS";
    public static final String GENERIC_NODEB_MODEL = "Generic NodeB";
    public static final String GENERIC_ENODEB_MODEL = "Generic ENodeB";
    public static final String GENERIC_GNODEB_MODEL = "Generic GNodeB";
    public static final String GENERIC_GNODEBCUUP_MODEL = "Generic GNodeBCUUP";
    public static final String GENERIC_GNODEBDU_MODEL = "Generic GNodeBDU";
    //Huawei
    public static final String HUAWEI_NODEB_MODEL = "HUAWEI Technology Co.,Ltd NodeB";
    public static final String HUAWEI_ENODEB_MODEL = "HUAWEI Technology Co.,Ltd eNodeB";
    public static final String HUAWEI_GNODEB_MODEL = "HUAWEI Technology Co.,Ltd gNodeB";
    public static final String BBU3900_MODEL = "BBU3900";
    public static final String BBU5900_MODEL = "BBU5900";
    public static final String RRU5301_MODEL = "RRU5301";
    public static final String RRU5501_MODEL = "RRU5501";
    public static final String AAU5614ANTENNA_MODEL = "AAU5614";
    public static final String AHP4517R7v06ANTENNA_MODEL = "AHP4517R7v06";
    public static final String AQU4518R22v07ANTENNA_MODEL = "AQU4518R22v07";
    public static final String UBBPg3_CARD_MODEL = "UBBPg3";
    public static final String ACOMT2H10v06COMBINER_MODEL = "ACOMT2H10v06";
    public static final String ATADU2022v07MHA_MODEL = "ATADU2022v07";
    //RFS
    public static final String RFS_MANUFACTURER = "RFS";
    public static final String LCF1250J_2m_CABLE = "1/2\"\" LCF 12-50J 2.0m";
    public static final String LCF1250J_3m_CABLE = "1/2\"\" LCF 12-50J 3.0m";
    public static final String LCF1250J12_CABLE = "LCF 12-50J 1/2\"\"";
    //types
    public static final String ADDRESS_TYPE = "Address";
    public static final String PHYSICAL_ELEMENT = "PhysicalElement";
    public static final String DEVICE_MODEL_TYPE = "DeviceModel";
    public static final String CARD_MODEL_TYPE = "CardModel";
    public static final String ANTENNA_TYPE = "RANAntenna";
    public static final String ANTENNA_MODEL_TYPE = "RANAntennaModel";
    public static final String LOCATION_TYPE = "Location";
    public static final String BUILDING_TYPE = "Building";
    public static final String SITE_TYPE = "Site";
    public static final String CONTROLLER_TYPE = "controller";
    public static final String BSC_TYPE = "BSC";
    public static final String RNC_TYPE = "RNC";
    public static final String BASE_STATION_TYPE = "BaseStation";
    public static final String BTS_TYPE = "BTS";
    public static final String NODE_B_TYPE = "NodeB";
    public static final String E_NODE_B_TYPE = "ENodeB";
    public static final String G_NODE_B_TYPE = "GNodeB";
    public static final String G_NODE_B_CUUP_TYPE = "GNodeBCUUP";
    public static final String G_NODE_B_DU_TYPE = "GNodeBDU";
    public static final String CELL_2G_TYPE = "Cell2G";
    public static final String CELL_3G_TYPE = "Cell3G";
    public static final String CELL_4G_TYPE = "Cell4G";
    public static final String CELL_5G_TYPE = "Cell5G";
    public static final String TECH_2G = "2G";
    public static final String TECH_3G = "3G";
    public static final String TECH_4G = "4G";
    public static final String TECH_5G = "5G";
    //common attributes like name, identifier, description etc. Specific type's attributes should be added in the types section.
    public static final String COMMON_NAME_ATTRIBUTE = "name";
    public static final String COMMON_IDENTIFIER_ATTRIBUTE = "identifier";

    private Constants() {
    }

}
