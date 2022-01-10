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
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Robert Nawrat
 */
public class TerminationWizardPage extends BasePage {

    private static final String TERMINATION_TYPE_COMPONENT_ID = "terminationType";
    private static final String NETWORK_ELEMENT_COMPONENT_ID = "deviceId";

    private final Wizard wizard;

    public TerminationWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void setTerminationType(TerminationType terminationType) {
        setComboboxField(TERMINATION_TYPE_COMPONENT_ID, terminationType.toString());
    }

    public void clearNetworkElement() {
        clearSearchField(NETWORK_ELEMENT_COMPONENT_ID);
    }

    public void proceed() {
        wizard.clickProceed();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setComboboxField(String componentId, String value) {
        Input comboboxField = wizard.getComponent(componentId, Input.ComponentType.COMBOBOX);
        comboboxField.setSingleStringValue(value);
    }

    private void clearSearchField(String componentId) {
        Input searchField = wizard.getComponent(componentId, Input.ComponentType.SEARCH_FIELD);
        searchField.clearByAction();
    }

    public enum TerminationType {
        Root, Access, Spoke, Start, End, Node, Hub, Leaf, Unknown
    }

}
