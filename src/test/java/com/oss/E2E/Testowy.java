package com.oss.E2E;

import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.issue.BaseDashboardPage;

public class Testowy extends BaseTestCase {

    private static final String PROBLEMS_DASHBOARD = "_ProblemManagement";

    @Test(priority = 1)
    public void testowy1() {
        waitForPageToLoad();
    }

    @Test(priority = 2)
    public void testowy2() {
        BaseDashboardPage baseDashboardPage = new BaseDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, PROBLEMS_DASHBOARD);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
