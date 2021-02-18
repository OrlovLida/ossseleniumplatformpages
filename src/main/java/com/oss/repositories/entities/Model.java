/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.repositories.entities;

import java.util.Map;

public interface Model {
    
    static final String NAME_ATTRIBUTE = "Name";
    
    String getType();
    
    String getModelName();
    
    Map<String, String> getSimpleAttributes();
    
    Map<String, ReferenceAttribute> getReferenceAttributes();
    
}
