package com.oss.repositories;

import com.comarch.oss.services.infrastructure.commonapi.dto.ObjectIdentifierDTO;
import com.comarch.oss.services.infrastructure.commonapi.dto.PerspectiveEnum;
import com.comarch.oss.services.infrastructure.commonapi.dto.PlanningContextDTO;
import com.comarch.oss.validationresults.api.dto.ObjectValidationResultsDTO;
import com.comarch.oss.validationresults.api.dto.ValidationResultDTO;
import com.comarch.oss.validationresults.api.dto.ValidationResultSuppressionDTO;
import com.comarch.oss.validationresults.api.dto.ValidationResultSuppressionDetailsDTO;
import com.comarch.oss.validationresults.api.dto.ValidationResultSuppressionsDTO;
import com.oss.planning.ObjectIdentifier;
import com.oss.planning.PlanningContext;
import com.oss.planning.validationresults.ValidationResult;
import com.oss.services.ValidationResultsClient;
import com.oss.untils.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ValidationResultsRepository {
    private final Environment env;


    public ValidationResultsRepository(Environment env) {
        this.env = env;
    }

    private ValidationResultsClient getValidationResultsClient() {
        return new ValidationResultsClient(env);
    }

    private PlanningContextDTO getPlanningContextDTO(PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            return PlanningContextDTO.builder()
                    .projectId(planningContext.getProjectId())
                    .perspective(PerspectiveEnum.PLAN)
                    .build();
        } else {
            return PlanningContextDTO.builder()
                    .perspective(PerspectiveEnum.valueOf(planningContext.getPerspective().name()))
                    .build();
        }
    }

    public void suppressValidationResult(UUID validationResultId, String suppressionReason) {
        getValidationResultsClient().saveSuppressions(ValidationResultSuppressionsDTO.builder()
                .addSuppressions(ValidationResultSuppressionDTO.builder()
                        .validationResultId(validationResultId)
                        .suppressionReason(suppressionReason)
                        .build())
                .build());
    }

    public void suppressValidationResults(List<UUID> validationResultIds, String suppressionReason) {
        getValidationResultsClient().saveSuppressions(ValidationResultSuppressionsDTO.builder()
                .suppressions(validationResultIds.stream().map(validationResultId -> ValidationResultSuppressionDTO.builder()
                                .validationResultId(validationResultId)
                                .suppressionReason(suppressionReason)
                                .build())
                        .collect(Collectors.toList()))
                .build());
    }

    public void removeValidationResultSuppression(UUID validationResultId) {
        getValidationResultsClient().saveSuppressions(ValidationResultSuppressionsDTO.builder()
                .addRemovedSuppressions(validationResultId)
                .build());
    }

    public void removeValidationResultSuppressions(List<UUID> validationResultIds) {
        getValidationResultsClient().saveSuppressions(ValidationResultSuppressionsDTO.builder()
                .removedSuppressions(validationResultIds)
                .build());
    }

    public void saveValidationResults(ObjectIdentifier objectIdentifier, List<ValidationResult>
            validationResults, PlanningContext planningContext) {
        List<ValidationResultDTO> validationResultDTOS = new ArrayList<>();
        for (ValidationResult validationResult : validationResults) {
            validationResultDTOS.add(ValidationResultDTO.builder()
                    .validationResultId(validationResult.getValidationResultId())
                    .description(validationResult.getDescription())
                    .type(validationResult.getType())
                    .owner(validationResult.getOwner())
                    .severity(validationResult.getSeverity().toString())
                    .validationStatus(validationResult.getValidationStatus().toString())
                    .isSuppressEnabled(validationResult.getSuppressEnabled())
                    .build());
        }
        getValidationResultsClient().saveValidationResults(
                Collections.singletonList(ObjectValidationResultsDTO.builder()
                        .objectId(ObjectIdentifierDTO.builder()
                                .identifier(String.valueOf(objectIdentifier.getId()))
                                .type(objectIdentifier.getType())
                                .build())
                        .planningContext(getPlanningContextDTO(planningContext))
                        .validationResults(validationResultDTOS)
                        .build()
                ));
    }

    public List<ValidationResult> getValidationResultsByObject(ObjectIdentifier objectIdentifier,
                                                               PlanningContext planningContext,
                                                               boolean withSuppressed, boolean withValid) {

        List<ValidationResultDTO> validationResultDTOs = getValidationResultsClient().
                getValidationResultsByObjects(objectIdentifier.getType(),
                        Collections.singletonList(String.valueOf(objectIdentifier.getId())),
                        planningContext, withSuppressed, withValid).get(0).getValidationResults();

        List<ValidationResult> validationResults = new ArrayList<>();
        for (ValidationResultDTO validationResultDTO : validationResultDTOs) {
            Optional<ValidationResultSuppressionDetailsDTO> suppressionDetailsDTO = validationResultDTO.suppression();
            Optional<ValidationResult.SuppressionDetails> suppression;
            suppression = suppressionDetailsDTO.map(validationResultSuppressionDetailsDTO -> new ValidationResult.SuppressionDetails(
                    validationResultSuppressionDetailsDTO.getSuppressionReason(),
                    validationResultSuppressionDetailsDTO.getSuppressedBy()));
            validationResults.add(ValidationResult.builder()
                    .validationResultId(validationResultDTO.getValidationResultId())
                    .type(validationResultDTO.getType())
                    .validationStatus(ValidationResult.ValidationStatus.valueOf(validationResultDTO.getValidationStatus()))
                    .description(validationResultDTO.getDescription())
                    .owner(validationResultDTO.getOwner())
                    .severity(ValidationResult.Severity.valueOf(validationResultDTO.getSeverity()))
                    .suppression(suppression)
                    .suppressEnabled(validationResultDTO.isSuppressEnabled())
                    .build());
        }
        return validationResults;
    }
}
