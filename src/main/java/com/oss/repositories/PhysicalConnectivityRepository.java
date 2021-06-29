package com.oss.repositories;

import com.comarch.oss.physicalconnectivitycable.api.dto.CableModelDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.CableSyncDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.CableSyncResultDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MediumPersistenceDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MultipleSegmentDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MultipleSegmentResultDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.PhysicalTerminationPersistenceDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.SegmentDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.TotalLengthWrapperDTO;
import com.oss.services.PhysicalConnectivityClient;
import com.oss.untils.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PhysicalConnectivityRepository {

    private Environment env;

    public PhysicalConnectivityRepository(Environment env) {
        this.env = env;
    }

    public Long createCable(Long terminationDeviceId1, Long terminationDeviceId2, Long terminationId1, Long terminationId2, String modelName, String manufacturerName, int length) {
        PhysicalConnectivityClient client = PhysicalConnectivityClient.getInstance(env);
        CableSyncResultDTO cable = client.createCable(buildCable(terminationDeviceId1, terminationDeviceId2, terminationId1, terminationId2, modelName, manufacturerName, length, Long.valueOf(1), Long.valueOf(1)));
        return cable.getCreatedCables().get(0).getId();
    }

    public Long createCableWithSpecificMediumAndBundleNumber(Long terminationDeviceId1, Long terminationDeviceId2, Long terminationId1, Long terminationId2, String modelName, String manufacturerName, int length, Long mediumNumber, Long bundleNumber) {
        PhysicalConnectivityClient client = PhysicalConnectivityClient.getInstance(env);
        CableSyncResultDTO cable = client.createCable(buildCable(terminationDeviceId1, terminationDeviceId2, terminationId1, terminationId2, modelName, manufacturerName, length, mediumNumber, bundleNumber));
        return cable.getCreatedCables().get(0).getId();
    }

    public void createMultipleSegmentCable(Long terminationId1, Long terminationId2, String modelName1, String manufacturerName, int length1, String modelName2, int length2, String modelName3, int length3) {
        PhysicalConnectivityClient client = PhysicalConnectivityClient.getInstance(env);
        MultipleSegmentResultDTO multipleSegmentCable = client.createMultipleSegmentCable(buildMultipleSegmentCable(terminationId1, terminationId2, modelName1,
                manufacturerName, length1, modelName2, length2, modelName3, length3));
    }

    public void removeCable(Long cableId) {
        PhysicalConnectivityClient client = PhysicalConnectivityClient.getInstance(env);
        client.removeCable(cableId);
    }

    private CableSyncDTO buildCable(Long terminationDeviceId1, Long terminationDeviceId2, Long terminationId1, Long terminationId2, String modelName, String manufacturerName, int length, Long mediumNumber, Long bundleNumber) {
        return CableSyncDTO.builder()
                .startTermination(getTermination(terminationDeviceId1, "Device"))
                .endTermination(getTermination(terminationDeviceId2, "Device"))
                .model(getModel(modelName, manufacturerName))
                .totalLength(getLength(length))
                .mediums(Collections.singleton(getMediums(terminationId1, terminationId2, mediumNumber, bundleNumber)))
                .build();
    }

    private MultipleSegmentDTO buildMultipleSegmentCable(Long terminationId1, Long terminationId2, String modelName1, String manufacturerName, int length1,
                                                         String modelName2, int length2, String modelName3, int length3) {
        return MultipleSegmentDTO.builder()
                .startTerminations(Collections.singleton(getTermination(terminationId1, "TerminationPoint")))
                .endTerminations(Collections.singleton(getTermination(terminationId2, "TerminationPoint")))
                .segments(buildSegments(modelName1, manufacturerName, length1, modelName2, length2, modelName3, length3))
                .build();
    }

    private List<SegmentDTO> buildSegments(String modelName1, String manufacturerName, int length1,
                                           String modelName2, int length2, String modelName3, int length3) {
        List<SegmentDTO> segments = new ArrayList<>();
        segments.add(getSegment(modelName1, manufacturerName, length1));
        segments.add(getSegment(modelName2, manufacturerName, length2));
        segments.add(getSegment(modelName3, manufacturerName, length3));
        return segments;
    }

    private PhysicalTerminationPersistenceDTO getTermination(Long terminationId, String terminationLevel) {
        return PhysicalTerminationPersistenceDTO.builder()
                .terminationId(terminationId)
                .terminationLevel(PhysicalTerminationPersistenceDTO.TerminationLevelEnum.valueOf(terminationLevel))
                .build();
    }

    private CableModelDTO getModel(String modelName, String manufacturerName) {
        return CableModelDTO.builder()
                .manufacturerName(manufacturerName)
                .modelName(modelName)
                .build();
    }

    private TotalLengthWrapperDTO getLength(int length) {
        return TotalLengthWrapperDTO.builder()
                .value(length)
                .build();
    }

    private MediumPersistenceDTO getMediums(Long terminationId1, Long terminationId2, Long mediumNumber, Long bundleNumber) {
        return MediumPersistenceDTO.builder()
                .startTermination(getTermination(terminationId1, "TerminationPoint"))
                .endTermination(getTermination(terminationId2, "TerminationPoint"))
                .mediumNumber(mediumNumber)
                .bundleNumber(bundleNumber)
                .build();
    }

    private SegmentDTO getSegment(String modelName, String manufacturerName, int length) {
        return SegmentDTO.builder()
                .model(getModel(modelName, manufacturerName))
                .length(length)
                .build();
    }

}
