package com.oss.repositories;

import com.comarch.oss.radio.api.dto.*;
import com.oss.services.Radio4gClient;
import com.oss.untils.Environment;

public class Radio4gRepository {

    private Environment env;

    public Radio4gRepository(Environment env) {
        this.env = env;
    }

    public Long createENodeB(String name, Long locationId, String mcc, String mnc, String eNodeBModel) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        BaseStationResponseIdDTO eNodeB = client.createENodeB(buildENodeB(name, locationId, mcc, mnc, eNodeBModel));
        return eNodeB.getId();
    }

    public Long createCell4g(String cellName, Integer cellId, Long eNodeBId, String mcc, String mnc, String carrier) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        CellResponseIdDTO cell = client.createCell4G(buildCell4g(cellName, cellId, mcc, mnc, carrier), eNodeBId);
        return  cell.getId();
    }

    public void createHRENodeB(Long hostingResourceId, Long eNodeBId) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRENodeB(buildHR(hostingResourceId), eNodeBId);
    }

    public void createHRCell(Long hostingResourceId, Long eNodeBId, Long cellId) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRCell(buildHR(hostingResourceId), eNodeBId, cellId);
    }

    public HostRelationDTO buildHR(Long hostingResourceId) {
        return HostRelationDTO.builder()
                .hostingResource(getHostingResource(hostingResourceId))
                .build();
    }

    private EnodeBDTO buildENodeB(String name, Long locationId, String mcc, String mnc, String eNodeBModel) {
        return EnodeBDTO.builder()
                .name(name)
                .modelIdentifier(eNodeBModel)
                .addMccMncs(getMccMnc(mcc, mnc))
                .enodeBId(-1)
                .locationId(locationId)
                .build();
    }

    private MccMncDTO getMccMnc(String mcc, String mnc) {
        return MccMncDTO.builder()
                .mcc(mcc)
                .mnc(mnc)
                .primary(true)
                .build();
    }

    private Cell4gDTO buildCell4g(String name, Integer cellId, String mcc, String mnc, String carrier) {
        return Cell4gDTO.builder()
                .name(name)
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

    private HostingResourceDTO getHostingResource(Long hostingResourceId) {
        return HostingResourceDTO.builder()
                .hostingResourceId(hostingResourceId)
                .build();
    }
}