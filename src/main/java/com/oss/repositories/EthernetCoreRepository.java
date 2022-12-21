package com.oss.repositories;

import java.util.UUID;

import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.EthernetLinkBulkDTO;
import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.EthernetLinkRoutingDTO;
import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.EthernetLinkSpeedDTO;
import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.EthernetLinkTerminationDTO;
import com.comarch.oss.transport.ip.ethernet.ethernetlink.api.dto.EthernetLinkTerminationLevelDTO;
import com.oss.services.EthernetCoreClient;
import com.oss.untils.Environment;

public class EthernetCoreRepository {

    private final EthernetCoreClient client;

    public EthernetCoreRepository(Environment env) {
        client = new EthernetCoreClient(env);
    }

    public Long createEthernetLink(UUID uuid, String name, Long firstEthernetInterface, Long secondEthernetInterface) {
        EthernetLinkBulkDTO ethernetLinkBulkDTO = EthernetLinkBulkDTO.builder()
                .uuid(uuid.toString())
                .name(name)
                .startTermination(getTerminationDTO(firstEthernetInterface))
                .endTermination(getTerminationDTO(secondEthernetInterface))
                .speed(EthernetLinkSpeedDTO.SPEED_10M)
                .routing(getRoutingDTO())
                .build();
        return client.createEthernetLink(ethernetLinkBulkDTO);
    }

    private EthernetLinkTerminationDTO getTerminationDTO(Long ethernetInterfaceId) {
        return EthernetLinkTerminationDTO.builder()
                .terminationLevel(EthernetLinkTerminationLevelDTO.TerminationPoint)
                .terminationId(ethernetInterfaceId)
                .build();
    }

    private EthernetLinkRoutingDTO getRoutingDTO() {
        return EthernetLinkRoutingDTO.builder()
                .build();
    }

}
