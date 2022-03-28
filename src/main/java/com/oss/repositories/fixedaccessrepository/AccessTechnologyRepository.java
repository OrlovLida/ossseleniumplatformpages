package com.oss.repositories.fixedaccessrepository;

import java.util.Arrays;

import com.comarch.oss.inventory.fixedaccess.layer.api.dto.AccessTechnologyLayerDTO;
import com.oss.services.fixedaccessclient.AccessTechnologyClient;
import com.oss.untils.Environment;

public class AccessTechnologyRepository {

    private final AccessTechnologyClient accessTechnologyClient;

    public AccessTechnologyRepository(Environment env) {
        accessTechnologyClient = AccessTechnologyClient.getInstance(env);
    }

    public String getAccessTechDownloadSpeedByLayer(String layerName) {
        AccessTechnologyLayerDTO[] accessTechnologyLayerDTOS = accessTechnologyClient.getAllAccessTechnologies();
        return Arrays.stream(accessTechnologyLayerDTOS)
                .filter(layer -> layer.getLayer().equals(layerName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find layer:" + layerName))
                .getLayerParameters()
                .get(0)
                .getMaxDownloadSpeed()
                .toString();
    }

    public String getAccessTechUploadSpeedByLayer(String layerName) {
        AccessTechnologyLayerDTO[] accessTechnologyLayerDTOS = accessTechnologyClient.getAllAccessTechnologies();
        return Arrays.stream(accessTechnologyLayerDTOS)
                .filter(layer -> layer.getLayer().equals(layerName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find layer:" + layerName))
                .getLayerParameters()
                .get(0)
                .getMaxUploadSpeed()
                .toString();
    }

    public String getAccessTechMediumTypeByLayer(String layerName) {
        AccessTechnologyLayerDTO[] accessTechnologyLayerDTOS = accessTechnologyClient.getAllAccessTechnologies();
        return Arrays.stream(accessTechnologyLayerDTOS)
                .filter(layer -> layer.getLayer().equals(layerName))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can't find layer:" + layerName))
                .getLayerParameters()
                .get(0)
                .getMediumType()
                .toString();
    }

}
