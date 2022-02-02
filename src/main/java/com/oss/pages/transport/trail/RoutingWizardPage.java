/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Robert Nawrat
 */
public class RoutingWizardPage extends BasePage {

    private final Wizard wizard;

    public RoutingWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void proceed() {
        wizard.clickProceed();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void accept() {
        wizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
