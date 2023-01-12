package com.oss.configuration;

import static com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property.GROUPS;
import static com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property.TYPE;

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
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.configuration.SaveConfigurationWizard;
import com.comarch.oss.web.pages.configuration.SaveConfigurationWizard.Property;
import com.oss.utils.TestListener;

@Listeners({TestListener.class})
public class DetailsConfigurationTest extends BaseTestCase {
    
    private static final String ID_LABEL = "ID";
    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private static final String ATTRIBUTE_ID_TYPE = "type";
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_PROPERTIES_TEST_ACTOR = "Properties_Test_Actor_Configuration";
    private final static String CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_USER_TEST_ACTOR = "Properties_User_Configuration";
    private final static String CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_GROUP_TEST_PERSON = "Properties_Group_Configuration";
    private final static String PROPERTY_PANEL_ID = "PropertyPanelWidget";
    private final static int ROW_ID_0 = 0;
    private static final String FIRST_NAME_ID = "firstName";
    private static final String NAME_LABEL = "First Name";
    private static final String NATIONALITY_LABEL = "Nationality";
    private static final String GENDER_LABEL = "Gender";
    private static final String GENDER_ID = "gender";
    private static final String TEST_PERSON_TYPE = "TestPerson";
    private static final String TEST_ACTOR_TYPE = "TestActor";
    private static final String TEST_DIRECTOR_TYPE = "TestDirector";
    private static final String ME = "Me";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";
    private static final String PROPERTIES = "Properties_";

    private NewInventoryViewPage inventoryViewPage;
    
    @BeforeClass
    public void goToInventoryView() {
        deleteOldConfigurations();
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
    
    private void deleteOldConfigurations() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR_TYPE);
        deletePropertiesConfigurations(inventoryViewPage.getPropertiesConfigurationsName(ROW_ID_0, PROPERTY_PANEL_ID));
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        deletePropertiesConfigurations(inventoryViewPage.getPropertiesConfigurationsName(ROW_ID_0, PROPERTY_PANEL_ID));
    }

    private void deletePropertiesConfigurations(List<String> tabsConfigurationsName2) {
        List<String> tabsConfigurationsName = tabsConfigurationsName2.stream().filter(name -> name.contains(PROPERTIES)).collect(Collectors.toList());
        inventoryViewPage.deleteConfigurations(tabsConfigurationsName);
    }
    
    @Test(priority = 1)
    public void saveNewConfigurationForPropertiesForSuperType() { inventoryViewPage.searchByAttributeValue(ATTRIBUTE_ID_TYPE, TEST_ACTOR_TYPE, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel.disableAttributeByLabel(NATIONALITY_LABEL);
        inventoryViewPage.saveConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_TEST_ACTOR);
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
    }
    
    @Test(priority = 2)
    public void setDefaultConfigurationForProperties() {
        inventoryViewPage.applyConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, DEFAULT_CONFIGURATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        Assert.assertTrue(propertyPanel.getPropertyLabels().contains(NATIONALITY_LABEL));
    }
    
    @Test(priority = 3)
    public void chooseConfigurationForProperties() {
        inventoryViewPage.applyConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_TEST_ACTOR);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        Assert.assertFalse(propertyPanel.getPropertyLabels().contains(NATIONALITY_LABEL));
    }
    
    @Test(priority = 4)
    public void updateConfigurationForProperties() {
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        int newPosition = propertyPanel.getPropertyLabels().indexOf(ID_LABEL);
        inventoryViewPage.changePropertiesOrder(ROW_ID_0, PROPERTY_PANEL_ID, FIRST_NAME_ID, newPosition);
        inventoryViewPage.updateConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID);
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
        
        driver.navigate().refresh();
        inventoryViewPage.searchByAttributeValue(ATTRIBUTE_ID_TYPE, TEST_ACTOR_TYPE, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewPage.applyConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_TEST_ACTOR);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> labels = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID).getPropertyLabels();
        Assert.assertEquals(labels.indexOf(NAME_LABEL), newPosition);
    }
    
    @Test(priority = 5)
    public void downloadConfigurationOfTabsWidget() {
        // when
        inventoryViewPage.downloadConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_TEST_ACTOR);
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
    }
    
    @Test(priority = 6)
    public void removeConfigurationOfProperties() {
        // when
        inventoryViewPage.removeConfigurationOfProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_TEST_ACTOR);
        // then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertSame(messages.get(0).getMessageType(), (SystemMessageContainer.MessageType.SUCCESS));
    }
    
    @Test(priority = 7)
    public void saveDefaultConfigurationForPropertiesForUserForSuperType() {
        // when
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel.disableAttributeByLabel(GENDER_LABEL);
        inventoryViewPage.saveConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID,
                CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_USER_TEST_ACTOR, createField(TYPE, TEST_ACTOR_TYPE),
                createField(DEFAULT_VIEW_FOR, ME));
        driver.navigate().refresh();
        inventoryViewPage.searchByAttributeValue(ATTRIBUTE_ID_TYPE, TEST_ACTOR_TYPE, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel1 = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        Assert.assertFalse(propertyPanel1.getPropertyLabels().contains(GENDER_LABEL));
    }
    
    @Test(priority = 8)
    public void saveDefaultConfigurationForPropertiesForGroupForType() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyPanel.enableAttributeByLabel(GENDER_LABEL);
        inventoryViewPage.changePropertiesOrder(ROW_ID_0, PROPERTY_PANEL_ID, GENDER_ID, 0);
        inventoryViewPage.saveConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID,
                CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_GROUP_TEST_PERSON, createField(GROUPS, GROUP_NAME),
                createField(TYPE, TEST_PERSON_TYPE));
        driver.navigate().refresh();
        inventoryViewPage.chooseGroupContext(GROUP_NAME);
        List<String> labels = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID).getPropertyLabels();
        Assert.assertEquals(labels.indexOf(GENDER_LABEL), 0);
    }
    
    @Test(priority = 9)
    public void groupAndTypeInheritanceDefaultConfigurationOfProperties() {
        // when
        inventoryViewPage.changeUser(USER2, PASSWORD_2);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR_TYPE);
        inventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> labels = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID).getPropertyLabels();
        Assert.assertEquals(labels.indexOf(GENDER_LABEL), 0);
        
        inventoryViewPage.changeUser(USER1, PASSWORD_1);
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_ACTOR_TYPE);
        PropertyPanel propertyPanel1 = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        Assert.assertFalse(propertyPanel1.getPropertyLabels().contains(GENDER_LABEL));
    }
    
    @AfterClass
    public void deleteConfiguration() {
        inventoryViewPage.removeConfigurationOfProperties(ROW_ID_0, PROPERTY_PANEL_ID,
                CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_GROUP_TEST_PERSON);
        inventoryViewPage.removeConfigurationOfProperties(ROW_ID_0, PROPERTY_PANEL_ID,
                CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_USER_TEST_ACTOR);
    }
    
    private SaveConfigurationWizard.Field createField(Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }
    
}
