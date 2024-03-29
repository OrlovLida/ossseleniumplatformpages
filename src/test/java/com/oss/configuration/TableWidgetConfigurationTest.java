package com.oss.configuration;

import static com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property.GROUPS;
import static com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property.TYPE;

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
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.configuration.SaveConfigurationWizard;
import com.oss.utils.TestListener;

@Listeners({TestListener.class})
public class TableWidgetConfigurationTest extends BaseTestCase {
    
    private NewInventoryViewPage newInventoryViewPage;
    
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET = "Table_Widget_Selenium_Test_Configuration_" + LocalDate.now();
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_GROUP_TEST_PERSON = "Table_Widget_Group_" + LocalDate.now();
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_USER_TEST_ACTOR = "Table_Widget_User_" + LocalDate.now();
    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private final static String TYPE_LABEL = "Type";
    private final static String GENDER_LABEL = "Gender";
    private final static String NATIONALITY_LABEL = "Nationality";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";
    private static final String TEST_PERSON = "TestPerson";
    private static final String TEST_ACTOR = "TestActor";
    private static final String TEST_DIRECTOR = "TestDirector";
    private static final String ME = "Me";
    private static final String TABLE = "Table_Widget";
    int newPositionOfTypeLabel;
    
    @BeforeClass
    public void goToInventoryView() {
        // given
        deleteOldConfiguration();
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void deleteOldConfiguration() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        deleteOldConfigurations(newInventoryViewPage.getTableConfigurationsName());
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR);
        deleteOldConfigurations(newInventoryViewPage.getTableConfigurationsName());
    }

    private void deleteOldConfigurations(List<String> tableConfigurationsName) {
        List<String> savedConfigurations = tableConfigurationsName.stream().filter(name-> name.contains(TABLE)).collect(Collectors.toList());
        newInventoryViewPage.deleteConfigurations(savedConfigurations);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForTableWidget() {
        // when
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(0);
        newInventoryViewPage.disableColumnAndApply(GENDER_LABEL);
        newInventoryViewPage.changeColumnsOrderInMainTable(firstHeader, 2);
        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);
        
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        systemMessage.close();
    }
    
    @Test(priority = 2)
    public void setDefaultConfigurationForTableWidget() {
        setDefaultConfigurationForTable();
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertTrue(columnHeaders.contains(GENDER_LABEL));
    }
    
    @Test(priority = 3)
    public void chooseConfigurationForTableWidget() {
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        
        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);
        
        newInventoryViewPage.applyConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeadersAfterChangeConfig = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertFalse(columnHeadersAfterChangeConfig.contains(GENDER_LABEL));
        Assert.assertEquals(columnHeadersAfterChangeConfig.indexOf(firstHeader), 2);
        Assert.assertEquals(columnHeadersAfterChangeConfig.indexOf(secondHeader), 0);
        Assert.assertEquals(columnHeadersAfterChangeConfig.indexOf(thirdHeader), 1);
    }
    
    @Test(priority = 4)
    public void updateConfigurationForTableWidget() {
        TableWidget tableWidget = newInventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        
        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        
        newInventoryViewPage.updateConfigurationForMainTable();
        
        setDefaultConfigurationForTable();
        newInventoryViewPage.applyConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        
        TableWidget tableWidget1 = newInventoryViewPage.getMainTable();
        int newSize = tableWidget1.getFirstColumnSize();
        Assert.assertEquals(defaultSize + offset, newSize);
    }
    
    @Test(priority = 5)
    public void downloadConfigurationOfTableWidget() {
        newInventoryViewPage.downloadConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);
        
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        systemMessage.close();
    }
    
    @Test(priority = 6)
    public void removeConfigurationOfTableWidget() {
        newInventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        systemMessage.close();
    }
    
    @Test(priority = 7)
    public void saveDefaultConfigurationOfTableWidgetForUserForSuperType() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.enableColumn(NATIONALITY_LABEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        int newPosition = columnHeaders.indexOf("ID");
        newInventoryViewPage.changeColumnsOrderInMainTable(NATIONALITY_LABEL, newPosition);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        
        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USER_TEST_ACTOR,
                createField(TYPE, TEST_ACTOR), createField(DEFAULT_VIEW_FOR, ME));
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertEquals(columnHeaders.indexOf(NATIONALITY_LABEL), newPosition);
        newInventoryViewPage.changeUser(USER2, PASSWORD_2);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR);
        columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertNotEquals(columnHeaders.indexOf(NATIONALITY_LABEL), newPosition);
    }
    
    @Test(priority = 8)
    public void saveDefaultConfigurationOfTableWidgetForGroupForType() {
        newInventoryViewPage.changeUser(USER1, PASSWORD_1);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON);
        newInventoryViewPage.enableColumn(TYPE_LABEL);
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        newPositionOfTypeLabel = columnHeaders.indexOf("ID");
        newInventoryViewPage.changeColumnsOrderInMainTable(TYPE_LABEL, newPositionOfTypeLabel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_GROUP_TEST_PERSON,
                createField(GROUPS, GROUP_NAME));
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        
        setDefaultConfigurationForTable();
        newInventoryViewPage.chooseGroupContext(GROUP_NAME);
        columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertEquals(columnHeaders.indexOf(TYPE_LABEL), newPositionOfTypeLabel);
    }
    
    @Test(priority = 9)
    public void groupAndTypeInheritanceDefaultConfigurationOfTableWidget() {
        newInventoryViewPage.changeUser(USER2, PASSWORD_2);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR);
        newInventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertEquals(columnHeaders.indexOf(TYPE_LABEL), newPositionOfTypeLabel);
        
        newInventoryViewPage.changeUser(USER1, PASSWORD_1);
        newInventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR);
        List<String> columnHeaders1 = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertEquals(columnHeaders1.indexOf(TYPE_LABEL), newPositionOfTypeLabel);
        
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR);
        List<String> columnHeaders2 = newInventoryViewPage.getActiveColumnsHeaders();
        Assert.assertNotEquals(columnHeaders2.indexOf(TYPE_LABEL), newPositionOfTypeLabel);
    }
    
    @AfterClass
    public void deleteConfigurationForTable() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR);
        newInventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USER_TEST_ACTOR);
        newInventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_GROUP_TEST_PERSON);
    }
    
    private void setDefaultConfigurationForTable() {
        newInventoryViewPage.applyConfigurationForMainTable(DEFAULT_CONFIGURATION);
        newInventoryViewPage.setDefaultSettings();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }
}
