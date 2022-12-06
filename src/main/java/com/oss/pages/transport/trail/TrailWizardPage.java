/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Robert Nawrat
 */
public abstract class TrailWizardPage extends BasePage {

    private static final String NAME_COMPONENT_ID = "uid_name";
    private static final String DESCRIPTION_COMPONENT_ID = "uid_description";
    private static final String CAPACITY_UNIT_COMPONENT_ID = "uid_capacity_unit";
    private static final String CAPACITY_VALUE_COMPONENT_ID = "uid_capacity_value";
    private static final String UNDEFINED = "Undefined";
    private static final String ID = "trailWizardId_prompt-card";

    private final Wizard wizard;

    protected TrailWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, ID);
    }

    public abstract String getTrailType();

    public void clearName() {
        clearTextField(NAME_COMPONENT_ID);
    }

    public void setName(String name) {
        setTextField(NAME_COMPONENT_ID, name);
    }

    public void clearDescription() {
        clearTextField(DESCRIPTION_COMPONENT_ID);
    }

    public void setDescription(String description) {
        setTextField(DESCRIPTION_COMPONENT_ID, description);
    }

    public void setDefaultCapacityUnit() {
        setCapacityUnit(MPLSNetworkWizardPage.CapacityUnit.bps);
    }

    public void setCapacityUnit(MPLSNetworkWizardPage.CapacityUnit capacityUnit) {
        setComboboxField(CAPACITY_UNIT_COMPONENT_ID, capacityUnit.toString());
    }

    public void setDefaultCapacityValue() {
        setCapacityValue(UNDEFINED);
    }

    public void setCapacityValue(String capacity) {
        setTextField(CAPACITY_VALUE_COMPONENT_ID, capacity);
    }

    public void proceed() {
        wizard.clickButtonByLabel("Proceed");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setTextField(String componentId, String value) {
        Input textField = wizard.getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue(value);
    }

    private void clearTextField(String componentId) {
        Input textField = wizard.getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue(" ");
        textField.clearByAction();
    }

    private void setComboboxField(String componentId, String value) {
        Input comboboxField = wizard.getComponent(componentId, Input.ComponentType.COMBOBOX);
        comboboxField.setSingleStringValue(value);
    }

    protected void setSearchField(String componentId, String value) {
        Input searchField = wizard.getComponent(componentId, Input.ComponentType.SEARCH_FIELD);
        searchField.setSingleStringValue(value);
    }

    protected void clearSearchField(String componentId) {
        Input searchField = wizard.getComponent(componentId, Input.ComponentType.SEARCH_FIELD);
        searchField.setSingleStringValue(" ");
        searchField.clearByAction();
    }

    public enum CapacityUnit {
        bps, kbps, Mbps, Gbps, Tbps
    }

}
