/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.repositories.entities;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DeviceModel implements Model {
    
    private String type;
    private Map<String, String> simpleAttributes = Collections.emptyMap();
    private Map<String, ReferenceAttribute> referenceAttributes = Collections.emptyMap();
    private List<PortModel> ports = Collections.emptyList();
    
    @Override
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
    
    public List<PortModel> getPorts() {
        return ports;
    }
    
    public void setPorts(List<PortModel> ports) {
        this.ports = ports;
    }
    
    @Override
    public String getModelName() {
        return simpleAttributes.get(NAME_ATTRIBUTE);
    }
    
}
