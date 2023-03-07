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
    public static final String CHASSIS_NAME = "Chassis";
    public static final String CARD_NAME = "Card";
    public static final String IP_DEVICE_NAME = "IPDevice";
    public static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String CARD_MODEL = "Card";
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
    private static final String NOT_IN_PLAN_CONTEXT_EXCEPTION = "Planning Context is not in PLAN context.";

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

    public static UUID createValidationResultForRouter(String routerId, ValidationResult validationResult, PlanningContext planningContext) {
        validationResultsRepository.saveValidationResults(ObjectIdentifier.ipDevice(Long.valueOf(routerId)),
                Collections.singletonList(validationResult), planningContext);
        return validationResultsRepository.getValidationResultsByObject(
                        ObjectIdentifier.ipDevice(Long.valueOf(routerId)), planningContext, true, true).get(0).
                getValidationResultId().get();
    }

    public static List<ValidationResult> getValidationResultsForRouter(String routerId, PlanningContext planningContext) {
        return validationResultsRepository.getValidationResultsByObject(
                ObjectIdentifier.ipDevice(Long.valueOf(routerId)), planningContext, true, true);
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

    public static String createBuilding(String buildingName, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            return locationInventoryRepository.
                    createLocation(buildingName, LOCATION_TYPE_BUILDING, getGeographicalAddress(), planningContext.getProjectId());
        } else {
            return locationInventoryRepository.
                    createLocation(buildingName, LOCATION_TYPE_BUILDING, getGeographicalAddress());
        }
    }

    public static void updateBuildingInPlan(String buildingName, String buildingId, String description, PlanningContext planningContext) {
        Preconditions.checkArgument(planningContext.isPlanContext(), NOT_IN_PLAN_CONTEXT_EXCEPTION);
        locationInventoryRepository.updateLocation(buildingName, LOCATION_TYPE_BUILDING,
                buildingId, getGeographicalAddress(), description, planningContext.getProjectId());
    }

    public static void deleteBuilding(String buildingId, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            locationInventoryRepository.deleteLocation(
                    Long.valueOf(buildingId), LOCATION_TYPE_BUILDING, planningContext.getProjectId());
        } else {
            locationInventoryRepository.deleteLocation(
                    Long.valueOf(buildingId), LOCATION_TYPE_BUILDING);
        }
    }

    private static Long getObjectModelId(String objectModelName) {
        return resourceCatalogClient.getModelIds(objectModelName);
    }

    public static boolean isDeviceVisibleInLIVE(String deviceName, String buildingId) {
        return physicalInventoryRepository.isDevicePresent(buildingId, deviceName);
    }

    public static String createIPDevice(String deviceName, String deviceModel, String buildingId, PlanningContext planningContext) {
        Long deviceModelId = getObjectModelId(deviceModel);
        if (planningContext.isPlanContext()) {
            return String.valueOf(physicalInventoryRepository.createDevice(LOCATION_TYPE_BUILDING,
                    Long.parseLong(buildingId), deviceModelId, deviceName, DEVICE_MODEL_TYPE, planningContext.getProjectId()));
        } else {
            return String.valueOf(physicalInventoryRepository.createDevice(LOCATION_TYPE_BUILDING,
                    Long.parseLong(buildingId), deviceModelId, deviceName, DEVICE_MODEL_TYPE));
        }

    }

    public static String createTechnicalIPDeviceInPlan(String deviceName, String deviceModel, String buildingId,
                                                       String parentDeviceId, PlanningContext planningContext) {
        Preconditions.checkArgument(planningContext.isPlanContext(), NOT_IN_PLAN_CONTEXT_EXCEPTION);
        Long deviceModelId = getObjectModelId(deviceModel);
        String routerId = String.valueOf(physicalInventoryRepository.createDevice(LOCATION_TYPE_BUILDING,
                Long.parseLong(buildingId), deviceModelId, deviceName, DEVICE_MODEL_TYPE, planningContext.getProjectId()));
        String chassisId = getDeviceChassisId(routerId, planningContext);
        ObjectIdentifier parentRouter = ObjectIdentifier.ipDevice(Long.valueOf(parentDeviceId));
        ObjectIdentifier childRouter = ObjectIdentifier.ipDevice(Long.valueOf(routerId));
        ObjectIdentifier childChassis = ObjectIdentifier.chassis(Long.valueOf(chassisId));
        planningRepository.connectRoots(parentRouter, childRouter, planningContext);
        DelayUtils.sleep(500);
        planningRepository.connectRoots(parentRouter, childChassis, planningContext);
        return routerId;
    }

    public static void updateIPDeviceSerialNumberInPlan(String deviceName, String deviceId, String deviceModel, String serialNumber,
                                                        String buildingId, PlanningContext planningContext) {
        Preconditions.checkArgument(planningContext.isPlanContext(), NOT_IN_PLAN_CONTEXT_EXCEPTION);
        Long deviceModelId = getObjectModelId(deviceModel);
        physicalInventoryRepository.updateDeviceSerialNumber(Long.parseLong(deviceId), deviceName,
                LOCATION_TYPE_BUILDING, Long.parseLong(buildingId), serialNumber,
                deviceModelId, DEVICE_MODEL_TYPE, planningContext.getProjectId());
    }

    public static void deleteIPDevice(String deviceId, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            physicalInventoryRepository.deleteDevice(deviceId, planningContext.getProjectId());
        } else {
            physicalInventoryRepository.deleteDevice(deviceId);
        }
    }

    public static String createCardForDevice(String deviceId, String deviceSlotName, PlanningContext planningContext) {
        Long cardModelId = getObjectModelId(CARD_MODEL);
        if (planningContext.isPlanContext()) {
            physicalInventoryRepository.createCard(Long.parseLong(deviceId), deviceSlotName,
                    cardModelId, CARD_MODEL_TYPE, planningContext.getProjectId());
            return String.valueOf(physicalInventoryClient.getDeviceCardUnderChassisId(
                    physicalInventoryClient.getDeviceChassisId(Long.parseLong(deviceId), CHASSIS_NAME,
                            planningContext.getProjectId()), CARD_NAME, planningContext.getProjectId()));
        } else {
            physicalInventoryRepository.createCard(Long.parseLong(deviceId), deviceSlotName,
                    cardModelId, CARD_MODEL_TYPE);
            return String.valueOf(physicalInventoryClient.getDeviceCardUnderChassisId(
                    physicalInventoryClient.getDeviceChassisId(Long.parseLong(deviceId), CHASSIS_NAME), CARD_NAME));
        }
    }

    public static String getDeviceChassisId(String deviceId, PlanningContext planningContext) {
        if (planningContext.isPlanContext()) {
            return String.valueOf(physicalInventoryClient.getDeviceChassisId(
                    Long.parseLong(deviceId), CHASSIS_NAME, planningContext.getProjectId()));
        } else {
            return String.valueOf(physicalInventoryClient.getDeviceChassisId(
                    Long.parseLong(deviceId), CHASSIS_NAME));
        }
    }


}
