package com.oss.configuration;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.GROUPS;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.TYPE;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.After;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
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
    
    private static final String TEST_PERSON_TYPE = "TestPerson";
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_IV = "IV_" + LocalDate.now();
    private final static String CONFIGURATION_NAME_IV_DEFAULT_FOR_USER = "IV_Default_For_User";
    private final static String CONFIGURATION_NAME_IV_TYPE_DEFAULT_FOR_GROUP = "IV_Default_For_GROUP_TYPE";
    private static final String DEFAULT = "DEFAULT";
    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG =
            "Table_Widget_Used_In_View_Configuration_Selenium_Test_" + LocalDate.now();
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_DEFAULT_VIEW_CONFIG =
            "Table_Configurationt_Used_In__Default_View_Configuration_" + LocalDate.now();
    private final static String CONFIGURATION_TEST_PERSON_TABS_WIDGET_USED_IN_DEFAULT_VIEW_CONFIG =
            "Tabs_Widget_Person_Configuration_Used_In_Default_View_Configuration_" + LocalDate.now();
    private final static String CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_DEFAULT_GROUP_CONFIG =
            "Tabs_Widget_Director_Configuration_Used_In_Default_Group_Configuration_" + LocalDate.now();
    private final static String CONFIGURATION_TEST_ACTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG =
            "Tabs_Widget_Actor_Configuration_Used_In_View_Configuration_Selenium_Tests_" + LocalDate.now();
    private final static String CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG =
            "Tabs_Widget_Director_Configuration_Used_In_View_Configuration_Selenium_Tests_" + LocalDate.now();
    private final static String TYPE_LABEL = "Type";
    private static final String LIFECYCLE_STATE_LABEL = "Lifecycle State";
    private final static String GENDER_LABEL = "Gender";
    private static final String ATTRIBUTE_ID_TYPE = "type";
    private static final String TEST_ACTOR_TYPE = "TestActor";
    private static final String TEST_DIRECTOR_TYPE = "TestDirector";
    private static final String INTERESTS = "Interests";
    private static final String MATERIALS = "Materials";
    private static final String TABLE_TYPE = "Table";
    private static final String MOVIES = "Movies";
    private static final String ME = "Me";
    private static final String HORIZONTAL_60_40_BUTTON_ID = "TWO_ROWS_60_40";
    private static final String HORIZONTAL_50_50_BUTTON_ID = "TWO_ROWS_50_50";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";
    private static final String MODEL = "MODEL";

    private NewInventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;
    
    @BeforeClass
    public void goToInventoryView() {
        deleteOldConfiguration();
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableWidget = inventoryViewPage.getMainTable();
    }

    private void deleteOldConfiguration(){
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        deleteOldConfigurations(inventoryViewPage.getPageConfigurationsName());
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        deleteOldConfigurations(inventoryViewPage.getPageConfigurationsName());
        deleteOldConfigurations(inventoryViewPage.getTableConfigurationsName());
        selectObjectOfSuperType(TEST_DIRECTOR_TYPE);
        deleteOldConfigurations(inventoryViewPage.getTabsConfigurationsName());
        inventoryViewPage.getMainTable().unselectAllRows();
    }

    private void deleteOldConfigurations(List<String> configurationNames) {
        List<String> savedConfigurations = configurationNames.stream().filter(name-> !name.equals(MODEL) && !name.equals(DEFAULT)).collect(Collectors.toList());
        inventoryViewPage.deleteConfigurations(savedConfigurations);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForIVPage() {
        inventoryViewPage.disableColumnAndApply(GENDER_LABEL);
        inventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG);
        
        saveConfigurationForTabsForSuperType(TEST_ACTOR_TYPE, TABLE_TYPE, INTERESTS,
                CONFIGURATION_TEST_ACTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        saveConfigurationForTabsForSuperType(TEST_DIRECTOR_TYPE, TABLE_TYPE, MATERIALS,
                CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        
        inventoryViewPage.setHorizontalLayout(HORIZONTAL_60_40_BUTTON_ID);
        inventoryViewPage.saveNewPageConfiguration(CONFIGURATION_NAME_IV);
        
        assertSuccessMessage();
    }
    
    @Test(priority = 2)
    public void setDefaultConfigurationForIVPage() {
        inventoryViewPage.applyConfigurationForPage(DEFAULT_CONFIGURATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        
        Assert.assertTrue(columnHeaders.contains(GENDER_LABEL));
        assertTabsNotVisible(TEST_ACTOR_TYPE, INTERESTS);
        assertTabsNotVisible(TEST_DIRECTOR_TYPE, MATERIALS);
        Assert.assertTrue(inventoryViewPage.isHorizontal(HORIZONTAL_50_50_BUTTON_ID));
    }
    
    @Test(priority = 3)
    public void chooseConfigurationForIVPage() {
        inventoryViewPage.applyConfigurationForPage(CONFIGURATION_NAME_IV);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        
        Assert.assertFalse(columnHeaders.contains(GENDER_LABEL));
        assertTabsVisible(TEST_ACTOR_TYPE, INTERESTS);
        assertTabsVisible(TEST_DIRECTOR_TYPE, MATERIALS);
        Assert.assertTrue(inventoryViewPage.isHorizontal(HORIZONTAL_60_40_BUTTON_ID));
    }
    
    @Test(priority = 4)
    public void checkConfigurationAfterRefreshPage() {
        inventoryViewPage.disableColumnAndApply(TYPE_LABEL);
        driver.navigate().refresh();
        
        assertTabsVisible(TEST_ACTOR_TYPE, INTERESTS);
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
        Assert.assertFalse(columnHeaders.contains(TYPE_LABEL));
    }
    
    @Test(priority = 6)
    public void downloadConfigurationOfPage() {
        inventoryViewPage.downloadConfigurationForPage(CONFIGURATION_NAME_IV);
        assertSuccessMessage();
        
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        inventoryViewPage.downloadConfigurationForPage(CONFIGURATION_NAME_IV);
        assertSuccessMessage();
    }
    
    @Test(priority = 7)
    public void tryRemoveConfigurationUsedInConfigurationOfPage() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        inventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG);
        assertDangerMessage();
        
        selectObjectOfSuperType(TEST_DIRECTOR_TYPE);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        assertDangerMessage();
    }
    
    @Test(priority = 8)
    public void removePageConfiguration() {
        inventoryViewPage.deletePageConfiguration(CONFIGURATION_NAME_IV);
        assertSuccessMessage();

        inventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_VIEW_CONFIG);
        assertSuccessMessage();

        selectObjectOfSuperType(TEST_ACTOR_TYPE);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_ACTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        assertSuccessMessage();

        tableWidget.unselectAllRows();
        selectObjectOfSuperType(TEST_DIRECTOR_TYPE);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_VIEW_CONFIG);
        assertSuccessMessage();
    }
    
    @Test(priority = 9)
    public void saveDefaultViewConfigurationForUserForSuperType() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        inventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_DEFAULT_VIEW_CONFIG,
                createField(TYPE, TEST_DIRECTOR_TYPE));
        inventoryViewPage.selectFirstRow();
        inventoryViewPage.enableWidget(TABLE_TYPE, MOVIES);
        inventoryViewPage.saveConfigurationForTabs(CONFIGURATION_TEST_PERSON_TABS_WIDGET_USED_IN_DEFAULT_VIEW_CONFIG,
                createField(TYPE, TEST_PERSON_TYPE));
        inventoryViewPage.saveNewPageConfiguration(CONFIGURATION_NAME_IV_DEFAULT_FOR_USER, createField(DEFAULT_VIEW_FOR, ME));
        assertSuccessMessage();
    }
    
    @Test(priority = 10)
    public void saveDefaultViewConfigurationForGroupForType() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        saveConfigurationForTabsForSuperType(TEST_DIRECTOR_TYPE, TABLE_TYPE, MATERIALS,
                CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_DEFAULT_GROUP_CONFIG);
        inventoryViewPage.saveNewPageConfiguration(CONFIGURATION_NAME_IV_TYPE_DEFAULT_FOR_GROUP, createField(GROUPS, GROUP_NAME));
        
        assertSuccessMessage();
        inventoryViewPage.chooseGroupContext(GROUP_NAME);
        assertTabsVisible(TEST_DIRECTOR_TYPE, MATERIALS);
    }
    
    @Test(priority = 11)
    public void groupAndTypeInheritanceDefaultViewConfiguration() {
        inventoryViewPage.changeUser(USER2, PASSWORD_2);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        int offset = 400;
        inventoryViewPage.chooseGroupContext(GROUP_NAME);
        inventoryViewPage.selectFirstRow();
        Assert.assertTrue(inventoryViewPage.isTabVisible(MATERIALS));
        Assert.assertFalse(inventoryViewPage.isTabVisible(MOVIES));
        
        inventoryViewPage.changeUser(USER1, PASSWORD_1);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        inventoryViewPage.chooseGroupContext(GROUP_NAME);
        TableWidget tableWidget1 = inventoryViewPage.getMainTable();
        int newSize = tableWidget1.getFirstColumnSize();
        Assert.assertEquals(defaultSize + offset, newSize);

        inventoryViewPage.selectFirstRow();
        Assert.assertFalse(inventoryViewPage.isTabVisible(MATERIALS));
        Assert.assertTrue(inventoryViewPage.isTabVisible(MOVIES));

    }
    
    @AfterMethod
    public void unselectAllRows() {
        if (!tableWidget.getSelectedObjectCount().equals("0 selected")) {
            tableWidget.unselectAllRows();
        }
    }

    @AfterClass
    public void deleteConfiguration(){
        inventoryViewPage.deletePageConfiguration(CONFIGURATION_NAME_IV_DEFAULT_FOR_USER);
        inventoryViewPage.deletePageConfiguration(CONFIGURATION_NAME_IV_TYPE_DEFAULT_FOR_GROUP);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_DIRECTOR_TABS_WIDGET_USED_IN_DEFAULT_GROUP_CONFIG);
        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_TEST_PERSON_TABS_WIDGET_USED_IN_DEFAULT_VIEW_CONFIG);
        inventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USED_IN_DEFAULT_VIEW_CONFIG);
    }
    
    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }
    
    private void saveConfigurationForTabsForSuperType(String typeValue, String widgetType, String widgetLabel, String configurationName) {
        selectObjectOfSuperType(typeValue);
        inventoryViewPage.enableWidget(widgetType, widgetLabel);
        inventoryViewPage.saveConfigurationForTabs(configurationName, createField(TYPE, typeValue));
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
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        systemMessage.close();
    }
    
    private void assertDangerMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 2);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.DANGER));
        systemMessage.close();
    }
}
