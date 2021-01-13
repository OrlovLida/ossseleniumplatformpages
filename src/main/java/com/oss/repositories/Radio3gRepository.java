package com.oss.repositories;

import com.comarch.oss.radio.api.dto.*;
import com.oss.services.Radio3gClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;

public class Radio3gRepository {

    private Environment env;

    public Radio3gRepository(Environment env) {
        this.env = env;
    }

    public Long createRnc(String name, Long locationId, String mcc, String mnc) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        return client.createRNC(buildRnc(name, locationId, mcc, mnc)).getId();
    }

    public Long createNodeB(String name, Long locationId, Long rncId, String nodeBModel) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        return client.createNodeB(buildNodeB(name, locationId, rncId, nodeBModel)).getId();
    }

    public void createCell3g(String name, Integer cellId, Long nodeBId, String mcc, String mnc, String carrier) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createCell3G(buildCell3g(name, cellId, mcc, mnc, carrier), nodeBId);
    }

    private RNCDTO buildRnc(String name, Long locationId, String mcc, String mnc) {
        return RNCDTO.builder()
                .name(name)
                .modelIdentifier(Constants.GENERIC_RNCMODEL)
                .addMccMncs(getMccMnc(mcc, mnc))
                .rncId(-1)
                .locationId(locationId)
                .build();
    }

    private NodeBDTO buildNodeB(String name, Long locationId, Long rncId, String nodeBModel) {
        return NodeBDTO.builder()
                .name(name)
                .modelIdentifier(nodeBModel)
                .nodeBId(-1)
                .controllerId(rncId)
                .locationId(locationId)
                .build();
    }

    private Cell3gDTO buildCell3g(String name, Integer cellId, String mcc, String mnc, String carrier) {
        return Cell3gDTO.builder()
                .name(name)
                .cellId(cellId)
                .carrier(carrier)
                .addMccMncs(getCellMccMnc(mcc, mnc))
                .build();
    }

    private ImmutableRNCHniDTO getMccMnc(String mcc, String mnc) {
        return RNCHniDTO.builder()
                .mcc(mcc)
                .mnc(mnc)
                .type(RNCHniTypeDTO.PRIMARY)
                .build();
    }

    private HniDTO getCellMccMnc(String mcc, String mnc) {
        return HniDTO.builder()
                .mcc(mcc)
                .mnc(mnc)
                .build();
    }
}
