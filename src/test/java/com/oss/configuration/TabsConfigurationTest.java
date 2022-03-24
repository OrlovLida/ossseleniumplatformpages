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

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.GROUPS;

@Listeners({TestListener.class})
public class TabsConfigurationTest extends BaseTestCase {
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABS_WIDGET = "Tabs_Widget_Configuration";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER = "Tabs_Widget_Supertype_Default_For_User";
    private final static String CONFIGURATION_NAME_TABS_WIDGET_GROUP = "Tabs_Widget_Group";

    private NewInventoryViewPage newInventoryViewPage;

    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private static final String TEST_PERSON = "TestPerson";
    private static final String TEST_ACTOR = "TestActor";
    private static final String INTERESTS = "Interests";
    private static final String OTHER_TYPE = "Other";
    private static final String PLANNING_INFO = "Planning Info";
    private static final String TABLE_TYPE = "Table";
    private static final String MOVIES = "Movies";
    private static final String ME = "Me";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";

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

        Assert.assertFalse(newInventoryViewPage.isTabVisible(MOVIES));

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

        Assert.assertFalse(newInventoryViewPage.isTabVisible(INTERESTS));

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

    @Test(priority = 7)
    public void saveDefaultConfigurationForTabsWidgetForUser() {
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.enableWidget(TABLE_TYPE, MOVIES);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER, createField(DEFAULT_VIEW_FOR, ME));

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        driver.navigate().refresh();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();

        Assert.assertTrue(newInventoryViewPage.isTabVisible(MOVIES));

        newInventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER);
    }

    @Test(priority = 8)
    public void saveDefaultConfigurationForTabsWidgetForGroup() {
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.enableWidget(TABLE_TYPE, INTERESTS);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_GROUP, createField(GROUPS, GROUP_NAME));

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        driver.navigate().refresh();
        newInventoryViewPage.chooseGroupContext(GROUP_NAME);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();

        Assert.assertTrue(newInventoryViewPage.isTabVisible(INTERESTS));
    }

    @Test(priority = 9)
    public void groupAndTypeInheritanceDefaultConfigurationOfTabsWidget() {
        //when
        newInventoryViewPage.changeUser(USER2, PASSWORD_2);
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR);

        newInventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        newInventoryViewPage.selectFirstRow();

        Assert.assertTrue(newInventoryViewPage.isTabVisible(INTERESTS));

        newInventoryViewPage.changeUser(USER1, PASSWORD_1);
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        newInventoryViewPage.selectFirstRow();

        newInventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_GROUP);

    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

}
