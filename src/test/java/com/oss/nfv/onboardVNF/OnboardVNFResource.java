/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.onboardVNF;

import com.comarch.oss.logical.function.api.dto.AttributeDTO;
import com.comarch.oss.logical.function.api.dto.LogicalLocationDTO;
import com.comarch.oss.logical.function.api.dto.ModelIdentificationDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionSyncDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO;
import com.oss.nfv.common.ResourceSpecification;

import org.assertj.core.util.Lists;

import java.util.Collections;
import java.util.List;

import static com.oss.nfv.onboardVNF.OnboardVNFConstants.MARKETPLACE_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.NFVO_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VIM_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VNFPKG_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VNFPKG_SPECIFICATION_IDENTIFIER;

/**
 * @author Marcin Kozioł
 */
public class OnboardVNFResource {

    private static final String SAMSUNG_NFVO_IDENTIFIER = "SamsungNFVO";
    private static final String VENDOR = "Vendor";
    private static final String CATEGORY = "Category";
    private static final String VENDOR_SAMSUNG = "Samsung";
    private static final String CATEGORY_NFVO = "NFVO";
    private static final String MASTER_OSS = "MasterOSS";
    private static final String TYPE_POP = "PoP";
    private static final String MARKETPLACE = "Marketplace";
    private static final String VIM = "VIM";
    private static final String GENERIC = "Generic";
    private static final String VNFPKG = "VNFPKG";

    public static ResourceSpecificationCreationDTO buildVNFPKGResourceSpecificationCreationDTO() {
        return ResourceSpecificationCreationDTO.builder()
                .id(VNFPKG_SPECIFICATION_IDENTIFIER)
                .name(VNFPKG_SPECIFICATION_IDENTIFIER)
                .type(ResourceSpecificationCreationDTO.TypeEnum.R)
                .baseType(VNFPKG)
                .instanceType(VNFPKG)
                .inventoryType(ResourceSpecification.LOGICAL_FUNCTION.getInventoryType())
                .build();
    }

    public static LogicalFunctionBulkDTO buildNFVOLogicalFunctionBulkDTO() {
        return LogicalFunctionBulkDTO.builder()
                .addCreateOrUpdate(buildNFVOLogicalFunctionSyncDTO())
                .build();
    }

    private static LogicalFunctionSyncDTO buildNFVOLogicalFunctionSyncDTO() {
        return LogicalFunctionSyncDTO.builder()
                .name(NFVO_NAME)
                .type(SAMSUNG_NFVO_IDENTIFIER)
                .model(getModelIdentification(SAMSUNG_NFVO_IDENTIFIER))
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

    public static LogicalFunctionBulkDTO buildVNFPKGLogicalFunctionBulkDTO(Long masterOSS) {
        return LogicalFunctionBulkDTO.builder()
                .addCreateOrUpdate(buildVNFPKGLogicalFunctionSyncDTO(masterOSS))
                .build();
    }

    public static LogicalFunctionSyncDTO buildVNFPKGLogicalFunctionSyncDTO(Long masterOSS) {
        return LogicalFunctionSyncDTO.builder()
                .name(VNFPKG_NAME)
                .type(VNFPKG)
                .model(getModelIdentification(VNFPKG_SPECIFICATION_IDENTIFIER))
                .addAttributes(getMasterOSS(masterOSS))
                .build();
    }

    public static LogicalFunctionBulkDTO buildMarketplaceLogicalFunctionBulkDTO(Long masterOSS) {
        return buildLogicalFunctionBulkDTO(MARKETPLACE, MARKETPLACE_NAME, getMarketplaceAttributes(masterOSS));
    }

    private static List<AttributeDTO> getMarketplaceAttributes(Long masterOSS) {
        return Collections.singletonList(getMasterOSS(masterOSS));
    }

    public static LogicalFunctionBulkDTO buildVIMLogicalFunctionBulkDTO(Long masterOSS) {
        return buildLogicalFunctionBulkDTO(VIM, VIM_NAME, getVIMAttributes(masterOSS));
    }

    private static List<AttributeDTO> getVIMAttributes(Long masterOSS) {
        return Lists.newArrayList(
                getMasterOSS(masterOSS),
                getAttributeDTO(CATEGORY, VIM),
                getAttributeDTO(VENDOR, GENERIC)
        );
    }

    private static AttributeDTO getAttributeDTO(String name, String value) {
        return AttributeDTO.builder()
                .name(name)
                .value(value)
                .build();
    }

    private static LogicalFunctionBulkDTO buildLogicalFunctionBulkDTO(String identifier,
                                                                      String name,
                                                                      List<AttributeDTO>  attributeDTOS) {
        return LogicalFunctionBulkDTO.builder()
                .addCreateOrUpdate(getLogicalFunctionSyncDTO(identifier, name, attributeDTOS))
                .build();
    }

    private static LogicalFunctionSyncDTO getLogicalFunctionSyncDTO(String identifier,
                                                                    String name,
                                                                    List<AttributeDTO> attributeDTOS) {
        return LogicalFunctionSyncDTO.builder()
                .name(name)
                .type(identifier)
                .model(getModelIdentification(identifier))
                .addAllAttributes(attributeDTOS)
                .build();
    }

    private static ModelIdentificationDTO getModelIdentification(String identifier) {
        return ModelIdentificationDTO.builder()
                .identifier(identifier)
                .resourceSpecification(true)
                .build();
    }

    private static AttributeDTO getMasterOSS(Long masterOSS) {
        return AttributeDTO.builder()
                .name(MASTER_OSS)
                .value(masterOSS.toString())
                .build();
    }
}
