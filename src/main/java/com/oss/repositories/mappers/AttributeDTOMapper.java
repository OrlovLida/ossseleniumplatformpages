package com.oss.repositories.mappers;

import com.comarch.oss.locationinventory.api.dto.AttributeDTO;

public class AttributeDTOMapper {

    private AttributeDTOMapper() {
    }

    static AttributeDTO from(Long id, String type) {
        return AttributeDTO.builder()
                .id(id)
                .type(type)
                .build();
    }
}
