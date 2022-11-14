package com.oss.repositories;

import java.util.List;

import com.comarch.oss.radio.api.dto.BaseStationResponseIdDTO;
import com.comarch.oss.radio.api.dto.CardIdentificationDTO;
import com.comarch.oss.radio.api.dto.CarrierDTO;
import com.comarch.oss.radio.api.dto.Cell4gDTO;
import com.comarch.oss.radio.api.dto.CellResponseIdDTO;
import com.comarch.oss.radio.api.dto.EnodeBDTO;
import com.comarch.oss.radio.api.dto.HniDTO;
import com.comarch.oss.radio.api.dto.HostRelationDTO;
import com.comarch.oss.radio.api.dto.HostingResourceDTO;
import com.comarch.oss.radio.api.dto.MccMncDTO;
import com.comarch.oss.radio.api.dto.PortIdentificationDTO;
import com.comarch.oss.radio.api.dto.RanBandType4GDTO;
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
        return cell.getId();
    }

    public void createHRENodeBDevice(Long hostingResourceId, Long eNodeBId) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRENodeB(buildDeviceHR(hostingResourceId), eNodeBId);
    }

    public void createHRCellDevice(Long hostingResourceId, Long eNodeBId, Long cellId) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRCell(buildDeviceHR(hostingResourceId), eNodeBId, cellId);
    }

    public void createHRENodeBDevicePort(Long hostingResourceId, Long eNodeBId, String portName) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRENodeB(buildDevicePortHR(hostingResourceId, portName), eNodeBId);
    }

    public void createHRCellDevicePort(Long hostingResourceId, Long eNodeBId, Long cellId, String portName) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRCell(buildDevicePortHR(hostingResourceId, portName), eNodeBId, cellId);
    }

    public void createHRENodeBDeviceCard(Long hostingResourceId, Long eNodeBId, String slotName, String cardName) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRENodeB(buildDeviceCardHR(hostingResourceId, slotName, cardName), eNodeBId);
    }

    public void createHRCellDeviceCard(Long hostingResourceId, Long eNodeBId, Long cellId, String slotName, String cardName) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        client.createHRCell(buildDeviceCardHR(hostingResourceId, slotName, cardName), eNodeBId, cellId);
    }

    public Long getOrCreateBandType(String bandTypeName, int dLfrequencyStart, int dLfrequencyEnd, int uLfrequencyStart, int uLfrequencyEnd) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        List<Integer> bandTypeIds = client.getBandTypeByName(bandTypeName);
        if (bandTypeIds.isEmpty()) {
            RanBandType4GDTO ranBandType4GDTO = RanBandType4GDTO.builder()
                    .name(bandTypeName)
                    .dLfrequencyStart(dLfrequencyStart)
                    .dLfrequencyEnd(dLfrequencyEnd)
                    .uLfrequencyStart(uLfrequencyStart)
                    .uLfrequencyEnd(uLfrequencyEnd)
                    .build();
            return client.createBandType(ranBandType4GDTO);
        }
        return Long.valueOf(bandTypeIds.get(0));
    }

    public void getOrCreateCarrier(String carrierName, int downlinkChannel, int uplinkChannel, int dlCentreFrequency, int ulCentreFrequency, Long bandTypeId) {
        Radio4gClient client = Radio4gClient.getInstance(env);
        if (!client.isCarriePresent(carrierName)) {
            CarrierDTO carrierDTO = CarrierDTO.builder()
                    .name(carrierName)
                    .downlinkChannel(downlinkChannel)
                    .uplinkChannel(uplinkChannel)
                    .dlCentreFrequency(dlCentreFrequency)
                    .ulCentreFrequency(ulCentreFrequency)
                    .bandTypeId(bandTypeId)
                    .build();
            client.createCarrier(carrierDTO);
        }
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