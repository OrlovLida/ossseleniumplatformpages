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

import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.CATEGORY_ATTRIBUTE_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_VENDOR_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.NFVO_CATEGORY_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_TYPE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_VENDOR_ATTRIBUTE_VALUE;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VENDOR_ATTRIBUTE_NAME;
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

    public static LogicalFunctionBulkDTO getCreateDto() {
        return LogicalFunctionBulkDTO.builder()
            .createOrUpdate(getSyncDtos())
            .build();
    }

    public static ResourceSpecificationCreationDTO getVNFPKGspec() {
        return ResourceSpecificationCreationDTO.builder()
            .name(VNFPKG_NAME)
            .id(VNFPKG_IDENTIFIER)
            .baseType(VNFPKG_TYPE)
            .instanceType(VNFPKG_TYPE)
            .provider(VNFPKG_PROVIDER)
            .resourceSpecCharacteristic(getVNFPKGcharacteristics())
            .type(TypeEnum.R)
            .version(VNFPKG_VERSION)
            .description(VNFPKG_DESCRIPTION)
            .build();
    }

    private static List<ResourceSpecCharacteristicDTO> getVNFPKGcharacteristics() {
        return ImmutableList.of(
            getPathCharacteristic()
        );
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

    private static List<LogicalFunctionSyncDTO> getSyncDtos() {
        return ImmutableList.of(
            getCreateEocmnfvoDto(),
            getCreateSamsungNfvoDto()
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
