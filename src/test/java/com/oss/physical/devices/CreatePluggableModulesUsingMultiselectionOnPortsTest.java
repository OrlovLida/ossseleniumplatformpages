package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.CreatePluggableModuleWizardPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;

public class CreatePluggableModulesUsingMultiselectionOnPortsTest extends BaseTestCase {

    private static final String LIVE_PERSPECTIVE = "Live";

    private static final String DEVICE_NAME = "seleniumTestDevice";
    private static final String LOCATION_NAME = "seleniumTestLocation";

    private static final String BUILDING_TYPE = "Building";
    private static final String ADDRESS_TYPE = "Address";

    private static final String DEVICE_MODEL_NAME = "PSP-32";
    private static final String DEVICE_MODEL_MANUFACTURER = "Optomer";
    private static final String DEVICE_MODEL_TYPE = "ODFModel";
    private static final String ODF_TYPE = "ODF";

    private static final String PLUGGABLE_MODULE_MODEL_NAME = "Generic SC/APC Front/Back";

    private static final int AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW = 1;
    private static final int AMOUNT_OF_PORTS = 3;
    private static final int AMOUNT_OF_CONNECTORS = 2;
    private static final String COMPACTED_PORT_FIELD_VALUE = "3 selected";

    private static final String PORT_PATH_FORMAT = "%s.ports.%s";
    private static final String PLUGGABLE_MODULE_PATH_FORMAT = "%s.ports.%s.pluggable module slot.%s.pluggable module.%s";
    private static final String CONNECTOR_PATH_FORMAT = "%s.ports.%s.termination points.connector";

    private static final String DEVICE_MODEL_NOT_FOUND_VALIDATION = "Device model %s %s was not found";
    private static final String WRONG_AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW_VALIDATION = "There is wrong amount of devices on Hierarchy View";
    private static final String WRONG_WIZARD_NAME_VALIDATION = "Wrong wizard name in Create Pluggable Module Wizard";
    private static final String WRONG_PORT_FIELD_VALUE_IN_PLUGGABLE_MODULE_WIZARD_VALIDATION = "Wrong value in port field in Pluggable Module Wizard";
    private static final String WRONG_AMOUNT_OF_PORTS_IN_PLUGGABLE_MODULE_WIZARD_VALIDATION = "Wrong amount of ports in port field in Pluggable Module Wizard";
    private static final String WRONG_MODELS_ARE_AVAILABLE_VALIDATION = "There are wrong models available in model field in Create Pluggable Module Wizard";
    private static final String NO_SUCCESS_MESSAGE_VALIDATION = "There is no success message after Pluggable Module creation";
    private static final String CANNOT_FIND_COMPONENT_VALIDATION = "Cannot find any component under object with id: %s";
    private static final String PLUGGABLE_MODULE_NOT_VISIBLE_VALIDATION = "Pluggable Module with id: %s is not visible on Hierarchy View";
    private static final String WRONG_AMOUNT_OF_CONNECTORS_VALIDATION = "Wrong amount of connectors in port with id: %s";

    private static final String CREATE_ACTION_ID = "CREATE";
    private static final String CREATE_PLUGGABLE_MODULE_ON_PORT_ACTION_ID = "CreatePluggableModuleOnPortAction";

    private static final String CREATE_PLUGGABLE_MODULE_WIZARD_NAME = "Create Pluggable Module";

    private static final String PLUGGABLE_MODULE_CREATED_MESSAGE = "Pluggable Module has been created successfully";

    private final LocationInventoryRepository locationInventoryRepository;
    private final PhysicalInventoryRepository physicalInventoryRepository;
    private final PlanningRepository planningRepository;

    private HierarchyViewPage hierarchyViewPage;
    private CreatePluggableModuleWizardPage createPluggableModuleWizardPage;

    private Long deviceId;
    private List<String> portsIds;
    private Long locationId;

    private final Set<String> compatiblePluggableModuleModels;

    private final String deviceNameWithTimestamp;
    private final String locationNameWithTimestamp;

    {
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        planningRepository = new PlanningRepository(Environment.getInstance());

        String timestamp = getTimestamp();
        deviceNameWithTimestamp = DEVICE_NAME + "_" + timestamp;
        locationNameWithTimestamp = LOCATION_NAME + "_" + timestamp;

        compatiblePluggableModuleModels = new HashSet<>(Arrays.asList("Generic SC/PC Front/Back", "Generic SC/APC Front/Back"));
    }

    @BeforeClass
    public void init() {
        Long deviceModelId = planningRepository.getObjectIdByTypeAndName(DEVICE_MODEL_TYPE, DEVICE_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(DEVICE_MODEL_NOT_FOUND_VALIDATION, DEVICE_MODEL_MANUFACTURER, DEVICE_MODEL_NAME)));

        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        String location = locationInventoryRepository.createLocation(locationNameWithTimestamp, BUILDING_TYPE, addressId);
        locationId = Long.parseLong(location);
        deviceId = physicalInventoryRepository.createDevice(BUILDING_TYPE, locationId, deviceModelId, deviceNameWithTimestamp, DEVICE_MODEL_TYPE);
    }

    @Test(priority = 1)
    public void hierarchyViewForDeviceIsOpened() {
        setLivePerspective();
        HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, ODF_TYPE, Long.toString(deviceId));
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);

        Assert.assertEquals(hierarchyViewPage.getMainTreeSize(), AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW, WRONG_AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW_VALIDATION);
    }

    @Test(priority = 2)
    public void createPluggableModuleWizardIsOpened() {
        Map<String, List<String>> hierarchyByParent = getDeviceHierarchyByParent();
        portsIds = getThreePortsIdsFromHierarchy(hierarchyByParent);
        portsIds.forEach(this::selectPortOnHierarchyView);

        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_PLUGGABLE_MODULE_ON_PORT_ACTION_ID);

        createPluggableModuleWizardPage = new CreatePluggableModuleWizardPage(driver);
        Assert.assertEquals(createPluggableModuleWizardPage.getWizardName(), CREATE_PLUGGABLE_MODULE_WIZARD_NAME, WRONG_WIZARD_NAME_VALIDATION);
    }

    @Test(priority = 3)
    public void portFieldIsFilledInInWizard() {
        List<String> portNamesInWizard = createPluggableModuleWizardPage.getPorts();
        int amountOfPorts = portNamesInWizard.size();

        if (amountOfPorts == 1) {
            Assert.assertEquals(portNamesInWizard.get(0), COMPACTED_PORT_FIELD_VALUE, WRONG_PORT_FIELD_VALUE_IN_PLUGGABLE_MODULE_WIZARD_VALIDATION);
        } else {
            Assert.assertEquals(amountOfPorts, AMOUNT_OF_PORTS, WRONG_AMOUNT_OF_PORTS_IN_PLUGGABLE_MODULE_WIZARD_VALIDATION);
        }
    }

    @Test(priority = 4)
    public void portFieldIsInactive() {
        Assert.assertEquals(createPluggableModuleWizardPage.getPortFieldCursor(), Input.MouseCursor.NOT_ALLOWED);
    }

    @Test(priority = 5)
    public void onlyCompatiblePluggableModuleModelsAreAvailableInWizard() {
        Set<String> availableModels = createPluggableModuleWizardPage.getAvailableModels();
        Assert.assertEquals(availableModels, compatiblePluggableModuleModels, WRONG_MODELS_ARE_AVAILABLE_VALIDATION);
    }

    @Test(priority = 6)
    public void modelIsSetInWizard() {
        createPluggableModuleWizardPage.setModel(PLUGGABLE_MODULE_MODEL_NAME);
    }

    @Test(priority = 7)
    public void pluggableModulesAreCreated() {
        createPluggableModuleWizardPage.accept();
        Assert.assertTrue(getSuccessMessage().isPresent(), NO_SUCCESS_MESSAGE_VALIDATION);
    }

    @Test(priority = 8)
    public void pluggableModulesAreAddedToDeviceStructure() {
        SoftAssert softAssert = new SoftAssert();

        Map<String, List<String>> hierarchyByParent = getDeviceHierarchyByParent();
        for (String portId : portsIds) {
            String portHolderId = getIdOfFirstComponentUnderObjectWithId(portId, hierarchyByParent);
            String pluggableModuleId = getIdOfFirstComponentUnderObjectWithId(portHolderId, hierarchyByParent);
            String pluggableModulePath = getPluggableModulePath(portId, portHolderId, pluggableModuleId);

            softAssert.assertTrue(hierarchyViewPage.isNodePresentByPath(pluggableModulePath), String.format(PLUGGABLE_MODULE_NOT_VISIBLE_VALIDATION, pluggableModuleId));
        }

        softAssert.assertAll();
    }

    @Test(priority = 9)
    public void pluggableModulesHaveConnectorsInStructure() {
        for (String portId : portsIds) {
            int amountOfConnectorsOnHierarchyView = hierarchyViewPage.getNodeChildrenByPath(getConnectorPath(portId)).size();
            Assert.assertEquals(amountOfConnectorsOnHierarchyView, AMOUNT_OF_CONNECTORS, String.format(WRONG_AMOUNT_OF_CONNECTORS_VALIDATION, portId));
        }
    }

    @AfterClass
    public void deleteCreatedObjects() {
        physicalInventoryRepository.deleteDeviceV3(Collections.singletonList(Long.toString(deviceId)), PlanningContext.perspectiveLive());
        locationInventoryRepository.deleteLocation(locationId, BUILDING_TYPE);
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equals(LIVE_PERSPECTIVE)) {
            perspectiveChooser.setLivePerspective();
        }
    }

    private Map<String, List<String>> getDeviceHierarchyByParent() {
        ResourceHierarchyDTO resourceHierarchy = physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", Long.toString(deviceId), "Root");
        return ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);
    }

    private List<String> getThreePortsIdsFromHierarchy(Map<String, List<String>> hierarchy) {
        return hierarchy.entrySet().stream()
                .filter(object -> object.getKey().contains(Long.toString(deviceId)))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(component -> component.contains("Port_"))
                .map(port -> port.split("_")[1])
                .limit(AMOUNT_OF_PORTS)
                .collect(Collectors.toList());
    }

    private void selectPortOnHierarchyView(String portId) {
        String path = getPortPath(portId);
        hierarchyViewPage.selectNodeByPath(path);
    }

    private String getPortPath(String portId) {
        return String.format(PORT_PATH_FORMAT, deviceId, portId);
    }

    private Optional<SystemMessageContainer.Message> getSuccessMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(message -> message.getText().contains(PLUGGABLE_MODULE_CREATED_MESSAGE))
                .findFirst();
    }

    private String getIdOfFirstComponentUnderObjectWithId(String parentId, Map<String, List<String>> hierarchy) {
        return hierarchy.entrySet().stream()
                .filter(object -> object.getKey().contains(parentId))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .map(component -> component.split("_")[1])
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format(CANNOT_FIND_COMPONENT_VALIDATION, parentId)));
    }

    private String getPluggableModulePath(String portId, String portHolderId, String pluggableModuleId) {
        return String.format(PLUGGABLE_MODULE_PATH_FORMAT, deviceId, portId, portHolderId, pluggableModuleId);
    }

    private String getConnectorPath(String portId) {
        return String.format(CONNECTOR_PATH_FORMAT, deviceId, portId);
    }

}