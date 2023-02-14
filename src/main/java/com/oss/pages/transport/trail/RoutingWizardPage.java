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
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

/**
 * @author Robert Nawrat
 */
public class RoutingWizardPage extends ConnectionWizardPage {

    private static final String ID = "RoutingPopupViewId_prompt-card";
    private static final String PROTECTION_TYPE = "routingFormProtectionTypeComponent";

    //    TODO: Poprawić po rozwiązaniu OSSTRAIL-7974
    private static final String LINE_TYPE = "__lineType";
    private static final String SEQUENCE_NUMBER = "__sequenceNumber";

    public RoutingWizardPage(WebDriver driver) {
        super(driver);
        getWizard();
    }

    @Override
    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, ID);
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
