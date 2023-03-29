/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

/**
 * @author Robert Nawrat
 */
public class RoutingWizardPage extends ConnectionWizardPage {

    private static final String PROTECTION_TYPE = "routingFormProtectionTypeComponent";
    private static final String LINE_TYPE = "routingFormLineTypeComponent";
    private static final String SEQUENCE_NUMBER = "routingFormSequenceNumberComponent";

    public RoutingWizardPage(WebDriver driver) {
        super(driver);
    }

    public void proceed() {
        getWizard().clickProceed();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void accept() {
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setProtectionType(String value) {
        getWizard().setComponentValue(PROTECTION_TYPE, value);
    }

    @Step("Set Line Type = {value}")
    public void setLineType(String value) {
        getWizard().setComponentValue(LINE_TYPE, value);
    }

    @Step("Set Sequence Number = {value}")
    public void setSequenceNumber(String value) {
        getWizard().setComponentValue(SEQUENCE_NUMBER, value);
    }

    public void selectConnection(String labelPath) {
        TreeComponent treeComponent = getWizard().getTreeComponent();
        treeComponent.getNodeByLabelsPath(labelPath).toggleNode();
        getWizard().waitForWizardToLoad();
    }
}