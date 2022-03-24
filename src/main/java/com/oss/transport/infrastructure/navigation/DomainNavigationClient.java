package com.oss.transport.infrastructure.navigation;

import com.oss.transport.infrastructure.ServicesClient;

import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DomainNavigationClient {
    private static List<DomainNavigation> DOMAIN_NAVIGATION_CACHE;
    
    private final ServicesClient servicesClient;
    
    public DomainNavigationClient(ServicesClient servicesClient) {
        this.servicesClient = servicesClient;
        if (Objects.isNull(DOMAIN_NAVIGATION_CACHE)) {
            this.DOMAIN_NAVIGATION_CACHE = new ArrayList<>();
        }
    }
    
    public LinkDTO getDomainOperationLinkDTO(String moduleName, LinkDTO domainNavigation, String operationName) {
        DomainNavigation searchedDomainNavigation = findDomainNavigation(moduleName, domainNavigation);
        return searchedDomainNavigation.findOperation(operationName);
    }

    private DomainNavigation findDomainNavigation(String moduleName, LinkDTO domainNavigation) {
        Optional<DomainNavigation> searchedDomainNavigation = DOMAIN_NAVIGATION_CACHE.stream()
                .filter(domain -> domain.getDomainName().equals(domainNavigation.getRel()) && domain.getModuleName().equals(moduleName))
                .findFirst();
        if (!searchedDomainNavigation.isPresent()) {
            List<LinkDTO> domainLinks = getDomainLinkDTOs(moduleName, domainNavigation.getHref());
            DomainNavigation domainNav = new DomainNavigation(moduleName, domainNavigation.getRel(), domainLinks);
            DOMAIN_NAVIGATION_CACHE.add(domainNav);
            return domainNav;
        }
        return searchedDomainNavigation.get();
    }
    
    private List<LinkDTO> getDomainLinkDTOs(String moduleName, String url) {
        LinkDTO[] ethernetNavigation = servicesClient.getRequestSpecificationByName(moduleName)
                .when().get(UriBuilder.fromPath(url).build())
                .then().log().status().log().body().extract().as(LinkDTO[].class);
        
        return Arrays.asList(ethernetNavigation);
    }
    
}
