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
import java.util.Optional;

/**
 * @author Gabriela Zaranek
 */
public class PlanningRepository {
    private final Environment env;

    public PlanningRepository(Environment env) {
        this.env = env;
    }

    public Long createProject(String projectName, String projectCode, LocalDate finishDueDate) {
        PlanningClient planningClient = new PlanningClient(env);
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
        PlanningClient planningClient = new PlanningClient(env);
        PlanningPerspectiveDTO perspectiveDTO = PlanningPerspectiveDTO.builder()
                .perspective(PlanningPerspectiveDTO.PerspectiveEnum.LIVE)
                .build();
        planningClient.moveProject(projectId, perspectiveDTO);
    }

    public void cancelProject(Long projectId) {
        PlanningClient planningClient = new PlanningClient(env);
        planningClient.cancelProject(projectId);
    }

    public Long getFirstObjectWithType(String objectType) {
        PlanningClient planningClient = new PlanningClient(env);
        return planningClient.getObjectByType(objectType, "1", "1")
                .getObjectIds()
                .get(0)
                .getId();
    }

    public Optional<Long> getObjectIdByTypeAndName(String objectType, String name) {
        PlanningClient planningClient = new PlanningClient(env);
        return planningClient.findObjectIdByNameAndType(name, objectType);
    }

    public void connectRoots(ObjectIdentifier parentObject, ObjectIdentifier childObject, PlanningContext planningContext) {
        PlanningClient planningClient = new PlanningClient(env);
        ObjectIdentifierDTO parent = ObjectIdentifierDTO.builder()
                .identifier(String.valueOf(parentObject.getId()))
                .type(parentObject.getType())
                .build();
        ObjectIdentifierDTO child = ObjectIdentifierDTO.builder()
                .identifier(String.valueOf(childObject.getId()))
                .type(childObject.getType())
                .build();
        ConnectionDTO connectionDTO = ConnectionDTO.builder()
                .parentId(parent)
                .rootId(child)
                .build();
        planningClient.connectRoots(Collections.singletonList(connectionDTO), planningContext);
    }
}
