package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ethernetInterface.EIWizardPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class EthernetLinkWizardPage extends TrailWizardPage {
    private final Wizard wizard;
    private static final String TRAIL_TYPE = "Ethernet Link";
    private static final String NAME_FIELD_ID = "trailNameComponent";
    private static final String SPEED_FIELD_ID = "oss.transport.trail.type.Ethernet Link.Speed";
    private static final String WIZARD_ID = "trailWizardId_prompt-card";
    private static final String DESCRIPTION_FIELD_ID = "trailDescriptionComponent";
    private static final String IS_TRUNK_FIELD_ID = "oss.transport.trail.type.Ethernet Link.Is Trunk";
    private static final String ROLE_FIELD_ID = "oss.transport.trail.type.Ethernet Link.Role";
    private static final String LATENCY_FIELD_ID = "oss.transport.trail.type.Ethernet Link.Latency";
    private static final String EFFECTIVE_CAPACITY_UNIT_FIELD_ID = "oss.transport.trail.type.Ethernet Link.Effective Capacity Unit";
    private static final String EFFECTIVE_CAPACITY_FIELD_ID = "oss.transport.trail.type.Ethernet Link.Effective Capacity [Mbps]";

    public EthernetLinkWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
    @Override
    public String getTrailType() {
        return TRAIL_TYPE;
    }
    public void setName(String name) {
        wizard.setComponentValue(NAME_FIELD_ID, name);
    }
    public void setDescription(String description){
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description);
    }
    public void setIsTrunk(String isTrunk){
        wizard.setComponentValue(IS_TRUNK_FIELD_ID, isTrunk);
    }
    public void setSpeed(String speed){
        wizard.setComponentValue(SPEED_FIELD_ID, speed);
    }
    public void setRole(String role){
        wizard.setComponentValue(ROLE_FIELD_ID, role);
    }
    public void setLatency(String latency){
        wizard.setComponentValue(LATENCY_FIELD_ID, latency);
    }
    public void setEffectiveCapacityUnit(String effectiveCapacityUnit){
        wizard.setComponentValue(EFFECTIVE_CAPACITY_UNIT_FIELD_ID, effectiveCapacityUnit);
    }
    public void setEffectiveCapacity(String effectiveCapacity){
        wizard.setComponentValue(EFFECTIVE_CAPACITY_FIELD_ID, effectiveCapacity);
    }
    public void clickNext(){
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNext();
    }
    public void clickAccept(){
        DelayUtils.waitForPageToLoad(driver,wait);
        wizard.clickAccept();
    }
}
