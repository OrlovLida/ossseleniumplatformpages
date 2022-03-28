package com.oss.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.comarch.oss.physicalconnectivitycable.api.dto.CableModelDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.CableSyncDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.CableSyncResultDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MediumPersistenceDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.MultipleSegmentDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.PhysicalTerminationPersistenceDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.SegmentDTO;
import com.comarch.oss.physicalconnectivitycable.api.dto.TotalLengthWrapperDTO;
import com.oss.services.PhysicalConnectivityClient;
import com.oss.untils.Environment;

public class PhysicalConnectivityRepository {

    private static final String DEVICE_TERMINATION_LEVEL = "Device";
    private static final String TP_TERMINATION_LEVEL = "TerminationPoint";
    private PhysicalConnectivityClient client;

    public PhysicalConnectivityRepository(Environment env) {
        client = PhysicalConnectivityClient.getInstance(env);
    }

    public Long createCable(Long terminationDeviceId1, Long terminationDeviceId2, Long terminationId1,
                            Long terminationId2, String modelName, String manufacturerName, int length) {
        CableSyncResultDTO cable = client.createCable(buildCable(terminationDeviceId1, terminationDeviceId2, terminationId1,
                terminationId2, modelName, manufacturerName, length, 1L, 1L));
        return cable.getCreatedCables()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find created cable on devices (" + terminationDeviceId1 + "," + terminationDeviceId2 + ")"))
                .getId()
                .longValue();
    }

    public Long createCableWithSpecificMediumAndBundleNumber(Long terminationDeviceId1, Long terminationDeviceId2,
                                                             Long terminationId1, Long terminationId2, String modelName, String manufacturerName, int length, Long mediumNumber, Long bundleNumber) {
        CableSyncResultDTO cable = client.createCable(buildCable(terminationDeviceId1, terminationDeviceId2,
                terminationId1, terminationId2, modelName, manufacturerName, length, mediumNumber, bundleNumber));
        return cable.getCreatedCables()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find created cable on devices (" + terminationDeviceId1 + "," + terminationDeviceId2 + ")"))
                .getId()
                .longValue();
    }

    public void createMultipleSegmentCable(Long terminationId1, Long terminationId2, String modelName1, String manufacturerName, int length1, String modelName2, int length2, String modelName3, int length3) {
        client.createMultipleSegmentCable(buildMultipleSegmentCable(terminationId1, terminationId2, modelName1,
                manufacturerName, length1, modelName2, length2, modelName3, length3));
    }

    public void removeCable(Long cableId) {
        client.removeCable(cableId);
    }

    private CableSyncDTO buildCable(Long terminationDeviceId1, Long terminationDeviceId2, Long terminationId1,
                                    Long terminationId2, String modelName, String manufacturerName, int length, Long mediumNumber, Long bundleNumber) {
        return CableSyncDTO.builder()
                .startTermination(getTermination(terminationDeviceId1, DEVICE_TERMINATION_LEVEL))
                .endTermination(getTermination(terminationDeviceId2, DEVICE_TERMINATION_LEVEL))
                .model(getModel(modelName, manufacturerName))
                .totalLength(getLength(length))
                .mediums(Collections.singleton(getMediums(terminationId1, terminationId2, mediumNumber, bundleNumber)))
                .build();
    }

    private MultipleSegmentDTO buildMultipleSegmentCable(Long terminationId1, Long terminationId2, String modelName1, String manufacturerName, int length1,
                                                         String modelName2, int length2, String modelName3, int length3) {
        return MultipleSegmentDTO.builder()
                .startTerminations(Collections.singleton(getTermination(terminationId1, TP_TERMINATION_LEVEL)))
                .endTerminations(Collections.singleton(getTermination(terminationId2, TP_TERMINATION_LEVEL)))
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
                .startTermination(getTermination(terminationId1, TP_TERMINATION_LEVEL))
                .endTermination(getTermination(terminationId2, TP_TERMINATION_LEVEL))
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
