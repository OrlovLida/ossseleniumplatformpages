package com.oss.configuration;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.ConnectionsViewPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners({TestListener.class})
public class PageConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private String GROUP_NAME = "SeleniumTests";
    private String CONFIGURATION_NAME_IV = "IV_User";
    private String CONFIGURATION_NAME_IV_GROUP = "IV_Group";
    private String CONFIGURATION_NAME_HV = "HV_User";
    private String CONFIGURATION_NAME_CV = "CV_User";


    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Saving new configuration for Inventory View page for user")
    public void saveConfigurationForIVPage() {
        //when
        newInventoryViewPage.changeLayoutToVertical();
        newInventoryViewPage.openSavePageConfigurationWizard().typeName(CONFIGURATION_NAME_IV).setAsDefaultForMe().save();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue(driver.getCurrentUrl().contains("Location"));
    }

    @Test(priority = 2)
    @Description("Saving new configuration for for Inventory View page for group")
    public void saveConfigurationForIVPageForGroup() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Building");

        //when
        newInventoryViewPage.changeLayoutToVertical();
        newInventoryViewPage.openSavePageConfigurationWizard().typeName(CONFIGURATION_NAME_IV_GROUP).setAsDefaultForGroup(GROUP_NAME).save();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue(driver.getCurrentUrl().contains("Building"));
    }

    @Test(priority = 3)
    @Description("Open saved configuration")
    public void isConfigurationSavedProperly() {
        //when
        newInventoryViewPage.openChooseConfigurationForPageWizard().chooseConfiguration(CONFIGURATION_NAME_IV).apply();

        //then
        Assert.assertEquals(newInventoryViewPage.howManyRows(), 2);
        Assert.assertTrue(driver.getCurrentUrl().contains("Location"));
    }

    @Test(priority = 4)
    @Description("Check configuration for second user")
    public void isConfigurationWorkingProperlyForSecondUser() {
        //given
        newInventoryViewPage.changeUser("webseleniumtests2", "webtests");

        //when
        newInventoryViewPage.openChooseConfigurationForPageWizard().chooseConfiguration(CONFIGURATION_NAME_IV_GROUP).apply();

        //then
        Assert.assertEquals(newInventoryViewPage.howManyRows(), 2);
        Assert.assertTrue(driver.getCurrentUrl().contains("Building"));
    }

    @Test(priority = 5)
    @Description("Saving new configuration for Hierarchy View page for user")
    public void saveConfigurationForHVPage() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        String idOfFirstObject = newInventoryViewPage.getIdOfFirstObject();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, "Location", idOfFirstObject);

        //when
        hierarchyViewPage.openSavePageConfigurationWizard().typeName(CONFIGURATION_NAME_HV).setAsDefaultForMe().save();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 6)
    @Description("Saving new configuration for Connections View page for user")
    public void saveConfigurationForCVPage() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        String idOfFirstObject = newInventoryViewPage.getIdOfFirstObject();
        ConnectionsViewPage connectionsViewPage = ConnectionsViewPage.goToConnectionsViewPage(driver, BASIC_URL);

        //when
        connectionsViewPage.openSavePageConfigurationWizard().typeName(CONFIGURATION_NAME_CV).setAsDefaultForMe().save();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

}
