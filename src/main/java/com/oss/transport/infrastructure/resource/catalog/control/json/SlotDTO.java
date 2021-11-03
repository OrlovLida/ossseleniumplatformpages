package com.oss.transport.infrastructure.resource.catalog.control.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.Map;

public class SlotDTO implements Model {

    public String instanceName;
    @JsonIgnore
    public String type = "SlotModel";
    public Map<String, String> simpleAttributes = Collections.emptyMap();
    public Map<String, ReferenceAttributeDTO> referenceAttributes = Collections.emptyMap();

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
