package com.oss.repositories;

import com.comarch.oss.transport.ipaddress.management.api.dto.IPAddressTypeDTO;
import com.comarch.oss.transport.ipaddress.management.api.dto.IPNetworkDTO;
import com.comarch.oss.transport.ipaddress.management.api.dto.IPSubnetCreateDTO;
import com.comarch.oss.transport.ipaddress.management.api.dto.SubnetTypeDTO;
import com.comarch.oss.transport.ipaddress.management.api.dto.UsageStateDTO;
import com.oss.services.IPAMClient;
import com.oss.untils.Environment;

public class IPAMRepository {

    private final Environment env;

    public IPAMRepository(Environment env) {
        this.env = env;
    }

    public void getOrCreateIPNetwork(String name, String description) {
        IPAMClient client = IPAMClient.getInstance(env);
        if (!client.isIPNetworkPresent(name)) {
            IPNetworkDTO ipNetworkDTO = IPNetworkDTO.builder()
                    .name(name)
                    .description(description)
                    .build();
            client.createIPNetwork(ipNetworkDTO);
        }
    }

    public void getOrCreateIPv4Subnet(String identifier) {
        IPAMClient client = IPAMClient.getInstance(env);
        if (!client.isIPv4SubnetPresent(identifier)) {
            String[] parts = identifier.split(" ");
            String ipAddress = parts[0].substring(0, parts[0].indexOf("/"));
            String subnetMask = parts[0].substring(parts[0].indexOf("/") + 1);
            String networkName = parts[1].substring(1, parts[1].length() - 1);
            IPSubnetCreateDTO ipSubnetCreateDTO = IPSubnetCreateDTO.builder()
                    .address(ipAddress)
                    .maskLength(Integer.valueOf(subnetMask))
                    .networkName(networkName)
                    .status(UsageStateDTO.FREE)
                    .subnetType(SubnetTypeDTO.NETWORK)
                    .type(IPAddressTypeDTO.IPV4)
                    .build();
            client.createIPv4Subnet(ipSubnetCreateDTO);
        }
    }
}
