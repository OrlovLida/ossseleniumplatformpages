package com.oss.transport.infrastructure.navigation;

import com.oss.transport.infrastructure.ServicesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ModuleNavigationClient {
    private static final String LINK_DOES_NOT_EXIST = "Specific Link (%s) does not exist.";

    private static Collection<ModuleNavigation> MODULE_NAVIGATION_CACHE;

    private final ServicesClient servicesClient;
    private static DomainNavigationClient domainNavigationClient;

    public ModuleNavigationClient(ServicesClient servicesClient) {
        this.servicesClient = servicesClient;
        domainNavigationClient = new DomainNavigationClient(servicesClient);
        if (Objects.isNull(MODULE_NAVIGATION_CACHE)) {
            MODULE_NAVIGATION_CACHE = new ArrayList<>();
        }
    }

    public LinkDTO getDomainOperation(String moduleName, String domainName, String operationName) {
        ModuleNavigation moduleNavigation = findModuleNavigation(moduleName);
        LinkDTO domainNavigationLinkDTO = getDomainNavigationLinkDTO(moduleNavigation, domainName);
        return domainNavigationClient.getDomainOperationLinkDTO(moduleName, domainNavigationLinkDTO, operationName);
    }

    private ModuleNavigation findModuleNavigation(String moduleName) {
        Optional<ModuleNavigation> moduleNavigation =
                MODULE_NAVIGATION_CACHE.stream().filter(module -> module.getModuleName().equals(moduleName)).findFirst();

        if (!moduleNavigation.isPresent()) {
            List<LinkDTO> moduleNavigationLinks = getModuleNavigation(moduleName);
            ModuleNavigation moduleNav = new ModuleNavigation(moduleName, moduleNavigationLinks);
            MODULE_NAVIGATION_CACHE.add(moduleNav);
            return moduleNav;
        }

        return moduleNavigation.get();
    }

    private List<LinkDTO> getModuleNavigation(String moduleName) {
        String navigationUrl = Arrays.stream(SpecificModule.values())
                .filter(module -> Objects.equals(moduleName, module.getName())).findAny().map(SpecificModule::getUri).orElse("");
        return Arrays.asList(servicesClient.getRequestSpecificationByName(moduleName).when().get(navigationUrl)
                .then().log().status().log().body().extract().as(LinkDTO[].class));
    }

    private LinkDTO getDomainNavigationLinkDTO(ModuleNavigation moduleNavigation, String domainName) {
        return moduleNavigation.getLinks().stream().filter(domain -> domain.getRel().equals(domainName)).findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format(LINK_DOES_NOT_EXIST, domainName)));
    }
}
