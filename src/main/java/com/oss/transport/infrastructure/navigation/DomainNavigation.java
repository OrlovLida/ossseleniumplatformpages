package com.oss.transport.infrastructure.navigation;

import java.util.List;

public class DomainNavigation {
    private static final String LINK_DOES_NOT_EXIST = "Specific Link (%s) does not exist.";

    private String moduleName;
    private String domainName;
    private List<LinkDTO> links;

    public DomainNavigation(String moduleName, String domainName, List<LinkDTO> links) {
        this.moduleName = moduleName;
        this.domainName = domainName;
        this.links = links;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public LinkDTO findOperation(String operationName) {
        return links.stream().filter(link -> operationName.equals(link.getRel())).findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format(LINK_DOES_NOT_EXIST, operationName)));
    }
}
