/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class ProcessInstancesPage extends BasePage {

    public static ProcessInstancesPage goToProcessInstancesPage(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/bpm/processes" +
                "?perspective=LIVE", basicURL));

        return new ProcessInstancesPage(driver);
    }
    protected ProcessInstancesPage(WebDriver driver) {
        super(driver);
    }
}
