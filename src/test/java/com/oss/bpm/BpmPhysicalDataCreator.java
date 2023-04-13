package com.oss.bpm;

import com.google.common.base.Preconditions;
import com.oss.framework.utils.DelayUtils;
import com.oss.planning.ObjectIdentifier;
import com.oss.planning.PlanningContext;
import com.oss.planning.validationresults.ValidationResult;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.repositories.ValidationResultsRepository;
import com.oss.services.PhysicalInventoryClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;
import com.oss.untils.FakeGenerator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BpmPhysicalDataCreator {
    public static final String BPM_USER_LOGIN = "bpm_webselenium";
    public static final String BPM_USER_PASSWORD = "Webtests123!";
    public static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    public static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";
    public static final String BPM_BASIC_USER_LOGIN = "bpm_basic_webselenium";
    public static final String BPM_BASIC_USER_PASSWORD = "Webtests123!";
    private static final String DEVICE_MODEL_TYPE = "IPDeviceModel";
    private static final String CARD_MODEL_TYPE = "CardModel";

    private static final Random RANDOM = new Random();
    private static final Environment env = Environment.getInstance();
    private static final PlanningRepository planningRepository = new PlanningRepository(env);
    private static final AddressRepository addressRepository = new AddressRepository(env);
    private static final LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
    private static final ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
    private static final PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
    private static final PhysicalInventoryClient physicalInventoryClient = new PhysicalInventoryClient(env);
    private static final ValidationResultsRepository validationResultsRepository = new ValidationResultsRepository(env);
    private static final String INVALID_PLANNING_CONTEXT = "Invalid Planning Context";
    private static final String NOT_IN_PLAN_CONTEXT_EXCEPTION = INVALID_PLANNING_CONTEXT + " Planning Context is not in PLAN context.";
    private static final String NETWORK_NOT_SUPPORTED = INVALID_PLANNING_CONTEXT + " Network context is not supported.";

    public static int nextMaxInt() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    public static String nextRandomBuildingName() {
        return FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    }

    public static String createProject(String code, LocalDate finishDueDate) {
        return String.valueOf(planningRepository.createProject(code, code, finishDueDate));
    }

    public static void completeProject(String projectId) {
        planningRepository.moveToLive(Long.parseLong(projectId));
    }

    public static void cancelProject(String projectId) {
        planningRepository.cancelProject(Long.parseLong(projectId));
    }

    public static UUID createValidationResultForObject(ObjectIdentifier object, ValidationResult validationResult, PlanningContext planningContext) {
        validationResultsRepository.saveValidationResults(object, Collections.singletonList(validationResult), planningContext);
        return validationResultsRepository.getValidationResultsByObject(
                object, planningContext, true, true).get(0).getValidationResultId().get();
    }

    public static List<ValidationResult> getValidationResultsForObject(ObjectIdentifier object, PlanningContext planningContext) {
        return validationResultsRepository.getValidationResultsByObject(object, planningContext, true, true);
    }

    public static void suppressValidationResult(UUID validationResultId, String suppressionReason) {
        validationResultsRepository.suppressValidationResult(validationResultId, suppressionReason);
    }

    public static void removeValidationResultSuppression(UUID validationResultId) {
        validationResultsRepository.removeValidationResultSuppression(validationResultId);
    }

    private static Long getGeographicalAddress() {
        return addressRepository.getFirstGeographicalAddressId();
    }

    public static ObjectIdentifier createBuilding(String buildingName, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            return ObjectIdentifier.building(Long.valueOf(locationInventoryRepository.
                    createLocation(buildingName, ObjectIdentifier.BUILDING_TYPE, getGeographicalAddress(), planningContext.getProjectId())));
        } else if (planningContext.isLiveContext()) {
            return ObjectIdentifier.building(Long.valueOf(locationInventoryRepository.
                    createLocation(buildingName, ObjectIdentifier.BUILDING_TYPE, getGeographicalAddress())));
        } else if (planningContext.isNetworkContext()) {
            throw new IllegalArgumentException(NETWORK_NOT_SUPPORTED);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    public static void updateLocationInPlan(ObjectIdentifier location, String buildingName, String description, PlanningContext planningContext) {
        Preconditions.checkArgument(planningContext.isPlanContext(), NOT_IN_PLAN_CONTEXT_EXCEPTION);
        locationInventoryRepository.updateLocation(buildingName, location.getType(),
                location.getId().toString(), getGeographicalAddress(), description, planningContext.getProjectId());
    }

    public static void deleteLocation(ObjectIdentifier location, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            locationInventoryRepository.deleteLocation(location.getId(), location.getType(), planningContext.getProjectId());
        } else if (planningContext.isLiveContext()) {
            locationInventoryRepository.deleteLocation(location.getId(), location.getType());
        } else if (planningContext.isNetworkContext()) {
            throw new IllegalArgumentException(NETWORK_NOT_SUPPORTED);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    private static Long getObjectModelId(String objectModelName) {
        return resourceCatalogClient.getModelIds(objectModelName);
    }

    public static boolean isObjectPresent(ObjectIdentifier objectIdentifier, PlanningContext planningContext) {
        return planningRepository.isObjectPresent(objectIdentifier, planningContext);
    }

    public static ObjectIdentifier createIPDevice(String deviceName, String deviceModel, ObjectIdentifier location, PlanningContext planningContext) {
        Long deviceModelId = getObjectModelId(deviceModel);
        if (planningContext.isPlanContext()) {
            return ObjectIdentifier.ipDevice(physicalInventoryRepository.createDevice(location.getType(), location.getId(),
                    deviceModelId, deviceName, DEVICE_MODEL_TYPE, planningContext.getProjectId()));
        } else if (planningContext.isLiveContext()) {
            return ObjectIdentifier.ipDevice(physicalInventoryRepository.createDevice(location.getType(), location.getId(),
                    deviceModelId, deviceName, DEVICE_MODEL_TYPE));
        } else if (planningContext.isNetworkContext()) {
            throw new IllegalArgumentException(NETWORK_NOT_SUPPORTED);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    public static ObjectIdentifier createTechnicalPlaTestResource(String technicalResourceName, ObjectIdentifier parentObject, PlanningContext planningContext) {
        ObjectIdentifier technicalResource = createObject(technicalResourceName, ObjectIdentifier.PLA_TEST_RESOURCE_TYPE, planningContext);
        DelayUtils.sleep(100);
        planningRepository.connectRoots(parentObject, technicalResource, planningContext);
        return technicalResource;
    }

    public static void updateIPDeviceSerialNumberInPlan(String deviceName, ObjectIdentifier device, String deviceModel, String serialNumber,
                                                        ObjectIdentifier location, PlanningContext planningContext) {
        Preconditions.checkArgument(planningContext.isPlanContext(), NOT_IN_PLAN_CONTEXT_EXCEPTION);
        Long deviceModelId = getObjectModelId(deviceModel);
        physicalInventoryRepository.updateDeviceSerialNumber(device.getId(), deviceName, location.getType(), location.getId(),
                serialNumber, deviceModelId, DEVICE_MODEL_TYPE, planningContext.getProjectId());
    }

    public static void deleteIPDevice(ObjectIdentifier device, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            physicalInventoryRepository.deleteDevice(device.getId().toString(), planningContext.getProjectId());
        } else if (planningContext.isLiveContext()) {
            physicalInventoryRepository.deleteDevice(device.getId().toString());
        } else if (planningContext.isNetworkContext()) {
            throw new IllegalArgumentException(NETWORK_NOT_SUPPORTED);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    public static ObjectIdentifier createCardForDevice(ObjectIdentifier device, String deviceSlotName, PlanningContext planningContext) {
        Long cardModelId = getObjectModelId(ObjectIdentifier.CARD_TYPE);
        if (planningContext.isPlanContext()) {
            physicalInventoryRepository.createCard(device.getId(), deviceSlotName, cardModelId, CARD_MODEL_TYPE, planningContext.getProjectId());
            return ObjectIdentifier.card(physicalInventoryClient.getDeviceCardUnderChassisId(
                    physicalInventoryClient.getDeviceChassisId(device.getId(), ObjectIdentifier.CHASSIS_TYPE,
                            planningContext.getProjectId()), ObjectIdentifier.CARD_TYPE, planningContext.getProjectId()));
        } else if (planningContext.isLiveContext()) {
            physicalInventoryRepository.createCard(device.getId(), deviceSlotName, cardModelId, CARD_MODEL_TYPE);
            return ObjectIdentifier.card(physicalInventoryClient.getDeviceCardUnderChassisId(
                    physicalInventoryClient.getDeviceChassisId(device.getId(), ObjectIdentifier.CHASSIS_TYPE), ObjectIdentifier.CARD_TYPE));
        } else if (planningContext.isNetworkContext()) {
            throw new IllegalArgumentException(NETWORK_NOT_SUPPORTED);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    public static ObjectIdentifier getDeviceChassis(ObjectIdentifier device, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            return ObjectIdentifier.chassis(physicalInventoryClient.getDeviceChassisId(device.getId(),
                    ObjectIdentifier.CHASSIS_TYPE, planningContext.getProjectId()));
        } else if (planningContext.isLiveContext()) {
            return ObjectIdentifier.chassis(physicalInventoryClient.getDeviceChassisId(device.getId(), ObjectIdentifier.CHASSIS_TYPE));
        } else if (planningContext.isNetworkContext()) {
            throw new IllegalArgumentException(NETWORK_NOT_SUPPORTED);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    public static void removeObject(ObjectIdentifier objectIdentifier, PlanningContext planningContext) {
        if (planningContext.isLiveContext()) {
            planningRepository.removeObjectInLive(objectIdentifier);
        } else if (planningContext.isPlanContext()) {
            planningRepository.removeObjectInPlan(planningContext.getProjectId(), objectIdentifier);
        } else if (planningContext.isNetworkContext()) {
            planningRepository.removeObjectInNetwork(objectIdentifier);
        }
    }

    public static void updateObjectDescription(ObjectIdentifier objectIdentifier, String description, PlanningContext planningContext) {
        if (planningContext.isLiveContext()) {
            planningRepository.updateObjectDescriptionInLive(objectIdentifier, description);
        } else if (planningContext.isPlanContext()) {
            planningRepository.updateObjectDescriptionInPlan(planningContext.getProjectId(), objectIdentifier, description);
        } else if (planningContext.isNetworkContext()) {
            planningRepository.updateObjectDescriptionInNetwork(objectIdentifier, description);
        }
    }

    public static ObjectIdentifier createObject(String objectName, String objectType, PlanningContext planningContext) {
        if (planningContext.isLiveContext()) {
            return planningRepository.createObjectInLive(objectName, objectType);
        } else if (planningContext.isPlanContext()) {
            return planningRepository.createObjectInPlan(planningContext.getProjectId(), objectName, objectType);
        } else if (planningContext.isNetworkContext()) {
            return planningRepository.createObjectInNetwork(objectName, objectType);
        } else {
            throw new IllegalArgumentException(INVALID_PLANNING_CONTEXT);
        }
    }

    public static void setPrerequisite(ObjectIdentifier prerequisite, ObjectIdentifier subsequent, PlanningContext planningContext) {
        planningRepository.setPrerequisite(prerequisite, subsequent, planningContext);
    }


}
