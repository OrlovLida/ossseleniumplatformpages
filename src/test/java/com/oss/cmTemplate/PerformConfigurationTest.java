package com.oss.cmTemplate;

import com.oss.BaseTestCase;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.templateCM.ChangeConfigurationPage;
import com.oss.pages.templateCM.SetParametersWizardPage;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
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
        sideMenu.callActionByLabel("Change Configuration", "Views", "Template CM");
        changeConfigurationPage = new ChangeConfigurationPage(driver);
    }

    @Test(priority = 1)
    @Description("Choose object type, specific device and template")
    public void chooseDeviceAndTemplate() {
        DelayUtils.sleep(1000);
        changeConfigurationPage.selectObjectType("Router");
        DelayUtils.sleep(1000);
        changeConfigurationPage.selectObject(DEVICE_NAME);
        DelayUtils.sleep(1000);
        changeConfigurationPage.selectTemplate(TEMPLATE_NAME);
        DelayUtils.sleep(1000);
    }

    @Test(priority = 2)
    @Description("Go to Set Parameters wizard, check parameters")
    public void testParameters() {
        changeConfigurationPage.clickSetParameters();
        setParametersWizardPage = new SetParametersWizardPage(driver);

        String name = setParametersWizardPage.getParameter("$name[NEW_INVENTORY]");
        Assertions.assertThat(name).isEqualTo(DEVICE_NAME);

        setParametersWizardPage.setParameter("$InterfaceName[USER]", "GE 0");
    }

    @Test(priority = 3)
    @Description("Deploy template")
    public void runTemplate() {
        DelayUtils.sleep(1000);
        setParametersWizardPage.clickFillParameters();
        DelayUtils.sleep(1000);
        changeConfigurationPage.deployImmediately();
        DelayUtils.sleep(1000);
        Assertions.assertThat(Notifications.create(driver, webDriverWait).waitAndGetFinishedNotificationText().equals("Scripts processing for template " + TEMPLATE_NAME + ": FINISHED")).isTrue();
    }
}
