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

    public Long createCell5g(String name, Integer cellId, Long gNodeBId, String mcc, String mnc, String carrier) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        CellResponseIdDTO cell = client.createCell5G(buildCell5g(name, cellId, mcc, mnc, carrier), gNodeBId);
        return cell.getId();
    }

    public void createHRGNodeBDevice(Long hostingResourceId, Long gNodeBId) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createHRGNodeB(buildDeviceHR(hostingResourceId), gNodeBId);
    }

    public void createHRCellDevice(Long hostingResourceId, Long gNodeBId, Long cellId) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createHRCell(buildDeviceHR(hostingResourceId), gNodeBId, cellId);
    }

    public void createHRGNodeBDevicePort(Long hostingResourceId, Long gNodeBId, String portName) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createHRGNodeB(buildDevicePortHR(hostingResourceId, portName), gNodeBId);
    }

    public void createHRCellDevicePort(Long hostingResourceId, Long gNodeBId, Long cellId, String portName) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createHRCell(buildDevicePortHR(hostingResourceId, portName), gNodeBId, cellId);
    }

    public void createHRGNodeBDeviceCard(Long hostingResourceId, Long gNodeBId, String slotName, String cardName) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createHRGNodeB(buildDeviceCardHR(hostingResourceId, slotName, cardName), gNodeBId);
    }

    public void createHRCellDeviceCard(Long hostingResourceId, Long gNodeBId, Long cellId, String slotName, String cardName) {
        Radio5gClient client = Radio5gClient.getInstance(env);
        client.createHRCell(buildDeviceCardHR(hostingResourceId, slotName, cardName), gNodeBId, cellId);
    }

    private HostRelationDTO buildDeviceHR(Long hostingResourceId) {
        return HostRelationDTO.builder()
                .hostingResource(getHostingResource(hostingResourceId))
                .build();
    }

    private HostRelationDTO buildDevicePortHR(Long hostingResourceId, String portName) {
        return HostRelationDTO.builder()
                .hostingResource(getHostingResourcePort(hostingResourceId, portName))
                .build();
    }

    private HostRelationDTO buildDeviceCardHR(Long hostingResourceId, String slotName, String cardName) {
        return HostRelationDTO.builder()
                .hostingResource(getHostingResourceCard(hostingResourceId, slotName, cardName))
                .build();
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

    private HostingResourceDTO getHostingResource(Long hostingResourceId) {
        return HostingResourceDTO.builder()
                .hostingResourceId(hostingResourceId)
                .build();
    }

    private HostingResourceDTO getHostingResourcePort(Long hostingResourceId, String portName) {
        return HostingResourceDTO.builder()
                .hostingResourceId(hostingResourceId)
                .hostingPort(getPort(portName))
                .build();
    }

    private HostingResourceDTO getHostingResourceCard(Long hostingResourceId, String slotName, String cardName) {
        return HostingResourceDTO.builder()
                .hostingResourceId(hostingResourceId)
                .hostingCard(getCard(slotName, cardName))
                .build();
    }

    private PortIdentificationDTO getPort(String portName) {
        return PortIdentificationDTO.builder()
                .name(portName)
                .build();
    }

    private CardIdentificationDTO getCard(String slotName, String cardName) {
        return CardIdentificationDTO.builder()
                .slotName(slotName)
                .name(cardName)
                .build();
    }
}
