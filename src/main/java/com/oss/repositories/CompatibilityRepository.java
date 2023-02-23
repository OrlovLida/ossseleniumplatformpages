package com.oss.repositories;

import java.util.List;

import com.comarch.oss.resourcecatalog.api.dto.CompatibilityDTO;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;

public class CompatibilityRepository {

    private final ResourceCatalogClient resourceCatalogClient;

    public CompatibilityRepository(Environment environment) {
        resourceCatalogClient = ResourceCatalogClient.getInstance(environment);
    }

    public List<CompatibilityDTO> getSlotModelCompatibilities(Long slotModelId) {
        return resourceCatalogClient.getSlotModelCompatibilities(slotModelId);
    }

}