package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.DATE_TIME;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class DeviceWizardPage extends BasePage {

    public static final String DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "preciseLocation_OSF";
    public static final String DEVICE_PHYSICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "physicalLocation_OSF";
    private static final String CREATE_BUTTON_DATA_ATTRIBUTE_NAME = "physical_device_common_buttons_app-1";
    private static final String UPDATE_BUTTON_DATA_ATTRIBUTE_NAME = "physical_device_update_common_buttons_app-1";
    private static final String NEXT_BUTTON_UPDATE_WIZARD_DATA_ATTRIBUTE_NAME = "wizard-next-button-device_update_wizard_view";
    private static final String ACCEPT_UPDATE_WIZARD_BUTTON_DATA_ATTRIBUTE_NAME = "wizard-submit-button-device_update_wizard_view";
    private static final String DEVICE_EQUIPMENT_TYPE_DATA_ATTRIBUTE_NAME = "equipmentType";
    private static final String DEVICE_MODEL_DATA_ATTRIBUTE_NAME = "model_OSF";
    private static final String DEVICE_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String DEVICE_NETWORK_FUNCTION_NAME_TYPE_DATA_ATTRIBUTE_NAME = "networkFunctionName";
    private static final String DEVICE_CHASSIS_ID_DATA_ATTRIBUTE_NAME = "chassisId";
    private static final String DEVICE_LOCATION_DATA_ATTRIBUTE_NAME = "location_OSF";
    private static final String DEVICE_LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME = "search_logical_location";
    private static final String DEVICE_NETWORK_DOMAIN_DATA_ATTRIBUTE_NAME = "networkDomain";
    private static final String DEVICE_SERIAL_NUMBER_DATA_ATTRIBUTE_NAME = "serialNumber";
    private static final String DEVICE_HOSTNAME_DATA_ATTRIBUTE_NAME = "hostname";
    private static final String DEVICE_OSS_DATA_ATTRIBUTE_NAME = "oss";
    private static final String DEVICE_MANUFACTURE_DATE_DATA_ATTRIBUTE_NAME = "manufactureDate";
    private static final String DEVICE_FIRMWARE_VERSION_DATA_ATTRIBUTE_NAME = "firmwareVersion";
    private static final String DEVICE_HARDWARE_VERSION_DATA_ATTRIBUTE_NAME = "hardwareVersion";
    private static final String DEVICE_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String DEVICE_CREATE_WIZARD_PLAN = "device_create_wizard_view";
    private static final String DEVICE_CREATE_WIZARD_LIVE = "devices_create_wizard_view";
    private static final String DEVICE_UPDATE_WIZARD = "device_update_wizard_view";
    private static final String DEVICE_AVAILABLE_MOUNTING_POSITIONS_DATA_ATTRIBUTE_NAME = "mountingPosition";

    private static final String DEVICE_TYPE_PATTERN = "oss__046__physical__045__inventory__046__physicaldevice__046__type__046__";

    public DeviceWizardPage(WebDriver driver) {
        super(driver);
    }

    public static DeviceWizardPage goToDeviceWizardPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/physical-inventory/wizard/devices/create?" + "perspective=LIVE", basicURL));
        return new DeviceWizardPage(driver);
    }

    @Step("Create Device with mandatory fields (Equipment type, Model, Name, Location, Precise Location) filled in")
    public void createDeviceOnlyByName(String name) {
        setEquipmentType(" ");
        DelayUtils.sleep(250);
        setModel(" ");
        DelayUtils.sleep(250);
        setName(name);
        DelayUtils.sleep(250);
        setPreciseLocation(" ");
        DelayUtils.sleep(250);
        create();
    }

    @Step("Create Device with mandatory fields (Equipment type, Model, Name, Location, Physical Location, Precise Location) filled in")
    public void createDevice(String model, String name, String location) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setModel(model);
        DelayUtils.waitForPageToLoad(driver, wait);
        setName(name);
        DelayUtils.waitForPageToLoad(driver, wait);
        next();
        DelayUtils.waitForPageToLoad(driver, wait);
        setPreciseLocation(location);
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Set Model using contains")
    public void setModel(String model) {
        getDeviceWizard().getComponent(DEVICE_MODEL_DATA_ATTRIBUTE_NAME, SEARCH_FIELD).setSingleStringValueContains(model);
    }

    @Step("Set Name")
    public void setName(String name) {
        getDeviceWizard().setComponentValue(DEVICE_NAME_DATA_ATTRIBUTE_NAME, name, TEXT_FIELD);
    }

    @Step("Set Network Function Name")
    public void setNetworkFunctionName(String networkFunctionName) {
        getDeviceWizard().setComponentValue(DEVICE_NETWORK_FUNCTION_NAME_TYPE_DATA_ATTRIBUTE_NAME, networkFunctionName, TEXT_FIELD);
    }

    @Step("Set Chassis Id")
    public void setChassisId(String chassisId) {
        getDeviceWizard().setComponentValue(DEVICE_CHASSIS_ID_DATA_ATTRIBUTE_NAME, chassisId, TEXT_FIELD);
    }

    @Step("Set Location using contains")
    public void setLocation(String location) {
        if (getDeviceWizard().getComponent(DEVICE_LOCATION_DATA_ATTRIBUTE_NAME, OBJECT_SEARCH_FIELD)
                .getStringValue().isEmpty()) {
            getDeviceWizard().getComponent(DEVICE_LOCATION_DATA_ATTRIBUTE_NAME, OBJECT_SEARCH_FIELD)
                    .setSingleStringValueContains(location);
        }
    }

    @Step("Set Physical Location using contains")
    public void setPhysicalLocation(String preciseLocation) {
        if (getDeviceWizard().getComponent(DEVICE_PHYSICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.OBJECT_SEARCH_FIELD)
                .getStringValue().isEmpty()) {
            getDeviceWizard().getComponent(DEVICE_PHYSICAL_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, OBJECT_SEARCH_FIELD)
                    .setSingleStringValueContains(preciseLocation);
        }
    }

    @Step("Set Precise Location using contains")
    public void setPreciseLocation(String preciseLocation) {
        if (getDeviceWizard().getComponent(DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.OBJECT_SEARCH_FIELD)
                .getStringValue().isEmpty()) {
            getDeviceWizard().getComponent(DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, OBJECT_SEARCH_FIELD)
                    .setSingleStringValueContains(preciseLocation);
        }
    }

    @Step("Set Available Mounting Positions")
    public void setFirstAvailableMountingPosition() {
        Input mountingPositionInput = getDeviceWizard().getComponent(DEVICE_AVAILABLE_MOUNTING_POSITIONS_DATA_ATTRIBUTE_NAME, COMBOBOX);
        mountingPositionInput.setSingleStringValueContains("");
    }

    @Step("Set Logical Location")
    public void setLogicalLocation(String logicalLocation) {
        getDeviceWizard().setComponentValue(DEVICE_LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME, logicalLocation, SEARCH_FIELD);
    }

    @Step("Set Network Domain")
    public void setNetworkDomain(String networkDomain) {
        getDeviceWizard().setComponentValue(DEVICE_NETWORK_DOMAIN_DATA_ATTRIBUTE_NAME, networkDomain, SEARCH_FIELD);
    }

    @Step("Set Serial Number")
    public void setSerialNumber(String serialNumber) {
        getDeviceWizard().setComponentValue(DEVICE_SERIAL_NUMBER_DATA_ATTRIBUTE_NAME, serialNumber, TEXT_FIELD);
    }

    @Step("Set Hostname")
    public void setHostname(String hostname) {
        getDeviceWizard().setComponentValue(DEVICE_HOSTNAME_DATA_ATTRIBUTE_NAME, hostname, TEXT_FIELD);
    }

    @Step("Set Oss")
    public void setOss(String oss) {
        getDeviceWizard().setComponentValue(DEVICE_OSS_DATA_ATTRIBUTE_NAME, oss, SEARCH_FIELD);
    }

    @Step("Set Manufacture Date")
    public void setManufactureDate(String manufactureDate) {
        getDeviceWizard().setComponentValue(DEVICE_MANUFACTURE_DATE_DATA_ATTRIBUTE_NAME, manufactureDate, DATE_TIME);
    }

    @Step("Set Firmware Version")
    public void setFirmwareVersion(String firmwareVersion) {
        getDeviceWizard().setComponentValue(DEVICE_FIRMWARE_VERSION_DATA_ATTRIBUTE_NAME, firmwareVersion, TEXT_FIELD);
    }

    @Step("Set Hardware Version")
    public void setHardwareVersion(String hardwareVersion) {
        getDeviceWizard().setComponentValue(DEVICE_HARDWARE_VERSION_DATA_ATTRIBUTE_NAME, hardwareVersion, TEXT_FIELD);
    }

    @Step("Set Description")
    public void setDescription(String description) {
        getDeviceWizard().setComponentValue(DEVICE_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, TEXT_AREA);
    }

    @Step("Set Cooling Capacity")
    public void setCoolingCapacity(String coolingCapacity) {
        String deviceCoolingCapacityDataAttributeName =
                DEVICE_TYPE_PATTERN + getEquipmentType() + "___CoolingCapacity";
        getDeviceWizard().setComponentValue(deviceCoolingCapacityDataAttributeName, coolingCapacity, TEXT_FIELD);
    }

    @Step("Set Heat Emission")
    public void setHeatEmission(String heatEmission) {
        String deviceHeatEmissionDataAttributeName =
                DEVICE_TYPE_PATTERN + getEquipmentType() + "___HeatEmission";
        getDeviceWizard().setComponentValue(deviceHeatEmissionDataAttributeName, heatEmission, TEXT_FIELD);
    }

    @Step("Set Power Consumption")
    public void setPowerConsumption(String powerConsumption) {
        String devicePowerConsumptionDataAttributeName =
                DEVICE_TYPE_PATTERN + getEquipmentType() + "___PowerConsumption";
        getDeviceWizard().setComponentValue(devicePowerConsumptionDataAttributeName, powerConsumption, TEXT_FIELD);
    }

    @Step("Set Power Capacity")
    public void setPowerCapacity(String powerCapacity) {
        String devicePowerCapacityDataAttributeName =
                DEVICE_TYPE_PATTERN + getEquipmentType() + "___PowerCapacity";
        getDeviceWizard().setComponentValue(devicePowerCapacityDataAttributeName, powerCapacity, TEXT_FIELD);
    }

    @Step("Click Cancel button")
    public void cancel() {
        getDeviceWizard().clickCancel();
    }

    @Step("Click Create button")
    public void create() {
        getDeviceWizard().clickButtonById(CREATE_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    @Step("Click Update button")
    public void update() {
        getDeviceWizard().clickButtonById(UPDATE_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    @Step("Click Next button in Create Device Wizard")
    public void next() {
        getDeviceWizard().clickNext();
    }

    @Step("Click Next button in Update Device Wizard")
    public void nextUpdateWizard() {
        getDeviceWizard().clickButtonById(NEXT_BUTTON_UPDATE_WIZARD_DATA_ATTRIBUTE_NAME);
    }

    @Step("Click Accept button in Create Device Wizard")
    public void accept() {
        getDeviceWizard().clickAccept();
    }

    @Step("Click Accept button in Update Device Wizard")
    public void acceptUpdateWizard() {
        getDeviceWizard().clickButtonById(ACCEPT_UPDATE_WIZARD_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    private Wizard getDeviceWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (driver.getPageSource().contains(DEVICE_CREATE_WIZARD_PLAN)) {
            return Wizard.createByComponentId(driver, wait, DEVICE_CREATE_WIZARD_PLAN);
        }
        if (driver.getPageSource().contains(DEVICE_CREATE_WIZARD_LIVE)) {
            return Wizard.createByComponentId(driver, wait, DEVICE_CREATE_WIZARD_LIVE);
        } else
            return Wizard.createByComponentId(driver, wait, DEVICE_UPDATE_WIZARD);
    }

    private String getEquipmentType() {
        String equipmentType = getDeviceWizard().getComponent(DEVICE_EQUIPMENT_TYPE_DATA_ATTRIBUTE_NAME, SEARCH_FIELD).getStringValue().trim().replaceAll("\\s", "");
        if (equipmentType.equals("DWDMDevice")) {
            return "DWDM";
        }
        return equipmentType;
    }

    public boolean isNextStepPresent() {
        return getDeviceWizard().isNextStepPresent();
    }

    public String getCurrentStateTitle() {
        return getDeviceWizard().getCurrentStepTitle();
    }

    @Step("Set Equipment Type")
    public void setEquipmentType(String equipmentType) {
        getDeviceWizard().setComponentValue(DEVICE_EQUIPMENT_TYPE_DATA_ATTRIBUTE_NAME, equipmentType, COMBOBOX);
    }
}
