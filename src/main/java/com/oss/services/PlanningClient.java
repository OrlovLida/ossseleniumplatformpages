/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.services;

import java.text.MessageFormat;
import java.util.Optional;
import com.comarch.oss.planning.api.dto.ObjectIdDTO;
import com.comarch.oss.planning.api.dto.ObjectsDescriptionDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.untils.Environment;

public class PlanningClient {
    
    private static final String CANNOT_FIND_OBJECT = "Cannot find object with type \"{0}\" and name \"{1}\"";
    
    private static final String QUERY__QUERY_PARAM = "query";
    private static final String RSQL_QUERY__NAME_PARAM = "Name==\"{0}\"";
    
    private static final String QUERY_OBJECTS_V2_PATH = "objects/v2/{0}";
    
    private static PlanningClient instance;
    
    private final Environment ENV;
    
    public static PlanningClient getInstance(Environment environment) {
        if (instance == null) {
            instance = new PlanningClient(environment);
        }
        return instance;
    }
    
    public PlanningClient(Environment env) {
        ENV = env;
    }
    
    public Long findExistingObjectIdByNameAndType(String objectName, String objectType) {
        Optional<Long> foundObject = findObjectIdByNameAndType(objectName, objectType);
        return foundObject.orElseThrow(() -> new IllegalStateException(MessageFormat.format(CANNOT_FIND_OBJECT, objectType, objectName)));
    }
    
    public Optional<Long> findObjectIdByNameAndType(String objectName, String objectType) {
        String rsqlQuery = MessageFormat.format(RSQL_QUERY__NAME_PARAM, objectName);
        ObjectsDescriptionDTO foundObjects = queryObjectsByRsqlQuery(rsqlQuery, objectType);
        return foundObjects.getObjectIds().stream()
                .findAny()
                .map(ObjectIdDTO::getId);
    }
    
    private ObjectsDescriptionDTO queryObjectsByRsqlQuery(String rsqlQuery, String objectType) {
        return ENV.getPlanningCoreSpecification()
                .given().contentType(ContentType.JSON).queryParam(QUERY__QUERY_PARAM, rsqlQuery)
                .log().path()
                .when().get(MessageFormat.format(QUERY_OBJECTS_V2_PATH, objectType))
                .then().log().status().log().body()
                .extract().as(ObjectsDescriptionDTO.class);
        
    }
    
}
