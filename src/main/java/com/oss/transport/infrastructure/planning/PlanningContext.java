package com.oss.transport.infrastructure.planning;

import com.comarch.oss.planning.api.dto.PlannedObjectIdDTO;
import com.comarch.oss.planning.api.dto.PlannedObjectIdDTO.PerspectiveEnum;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import javax.ws.rs.QueryParam;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class PlanningContext implements Serializable {

    public static final String PROJECT_PARAM = "project_id";
    public static final String PERSPECTIVE_PARAM = "perspective";
    public static final String WITH_REMOVED_PARAM = "withRemoved";

    private static final String TAG_PARAM = "tag";
    private static final String VERSION_PARAM = "version";

    private static final String PERSPECTIVE_NOT_DEFINED_FOR_PLANNING_CONTEXT = "Perspective not defined for planning context.";
    private static final String PROJECT_ID_NOT_DEFINED_FOR_PLANNING_CONTEXT = "Project ID not defined for planning context.";

    private transient Optional<Long> projectIdValue = Optional.empty();
    private transient Optional<Perspective> perspectiveValue = Optional.empty();
    private boolean withRemoved = false;

    private Long projectId;
    private Perspective perspective;

    public static PlanningContext projectContext(Long projectId) {
        return new PlanningContext(projectId);
    }

    public static PlanningContext perspectiveContext(Perspective perspective) {
        return new PlanningContext(perspective);
    }

    public static PlanningContext perspectiveLive() {
        return new PlanningContext(Perspective.LIVE);
    }

    public static PlanningContext extractFromPlannedObjectIdDTO(PlannedObjectIdDTO plannedObject) {
        PerspectiveEnum perspective = plannedObject.getPerspective().orElseThrow(() -> new IllegalStateException("Perspective not set"));
        if (perspective == PerspectiveEnum.LIVE) {
            return PlanningContext.perspectiveContext(Perspective.LIVE);
        } else if (perspective == PerspectiveEnum.NETWORK) {
            return PlanningContext.perspectiveContext(Perspective.NETWORK);
        } else {
            long projectId =
                    plannedObject.getProject().orElseThrow(() -> new IllegalStateException("Project id not set in planned object."));
            return PlanningContext.projectContext(projectId);
        }
    }

    @Deprecated
    public static PlanningContext projectContext(Optional<Long> projectId) {
        return projectId.map(PlanningContext::new).orElseGet(() -> new PlanningContext(Perspective.LIVE));
    }

    public PlanningContext() {
        /**
         * Planning context can be set later using QueryParam.
         */
    }

    public PlanningContext(Long pProjectId) {
        projectIdValue = Optional.of(pProjectId);
        projectId = pProjectId;
    }

    public PlanningContext(Perspective pPerspective) {
        perspectiveValue = Optional.of(pPerspective);
        perspective = pPerspective;
    }

    public boolean isProjectContext() {
        return projectIdValue.isPresent();
    }

    public boolean isPerspectiveContext() {
        return perspectiveValue.isPresent();
    }

    public boolean isLivePerspective() {
        return isPerspectiveContext() && perspectiveValue.get().equals(Perspective.LIVE);
    }

    public boolean isNetworkPerspective() {
        return isPerspectiveContext() && perspectiveValue.get().equals(Perspective.NETWORK);
    }

    public String getQueryParamName() {
        if (isProjectContext()) {
            return PROJECT_PARAM;
        }
        return PERSPECTIVE_PARAM;
    }

    public String getQueryParamValue() {
        if (isProjectContext()) {
            return getProjectId().toString();
        }
        return getPerspective().toString();
    }

    public Long getProjectId() {
        return projectIdValue.orElseThrow(() -> new IllegalStateException(PROJECT_ID_NOT_DEFINED_FOR_PLANNING_CONTEXT));
    }

    public Optional<Long> getProjectIdAsOptional() {
        return projectIdValue;
    }

    @QueryParam(PROJECT_PARAM)
    public void setProjectId(Long pProjectId) {
        if (!perspectiveValue.isPresent()) {
            projectIdValue = Optional.ofNullable(pProjectId);
            projectId = pProjectId;
        }
    }

    public Perspective getPerspective() {
        return perspectiveValue.orElseThrow(() -> new IllegalStateException(PERSPECTIVE_NOT_DEFINED_FOR_PLANNING_CONTEXT));
    }

    public Optional<Perspective> getPerspectiveAsOptional() {
        return perspectiveValue;
    }

    @QueryParam(PERSPECTIVE_PARAM)
    public void setPerspective(String pPerspective) {
        if ((pPerspective != null) && getAllowedPerspectiveValues().contains(pPerspective.toUpperCase())) {
            perspectiveValue = Optional.ofNullable(pPerspective)
                    .map(String::toUpperCase)
                    .map(Perspective::valueOf);
            perspective = perspectiveValue.orElse(null);
            projectIdValue = Optional.empty();
            projectId = null;
        }
    }

    private Collection<String> getAllowedPerspectiveValues() {
        return Lists.newArrayList(Perspective.values()).stream().map(Perspective::toString).collect(Collectors.toList());
    }

    public boolean isWithRemoved() {
        return withRemoved;
    }

    @QueryParam(WITH_REMOVED_PARAM)
    public void setWithRemoved(boolean pWithRemoved) {
        withRemoved = pWithRemoved;
    }

    public WebTarget applyPlanningContext(WebTarget webTarget) {
        if (isProjectContext()) {
            return webTarget.queryParam(PROJECT_PARAM, getProjectId()).queryParam(WITH_REMOVED_PARAM, isWithRemoved());
        } else if (isPerspectiveContext()) {
            return webTarget.queryParam(PERSPECTIVE_PARAM, getPerspective()).queryParam(WITH_REMOVED_PARAM, isWithRemoved());
        }
        return webTarget;
    }

    public WebTarget applyAllPlanningContext(WebTarget webTarget) {
        if (isProjectContext()) {
            return webTarget.queryParam(PROJECT_PARAM, getProjectId());
        }
        if (isPerspectiveContext()) {
            return webTarget.queryParam(PERSPECTIVE_PARAM, getPerspective())
                    .queryParam(TAG_PARAM, perspectiveValue.get().tag)
                    .queryParam(VERSION_PARAM, perspectiveValue.get().version);
        }
        return webTarget;
    }

    public UriBuilder applyPlanningContext(UriBuilder uriBuilder) {
        if (isProjectContext()) {
            return uriBuilder.queryParam(PROJECT_PARAM, getProjectId()).queryParam(WITH_REMOVED_PARAM, isWithRemoved());
        } else if (isPerspectiveContext()) {
            return uriBuilder.queryParam(PERSPECTIVE_PARAM, getPerspective()).queryParam(WITH_REMOVED_PARAM, isWithRemoved());
        }
        return uriBuilder;
    }

    public Map<String, String> asMap() {
        if (isProjectContext()) {
            return ImmutableMap.of(PROJECT_PARAM, getProjectId().toString(), WITH_REMOVED_PARAM, String.valueOf(withRemoved));
        } else if (isPerspectiveContext()) {
            return ImmutableMap.of(PERSPECTIVE_PARAM, getPerspective().toString(), WITH_REMOVED_PARAM, String.valueOf(withRemoved));
        }
        return ImmutableMap.of();
    }

    @Override
    public int hashCode() {
        return Objects.hash(perspectiveValue, projectIdValue, withRemoved);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningContext)) {
            return false;
        }
        PlanningContext that = (PlanningContext) o;
        return Objects.equals(perspectiveValue, that.perspectiveValue) && Objects.equals(projectIdValue, that.projectIdValue)
                && Objects.equals(withRemoved, that.withRemoved);
    }

    public enum Perspective {
        LIVE("C", 0L),
        NETWORK("CD", 1L);

        private static final String LIVE_TAG = "C";
        private static final String NETWORK_TAG = "CD";
        private String tag;
        private Long version;

        Perspective(String tag, Long version) {
            this.tag = tag;
            this.version = version;
        }

        public static Perspective getPerspective(String xTag) {
            switch (xTag) {
                case LIVE_TAG:
                    return LIVE;
                case NETWORK_TAG:
                    return NETWORK;
                default:
                    throw new IllegalStateException("There is no perspective with tag: " + xTag);
            }
        }

        public String getTag() {
            return tag;
        }

        public Long getVersion() {
            return version;
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        perspectiveValue.ifPresent(value -> stringJoiner.add(PERSPECTIVE_PARAM + "='" + value.name() + "'"));
        projectIdValue.ifPresent(value -> stringJoiner.add(PROJECT_PARAM + "=" + value.toString()));

        return stringJoiner
                .add(WITH_REMOVED_PARAM + "=" + withRemoved)
                .toString();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        perspectiveValue = Optional.ofNullable(perspective);
        projectIdValue = Optional.ofNullable(projectId);
    }
}
