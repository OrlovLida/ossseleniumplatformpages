package com.oss.planning;

import java.util.Optional;

public class PlanningContext {

    private static final String PROJECT_ID_NOT_DEFINED_FOR_PLANNING_CONTEXT = "Project ID not defined for planning context.";
    private final Perspective perspective;
    private Optional<Long> projectId = Optional.empty();

    private PlanningContext(Long projectId) {
        this.projectId = Optional.of(projectId);
        this.perspective = Perspective.PLAN;
    }

    private PlanningContext(Perspective perspective) {
        this.perspective = perspective;
    }

    public static PlanningContext live() {
        return new PlanningContext(Perspective.LIVE);
    }

    public static PlanningContext network() {
        return new PlanningContext(Perspective.NETWORK);
    }


    public static PlanningContext plan(Long projectId) {
        return new PlanningContext(projectId);
    }

    public boolean isPlanContext() {
        return perspective.equals(Perspective.PLAN);
    }

    public boolean isLiveContext() {
        return perspective.equals(Perspective.LIVE);
    }

    public boolean isNetworkContext() {
        return perspective.equals(Perspective.NETWORK);
    }

    public Long getProjectId() {
        return projectId.orElseThrow(() -> new IllegalStateException(PROJECT_ID_NOT_DEFINED_FOR_PLANNING_CONTEXT));
    }

    public Perspective getPerspective() {
        return perspective;
    }

    public enum Perspective {
        LIVE,
        NETWORK,
        PLAN
    }
}
