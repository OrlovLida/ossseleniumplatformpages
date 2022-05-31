package com.oss.E2E;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;

public class Testowy extends BaseTestCase {

    private static final String BUSINESS_PROCESS_MANAGEMENT = "Business Process Management";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_INSTANCES = "Process Instances";

    @Test(priority = 1)
    public void testowy1() {
        waitForPageToLoad();
    }

    @Test(priority = 2)
    public void testowy2() {
        homePage.chooseFromLeftSideMenu(PROCESS_INSTANCES, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        waitForPageToLoad();
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processWizardPage.createSimpleNRP();
        SystemMessageInterface systemMessage = getSuccesSystemMessage();
        systemMessage.close();
        System.out.println("SIZE= " + systemMessage.getMessages());
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    public void testowy4() {
        waitForPageToLoad();
    }

    private SystemMessageInterface getSuccesSystemMessage() {
        return SystemMessageContainer.create(driver, new WebDriverWait(driver, 10));
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
