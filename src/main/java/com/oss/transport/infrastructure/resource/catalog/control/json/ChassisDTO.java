package com.oss.transport.infrastructure.resource.catalog.control.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ChassisDTO implements Model {

    public String instanceName;
    @JsonIgnore
    public String type = "ChassisModel";
    public Map<String, String> simpleAttributes = Collections.emptyMap();
    public Map<String, ReferenceAttributeDTO> referenceAttributes = Collections.emptyMap();
    public List<SlotDTO> slots = Collections.emptyList();

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
