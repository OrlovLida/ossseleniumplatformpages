package com.oss.E2E;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;

public class Testowy extends BaseTestCase {

    @Test(priority = 1)
    public void testowy1() {
        waitForPageToLoad();
    }

    @Test(priority = 2)
    public void testowy2() {
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
