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
public class EditNetworkSliceSubnetConstants {
    public static final String NETWORK_SLICE_SUBNET_NAME = "NETWORK-SLICE-SUBNET-SELENIUM-TEST";
    public static final String NETWORK_SLICE_SUBNET_DESCRIPTION_NEW = "NETWORK-SLICE-SUBNET-EDIT-DESCRIPTION-NEW";
    public static final String EDIT_LOGICAL_ELEMENT_ACTION_LABEL = "Edit Logical Element";
    public static final String OPERATIONAL_STATE_VALUE = "ENABLED";
    public static final String ADMINISTRATIVE_STATE_VALUE = "UNLOCKED";

    public static final String CAPACITY_SLICE_PROFILE_NAME = "CAPACITY-SLICE-PROFILE-EDIT-SELENIUM-TEST";
    public static final String SLICE_PROFILE_NAME = "SLICE-PROFILE-EDIT-SELENIUM-TEST";
    public static final String PLMN_INFO_DEFAULT_LABEL_PATH = SLICE_PROFILE_NAME
            + ".PLMN Info 0 (mcc: 805, mnc: 6015, sd: 16007215, sst: 145)";
    public static final String CAPACITY_PLMN_INFO_DEFAULT_LABEL_PATH = CAPACITY_SLICE_PROFILE_NAME
            + ".PLMN Info 0 (mcc: , mnc: , sd: , sst: 145)";
    public static final String MCC_VALUE = "805";
    public static final String MNC_VALUE = "6015";
    public static final String SST_VALUE = "145";
    public static final String SD_VALUE = "16007215";
}
