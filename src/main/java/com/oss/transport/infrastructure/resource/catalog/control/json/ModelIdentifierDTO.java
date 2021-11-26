package com.oss.transport.infrastructure.resource.catalog.control.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oss.transport.infrastructure.resource.catalog.control.ModelRepository;

import java.util.Optional;

public class ModelIdentifierDTO {

    public String name;
    @JsonIgnore
    public String manufacturer = ModelRepository.MANUFACTURER_NAME;
    public Optional<String> instanceName = Optional.empty();

}
