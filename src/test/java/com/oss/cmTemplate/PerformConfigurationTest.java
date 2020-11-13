package com.oss.cmTemplate;

import com.oss.BaseTestCase;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.templateCM.ChangeConfigurationPage;
import com.oss.pages.templateCM.SetParametersWizardPage;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PerformConfigurationTest extends BaseTestCase {

    private ChangeConfigurationPage changeConfigurationPage;
    private SetParametersWizardPage setParametersWizardPage;
    private static String DEVICE_NAME = "SeleniumTemplateTestDevice";
    private static String TEMPLATE_NAME = "E2E_Test_Loopback_v2";

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
        String name = setParametersWizardPage.getParameter("$name[NEW_INVENTORY]");
        Assertions.assertThat(name).isEqualTo(DEVICE_NAME);
        setParametersWizardPage.setParameter("$InterfaceName[USER]", "GE 0");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setParametersWizardPage.setParameter("$Password[SYSTEM]", "cisco");
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
        Notifications.create(driver, webDriverWait).waitForSpecificNotification(TEMPLATE_NAME, "FINISHED");
    }
}
