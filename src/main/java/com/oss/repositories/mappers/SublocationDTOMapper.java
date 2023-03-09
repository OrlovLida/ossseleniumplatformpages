package com.oss.repositories.mappers;

import java.util.Optional;

import com.comarch.oss.locationinventory.api.dto.ImmutableSublocationDTO;
import com.comarch.oss.locationinventory.api.dto.SublocationDTO;
import com.oss.repositories.entities.Sublocation;

public class SublocationDTOMapper {

    private SublocationDTOMapper() {}

    public static SublocationDTO from(Sublocation sublocation) {
        ImmutableSublocationDTO.Builder builder = SublocationDTO.builder();

        Optional<Long> subLocationModelId = sublocation.getSubLocationModelId();
        Optional<String> subLocationModelType = sublocation.getSubLocationModelType();
        if (subLocationModelId.isPresent() && subLocationModelType.isPresent()) {
            builder.model(AttributeDTOMapper.from(subLocationModelId.get(), subLocationModelType.get()));
        }

        return builder.location(AttributeDTOMapper.from(sublocation.getParentLocationId(), sublocation.getParentLocationType()))
                .preciseLocation(AttributeDTOMapper.from(sublocation.getPreciseLocation(), sublocation.getPreciseLocationType()))
                .name(sublocation.getSubLocationName())
                .type(sublocation.getSubLocationType())
                .build();
    }
}
