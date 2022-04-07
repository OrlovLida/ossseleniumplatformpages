/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.manualOnboardNetworkService;

import java.util.List;

import org.assertj.core.util.Lists;

import com.comarch.oss.logical.function.api.dto.AttributeDTO;
import com.comarch.oss.logical.function.api.dto.LogicalLocationDTO;
import com.comarch.oss.logical.function.api.dto.ModelIdentificationDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionSyncDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO.TypeEnum;

import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_CATEGORY_ATTR;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_CATEGORY_VALUE;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_DESCRIPTION;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_IDENTIFIER;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_LOCATION_TYPE;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_NAME;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_VENDOR_ATTR;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_VENDOR_VALUE;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NS_PACKAGE_BASE_TYPE;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NS_PACKAGE_DESCRIPTION;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NS_PACKAGE_IDENTIFIER;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NS_PACKAGE_VERSION;

/**
 * @author Marcin Kozioł
 */
public class ManualOnboardNetworkServiceDTOBuilder {

    public static LogicalFunctionBulkDTO buildNFVOLogicalFunctionBulkDTO() {
        return LogicalFunctionBulkDTO.builder()
                .addCreateOrUpdate(buildNFVOLogicalFunctionSyncDTO())
                .build();
    }

    public static ResourceSpecificationCreationDTO buildNSPackageSpecification() {
        return ResourceSpecificationCreationDTO.builder()
                .name(NS_PACKAGE_IDENTIFIER)
                .id(NS_PACKAGE_IDENTIFIER)
                .baseType(NS_PACKAGE_BASE_TYPE)
                .instanceType(NS_PACKAGE_BASE_TYPE)
                .type(TypeEnum.TEMPLATE)
                .version(NS_PACKAGE_VERSION)
                .description(NS_PACKAGE_DESCRIPTION)
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
