package com.oss.fixedaccess.datafortests.AccessTechnology;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccessTechnologiesContainer {

    private Map<String, AccessTechnology> accessTechnologiesHashMap;

    public AccessTechnologiesContainer() {
        accessTechnologiesHashMap = new HashMap<>();
    }

    public void addAccessTechnology(String accessTechnologyName, AccessTechnology accessTechnology) {
        accessTechnologiesHashMap.put(accessTechnologyName, accessTechnology);
    }

    public AccessTechnology getAccessTechnology(String accessTechnologyName) {
        return accessTechnologiesHashMap.get(accessTechnologyName);
    }

    public Set<String> getAccessTechnologiesContainerKeySet() {
        return accessTechnologiesHashMap.keySet();
    }
}
