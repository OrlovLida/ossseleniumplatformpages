package com.oss.pages.physical;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

public class DeviceWizardPage extends BasePage {
    public static DeviceWizardPage goToDeviceWizardPageLive(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/physical-inventory/devicewizard?"+"perspective=LIVE",basicURL));
        return new DeviceWizardPage(driver);
    }
    public static DeviceWizardPage goToDeviceWizardPagePlan(WebDriver driver, String basicURL,String perspective){
        driver.get(String.format("%s/#/view/physical-inventory/devicewizard?"+ perspective,basicURL));
        return new DeviceWizardPage(driver);
    }

    private Wizard wizard;

    public DeviceWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard getWizard() {
        if(this.wizard == null) {
            this.wizard = Wizard.createWizard(this.driver, this.wait);
        }
        return wizard;
    }
   // private Wizard physicalDeviceWizard= Wizard.createWizard(driver,wait);

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        Input input = Wizard.createWizard(driver,wait).getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {

        Input input = Wizard.createWizard(driver,wait).getComponent(componentId, componentType);
        //Input input = getWizard().getComponent(componentId, componentType);
        return input.getStringValue();
    }

    public String createGenericIPDevice(){
        Wizard physicalDeviceWizard= Wizard.createWizard(driver,wait);
        String deviceName="Device-Selenium-"+ (int) (Math.random() * 1001);
        createDevice("Generic IP Device",deviceName,"A","Other",deviceName);
        DelayUtils.sleep(2000);
        deviceName = getComponentValue("text_name", Input.ComponentType.TEXT_FIELD);
        Assertions.assertThat(getComponentValue("search_location", Input.ComponentType.SEARCH_FIELD)).isNotEmpty();
        physicalDeviceWizard.clickCreate();
        physicalDeviceWizard.waitToClose();
        return deviceName;
    }
    public void createDevice(String deviceName, String preciseLocation,String deviceModel){
        Wizard physicalDeviceWizard= Wizard.createWizard(driver,wait);

        createDevice(deviceModel,deviceName,preciseLocation,"Other",deviceName);
        DelayUtils.sleep(1000);
        Assertions.assertThat(getComponentValue("search_location", Input.ComponentType.SEARCH_FIELD)).isNotEmpty();
        physicalDeviceWizard.clickCreate();
        physicalDeviceWizard.waitToClose();

    }
    private void createDevice(String deviceModel,String deviceName, String preciseLocation, String networkDomain, String hostName){
        setComponentValue("search_model", deviceModel, Input.ComponentType.SEARCH_FIELD);
        setComponentValue("text_name",deviceName, Input.ComponentType.TEXT_FIELD);
        setComponentValue("search_precise_location",preciseLocation, Input.ComponentType.SEARCH_FIELD);
        setComponentValue("search_network_domain",networkDomain, Input.ComponentType.SEARCH_FIELD);
       if(driver.getPageSource().contains("Hostname")){
            setComponentValue("text_hostname",hostName, Input.ComponentType.TEXT_FIELD);
        }

    }


    public void cancel() {
        Wizard.createWizard(driver,wait).cancel();
    }
    public void create(){Wizard.createWizard(driver,wait).clickCreate();}

}
