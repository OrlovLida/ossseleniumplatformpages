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
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.utils.TestListener;

@Listeners({TestListener.class})
public class InventoryViewConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;

    private static final String TEST_PERSON_TYPE = "TestPerson";
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_IV = "IV";
    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG = "Table_Widget_Used_In_View_Configuration_Selenium_Test";
    private final static String CONFIGURATION_TEST_ACTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG = "Tabs_Widget_Actor_Configuration_Used_In_View_Configuration_Selenium_Tests";
    private final static String CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG = "Tabs_Widget_Director_Configuration_Used_In_View_Configuration_Selenium_Tests";
    private final static String TYPE_LABEL = "Type";
    private final static String GENDER_LABEL = "Gender";
    private static final String ATTRIBUTE_ID_TYPE = "type";
    private static final String TYPE_VALUE_TEST_ACTOR = "TestActor";
    private static final String TYPE_VALUE_TEST_DIRECTOR = "TestDirector";
    private static final String INTERESTS = "Interests";
    private static final String OTHER_TYPE = "Other";
    private static final String PLANNING_INFO = "Planning Info";
    private static final String TABLE_TYPE = "Table";
    private static final String MOVIES = "Movies";
    private static final String ME = "Me";
    private static final String HORIZONTAL_60_40_BUTTON_ID = "TWO_ROWS_60_40";
    private static final String HORIZONTAL_40_60_BUTTON_ID = "TWO_ROWS_40_60";
    private static final String HORIZONTAL_50_50_BUTTON_ID = "TWO_ROWS_50_50";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";

    @BeforeClass
    public void goToInventoryView() {
        //given
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableWidget = inventoryViewPage.getMainTable();
    }

    @Test(priority = 1)
    public void saveNewConfigurationForIVPage() {
        //save config for table
        inventoryViewPage.disableColumnAndApply(GENDER_LABEL);
        inventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG);

        saveConfigurationForTabsForSuperType(TYPE_VALUE_TEST_ACTOR, TABLE_TYPE, INTERESTS, CONFIGURATION_TEST_ACTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        saveConfigurationForTabsForSuperType(TYPE_VALUE_TEST_DIRECTOR, OTHER_TYPE, PLANNING_INFO, CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);

        inventoryViewPage.setHorizontalLayout(HORIZONTAL_60_40_BUTTON_ID);

        inventoryViewPage.saveNewPageConfiguration(CONFIGURATION_NAME_IV);

        assertSuccessMessage();

    }

    @Test(priority = 2)
    public void setDefaultConfigurationForIVPage() {

        inventoryViewPage.applyConfigurationForPage(DEFAULT_CONFIGURATION);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).contains(GENDER_LABEL);

        assertTabsNotVisible(TYPE_VALUE_TEST_ACTOR, INTERESTS);
        assertTabsNotVisible(TYPE_VALUE_TEST_DIRECTOR, PLANNING_INFO);

        Assert.assertTrue(inventoryViewPage.isHorizontal(HORIZONTAL_50_50_BUTTON_ID));

    }

    @Test(priority = 3)
    public void chooseConfigurationForIVPage() {
        inventoryViewPage.applyConfigurationForPage(CONFIGURATION_NAME_IV);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).isNotEmpty().doesNotContain(GENDER_LABEL);

        assertTabsVisible(TYPE_VALUE_TEST_ACTOR, INTERESTS);
        assertTabsVisible(TYPE_VALUE_TEST_DIRECTOR, PLANNING_INFO);

        Assert.assertTrue(inventoryViewPage.isHorizontal(HORIZONTAL_60_40_BUTTON_ID));

    }

    @Test(priority = 4)
    public void checkConfigurationAfterRefreshPage() {

        inventoryViewPage.disableColumnAndApply(TYPE_LABEL);
        driver.navigate().refresh();

        assertTabsVisible(TYPE_VALUE_TEST_ACTOR, INTERESTS);
        Assert.assertTrue(inventoryViewPage.isHorizontal(HORIZONTAL_60_40_BUTTON_ID));
    }

    @Test(priority = 5)
    public void updateConfigurationForPage() {

        inventoryViewPage.disableColumnAndApply(TYPE_LABEL);
        inventoryViewPage.updateConfigurationForMainTable();
        inventoryViewPage.updatePageConfiguration();

        driver.navigate().refresh();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).isNotEmpty().doesNotContain(TYPE_LABEL);

    }

    @Test(priority = 6)
    public void downloadConfigurationOfPage() {

        inventoryViewPage.downloadConfigurationForPage(CONFIGURATION_NAME_IV);
        assertSuccessMessage();
    }

    @Test(priority = 7)
    public void tryRemoveConfigurationUsedInConfigurationOfPage() {

        inventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG);
        assertDangerMessage();

        selectObjectOfSuperType(TYPE_VALUE_TEST_ACTOR);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        assertDangerMessage();
    }

    @Test(priority = 7)
    public void removePageConfiguration() {
        inventoryViewPage.deletePageConfiguration(CONFIGURATION_NAME_IV);

        assertSuccessMessage();

        inventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG);
        assertSuccessMessage();

        selectObjectOfSuperType(TYPE_VALUE_TEST_ACTOR);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_ACTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        assertSuccessMessage();
        tableWidget.unselectAllRows();

        selectObjectOfSuperType(TYPE_VALUE_TEST_DIRECTOR);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        assertSuccessMessage();
        tableWidget.unselectAllRows();
    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

    private void saveConfigurationForTabsForSuperType(String typeValue, String widgetType, String widgetLabel, String configurationName) {
        selectObjectOfSuperType(typeValue);
        inventoryViewPage.enableWidget(widgetType, widgetLabel);
        inventoryViewPage.saveConfigurationForTabs(configurationName);
        tableWidget.unselectAllRows();

    }

    private void selectObjectOfSuperType(String typeValue) {
        inventoryViewPage.searchByAttributeValue(ATTRIBUTE_ID_TYPE, typeValue, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewPage.selectFirstRow();
        inventoryViewPage.clearFilters();
    }

    private void assertTabsNotVisible(String typeValue, String widgetLabel) {
        selectObjectOfSuperType(typeValue);
        Assert.assertFalse(inventoryViewPage.isTabVisible(widgetLabel));
        tableWidget.unselectAllRows();
    }

    private void assertTabsVisible(String typeValue, String widgetLabel) {
        selectObjectOfSuperType(typeValue);
        Assert.assertTrue(inventoryViewPage.isTabVisible(widgetLabel));
        tableWidget.unselectAllRows();
    }

    private void assertSuccessMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
    }

    private void assertDangerMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(2);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.DANGER);
        systemMessage.close();
    }
}
