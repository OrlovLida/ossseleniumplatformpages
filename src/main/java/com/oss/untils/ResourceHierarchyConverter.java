package com.oss.untils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyElementDTO;
import com.comarch.oss.resourcehierarchy.api.dto.ResourceIdentifierDTO;

public class ResourceHierarchyConverter {

    private ResourceHierarchyConverter() {}

    public static Map<String, List<String>> toHierarchiesByParent(ResourceHierarchyDTO resourceHierarchyDTO) {
        Map<ResourceIdentifierDTO, List<ResourceHierarchyElementDTO>> hierarchyByParent = resourceHierarchyDTO.getHierarchy().stream()
                .filter(element -> element.getParentId().isPresent())
                .collect(Collectors.groupingBy(element -> element.getParentId().get()));

        return hierarchyByParent.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> toIdWithType(entry.getKey()),
                        entry -> toParentIdsWithTypes(entry.getValue())
                ));
    }

    private static String toIdWithType(ResourceIdentifierDTO resourceIdentifierDTO) {
        return resourceIdentifierDTO.getType() + "_" + resourceIdentifierDTO.getIdentifier();
    }

    private static List<String> toParentIdsWithTypes(Collection<ResourceHierarchyElementDTO> resourceIdentifierDTOs) {
        return resourceIdentifierDTOs.stream()
                .map(ResourceHierarchyElementDTO::getId)
                .map(ResourceHierarchyConverter::toIdWithType)
                .collect(Collectors.toList());
    }

}
