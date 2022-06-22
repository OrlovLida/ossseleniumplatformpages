/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.networkSliceSubnet;

import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationCreationDTO;
import com.oss.nfv.common.ResourceSpecification;


import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER;

/**
 * @author Marcin Kozioł
 */
public class NetworkSliceSubnetResource {

    public static final String NETWORK_SLICE_SUBNET = "NetworkSliceSubnet";

    public static ResourceSpecificationCreationDTO buildNetworkSliceSubnetResourceSpecificationCreationDTO() {
        return ResourceSpecificationCreationDTO.builder()
                .id(NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER)
                .name(NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER)
                .type(ResourceSpecificationCreationDTO.TypeEnum.R)
                .baseType(NETWORK_SLICE_SUBNET)
                .instanceType(NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER)
                .inventoryType(ResourceSpecification.LOGICAL_FUNCTION.getInventoryType())
                .build();
    }
}
