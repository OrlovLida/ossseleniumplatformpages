/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.networkSliceSubnet;

/**
 * @author Marcin Kozioł
 */
public class CreateNetworkSliceSubnetConstants {

    public static final String NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER = "RANSliceSubnet";
    public static final String NETWORK_SLICE_SUBNET_NAME = "NETWORK-SLICE-SUBNET-CREATE-SELENIUM-TEST";
    public static final String NETWORK_SLICE_SUBNET_DESCRIPTION = "NETWORK-SLICE-SUBNET-CREATE-DESCRIPTION";
    public static final String OPERATIONAL_STATE_VALUE = "ENABLED";
    public static final String SLICE_PROFILE_NAME = "SLICE-PROFILE-CREATE-SELENIUM-TEST";

    public static final String SLICE_PROFILE_DEFAULT_LABEL_PATH = "Slice Profile 1";
    public static final String PLMN_INFO_DEFAULT_LABEL_PATH = SLICE_PROFILE_NAME
            + ".PLMN Info 1 (mcc: , mnc: , sd: , sst: )";

    public static final String MCC_VALUE = "809";
    public static final String MNC_VALUE = "6019";
    public static final String SST_VALUE = "149";
    public static final String SD_VALUE = "16007219";
}