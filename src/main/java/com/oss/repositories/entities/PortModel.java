/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.repositories.entities;

import java.util.Collections;
import java.util.Map;
import com.comarch.oss.resourcecatalog.api.dto.TerminationPointsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PortModel implements Model {
    
    @JsonIgnore
    private final String type = "EquipmentInterfaceType";
    
    private String instanceName;
    private Map<String, String> simpleAttributes = Collections.emptyMap();
    private Map<String, ReferenceAttribute> referenceAttributes = Collections.emptyMap();
    private TerminationPointsDTO terminationPoints;
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
    
    @Override
    public Map<String, String> getSimpleAttributes() {
        return simpleAttributes;
    }
    
    public void setSimpleAttributes(Map<String, String> simpleAttributes) {
        this.simpleAttributes = simpleAttributes;
    }
    
    @Override
    public Map<String, ReferenceAttribute> getReferenceAttributes() {
        return referenceAttributes;
    }
    
    public void setReferenceAttributes(Map<String, ReferenceAttribute> referenceAttributes) {
        this.referenceAttributes = referenceAttributes;
    }
    
    public TerminationPointsDTO getTerminationPoints() {
        return terminationPoints;
    }
    
    public void setTerminationPoints(TerminationPointsDTO terminationPoints) {
        this.terminationPoints = terminationPoints;
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public String getModelName() {
        return simpleAttributes.get(NAME_ATTRIBUTE);
    }
    
}
