package com.oss.transport.infrastructure.planning;

import com.comarch.oss.planning.api.dto.NewProjectDTO;
import com.comarch.oss.planning.api.dto.PlannedActionIdDTO;
import com.comarch.oss.planning.api.dto.PlanningPerspectiveDTO;
import com.comarch.oss.planning.api.dto.PlanningResourceDTO;
import com.comarch.oss.planning.api.dto.ProjectDTO;
import com.comarch.oss.planning.api.dto.ProjectIdDTO;
import com.comarch.oss.planning.api.dto.ProjectSearchResultContainerDTO;
import com.google.common.base.Preconditions;
import com.jayway.restassured.http.ContentType;
import com.oss.transport.infrastructure.EnvironmentRequestClient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectClient {

    private static final String SEARCH_PROJECT_PATH = "/planning/projects/search";
    private static final String MOVE_PROJECT_PATH = "/planning/projects/{projectId}/perspective";
    protected static final String PLANNED_ACTIONS = "/planned-actions";
    protected static final String PLANNING_PLANNED_ACTIONS = "planning/planned-actions/";
    protected static final String PLANNING_PROJECTS = "planning/projects/";

    private static final String PROJECT_SEARCH_QUERY_PARAM = "q";
    private static final Integer STATUS_CODE_OK = 200;
    private static final Integer STATUS_CODE_NO_CONTENT = 204;
    private static final EnumSet<ProjectDTO.LockStatusEnum> OPEN_PROJECT_STATUSES =
            EnumSet.of(ProjectDTO.LockStatusEnum.OPEN, ProjectDTO.LockStatusEnum.MAINTENANCE_OPEN);

    private final EnvironmentRequestClient requestClient;

    public ProjectClient(EnvironmentRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    public void moveProject(Long projectId, String perspective) {
        PlanningPerspectiveDTO planningPerspectiveDTO =
                PlanningPerspectiveDTO.builder().perspective(PlanningPerspectiveDTO.PerspectiveEnum.valueOf(perspective)).build();

        requestClient.getPlanningCoreRequestSpecification()
                .given().contentType("application/json").body(planningPerspectiveDTO)
                .when().put(MOVE_PROJECT_PATH.replace("{projectId}", projectId.toString()))
                .then().log().status().log().body().statusCode(STATUS_CODE_NO_CONTENT);
    }

    public Long getProjectId(String projectName, OffsetDateTime dueDate) {
        return getProjectIdByName(projectName).orElseGet(() -> createProjectWithName(dueDate, projectName));
    }

    public Optional<Long> getProjectIdByName(String shortName) {
        ProjectSearchResultContainerDTO foundProjects = searchProjectsByName(shortName);
        if (foundProjects.getProjects().isEmpty()) {
            return Optional.empty();
        }

        ProjectDTO[] foundProjectsDetails = getCreatedProject(foundProjects.getUriAggregatedProject());
        return Arrays.stream(foundProjectsDetails)
                .filter(dto -> OPEN_PROJECT_STATUSES.contains(dto.getLockStatus()))
                .map(ProjectDTO::getId)
                .sorted()
                .findFirst();
    }

    public void cleanUpProject(long projectId) {
        PlannedActionIdDTO[] plannedActions =
                requestClient.getPlanningCoreRequestSpecification().when().get(PLANNING_PROJECTS + projectId + PLANNED_ACTIONS).then()
                        .log()
                        .status()
                        .log().body().statusCode(STATUS_CODE_OK).contentType(ContentType.JSON).extract().as(PlannedActionIdDTO[].class);

        if (plannedActions.length > 0) {
            String joinedIds =
                    Arrays.stream(plannedActions).map(PlannedActionIdDTO::getId).map(String::valueOf).collect(Collectors.joining(","));
            requestClient.getPlanningCoreRequestSpecification().when().delete(PLANNING_PLANNED_ACTIONS + joinedIds).then().log()
                    .status()
                    .log()
                    .body().statusCode(STATUS_CODE_NO_CONTENT).extract().response();
        }
    }

    private ProjectSearchResultContainerDTO searchProjectsByName(String shortName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(PROJECT_SEARCH_QUERY_PARAM, shortName);

        return requestClient.getPlanningCoreRequestSpecification()
                .given().contentType("application/json").queryParams(queryParams)
                .when().get(SEARCH_PROJECT_PATH)
                .then().log().status().log().body().statusCode(STATUS_CODE_OK)
                .contentType(ContentType.JSON).extract().as(ProjectSearchResultContainerDTO.class);
    }

    private Long createProjectWithName(OffsetDateTime date, String name) {
        Preconditions.checkNotNull(name, "Process name cannot be empty");
        return createProject(date, name);
    }

    private Long createProject(OffsetDateTime date, String name) {
        NewProjectDTO newProjectDTO = buildProjectDto(date, name);
        PlanningResourceDTO planningResource = getPlanningNavigation();
        URI projectPath = createNewProject(newProjectDTO, planningResource);
        ProjectDTO[] newProjects = getCreatedProject(projectPath);
        return Arrays.stream(newProjects)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Project with name=" + name + " was not created."))
                .getId();
    }

    private ProjectDTO[] getCreatedProject(URI projectPath) {
        return requestClient.getPlanningCoreRequestSpecification()
                .when().get(projectPath)
                .then().log().status().log().body()
                .contentType(ContentType.JSON).extract()
                .as(ProjectDTO[].class);
    }

    private URI createNewProject(NewProjectDTO newProjectDTO, PlanningResourceDTO planningResource) {
        return requestClient.getPlanningCoreRequestSpecification()
                .given().contentType(ContentType.JSON).body(newProjectDTO)
                .when().post(planningResource.getUriProject())
                .then().log().status().log().body().extract().as(ProjectIdDTO.class)
                .getUri().orElseThrow(() -> new IllegalStateException("Project not created!"));
    }

    private PlanningResourceDTO getPlanningNavigation() {
        return requestClient.getPlanningCoreRequestSpecification()
                .when().get()
                .then().log().status().log().body()
                .contentType(ContentType.JSON)
                .extract().as(PlanningResourceDTO.class);
    }

    private NewProjectDTO buildProjectDto(OffsetDateTime date, String name) {
        return NewProjectDTO.builder()
                .finishedDueDate(date)
                .name(name)
                .build();
    }
}
