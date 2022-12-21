package com.oss.services;

import com.comarch.oss.validationresults.api.dto.ObjectValidationResultsDTO;
import com.comarch.oss.validationresults.api.dto.ValidationResultSuppressionsDTO;
import com.jayway.restassured.http.ContentType;
import com.oss.planning.PlanningContext;
import com.oss.untils.Environment;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.oss.untils.Constants.PERSPECTIVE;
import static com.oss.untils.Constants.PROJECT_ID;

public class ValidationResultsClient {
    private static final String OBJECT_ID_PARAMETER_ID = "object_id";
    private static final String WITH_SUPPRESSED_PARAMETER_ID = "with_suppressed";
    private static final String WITH_VALID_PARAMETER_ID = "with_valid";
    private static final String VR_API_PATH = "/vr";
    private static final String SUPPRESSION_API_PATH = "/suppressions";

    private static final String QUERY_VR_BY_OBJECT_API_PATH = VR_API_PATH + "/resource-type/%s";
    private final Environment env;

    public ValidationResultsClient(Environment env) {
        this.env = env;
    }

    public void saveValidationResults(List<ObjectValidationResultsDTO> objectValidationResultsDTOList) {
        env.getValidationResultsCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(objectValidationResultsDTOList)
                .when()
                .post(VR_API_PATH)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public void saveSuppressions(ValidationResultSuppressionsDTO suppressionsDTO) {
        env.getValidationResultsCoreSpecification()
                .given()
                .contentType(ContentType.JSON)
                .body(suppressionsDTO)
                .when()
                .post(SUPPRESSION_API_PATH)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).assertThat();
    }

    public List<ObjectValidationResultsDTO> getValidationResultsByObjects(
            String objectType, List<String> objectIds, PlanningContext planningContext, boolean withSuppressed, boolean withValid) {
        String vrQueryPath = String.format(QUERY_VR_BY_OBJECT_API_PATH, objectType);
        if (planningContext.isPlanContext()) {
            return Arrays.stream(env.getValidationResultsCoreSpecification()
                            .given()
                            .queryParam(PERSPECTIVE, PlanningContext.Perspective.PLAN)
                            .queryParam(PROJECT_ID, planningContext.getProjectId())
                            .queryParam(OBJECT_ID_PARAMETER_ID, objectIds)
                            .queryParam(WITH_SUPPRESSED_PARAMETER_ID, withSuppressed)
                            .queryParam(WITH_VALID_PARAMETER_ID, withValid)
                            .when()
                            .get(vrQueryPath)
                            .then()
                            .log().body()
                            .extract()
                            .as(ObjectValidationResultsDTO[].class))
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(env.getValidationResultsCoreSpecification()
                            .given()
                            .queryParam(PERSPECTIVE, planningContext.getPerspective())
                            .queryParam(OBJECT_ID_PARAMETER_ID, objectIds)
                            .queryParam(WITH_SUPPRESSED_PARAMETER_ID, withSuppressed)
                            .queryParam(WITH_VALID_PARAMETER_ID, withValid)
                            .when()
                            .get(vrQueryPath)
                            .then()
                            .log().body()
                            .extract()
                            .as(ObjectValidationResultsDTO[].class))
                    .collect(Collectors.toList());
        }
    }

}
