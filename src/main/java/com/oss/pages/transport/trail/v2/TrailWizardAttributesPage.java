/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;

public abstract class TrailWizardAttributesPage extends BasePage {
    
    private static final String NAME_COMPONENT_ID = "trailNameComponent";
    private static final String DESCRIPTION_COMPONENT_ID = "trailDescriptionComponent";
    private static final String CAPACITY_UNIT_COMPONENT_ID = "trailCapacityUnitComponent";
    private static final String CAPACITY_VALUE_COMPONENT_ID = "trailCapacityValueComponent";
    private static final String UNDEFINED = "Undefined";
    
    private final Wizard wizard;
    
    protected TrailWizardAttributesPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }
    
    @Step("Clear Name")
    public void clearName() {
        clearTextField(NAME_COMPONENT_ID);
    }
    
    @Step("Set Name - {name}")
    public void setName(String name) {
        setTextField(NAME_COMPONENT_ID, name);
    }
    
    @Step("Clear Description")
    public void clearDescription() {
        clearTextField(DESCRIPTION_COMPONENT_ID);
    }
    
    @Step("Set Description - {description}")
    public void setDescription(String description) {
        setTextField(DESCRIPTION_COMPONENT_ID, description);
    }
    
    @Step("Set Capacity Unit - bps")
    public void setDefaultCapacityUnit() {
        setCapacityUnit(CapacityUnit.bps);
    }
    
    @Step("Set Capacity Unit - {capacityUnit}")
    public void setCapacityUnit(CapacityUnit capacityUnit) {
        setComboboxField(CAPACITY_UNIT_COMPONENT_ID, capacityUnit.toString());
    }
    
    @Step("Set Capacity Value - Undefined")
    public void setDefaultCapacityValue() {
        setCapacityValue(UNDEFINED);
    }
    
    @Step("Set Capacity Value - {capacity}")
    public void setCapacityValue(String capacity) {
        setTextField(CAPACITY_VALUE_COMPONENT_ID, capacity);
    }
    
    protected void clearTextField(String componentId) {
        setTextField(componentId, " ");
    }
    
    protected void setTextField(String componentId, String value) {
        wizard.setComponentValue(componentId, value, Input.ComponentType.TEXT_FIELD);
    }
    
    protected void setComboboxField(String componentId, String value) {
        wizard.setComponentValue(componentId, value, Input.ComponentType.COMBOBOX);
    }
    
    protected void clearCheckbox(String componentId) {
        setCheckbox(componentId, false);
    }
    
    protected void setCheckbox(String componentId, Boolean value) {
        wizard.setComponentValue(componentId, value.toString(), Input.ComponentType.CHECKBOX);
    }
    
    @Step("Click Accept button")
    public void accept() {
        wizard.clickAccept();
    }
    
    @Step("Click Next button")
    public TerminationStepPage next() {
        clickNext();
        return new TerminationStepPage(driver);
    }
    
    protected void clickNext() {
        wizard.clickNext();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
    
    public enum CapacityUnit {
        bps, kbps, Mbps, Gbps, Tbps
    }
}
