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

    public Long createCell3g(String name, Integer cellId, Long nodeBId, String mcc, String mnc, String carrier) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        CellResponseIdDTO cell = client.createCell3G(buildCell3g(name, cellId, mcc, mnc, carrier), nodeBId);
        return cell.getId();
    }

    public void createHRNodeBDevice(Long hostingResourceId, Long nodeBId) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createHRNodeB(buildDeviceHR(hostingResourceId), nodeBId);
    }

    public void createHRCellDevice(Long hostingResourceId, Long nodeBId, Long cellId) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createHRCell(buildDeviceHR(hostingResourceId), nodeBId, cellId);
    }

    public void createHRNodeBDevicePort(Long hostingResourceId, Long nodeBId, String portName) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createHRNodeB(buildDevicePortHR(hostingResourceId, portName), nodeBId);
    }

    public void createHRCellDevicePort(Long hostingResourceId, Long nodeBId, Long cellId, String portName) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createHRCell(buildDevicePortHR(hostingResourceId, portName), nodeBId, cellId);
    }

    public void createHRNodeBDeviceCard(Long hostingResourceId, Long nodeBId, String slotName, String cardName) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createHRNodeB(buildDeviceCardHR(hostingResourceId, slotName, cardName), nodeBId);
    }

    public void createHRCellDeviceCard(Long hostingResourceId, Long nodeBId, Long cellId, String slotName, String cardName) {
        Radio3gClient client = Radio3gClient.getInstance(env);
        client.createHRCell(buildDeviceCardHR(hostingResourceId, slotName, cardName), nodeBId, cellId);
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
