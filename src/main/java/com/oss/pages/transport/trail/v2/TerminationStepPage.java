/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;

public class TerminationStepPage extends BasePage {
    
    private static final String TREE_CONTROLLER_WIDGET_CLASS = "tree-controller";
    
    private static final String NETWORK_ELEMENT_COMPONENT_ID = "terminationFormDeviceComponent";
    private static final String CARD_COMPONENT_ID = "terminationFormCardComponent";
    private static final String NONEXISTENT_CARD_VALUE = "No Card/Component";
    private static final String PORT_COMPONENT_ID = "terminationFormPortComponent";
    private static final String TERMINATION_POINT_COMPONENT_ID = "terminationFormPointComponent";
    
    private final Wizard wizard;
    
    public TerminationStepPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }
    
    @Step("Choose termination in tree - {terminationType}")
    public void chooseTerminationType(TerminationType terminationType) {
        TreeWidget treeWidget = TreeWidget.createByClass(driver, TREE_CONTROLLER_WIDGET_CLASS, wait);
        treeWidget.setValueOnCheckboxByNodeLabel(terminationType.getLabel(), true);
        waitForPageToLoad();
    }
    
    @Step("Clear network element")
    public void clearNetworkElement() {
        clearSearchField(NETWORK_ELEMENT_COMPONENT_ID);
        waitForPageToLoad();
    }
    
    private void clearSearchField(String componentId) {
        wizard.setComponentValue(componentId, " ", Input.ComponentType.SEARCH_FIELD);
    }
    
    @Step("Set nonexistent card")
    public void setNonexistentCard() {
        setSearchFieldContains(CARD_COMPONENT_ID, NONEXISTENT_CARD_VALUE);
        waitForPageToLoad();
    }
    
    @Step("Set port - {portName}")
    public void setPort(String portName) {
        setSearchFieldContains(PORT_COMPONENT_ID, portName);
        waitForPageToLoad();
    }
    
    @Step("Set termination point - {tpName}")
    public void setTerminationPoint(String tpName) {
        setSearchFieldContains(TERMINATION_POINT_COMPONENT_ID, tpName);
        waitForPageToLoad();
    }
    
    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
    
    private void setSearchFieldContains(String componentId, String value) {
        Input searchField = wizard.getComponent(componentId, Input.ComponentType.SEARCH_FIELD);
        searchField.setSingleStringValueContains(value);
    }
    
    @Step("Click Accept button")
    public void accept() {
        wizard.clickAccept();
    }
    
    protected void setCheckbox(String componentId, Boolean value) {
        wizard.setComponentValue(componentId, value.toString(), Input.ComponentType.CHECKBOX);
    }
    
    public enum TerminationType {
        Start("1 Start"),
        End("2 End");
        
        private final String label;
        
        private TerminationType(String value) {
            label = value;
        }
        
        private String getLabel() {
            return label;
        }
    }
}
