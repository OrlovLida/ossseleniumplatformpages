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

@Listeners({TestListener.class})
public class TabsConfigurationTest extends BaseTestCase {
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABS_WIDGET = "Tabs_Widget_Configuration";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER = "Tabs_Widget_Supertype_Default_For_User";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_GROUP = "Tabs_Widget_Group";

    private NewInventoryViewPage newInventoryViewPage;

    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private static final String TEST_PERSON = "TestPerson";
    ;
    private static final String INTERESTS = "Interests";
    private static final String OTHER_TYPE = "Other";
    private static final String PLANNING_INFO = "Planning Info";
    private static final String TABLE_TYPE = "Table";
    private static final String MOVIES = "Movies";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForTabsWidgetForType() {
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.enableWidget(OTHER_TYPE, PLANNING_INFO);
        newInventoryViewPage.enableWidget(TABLE_TYPE, MOVIES);
        newInventoryViewPage.enableWidget(TABLE_TYPE, INTERESTS);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

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

        Assert.assertTrue(!newInventoryViewPage.isTabVisible(MOVIES));

    }

    @Test(priority = 3)
    public void chooseConfigurationForTabsWidget() {
        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

        Assert.assertTrue(newInventoryViewPage.isTabVisible(PLANNING_INFO));

    }

    @Test(priority = 4)
    public void updateConfigurationForTabsWidget() {

        newInventoryViewPage.disableWidget(INTERESTS);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        newInventoryViewPage.updateConfigurationForTabs();

        driver.navigate().refresh();
        newInventoryViewPage.selectFirstRow();

        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);

        Assert.assertTrue(!newInventoryViewPage.isTabVisible(INTERESTS));

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
