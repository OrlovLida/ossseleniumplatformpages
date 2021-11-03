package com.oss.transport.infrastructure.resource.catalog.control.json;

import com.comarch.oss.resourcecatalog.api.dto.LogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PhysicalCrossconnectDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DeviceModelDTO implements Model {

    public String type;
    public Map<String, String> simpleAttributes = Collections.emptyMap();
    public Map<String, ReferenceAttributeDTO> referenceAttributes = Collections.emptyMap();
    public List<String> supportedLayers = Collections.emptyList();
    public List<ChassisDTO> chassis = Collections.emptyList();
    public List<PortDTO> ports = Collections.emptyList();
    public List<PhysicalCrossconnectDTO> physicalCrossconnects = Collections.emptyList();
    public List<LogicalCrossconnectDTO> dsrCrossconnects = Collections.emptyList();

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Map<String, String> getSimpleAttributes() {
        return simpleAttributes;
    }

    @Override
    public Map<String, ReferenceAttributeDTO> getReferenceAttributes() {
        return referenceAttributes;
    }
}
