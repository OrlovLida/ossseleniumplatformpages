/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.onboardNetworkService;

import com.comarch.oss.logical.function.api.dto.AttributeDTO;
import com.comarch.oss.logical.function.api.dto.LogicalLocationDTO;
import com.comarch.oss.logical.function.api.dto.ModelIdentificationDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionSyncDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO.TypeEnum;

import org.assertj.core.util.Lists;

import java.util.List;

import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_CATEGORY_ATTR;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_CATEGORY_VALUE;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_DESCRIPTION;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_IDENTIFIER;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_NAME;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_LOCATION_TYPE;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_VENDOR_ATTR;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_VENDOR_VALUE;

/**
 * @author Marcin Kozioł
 */
public class OnboardNetworkServiceDTOBuilder {

    public static LogicalFunctionBulkDTO buildNFVOLogicalFunctionBulkDTO() {
        return LogicalFunctionBulkDTO.builder()
                .addCreateOrUpdate(buildNFVOLogicalFunctionSyncDTO())
                .build();
    }

    private static LogicalFunctionSyncDTO buildNFVOLogicalFunctionSyncDTO() {
        return LogicalFunctionSyncDTO.builder()
                .name(ERICSSON_NFVO_NAME)
                .type(ERICSSON_NFVO_IDENTIFIER)
                .model(getModelIdentification())
                .addAllAttributes(getNFVOAttributes())
                .logicalLocation(getLogicalLocation())
                .description(ERICSSON_NFVO_DESCRIPTION)
                .build();
    }

    private static List<AttributeDTO> getNFVOAttributes() {
        return Lists.list(
                getAttributeDTO(ERICSSON_NFVO_VENDOR_ATTR, ERICSSON_NFVO_VENDOR_VALUE),
                getAttributeDTO(ERICSSON_NFVO_CATEGORY_ATTR, ERICSSON_NFVO_CATEGORY_VALUE));
    }

    private static AttributeDTO getAttributeDTO(String name, String value) {
        return AttributeDTO.builder()
                .name(name)
                .value(value)
                .build();
    }

    private static LogicalLocationDTO getLogicalLocation() {
        return LogicalLocationDTO.builder()
                .type(ERICSSON_NFVO_LOCATION_TYPE)
                .build();
    }

    private static ModelIdentificationDTO getModelIdentification() {
        return ModelIdentificationDTO.builder()
                .identifier(ERICSSON_NFVO_IDENTIFIER)
                .resourceSpecification(true)
                .build();
    }

}
