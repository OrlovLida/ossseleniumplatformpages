package com.oss.fixedaccess.datafortests.AccessTechnology;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.oss.repositories.fixedaccessrepository.AccessTechnologyRepository;
import com.oss.untils.Environment;

public class AccessTechnologyController {

    private final AccessTechnologiesContainer accessTechnologiesContainer;
    private final AccessTechnologyRepository accessTechnologyRepository;
    private final List<String> accessTechnologiesNames = new ArrayList<String>() {{
        add("GPON");
        add("1G-EPON");
        add("10G-EPON");
        add("10G-1G-EPON");
        add("VDSL");
        add("VDSL2");
        add("ADSL");
        add("ADSL2");
        add("ADSL2+");
        add("DOCSIS 2.0");
        add("DOCSIS 3.0");
        add("DOCSIS 3.1");
    }};

    public AccessTechnologyController(Environment env) {
        accessTechnologiesContainer = new AccessTechnologiesContainer();
        accessTechnologyRepository = new AccessTechnologyRepository(env);
    }

    public void fillAccessTechnologyContainer() {
        for (String layer : accessTechnologiesNames) {
            accessTechnologiesContainer.addAccessTechnology(layer, new AccessTechnology(layer,
                    accessTechnologyRepository.getAccessTechDownloadSpeedByLayer(layer),
                    accessTechnologyRepository.getAccessTechUploadSpeedByLayer(layer),
                    accessTechnologyRepository.getAccessTechMediumTypeByLayer(layer)));
        }
    }

    public AccessTechnology getAccessTechnology(String layer) {
        return accessTechnologiesContainer.getAccessTechnology(layer);
    }

    public Set<String> getAccessTechnologiesNamesSet() {
        return accessTechnologiesContainer.getAccessTechnologiesContainerKeySet();
    }

}
