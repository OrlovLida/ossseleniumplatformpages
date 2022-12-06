package com.oss.E2E;

import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;

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
        driver.get("https://oneossga-lb.krakow.comarch:25081/#/views/management/views/hierarchy-view/Building?id=1390860&perspective=LIVE");
        waitForPageToLoad();

        waitForPageToLoad();
    }

    private SystemMessageInterface getSuccesSystemMessage() {
        return SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(10)));
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
