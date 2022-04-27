/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.logicalfunction;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

/**
 * @author Marcin Kozioł
 */
public class LogicalFunctionWizardPreStep extends BasePage {

    private static final String WIZARD_ID = "wizard-widget-id";
    private static final String RESOURCE_SPECIFICATION_COMPONENT_ID = "step0-search-comp-id";
    private final Wizard wizard;

    private LogicalFunctionWizardPreStep(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static LogicalFunctionWizardPreStep create(WebDriver driver, WebDriverWait wait) {
        return new LogicalFunctionWizardPreStep(driver, wait);
    }

    public void searchResourceSpecification(String name){
        wizard.setComponentValue(RESOURCE_SPECIFICATION_COMPONENT_ID, name, SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickAccept() {
        wizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public Wizard getWizard(){
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
