package com.oss.transport.infrastructure.resource.catalog.control.json;

import com.comarch.oss.resourcecatalog.api.dto.PortLogicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.PortPhysicalCrossconnectDTO;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PluggableModuleModelDTO implements Model {

    public String instanceName;
    @JsonIgnore
    public String type = "PluggableModuleModel";
    public Map<String, String> simpleAttributes = Collections.emptyMap();
    public Map<String, ReferenceAttributeDTO> referenceAttributes = Collections.emptyMap();
    public TerminationPointsDTO terminationPoints;
    public List<PortPhysicalCrossconnectDTO> physicalCrossconnects = Collections.emptyList();
    public List<PortLogicalCrossconnectDTO> dsrCrossconnects = Collections.emptyList();

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
