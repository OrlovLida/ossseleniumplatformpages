package com.oss.nfv.vnfpkg;

import java.util.List;

import com.comarch.oss.logical.function.api.dto.AttributeDTO;
import com.comarch.oss.logical.function.api.dto.ModelIdentificationDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionBulkDTO;
import com.comarch.oss.logical.function.v2.api.dto.LogicalFunctionSyncDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecCharacteristicDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO.TypeEnum;
import com.google.common.collect.ImmutableList;
import com.oss.nfv.common.ResourceSpecification;
import org.assertj.core.util.Lists;

import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.CATEGORY_ATTRIBUTE_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_VENDOR_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.GENERIC_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.MARKETPLACE_SAMSUNGNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.MARKETPLACE_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.MASTER_MANAGEMENT_SYSTEM_RELATION_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.NFVO_CATEGORY_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_VENDOR_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VENDOR_ATTRIBUTE_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VIM_CATEGORY_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VIM_SAMSUNGNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VIM_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFM_EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFM_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_DESCRIPTION;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_IDENTIFIER;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_PATH_CHARACTERISTIC_DEFAULT_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_PATH_CHARACTERISTIC_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_PATH_CHARACTERISTIC_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_PROVIDER;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_VERSION;

public class VNFPKGManualOnboardingDtoBuilder {

    private VNFPKGManualOnboardingDtoBuilder() {

    }

    public static LogicalFunctionBulkDTO getNFVOsCreateDto() {
        return LogicalFunctionBulkDTO.builder()
            .createOrUpdate(getNFVOsSyncDto())
            .build();
    }

    public static LogicalFunctionBulkDTO getRelatedObjectsCreateDto(Long eocmNfvoId, Long samsungNfvoId) {
        return LogicalFunctionBulkDTO.builder()
            .createOrUpdate(getRelatedObjectsSyncDto(eocmNfvoId, samsungNfvoId))
            .build();
    }

    public static ResourceSpecificationCreationDTO getVNFPKGspec() {
        return ResourceSpecificationCreationDTO.builder()
            .name(VNFPKG_NAME)
            .id(VNFPKG_IDENTIFIER)
            .baseType(VNFPKG_TYPE)
            .instanceType(VNFPKG_TYPE)
            .inventoryType(ResourceSpecification.LOGICAL_FUNCTION.getInventoryType())
            .provider(VNFPKG_PROVIDER)
            .addResourceSpecCharacteristic(getPathCharacteristic())
            .type(TypeEnum.R)
            .version(VNFPKG_VERSION)
            .description(VNFPKG_DESCRIPTION)
            .build();
    }

    private static ResourceSpecCharacteristicDTO getPathCharacteristic() {
        return ResourceSpecCharacteristicDTO.builder()
            .name(VNFPKG_PATH_CHARACTERISTIC_NAME)
            .defaultValue(VNFPKG_PATH_CHARACTERISTIC_DEFAULT_VALUE)
            .isEnum(false)
            .isUnique(false)
            .mandatory(false)
            .isBusinessAdditionalAttr(true)
            .type(VNFPKG_PATH_CHARACTERISTIC_TYPE)
            .build();
    }

    private static List<LogicalFunctionSyncDTO> getNFVOsSyncDto() {
        return ImmutableList.of(
            getCreateEocmnfvoDto(),
            getCreateSamsungNfvoDto()
        );
    }

    private static List<LogicalFunctionSyncDTO> getRelatedObjectsSyncDto(Long eocmNfvoId, Long samsungNfvoId) {
        return ImmutableList.of(
            getCreateEocmNfvoVnfmDto(eocmNfvoId),
            getCreateSamsungNfvoMarketplaceDto(samsungNfvoId),
            getCreateSamsungNfvoVimDto(samsungNfvoId)
        );
    }

    private static LogicalFunctionSyncDTO getCreateSamsungNfvoMarketplaceDto(Long samsungNfvoId) {
        return getCreateNfvoRelatedDto(MARKETPLACE_SAMSUNGNFVO_NAME, MARKETPLACE_TYPE, getMasterManagementSystemRelation(samsungNfvoId));
    }

    private static LogicalFunctionSyncDTO getCreateSamsungNfvoVimDto(Long samsungNfvoId) {
        return getCreateNfvoRelatedDto(VIM_SAMSUNGNFVO_NAME, VIM_TYPE, getVIMAttributes(samsungNfvoId));
    }

    private static LogicalFunctionSyncDTO getCreateEocmNfvoVnfmDto(Long eocmNfvoId) {
        return getCreateNfvoRelatedDto(VNFM_EOCMNFVO_NAME, VNFM_TYPE, getMasterManagementSystemRelation(eocmNfvoId));
    }

    private static LogicalFunctionSyncDTO getCreateNfvoRelatedDto(String name,
                                                                  String type,
                                                                  List<AttributeDTO> attributeDTOS) {
        return LogicalFunctionSyncDTO.builder()
            .name(name)
            .type(type)
            .model(getRsModel(type))
            .attributes(attributeDTOS)
            .build();
    }

    private static List<AttributeDTO> getMasterManagementSystemRelation(Long eocmNfvoId) {
        return ImmutableList.of(
            getAttribute(MASTER_MANAGEMENT_SYSTEM_RELATION_NAME, eocmNfvoId.toString())
        );
    }

    private static List<AttributeDTO> getVIMAttributes(Long masterManagementSystem) {
        return Lists.newArrayList(
                getAttribute(MASTER_MANAGEMENT_SYSTEM_RELATION_NAME, masterManagementSystem.toString()),
                getAttribute(CATEGORY_ATTRIBUTE_NAME, VIM_CATEGORY_ATTRIBUTE_VALUE),
                getAttribute(VENDOR_ATTRIBUTE_NAME, GENERIC_ATTRIBUTE_VALUE)
        );
    }

    private static LogicalFunctionSyncDTO getCreateSamsungNfvoDto() {
        return LogicalFunctionSyncDTO.builder()
            .name(SamsungNFVO_NAME)
            .type(SamsungNFVO_TYPE)
            .model(getRsModel(SamsungNFVO_TYPE))
            .attributes(getCreateSamsungNfvoAttributes())
            .build();
    }

    private static LogicalFunctionSyncDTO getCreateEocmnfvoDto() {
        return LogicalFunctionSyncDTO.builder()
            .name(EOCMNFVO_NAME)
            .type(EOCMNFVO_TYPE)
            .model(getRsModel(EOCMNFVO_TYPE))
            .attributes(getCreateEocmnfvoAttributes())
            .build();
    }

    private static ModelIdentificationDTO getRsModel(String type) {
        return ModelIdentificationDTO.builder()
            .identifier(type)
            .resourceSpecification(true)
            .build();
    }

    private static List<AttributeDTO> getCreateSamsungNfvoAttributes() {
        return ImmutableList.of(
            getAttribute(VENDOR_ATTRIBUTE_NAME, SamsungNFVO_VENDOR_ATTRIBUTE_VALUE),
            getAttribute(CATEGORY_ATTRIBUTE_NAME, NFVO_CATEGORY_ATTRIBUTE_VALUE)
        );
    }

    private static List<AttributeDTO> getCreateEocmnfvoAttributes() {
        return ImmutableList.of(
            getAttribute(VENDOR_ATTRIBUTE_NAME, EOCMNFVO_VENDOR_ATTRIBUTE_VALUE),
            getAttribute(CATEGORY_ATTRIBUTE_NAME, NFVO_CATEGORY_ATTRIBUTE_VALUE)
        );
    }

    private static AttributeDTO getAttribute(String name, String value) {
        return AttributeDTO.builder()
            .name(name)
            .value(value)
            .build();
    }
}
