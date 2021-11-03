package com.oss.transport.infrastructure.resource.catalog.control.json;

import java.util.Map;

public interface Model {

    String getType();

    Map<String, String> getSimpleAttributes();

    Map<String, ReferenceAttributeDTO> getReferenceAttributes();

}
