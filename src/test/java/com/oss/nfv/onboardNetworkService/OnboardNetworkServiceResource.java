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
import org.assertj.core.util.Lists;

import java.util.List;

import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.NFVO_NAME;

/**
 * @author Marcin Kozioł
 */
public class OnboardNetworkServiceResource {

    private static final String ERICSSON_NFVO_IDENTIFIER = "EOCMNFVO";
    private static final String VENDOR = "Vendor";
    private static final String CATEGORY = "Category";
    private static final String VENDOR_SAMSUNG = "Samsung";
    private static final String CATEGORY_NFVO = "NFVO";
    private static final String TYPE_POP = "PoP";


    public static LogicalFunctionBulkDTO buildNFVOLogicalFunctionBulkDTO() {
        return LogicalFunctionBulkDTO.builder()
                .addCreateOrUpdate(buildNFVOLogicalFunctionSyncDTO())
                .build();
    }

    private static LogicalFunctionSyncDTO buildNFVOLogicalFunctionSyncDTO() {
        return LogicalFunctionSyncDTO.builder()
                .name(NFVO_NAME)
                .type(ERICSSON_NFVO_IDENTIFIER)
                .model(getModelIdentification())
                .addAllAttributes(getNFVOMandatoryAdditionalAttributes())
                .logicalLocation(getLogicalLocation())
                .build();
    }

    private static List<AttributeDTO> getNFVOMandatoryAdditionalAttributes() {
        AttributeDTO vendor = getVendor();
        AttributeDTO category = getCategory();
        return Lists.list(vendor, category);
    }

    private static AttributeDTO getVendor() {
        return AttributeDTO.builder()
                .name(VENDOR)
                .value(VENDOR_SAMSUNG)
                .build();
    }

    private static AttributeDTO getCategory() {
        return AttributeDTO.builder()
                .name(CATEGORY)
                .value(CATEGORY_NFVO)
                .build();
    }

    private static LogicalLocationDTO getLogicalLocation() {
        return LogicalLocationDTO.builder()
                .type(TYPE_POP)
                .build();
    }

    private static ModelIdentificationDTO getModelIdentification() {
        return ModelIdentificationDTO.builder()
                .identifier(OnboardNetworkServiceResource.ERICSSON_NFVO_IDENTIFIER)
                .resourceSpecification(true)
                .build();
    }

}
