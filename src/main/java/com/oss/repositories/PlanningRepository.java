/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.repositories;

import java.time.LocalDate;
import java.util.Optional;

import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.v2.ProjectDTO;
import com.oss.services.PlanningClient;
import com.oss.untils.Environment;

/**
 * @author Gabriela Zaranek
 */
public class PlanningRepository {
    private Environment env;
    
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
}
