/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.repositories;

import com.comarch.oss.planning.api.dto.NetworkDTO;
import com.comarch.oss.planning.api.dto.PerformObjectIdDTO;
import com.comarch.oss.planning.api.dto.PlanDTO;
import com.comarch.oss.planning.api.dto.PlannedObjectDTO;
import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.ProjectObjectsDTO;
import com.comarch.oss.planning.api.dto.v2.ProjectDTO;
import com.comarch.oss.planningmanager.api.dto.internal.ConnectionDTO;
import com.comarch.oss.planningmanager.api.dto.internal.RootPrerequisitesDTO;
import com.comarch.oss.services.infrastructure.commonapi.dto.ObjectIdentifierDTO;
import com.oss.planning.ObjectIdentifier;
import com.oss.planning.PlanningContext;
import com.oss.services.PlanningClient;
import com.oss.untils.Environment;
import dev.failsafe.internal.util.Assert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Gabriela Zaranek
 */
public class PlanningRepository {
    private static final String EMPTY_PERFORM_RESULT_MESSAGE = "Cannot get object id from perform result DTO. performResults is empty.";
    private static final String OBJECT_NOT_REMOVED = "Remove object in live is not proceed properly. Object %s is not removed.";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String DESCRIPTION_ATTRIBUTE = "Description";
    private final PlanningClient planningClient;

    private ObjectIdentifierDTO buildObjectIdentifierDTO(ObjectIdentifier objectIdentifier) {
        return ObjectIdentifierDTO.builder()
                .identifier(String.valueOf(objectIdentifier.getId()))
                .type(objectIdentifier.getType())
                .build();
    }

    public PlanningRepository(Environment env) {
        this.planningClient = new PlanningClient(env);
    }

    public Long createProject(String projectName, String projectCode, LocalDate finishDueDate) {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .code(projectCode)
                .name(projectName)
                .finishedDueDate(finishDueDate)
                .projectLockStatus(ProjectDTO.ProjectLockStatus.OPEN)
                .projectStatus(ProjectDTO.ProjectStatus.ACTIVE)
                .isAutomaticWOIGeneration(false)
                .objectsMoveOption(ProjectDTO.MoveOption.LIVE)
                .build();
        return planningClient.createProject(projectDTO);
    }

    public void moveToLive(Long projectId) {
        PlanningPerspectiveDTO perspectiveDTO = PlanningPerspectiveDTO.builder()
                .perspective(PlanningPerspectiveDTO.PerspectiveEnum.LIVE)
                .build();
        planningClient.moveProject(projectId, perspectiveDTO);
    }

    public void cancelProject(Long projectId) {
        planningClient.cancelProject(projectId);
    }

    public Long getFirstObjectWithType(String objectType) {
        return planningClient.getObjectByType(objectType, "1", "1")
                .getObjectIds()
                .get(0)
                .getId();
    }

    public Optional<Long> getObjectIdByTypeAndName(String objectType, String name) {
        return planningClient.findObjectIdByNameAndType(name, objectType);
    }

    public void connectRoots(ObjectIdentifier parentObject, ObjectIdentifier childObject, PlanningContext planningContext) {
        connectRoots(parentObject, Collections.singletonList(childObject), planningContext);
    }

    public void connectRoots(ObjectIdentifier parentObject, List<ObjectIdentifier> childObjects, PlanningContext planningContext) {
        List<ConnectionDTO> connectionDTOs = childObjects.stream().map(childObject ->
                ConnectionDTO.builder()
                        .parentId(buildObjectIdentifierDTO(parentObject))
                        .rootId(buildObjectIdentifierDTO(childObject))
                        .build()).collect(Collectors.toList());
        planningClient.connectRoots(connectionDTOs, planningContext);
    }

    public ObjectIdentifier createObjectInPlan(Long projectId, String objectName, String objectType) {
        UUID objectUUID = UUID.randomUUID();
        PerformObjectIdDTO root = PerformObjectIdDTO.builder()
                .objectType(objectType)
                .uuid(objectUUID)
                .build();
        ProjectObjectsDTO projectObjectsDTO = ProjectObjectsDTO.builder()
                .addPlans(PlanDTO.builder()
                        .rootId(root)
                        .addPlannedObjects(PlannedObjectDTO.builder()
                                .plannedObjectId(root)
                                .operationType(PlannedObjectDTO.OperationTypeEnum.CREATE)
                                .putSimpleAttributes(NAME_ATTRIBUTE, Optional.ofNullable(objectName))
                                .build())
                        .build())
                .build();
        Long objectId = planningClient.changeObjectsInPlan(projectId, projectObjectsDTO, false, true)
                .getObjectIdMappings().get(objectUUID.toString());
        return new ObjectIdentifier(objectId, objectType);
    }

    public void updateObjectDescriptionInPlan(Long projectId, ObjectIdentifier objectIdentifier, String description) {
        PerformObjectIdDTO root = PerformObjectIdDTO.builder()
                .objectType(objectIdentifier.getType())
                .id(objectIdentifier.getId())
                .build();
        ProjectObjectsDTO projectObjectsDTO = ProjectObjectsDTO.builder()
                .addPlans(PlanDTO.builder()
                        .rootId(root)
                        .addPlannedObjects(PlannedObjectDTO.builder()
                                .plannedObjectId(root)
                                .operationType(PlannedObjectDTO.OperationTypeEnum.UPDATE)
                                .putSimpleAttributes(DESCRIPTION_ATTRIBUTE, Optional.ofNullable(description))
                                .build())
                        .build())
                .build();
        planningClient.changeObjectsInPlan(projectId, projectObjectsDTO, false, true);
    }

    public void removeObjectInPlan(Long projectId, ObjectIdentifier objectIdentifier) {
        PerformObjectIdDTO root = PerformObjectIdDTO.builder()
                .objectType(objectIdentifier.getType())
                .id(objectIdentifier.getId())
                .build();
        ProjectObjectsDTO projectObjectsDTO = ProjectObjectsDTO.builder()
                .addPlans(PlanDTO.builder()
                        .rootId(root)
                        .addPlannedObjects(PlannedObjectDTO.builder()
                                .plannedObjectId(root)
                                .operationType(PlannedObjectDTO.OperationTypeEnum.REMOVE)
                                .build())
                        .build())
                .build();
        planningClient.changeObjectsInPlan(projectId, projectObjectsDTO, false, true);
    }

    public void setPrerequisite(ObjectIdentifier prerequisite, ObjectIdentifier subsequent, PlanningContext planningContext) {
        planningClient.savePrerequisites(Collections.singletonList(RootPrerequisitesDTO.builder()
                .rootId(buildObjectIdentifierDTO(subsequent))
                .addPrerequisites(buildObjectIdentifierDTO(prerequisite))
                .build()), planningContext);
    }

    public boolean isObjectPresent(ObjectIdentifier objectIdentifier, PlanningContext planningContext) {
        return !planningClient.queryObject(objectIdentifier, planningContext,
                "", "", "", false, false).isEmpty();
    }

    public String getTypeLabel(String typeName) {
        return planningClient.getTypeDescription(typeName).getLabel();
    }

    public ObjectIdentifier createObjectInLive(String objectName, String objectType) {
        PlannedObjectDTO plannedObjectDTO = PlannedObjectDTO.builder()
                .plannedObjectId(PerformObjectIdDTO.builder()
                        .objectType(objectType)
                        .uuid(UUID.randomUUID())
                        .build())
                .operationType(PlannedObjectDTO.OperationTypeEnum.CREATE)
                .putSimpleAttributes(NAME_ATTRIBUTE, Optional.ofNullable(objectName))
                .build();
        Long objectId = planningClient.changeObjectsInLive(Collections.singletonList(plannedObjectDTO), "", true)
                .getPerformResults().stream().findFirst().orElseThrow(() -> new IllegalStateException(EMPTY_PERFORM_RESULT_MESSAGE)).getId();
        return new ObjectIdentifier(objectId, objectType);
    }

    public void updateObjectDescriptionInLive(ObjectIdentifier object, String description) {
        PlannedObjectDTO plannedObjectDTO = PlannedObjectDTO.builder()
                .plannedObjectId(PerformObjectIdDTO.builder()
                        .objectType(object.getType())
                        .id(object.getId())
                        .build())
                .operationType(PlannedObjectDTO.OperationTypeEnum.UPDATE)
                .putSimpleAttributes(DESCRIPTION_ATTRIBUTE, Optional.ofNullable(description))
                .build();
        planningClient.changeObjectsInLive(Collections.singletonList(plannedObjectDTO), "", true);
    }

    public void removeObjectInLive(ObjectIdentifier object) {
        PlannedObjectDTO plannedObjectDTO = PlannedObjectDTO.builder()
                .plannedObjectId(PerformObjectIdDTO.builder()
                        .objectType(object.getType())
                        .id(object.getId())
                        .build())
                .operationType(PlannedObjectDTO.OperationTypeEnum.REMOVE)
                .build();
        Assert.isTrue(!planningClient.changeObjectsInLive(Collections.singletonList(plannedObjectDTO), "", true)
                .getRemoveResults().isEmpty(), OBJECT_NOT_REMOVED, object.toString());
    }

    public ObjectIdentifier createObjectInNetwork(String objectName, String objectType) {
        PerformObjectIdDTO root = PerformObjectIdDTO.builder()
                .uuid(UUID.randomUUID())
                .objectType(objectType)
                .build();
        NetworkDTO networkDTO = NetworkDTO.builder()
                .rootId(root)
                .addPlannedObjects(PlannedObjectDTO.builder()
                        .plannedObjectId(root)
                        .operationType(PlannedObjectDTO.OperationTypeEnum.CREATE)
                        .putSimpleAttributes(NAME_ATTRIBUTE, Optional.ofNullable(objectName))
                        .build())
                .build();
        Long objectId = planningClient.changeObjectsInNetwork(Collections.singletonList(networkDTO), false)
                .getPerformResults().stream().findFirst().orElseThrow(() -> new IllegalStateException(EMPTY_PERFORM_RESULT_MESSAGE)).getRootId().getId();
        return new ObjectIdentifier(objectId, objectType);
    }

    public void updateObjectDescriptionInNetwork(ObjectIdentifier objectIdentifier, String description) {
        PerformObjectIdDTO root = PerformObjectIdDTO.builder()
                .objectType(objectIdentifier.getType())
                .id(objectIdentifier.getId())
                .build();
        NetworkDTO networkDTO = NetworkDTO.builder()
                .rootId(root)
                .addPlannedObjects(PlannedObjectDTO.builder()
                        .plannedObjectId(root)
                        .operationType(PlannedObjectDTO.OperationTypeEnum.UPDATE)
                        .putSimpleAttributes(DESCRIPTION_ATTRIBUTE, Optional.ofNullable(description))
                        .build())
                .build();
        planningClient.changeObjectsInNetwork(Collections.singletonList(networkDTO), false);
    }

    public void removeObjectInNetwork(ObjectIdentifier objectIdentifier) {
        PerformObjectIdDTO root = PerformObjectIdDTO.builder()
                .objectType(objectIdentifier.getType())
                .id(objectIdentifier.getId())
                .build();
        NetworkDTO networkDTO = NetworkDTO.builder()
                .rootId(root)
                .addPlannedObjects(PlannedObjectDTO.builder()
                        .plannedObjectId(root)
                        .operationType(PlannedObjectDTO.OperationTypeEnum.REMOVE)
                        .build())
                .build();
        planningClient.changeObjectsInNetwork(Collections.singletonList(networkDTO), false);
    }
}

