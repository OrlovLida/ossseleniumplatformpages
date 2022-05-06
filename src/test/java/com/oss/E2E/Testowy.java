package com.oss.E2E;

import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.issue.problem.ProblemDashboardPage;

public class Testowy extends BaseTestCase {

    @Test(priority = 1)
    public void testowy1() {
        waitForPageToLoad();
    }

    @Test(priority = 2)
    public void testowy2() {
        ProblemDashboardPage problemDashboardPage = new ProblemDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
