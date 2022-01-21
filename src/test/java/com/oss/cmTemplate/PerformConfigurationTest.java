package com.oss.cmTemplate;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.LogManagerPage;
import com.oss.pages.template_cm.ChangeConfigurationPage;
import com.oss.pages.template_cm.SetParametersWizardPage;

import io.qameta.allure.Description;

public class PerformConfigurationTest extends BaseTestCase {

    private static final String DEVICE_NAME = "SeleniumTemplateTestDevice";
    private static final String TEMPLATE_NAME = "E2E_Test_Loopback_v2";
    private static final String TEMPLATE_EXECUTION_NOTIFICATION = "Scripts execution for template E2E_Test_Loopback_v2";
    private static ChangeConfigurationPage changeConfigurationPage;
    private static SetParametersWizardPage setParametersWizardPage;

    @BeforeClass
    public void goToPerformConfigurationChange() {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sideMenu.callActionByLabel("Change Configuration", "Views", "Template CM");
        changeConfigurationPage = new ChangeConfigurationPage(driver);
    }

    @Test(priority = 1)
    @Description("Choose object type, specific device and template")
    public void chooseDeviceAndTemplate() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Notifications.create(driver, webDriverWait).clearAllNotification();
        changeConfigurationPage.selectObjectType("Router");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.selectObject(DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.selectTemplate(TEMPLATE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Go to Set Parameters wizard, check parameters")
    public void testParameters() {
        changeConfigurationPage.clickSetParameters();
        setParametersWizardPage = new SetParametersWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String name = setParametersWizardPage.getName();
        Assertions.assertThat(name).isEqualTo(DEVICE_NAME);
        setParametersWizardPage.setInterfaceName("GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.setPassword("oss");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Deploy template")
    public void runTemplate() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.clickFillParameters();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeConfigurationPage.deployImmediately();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Notifications.create(driver, new WebDriverWait(driver, 180)).openDetails(TEMPLATE_EXECUTION_NOTIFICATION, "FINISHED");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LogManagerPage logManagerPage = new LogManagerPage(driver);
        Assertions.assertThat(logManagerPage.getStatus()).isEqualTo("UPLOAD_SUCCESS");
    }
}
