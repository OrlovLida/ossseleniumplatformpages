/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.repositories;

import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.v2.ProjectDTO;
import com.comarch.oss.planningmanager.api.dto.internal.ConnectionDTO;
import com.comarch.oss.services.infrastructure.commonapi.dto.ObjectIdentifierDTO;
import com.oss.planning.ObjectIdentifier;
import com.oss.planning.PlanningContext;
import com.oss.services.PlanningClient;
import com.oss.untils.Environment;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Gabriela Zaranek
 */
public class PlanningRepository {
    private final PlanningClient planningClient;

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
        ObjectIdentifierDTO parent = ObjectIdentifierDTO.builder()
                .identifier(String.valueOf(parentObject.getId()))
                .type(parentObject.getType())
                .build();
        List<ConnectionDTO> connectionDTOs = childObjects.stream().map(childObject -> {
            ObjectIdentifierDTO child = ObjectIdentifierDTO.builder()
                    .identifier(String.valueOf(childObject.getId()))
                    .type(childObject.getType())
                    .build();
            return ConnectionDTO.builder()
                    .parentId(parent)
                    .rootId(child)
                    .build();
        }).collect(Collectors.toList());
        planningClient.connectRoots(connectionDTOs, planningContext);
    }
}

