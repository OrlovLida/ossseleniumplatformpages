package com.oss.repositories;

import com.comarch.oss.transport.trail.api.dto.RoutingDTO;
import com.comarch.oss.transport.trail.api.dto.TerminationDTO;
import com.comarch.oss.transport.trail.api.dto.TerminationLevelDTO;
import com.comarch.oss.transport.trail.api.dto.TerminationTypeDTO;
import com.comarch.oss.transport.trail.api.dto.TrailCreationDTO;
import com.comarch.oss.transport.trail.api.dto.TrailDirectionDTO;
import com.comarch.oss.transport.trail.api.dto.TrailIdentificationDTO;
import com.oss.services.TrailCoreClient;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;

public class TrailCoreRepository {

    private final TrailCoreClient trailCoreClient;

    public TrailCoreRepository(Environment env) {
        trailCoreClient = TrailCoreClient.getInstance(env);
    }

    public Long getFirstMediumIdConnectedOnTerminationPoint(Long terminationPointId, String mediumType) {
        return trailCoreClient.searchTrailsByTerminations(terminationPointId)
                .getTrails()
                .stream()
                .filter(e -> e.getType().equals(mediumType))
                .map(TrailIdentificationDTO::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find any medium type " + mediumType + " on TP with id " + terminationPointId))
                .longValue();

    }

    public Long createUnidirectionalTrail(String trailType,
                                          TerminationLevelDTO terminationLevel,
                                          Long terminationId,
                                          PlanningContext planningContext) {
        RoutingDTO routing = RoutingDTO.builder().build();

        TerminationDTO termination = TerminationDTO.builder()
                .terminationId(terminationId)
                .terminationLevel(terminationLevel)
                .terminationType(TerminationTypeDTO.Unknown)
                .build();

        TrailCreationDTO trail = TrailCreationDTO.builder()
                .direction(TrailDirectionDTO.Unidirectional)
                .routing(routing)
                .addTermination(termination)
                .build();

        return trailCoreClient.createTrail(trail, trailType, planningContext).getId();
    }

}
