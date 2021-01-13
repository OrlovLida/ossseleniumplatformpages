/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.repositories;

import com.comarch.oss.radio.api.dto.*;
import com.oss.services.Radio5gClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

/**
 * @author Patryk Gorczynski
 */

public class Radio5gRepository {

    private Environment env;

    public Radio5gRepository(Environment env) {
        this.env = env;
    }

    public Long createGNodeB(String name, Long locationId, String mcc, String mnc, String gNodeBModel) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        return client.createGNodeB(buildGNodeB(name, locationId, mcc, mnc, gNodeBModel)).getId();
    }

    public Long createGNodeBCUUP(String name, Long locationId) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        return client.createGNodeBCUUP(buildGNodeBCUUP(name, locationId)).getId();
    }

    public Long createGNodeBDU(String name, Long locationId) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        return client.createGNodeBDU(buildGNodeBDU(name, locationId)).getId();
    }

    public void createCell5g(String name, Integer cellId, Long gNodeBId, String mcc, String mnc, String carrier) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createCell5G(buildCell5g(name, cellId, mcc, mnc, carrier), gNodeBId);
    }

    private GnodeBDTO buildGNodeB(String name, Long locationId, String mcc, String mnc, String gNodeBModel) {
        return GnodeBDTO.builder()
                .name(name)
                .modelIdentifier(gNodeBModel)
                .gnodeBId(-1L)
                .addMccMncs(getHni(mcc, mnc))
                .locationId(locationId)
                .build();
    }

    private GnodeBCUUPDTO buildGNodeBCUUP(String name, Long locationId) {
        return GnodeBCUUPDTO.builder()
                .name(name)
                .modelIdentifier(Constants.GENERIC_GNODEBCUUP_MODEL)
                .gnodeBId(-1L)
                .locationId(locationId)
                .build();
    }

    private GnodeBDUDTO buildGNodeBDU(String name, Long locationId) {
        return GnodeBDUDTO.builder()
                .name(name)
                .modelIdentifier(Constants.GENERIC_GNODEBDU_MODEL)
                .gnodeBId(-1L)
                .locationId(locationId)
                .build();
    }

    private Cell5gDTO buildCell5g(String cellName, Integer cellId, String mcc, String mnc, String carrier) {
        return Cell5gDTO.builder()
                .name(cellName)
                .cellId(cellId)
                .carrier(carrier)
                .addMccMncs(getHni(mcc, mnc))
                .build();
    }

    private HniDTO getHni(String mcc, String mnc) {
        return HniDTO.builder()
                .mcc(mcc)
                .mnc(mnc)
                .build();
    }

}
