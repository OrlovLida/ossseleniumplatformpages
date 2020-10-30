package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class DeviceWizardPage extends BasePage {

    private static final String DEVICE_CREATE_WIZARD_DATA_ATTRIBUTE_NAME = "physical_device_create_wizard_view";
    private static final String DEVICE_UPDATE_WIZARD_DATA_ATTRIBUTE_NAME = "physical_device_update_wizard_view";
    private static final String CREATE_BUTTON_DATA_ATTRIBUTE_NAME = "physical_device_common_buttons_app-1";
    private static final String UPDATE_BUTTON_DATA_ATTRIBUTE_NAME = "physical_device_update_common_buttons_app-1";
    private static final String DEVICE_EQUIPMENT_TYPE_DATA_ATTRIBUTE_NAME = "search_equipment_type";
    private static final String DEVICE_MODEL_DATA_ATTRIBUTE_NAME = "search_model";
    private static final String DEVICE_NAME_DATA_ATTRIBUTE_NAME = "text_name";
    private static final String DEVICE_NETWORK_FUNCTION_NAME_TYPE_DATA_ATTRIBUTE_NAME = "text_network_function_name";
    private static final String DEVICE_CHASSIS_ID_DATA_ATTRIBUTE_NAME = "text_chassis_id";
    private static final String DEVICE_LOCATION_DATA_ATTRIBUTE_NAME = "search_location";
    private static final String DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME = "search_precise_location";
    private static final String DEVICE_LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME = "search_logical_location";
    private static final String DEVICE_NETWORK_DOMAIN_DATA_ATTRIBUTE_NAME = "search_network_domain";
    private static final String DEVICE_SERIAL_NUMBER_DATA_ATTRIBUTE_NAME = "text_serial_number";
    private static final String DEVICE_HOSTNAME_DATA_ATTRIBUTE_NAME = "text_hostname";
    private static final String DEVICE_OSS_DATA_ATTRIBUTE_NAME = "search_oss";
    private static final String DEVICE_MANUFACTURE_DATE_DATA_ATTRIBUTE_NAME = "date_time_manufacture";
    private static final String DEVICE_FIRMWARE_VERSION_DATA_ATTRIBUTE_NAME = "text_firmware_licence";
    private static final String DEVICE_HARDWARE_VERSION_DATA_ATTRIBUTE_NAME = "text_hardware_licence";
    private static final String DEVICE_DESCRIPTION_DATA_ATTRIBUTE_NAME = "text_description";
    private static final String DEVICE_IS_OWNED_BY_3RD_PARTY_DATA_ATTRIBUTE_NAME = "checkbox_is_leased";

    public static DeviceWizardPage goToDeviceWizardPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/physical-inventory/devicewizard?" + "perspective=LIVE", basicURL));
        return new DeviceWizardPage(driver);
    }

    public static DeviceWizardPage goToDeviceWizardPagePlan(WebDriver driver, String basicURL, String perspective) {
        driver.get(String.format("%s/#/view/physical-inventory/devicewizard?" + perspective, basicURL));
        return new DeviceWizardPage(driver);
    }

    public DeviceWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create Device with mandatory fields (Equipment type, Model, Name, Location, Precise Location) filled in")
    public void createDevice(String model, String name, String preciseLocation) {
        setModel(model);
        setName(name);
        setPreciseLocationContains(preciseLocation);
        create();
    }

    @Step("Set Equipment Type")
    public void setEquipmentType(String equipmentType) {
        getCreateDeviceWizard().setComponentValue(DEVICE_EQUIPMENT_TYPE_DATA_ATTRIBUTE_NAME, equipmentType, SEARCH_FIELD);
    }

    @Step("Set Model")
    public void setModel(String model) {
        getCreateDeviceWizard().setComponentValue(DEVICE_MODEL_DATA_ATTRIBUTE_NAME, model, SEARCH_FIELD);
    }

    @Step("Update Model")
    public void updateModel(String model) {
        getUpdateDeviceWizard().setComponentValue(DEVICE_MODEL_DATA_ATTRIBUTE_NAME, model, SEARCH_FIELD);
    }

    @Step("Set Name")
    public void setName(String name) {
        getCreateDeviceWizard().setComponentValue(DEVICE_NAME_DATA_ATTRIBUTE_NAME, name, TEXT_FIELD);
    }

    @Step("Set Network Function Name")
    public void setNetworkFunctionName(String networkFunctionName) {
        getCreateDeviceWizard().setComponentValue(DEVICE_NETWORK_FUNCTION_NAME_TYPE_DATA_ATTRIBUTE_NAME, networkFunctionName, TEXT_FIELD);
    }

    @Step("Set Chassis Id")
    public void setChassisId(String chassisId) {
        getCreateDeviceWizard().setComponentValue(DEVICE_CHASSIS_ID_DATA_ATTRIBUTE_NAME, chassisId, TEXT_FIELD);
    }

    @Step("Set Location")
    public void setLocation(String location) {
        getCreateDeviceWizard().setComponentValue(DEVICE_LOCATION_DATA_ATTRIBUTE_NAME, location, SEARCH_FIELD);
    }

    @Step("Set Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        getCreateDeviceWizard().setComponentValue(DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, preciseLocation, SEARCH_FIELD);
    }

    @Step("Set Precise Location")
    public void setPreciseLocationContains(String preciseLocation) {
        if (getCreateDeviceWizard().getComponent(DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, Input.ComponentType.SEARCH_FIELD).getStringValue().isEmpty()) {
            getCreateDeviceWizard().getComponent(DEVICE_PRECISE_LOCATION_TYPE_DATA_ATTRIBUTE_NAME, SEARCH_FIELD).setSingleStringValueContains(preciseLocation);
        }
    }

    @Step("Set Logical Location")
    public void setLogicalLocation(String logicalLocation) {
        getCreateDeviceWizard().setComponentValue(DEVICE_LOGICAL_LOCATION_DATA_ATTRIBUTE_NAME, logicalLocation, SEARCH_FIELD);
    }

    @Step("Set Network Domain")
    public void setNetworkDomain(String networkDomain) {
        getCreateDeviceWizard().setComponentValue(DEVICE_NETWORK_DOMAIN_DATA_ATTRIBUTE_NAME, networkDomain, SEARCH_FIELD);
    }

    @Step("Set Serial Number")
    public void setSerialNumber(String serialNumber) {
        getCreateDeviceWizard().setComponentValue(DEVICE_SERIAL_NUMBER_DATA_ATTRIBUTE_NAME, serialNumber, TEXT_FIELD);
    }

    @Step("Set Hostname")
    public void setHostname(String hostname) {
        getCreateDeviceWizard().setComponentValue(DEVICE_HOSTNAME_DATA_ATTRIBUTE_NAME, hostname, TEXT_FIELD);
    }

    @Step("Set Oss")
    public void setOss(String oss) {
        getCreateDeviceWizard().setComponentValue(DEVICE_OSS_DATA_ATTRIBUTE_NAME, oss, SEARCH_FIELD);
    }

    @Step("Set Manufacture Date")
    public void setManufactureDate(String manufactureDate) {
        getCreateDeviceWizard().setComponentValue(DEVICE_MANUFACTURE_DATE_DATA_ATTRIBUTE_NAME, manufactureDate, DATE_TIME);
    }

    @Step("Set Firmware Version")
    public void setFirmwareVersion(String firmwareVersion) {
        getCreateDeviceWizard().setComponentValue(DEVICE_FIRMWARE_VERSION_DATA_ATTRIBUTE_NAME, firmwareVersion, TEXT_FIELD);
    }

    @Step("Set Hardware Version")
    public void setHardwareVersion(String hardwareVersion) {
        getCreateDeviceWizard().setComponentValue(DEVICE_HARDWARE_VERSION_DATA_ATTRIBUTE_NAME, hardwareVersion, TEXT_FIELD);
    }

    @Step("Set Description")
    public void setDescription(String description) {
        getCreateDeviceWizard().setComponentValue(DEVICE_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, TEXT_AREA);
    }

    @Step("Update Description")
    public void updateDescription(String description) {
        Wizard descriptionComponent = getUpdateDeviceWizard();
        descriptionComponent.getComponent(DEVICE_UPDATE_WIZARD_DATA_ATTRIBUTE_NAME, TEXT_AREA).clear();
        descriptionComponent.setComponentValue(DEVICE_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, TEXT_AREA);
    }

    @Step("Click Cancel button")
    public void cancel() {
        getCreateDeviceWizard().cancel();
    }

    @Step("Click Create button")
    public void create() {
        getCreateDeviceWizard().clickActionById(CREATE_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    @Step("Click Update button")
    public void update() {
        getUpdateDeviceWizard().clickActionById(UPDATE_BUTTON_DATA_ATTRIBUTE_NAME);
    }

    private Wizard getCreateDeviceWizard() {
        return Wizard.createByComponentId(driver, wait, DEVICE_CREATE_WIZARD_DATA_ATTRIBUTE_NAME);
    }

    private Wizard getUpdateDeviceWizard() {
        return Wizard.createByComponentId(driver, wait, DEVICE_UPDATE_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}