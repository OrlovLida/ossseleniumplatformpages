package com.oss.transport.infrastructure.navigation;

import java.util.List;

public class ModuleNavigation {
    private String moduleName;
    private List<LinkDTO> links;

    public ModuleNavigation(String moduleName, List<LinkDTO> links) {
        this.moduleName = moduleName;
        this.links = links;
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<LinkDTO> getLinks() {
        return links;
    }

}
