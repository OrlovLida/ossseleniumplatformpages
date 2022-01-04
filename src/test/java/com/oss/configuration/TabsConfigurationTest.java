package com.oss.configuration;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.portals.SaveConfigurationWizard;
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

import static com.oss.framework.components.portals.SaveConfigurationWizard.Property.GROUPS;
import static com.oss.framework.components.portals.SaveConfigurationWizard.Property.TYPE;

@Listeners({TestListener.class})
public class TabsConfigurationTest extends BaseTestCase {
    private NewInventoryViewPage newInventoryViewPage;

    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_BUILDING= "Tabs_Widget_Default";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_LOCATION= "Tabs_Widget_Location";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_GROUP= "Tabs_Widget_Group";


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
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.enableWidgetAndApply("Attachments");
        newInventoryViewPage.changeTabsOrder("Devices", 2);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_BUILDING, createField(TYPE, "Building"));

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
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.disableWidgetAndApply("Attachments");
        newInventoryViewPage.changeTabsOrder("Devices", 3);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_LOCATION, createField(TYPE,"Location"));

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
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.changeTabsOrder("Devices", 4);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_GROUP,  createField(TYPE,"Location"), createField(GROUPS, GROUP_NAME));

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
        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_BUILDING);

        //then
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(3), "Devices");
        Assert.assertTrue(newInventoryViewPage.getTabsWidget().isTabVisible("Attachments"));
    }

    @Test(priority = 5)
    @Description("Checking that configuration for tabs widget for supertype works")
    public void isConfigurationForTabsWidgetForSupertypeWorks(){
        //when
        NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Site");
        newInventoryViewPage.selectFirstRow();

        //then
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(2), "Devices");
        Assert.assertFalse(newInventoryViewPage.getTabsWidget().isTabVisible("Attachments"));
    }

    @Test(priority = 6)
    @Description("Checking that configuration for tabs widget for supertype works")
    public void isConfigurationForTabsWidgetForSTypeWorksInHierarchyView(){
        //given
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Site");
        newInventoryViewPage.selectFirstRow();
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
        newInventoryViewPage.selectFirstRow();
        //then
        Assert.assertEquals(newInventoryViewPage.getTabsWidget().getTabLabel(4), "Devices");
    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }
}
