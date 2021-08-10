package com.oss.repositories.fixedaccessrepository;

import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AccessInterfaceDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AccessNodeDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AccessNodeWrapperDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AccessPointDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AccessPointWrapperDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AddressDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.AddressesWrapperDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.CentralOfficeSyncDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.DistributionAreaPersistenceResponseDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.DistributionAreaSyncDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.ImmutableInstallationDevicesDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.InstallationDeviceWrapperDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.InstallationDevicesDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.InstallationMediaDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.InstallationMediaWrapperDTO;
import com.comarch.oss.inventory.fixedaccess.distributionarea.api.dto.InstallationNetworkSyncDTO;
import com.oss.services.fixedaccessclient.DistributionAreaClient;
import com.oss.untils.Environment;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toSet;

public class DistributionAreaRepository {

    private final DistributionAreaClient distributionAreaClient;
    private final Environment env;

    public DistributionAreaRepository(Environment env) {
        this.env = env;
        distributionAreaClient = DistributionAreaClient.getInstance(env);
    }

    public Long createDistributionAreaWithAddressAccessPointAccessNodeInstallationMediumsInstllationDevices(boolean coverageFlag, String technicalStandard, Long pAddressId,
        Long pAccessPointId, List<Long> installationDevicesIds, List<Long> installationMediumsIds, Long accessNodeId, List<Long> accessInterfacesIds) {
        Map<Long, Collection<Long>> accessNodeToAccessInterfacesMapping = singletonMap(accessNodeId, accessInterfacesIds);
        DistributionAreaPersistenceResponseDTO responseDTO = distributionAreaClient.synchronizeDistributionArea(
                buildDistributionAreaWithAddressAccessPointAccessNodeInstallationMediumsInstllationDevices(coverageFlag,
                        technicalStandard,
                        pAddressId,
                        pAccessPointId,
                        installationDevicesIds,
                        installationMediumsIds,
                        accessNodeToAccessInterfacesMapping));
        return responseDTO.getId();
    }

    public void removeDistributionArea(Long pDistributionAreaId) {
        distributionAreaClient.removeDistributionArea(pDistributionAreaId);
    }

    public String getDistributionAreaName(Long pDistributionAreaId) {
        if(!(distributionAreaClient.getDistributionAreaV2(pDistributionAreaId).getName().equals(Optional.empty()))) {
            String dAName = String.valueOf(distributionAreaClient.getDistributionAreaV2(pDistributionAreaId).getName());
            return dAName.substring(dAName.indexOf("[") + 1, dAName.indexOf("]")).trim();
        }
        return "";
    }

    private DistributionAreaSyncDTO buildDistributionAreaWithAddressAccessPointAccessNodeInstallationMediumsInstllationDevices(boolean coverageFlag, String technicalStandart,
        Long pAddressId, Long pAccessPointId, List<Long> installationDevicesIds, List<Long> installationMediumsIds, Map<Long, Collection<Long>> accessNodeToAccessInterfacesMapping) {
        DistributionAreaSyncDTO dto = DistributionAreaSyncDTO.builder()
                .networkCoverage(coverageFlag)
                .technicalStandard(technicalStandart)
                .addresses(buildAddressWrapperDTO(singleton(pAddressId)))
                .installationNetwork(buildInstallationNetworkDTO(
                        Optional.of(buildAccessPointsDTO(singleton(pAccessPointId))),
                        Optional.of(buildInstallationDevicesDTO(installationDevicesIds)),
                        Optional.of(buildInstallationMediumsDTO(installationMediumsIds))))
                .centralOffice(buildCentralOfficeDTO(accessNodeToAccessInterfacesMapping))
                .build();
        return dto;
    }

    private AddressesWrapperDTO buildAddressWrapperDTO(Collection<Long> addresses) {
        return AddressesWrapperDTO.builder()
                .addAllElements(addresses.stream()
                .map(id -> AddressDTO.builder().id(id).build())
                .collect(Collectors.toList()))
                .build();
    }

    private InstallationNetworkSyncDTO buildInstallationNetworkDTO(Optional<AccessPointWrapperDTO> accessPoints,
                                                                   Optional<InstallationDeviceWrapperDTO> installationDevices, Optional<InstallationMediaWrapperDTO> installationMedia) {
        return InstallationNetworkSyncDTO.builder()
                .accessPoints(accessPoints)
                .installationDevices(installationDevices)
                .installationMedia(installationMedia)
                .build();
    }

    private InstallationDeviceWrapperDTO buildInstallationDevicesDTO(Collection<Long> ids) {
        Set<ImmutableInstallationDevicesDTO> installationDevices = ids.stream()
                .map(id -> InstallationDevicesDTO.builder().id(id).build())
                .collect(toSet());
        return InstallationDeviceWrapperDTO.builder()
                .elements(installationDevices)
                .build();
    }

    private InstallationMediaWrapperDTO buildInstallationMediumsDTO(Collection<Long> mediumsIds) {
        Set<InstallationMediaDTO> installationDevices = mediumsIds.stream()
                .map(id -> InstallationMediaDTO.builder().id(id).build())
                .collect(toSet());
        return InstallationMediaWrapperDTO.builder()
                .elements(installationDevices)
                .build();
    }

    private AccessPointWrapperDTO buildAccessPointsDTO(Collection<Long> accessPointsIds) {
        Set<AccessPointDTO> installationDevices = accessPointsIds.stream()
                .map(id -> AccessPointDTO.builder().id(id).build())
                .collect(toSet());
        return AccessPointWrapperDTO.builder()
                .elements(installationDevices)
                .build();
    }

    private CentralOfficeSyncDTO buildCentralOfficeDTO(Map<Long, Collection<Long>> accessNodeToAccessInterfacesMapping) {
        List<AccessNodeDTO> accessNodes = accessNodeToAccessInterfacesMapping.keySet().stream()
                .map(accessNodeId -> buildAccessNodeDTO(accessNodeId, accessNodeToAccessInterfacesMapping.get(accessNodeId)))
                .collect(Collectors.toList());
        return CentralOfficeSyncDTO.builder()
                .accessNodes(AccessNodeWrapperDTO.builder()
                        .elements(accessNodes)
                        .build())
                .build();
    }

    private AccessNodeDTO buildAccessNodeDTO(Long accessNodeId, Collection<Long> accessInterfaceIds) {
        List<AccessInterfaceDTO> accessInterfaces =
                accessInterfaceIds.stream().map(id -> AccessInterfaceDTO.builder().id(id).build()).collect(Collectors.toList());
        return AccessNodeDTO.builder()
                .id(accessNodeId)
                .accessInterfaces(accessInterfaces)
                .build();
    }

}
