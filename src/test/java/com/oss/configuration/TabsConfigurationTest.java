package com.oss.configuration;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
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
public class TabsConfigurationTest extends BaseTestCase {
    private NewInventoryViewPage newInventoryViewPage;

    private String GROUP_NAME = "SeleniumTests";
    private String CONFIGURATION_NAME_TABS_WIDGET_BUILDING= "Tabs_Widget_Default";
    private String CONFIGURATION_NAME_TABS_WIDGET_LOCATION= "Tabs_Widget_Location";
    private String CONFIGURATION_NAME_TABS_WIDGET_GROUP= "Tabs_Widget_Group";


    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Building");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Saving new configuration for tabs widget for type")
    public void saveNewConfigurationForTabsWidgetForType(){
        //when
        newInventoryViewPage.checkFirstCheckbox();
        newInventoryViewPage.enableWidgetAndApply("Attachments");
        newInventoryViewPage.changeTabsOrder("Devices", 2);
        newInventoryViewPage.openSaveTabsConfigurationWizard().typeName(CONFIGURATION_NAME_TABS_WIDGET_BUILDING).setForType("Building").setAsDefaultForMe().saveAsNew();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    @Description("Saving new configuration for tabs widget for supertype")
    public void saveNewConfigurationForTabsWidgetForSupertype(){
        //when
        newInventoryViewPage.checkFirstCheckbox();
        newInventoryViewPage.disableWidgetAndApply("Attachments");
        newInventoryViewPage.changeTabsOrder("Devices", 3);
        newInventoryViewPage.openSaveTabsConfigurationWizard().typeName(CONFIGURATION_NAME_TABS_WIDGET_LOCATION).setForType("Location").setAsDefaultForMe().saveAsNew();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 3)
    @Description("Saving new configuration for tabs widget for groups")
    public void saveNewConfigurationForTabsWidgetForGroup(){
        //when
        newInventoryViewPage.checkFirstCheckbox();
        newInventoryViewPage.changeTabsOrder("Devices", 4);
        newInventoryViewPage.openSaveTabsConfigurationWizard().typeName(CONFIGURATION_NAME_TABS_WIDGET_GROUP).setForType("Location").setAsDefaultForGroup(GROUP_NAME).saveAsNew();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 4)
    @Description("Checking that configuration for tabs widget for type works")
    public void isConfigurationForTabsWidgetForTypeWorks(){
        //when
        newInventoryViewPage.openChooseConfigurationForTabsWizard().chooseConfiguration(CONFIGURATION_NAME_TABS_WIDGET_BUILDING).apply();

        //then
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(3), "Devices");
        Assert.assertTrue(newInventoryViewPage.getTabsWidget().isTabVisible("Attachments"));
    }

    @Test(priority = 5)
    @Description("Checking that configuration for tabs widget for supertype works")
    public void isConfigurationForTabsWidgetForSupertypeWorks(){
        //when
        NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Site");
        newInventoryViewPage.checkFirstCheckbox();

        //then
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(2), "Devices");
        Assert.assertFalse(newInventoryViewPage.getTabsWidget().isTabVisible("Attachments"));
    }

    @Test(priority = 6)
    @Description("Checking that configuration for tabs widget for supertype works")
    public void isConfigurationForTabsWidgetForSTypeWorksInHierarchyView(){
        //given
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Site");
        newInventoryViewPage.checkFirstCheckbox();
        //when
        HierarchyViewPage hierarchyViewPage = newInventoryViewPage.goToHierarchyViewForSelectedObject();
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //then
        Assert.assertEquals(hierarchyViewPage.getBottomTabsWidget("Building").getTabLabel(2), "Devices");
    }

    @Test(priority = 7)
    @Description("")
    public void checkingConfigurationForTabsForGroup(){
        //given
        newInventoryViewPage.changeUser("webseleniumtests2","webtests");
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        //when
        newInventoryViewPage.checkFirstCheckbox();
        //then
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(4), "Devices");
    }
}
