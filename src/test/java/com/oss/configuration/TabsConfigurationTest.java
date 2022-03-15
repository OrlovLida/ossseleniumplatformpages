package com.oss.configuration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.utils.TestListener;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.TYPE;

@Listeners({TestListener.class})
public class TabsConfigurationTest extends BaseTestCase {
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABS_WIDGET = "Tabs_Widget_Configuration";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER = "Tabs_Widget_Supertype_Default_For_User";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_GROUP = "Tabs_Widget_Group";

    private NewInventoryViewPage newInventoryViewPage;

    private final static String DEFAULT_CONFIGURATION = "DEFAULT";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForTabsWidgetForType() {
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.enableWidget("Other", "Connections");
        newInventoryViewPage.changeTabsOrder("Devices", 2);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET, createField(TYPE, "Location"));

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    public void setDefaultConfigurationForTabsWidget() {

        newInventoryViewPage.applyConfigurationForTabs(DEFAULT_CONFIGURATION);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();

        Assert.assertTrue(!newInventoryViewPage.isTabVisible("Connections"));

    }

    @Test(priority = 3)
    public void chooseConfigurationForTabsWidget() {
        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

        Assert.assertTrue(newInventoryViewPage.isTabVisible("Connections"));
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(3), "Devices");

    }

    @Test(priority = 4)
    public void updateConfigurationForTabsWidget() {

        newInventoryViewPage.disableWidget("Interests");

        newInventoryViewPage.updateConfigurationForTabs();

        driver.navigate().refresh();
        newInventoryViewPage.selectFirstRow();

        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

        Assert.assertTrue(!newInventoryViewPage.isTabVisible("Interests"));

    }

    @Test(priority = 5)
    public void downloadConfigurationOfTabsWidget() {
        //when
        newInventoryViewPage.selectFirstRow();

        newInventoryViewPage.downloadConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 6)
    public void removeConfigurationOfTabsWidget() {
        //when
        newInventoryViewPage.selectFirstRow();

        newInventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

}
