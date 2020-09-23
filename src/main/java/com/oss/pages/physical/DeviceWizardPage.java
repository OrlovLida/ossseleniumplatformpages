package com.oss.pages.physical;
import org.openqa.selenium.WebDriver;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import static com.oss.framework.components.inputs.Input.ComponentType.DATE_TIME;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class DeviceWizardPage extends BasePage {

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

    private Wizard physicalDeviceWizard = Wizard.createByComponentId(driver, wait,"physical_device_create_wizard_view");

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        Input input = physicalDeviceWizard.getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {

//        Input input = physicalDeviceWizard.getComponent(componentId, componentType);
        Input input =  Wizard.createByComponentId(driver,wait,"physical_device_create_wizard_view")
                .getComponent(componentId, componentType);
        return input.getStringValue();
    }

    public void cancel() {
        physicalDeviceWizard.cancel();
    }

    public void create() {
        Wizard.createByComponentId(driver,wait,"physical_device_create_wizard_view")
                .clickActionById("physical_device_common_buttons_app-1");
    }

    public void setModel(String model) {
        Input input = physicalDeviceWizard.getComponent("search_model", SEARCH_FIELD);
        input.setSingleStringValue(model);
    }

    public void setName(String name) {
        Input input = physicalDeviceWizard.getComponent("text_name", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    public void setNetworkFunctionName(String networkFunctionName) {
        Input input = physicalDeviceWizard.getComponent("text_network_function_name", TEXT_FIELD);
        input.setSingleStringValue(networkFunctionName);
    }

    public void setChassisId(String chassisId) {
        Input input = physicalDeviceWizard.getComponent("text_chassis_id", TEXT_FIELD);
        input.setSingleStringValue(chassisId);
    }

    public void setLocation(String location) {
        Input input = physicalDeviceWizard.getComponent("search_location", SEARCH_FIELD);
        input.setSingleStringValue(location);
    }

    public void setPreciseLocation(String preciseLocation) {
        Input input = physicalDeviceWizard.getComponent("search_precise_location", SEARCH_FIELD);
        input.setSingleStringValue(preciseLocation);
    }

    public void setLogicalLocation(String logicalLocation) {
        Input input = physicalDeviceWizard.getComponent("search_logical_location", SEARCH_FIELD);
        input.setSingleStringValue(logicalLocation);
    }

    public void setNetworkDomain(String networkDomain) {
        Input input = physicalDeviceWizard.getComponent("search_network_domain", SEARCH_FIELD);
        input.setSingleStringValue(networkDomain);
    }

    public void setDeviceCategory(String deviceCategory) {
        Input input = physicalDeviceWizard.getComponent("search_device_category", SEARCH_FIELD);
        input.setSingleStringValue(deviceCategory);
    }

    public void setSerialNumber(String serialNumber) {
        Input input = physicalDeviceWizard.getComponent("text_serial_number", TEXT_FIELD);
        input.setSingleStringValue(serialNumber);
    }

    public void setHostname(String hostname) {
        Input input = physicalDeviceWizard.getComponent("text_hostname", TEXT_FIELD);
        input.setSingleStringValue(hostname);
    }

    public void setOss(String oss) {
        Input input = physicalDeviceWizard.getComponent("search_oss", SEARCH_FIELD);
        input.setSingleStringValue(oss);
    }

    public void setManufactureDate(String manufactureDate) {
        Input input = physicalDeviceWizard.getComponent("date_time_manufacture", DATE_TIME);
        input.setSingleStringValue(manufactureDate);
    }

    public void setFirmwareVersion(String firmwareVersion) {
        Input input = physicalDeviceWizard.getComponent("text_firmware_licence", TEXT_FIELD);
        input.setSingleStringValue(firmwareVersion);
    }

    public void setHardwareVersion(String hardwareVersion) {
        Input input = physicalDeviceWizard.getComponent("text_hardware_licence", TEXT_FIELD);
        input.setSingleStringValue(hardwareVersion);
    }

    public void setDescription(String description) {
        Input input = physicalDeviceWizard.getComponent("text_description", TEXT_AREA);
        input.setSingleStringValue(description);
    }

}
