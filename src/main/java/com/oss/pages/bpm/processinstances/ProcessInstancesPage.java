/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.processinstances;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.platform.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Gabriela Kasza
 * @deprecated ProcessInstancesPage class is replaced by {@link ProcessOverviewPage} class
 */
@Deprecated
public class ProcessInstancesPage extends ProcessOverviewPage {

    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String NETWORK_PLANNING = "Network Planning";
    private static final String PROCESS_OVERVIEW = "Process Overview";

    /**
     * @deprecated ProcessInstancesPage class is replaced by {@link ProcessOverviewPage} class
     */
    @Deprecated
    public ProcessInstancesPage(WebDriver driver) {
        super(driver);
    }

    /**
     * @deprecated ProcessInstancesPage class is replaced by {@link ProcessOverviewPage} class.
     * Use {@link ProcessOverviewPage#goToProcessOverviewPage(WebDriver, String)} method
     */
    @Deprecated
    public static ProcessInstancesPage goToProcessInstancesPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/processes" +
                "?perspective=LIVE", basicURL));

        return new ProcessInstancesPage(driver);
    }

    /**
     * @deprecated ProcessInstancesPage class is replaced by {@link ProcessOverviewPage} class.
     * Use {@link ProcessOverviewPage#goToProcessOverviewPage(WebDriver, WebDriverWait)}  method
     */
    @Deprecated
    public static ProcessInstancesPage goToProcessInstancesPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PROCESS_OVERVIEW, BPM_AND_PLANNING, NETWORK_PLANNING);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessInstancesPage(driver);
    }


}
