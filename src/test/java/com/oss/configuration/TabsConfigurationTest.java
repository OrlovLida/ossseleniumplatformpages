package com.oss.configuration;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.GROUPS;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.TYPE;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.utils.TestListener;

@Listeners({TestListener.class})
public class TabsConfigurationTest extends BaseTestCase {
    private final static String GROUP_NAME = "SeleniumTests";
    private static final String ATTRIBUTE_ID_TYPE = "type";
    private final static String CONFIGURATION_NAME_TABS_WIDGET = "Tabs_Widget_TestActor_Configuration_" + LocalDate.now();
    private final static String CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER =
            "Tabs_Widget_Supertype_Default_For_User_" + LocalDate.now();
    private final static String CONFIGURATION_NAME_TABS_WIDGET_GROUP = "Tabs_Widget_Group_" + LocalDate.now();
    private static final String DEFAULT = "DEFAULT";
    private static final String MODEL = "MODEL";

    private NewInventoryViewPage newInventoryViewPage;
    private TableWidget tableWidget;

    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private static final String TEST_PERSON = "TestPerson";
    private static final String TEST_DIRECTOR = "TestDirector";
    private static final String TEST_ACTOR = "TestActor";
    private static final String INTERESTS = "Interests";
    private static final String MATERIALS = "Materials";
    private static final String TABLE_TYPE = "Table";
    private static final String MOVIES = "Movies";
    private static final String ME = "Me";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";

    @BeforeClass
    public void goToInventoryView() {
        deleteOldConfiguration();
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void deleteOldConfiguration(){
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR);
        List<String> pageConfigurationsNameDirector = newInventoryViewPage.getPageConfigurationsName().stream().filter(name-> !name.equals(MODEL) && !name.equals(DEFAULT)).collect(Collectors.toList());
        newInventoryViewPage.deleteConfigurations(pageConfigurationsNameDirector);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        deleteOldTabsConfigurations(TEST_DIRECTOR);
        deleteOldTabsConfigurations(TEST_ACTOR);
    }

    private void deleteOldTabsConfigurations(String objectType) {
        selectObjectOfSuperType(objectType);
        List<String> tabsConfigurationsName = newInventoryViewPage.getTabsConfigurationsName().stream().filter(name -> !name.equals(MODEL) && !name.equals(DEFAULT)).collect(Collectors.toList());
        newInventoryViewPage.deleteConfigurations(tabsConfigurationsName);
        newInventoryViewPage.getMainTable().unselectAllRows();
    }

    @Test(priority = 1)
    public void saveNewConfigurationForTabsWidgetForSupertype() {
        selectObjectOfSuperType(TEST_ACTOR);
        newInventoryViewPage.enableWidget(TABLE_TYPE, MATERIALS);
        newInventoryViewPage.enableWidget(TABLE_TYPE, INTERESTS);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET, createField(TYPE, TEST_ACTOR));

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();

        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
    }

    @Test(priority = 2)
    public void setDefaultConfigurationForTabsWidget() {
        newInventoryViewPage.applyConfigurationForTabs(DEFAULT_CONFIGURATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.isTabVisible(MATERIALS));
    }

    @Test(priority = 3)
    public void chooseConfigurationForTabsWidget() {
        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(MATERIALS));
    }

    @Test(priority = 4)
    public void updateConfigurationForTabsWidget() {
        newInventoryViewPage.disableWidget(INTERESTS);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.updateConfigurationForTabs();
        driver.navigate().refresh();
        selectObjectOfSuperType(TEST_ACTOR);
        newInventoryViewPage.applyConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);
        Assert.assertFalse(newInventoryViewPage.isTabVisible(INTERESTS));
    }

    @Test(priority = 5)
    public void downloadConfigurationOfTabsWidget() {
        // when
        newInventoryViewPage.downloadConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
    }

    @Test(priority = 6)
    public void removeConfigurationOfTabsWidget() {
        // when
        newInventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET);
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
    }

    @Test(priority = 7)
    public void saveDefaultConfigurationForTabsWidgetForUserForSuperType() {
        // when
        selectObjectOfSuperType(TEST_ACTOR);
        newInventoryViewPage.enableWidget(TABLE_TYPE, MOVIES);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER, createField(DEFAULT_VIEW_FOR, ME),
                createField(TYPE, TEST_ACTOR));

        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        selectObjectOfSuperType(TEST_ACTOR);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(MOVIES));

        newInventoryViewPage.changeUser(USER2, PASSWORD_2);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        selectObjectOfSuperType(TEST_ACTOR);
        Assert.assertFalse(newInventoryViewPage.isTabVisible(MOVIES));
    }

    @Test(priority = 8)
    public void saveDefaultConfigurationForTabsWidgetForGroupForType() {
        newInventoryViewPage.changeUser(USER1, PASSWORD_1);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        selectObjectOfSuperType(TEST_ACTOR);
        // when
        newInventoryViewPage.enableWidget(TABLE_TYPE, INTERESTS);
        newInventoryViewPage.saveConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_GROUP, createField(GROUPS, GROUP_NAME),
                createField(TYPE, TEST_PERSON));

        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));

        driver.navigate().refresh();
        newInventoryViewPage.chooseGroupContext(GROUP_NAME);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        selectObjectOfSuperType(TEST_DIRECTOR);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(INTERESTS));
    }

    @Test(priority = 9)
    public void groupAndTypeInheritanceDefaultConfigurationOfTabsWidget() {
        // when
        newInventoryViewPage.changeUser(USER2, PASSWORD_2);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        newInventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        selectObjectOfSuperType(TEST_DIRECTOR);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(INTERESTS));

        newInventoryViewPage.changeUser(USER1, PASSWORD_1);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        selectObjectOfSuperType(TEST_ACTOR);
        Assert.assertTrue(newInventoryViewPage.isTabVisible(MOVIES));
        Assert.assertFalse(newInventoryViewPage.isTabVisible(INTERESTS));
    }

    @AfterClass
    public void deleteConfiguration() {
        NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        selectObjectOfSuperType(TEST_ACTOR);
        newInventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_GROUP);
        newInventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_TABS_WIDGET_DEFAULT_FOR_USER);
    }

    private void selectObjectOfSuperType(String typeValue) {
        newInventoryViewPage.searchByAttributeValue(ATTRIBUTE_ID_TYPE, typeValue, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.clearFilters();
    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

}
