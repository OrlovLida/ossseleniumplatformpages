package com.oss.configuration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard.Property;
import com.oss.utils.TestListener;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.GROUPS;

@Listeners({TestListener.class})
public class DetailsConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;

    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_PROPERTIES = "Properties_Configuration";
    private final static String CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_USER = "Properties_User_Configuration";
    private final static String CONFIGURATION_NAME_PROPERTIES_GROUP = "Properties_Group";

    private final static String PROPERTY_PANEL_ID = "PropertyPanelWidget";
    private final static int ROW_ID_0 = 0;

    private static final String FIRST_NAME_ID = "firstName";
    private static final String NAME_LABEL = "First Name";
    private static final String NATIONALITY_LABEL = "Nationality";
    private static final String GENDER_LABEL = "Gender";
    private static final String GENDER_ID = "gender";
    private static final String TEST_PERSON_TYPE = "TestPerson";
    private static final String TEST_DIRECTOR_TYPE = "TestDirector";
    private static final String ME = "Me";
    private final static String USER2 = "webseleniumtests2";
    private static final String PASSWORD_2 = "oss";
    private final static String USER1 = "webseleniumtests";
    private static final String PASSWORD_1 = "Webtests123!";

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForPropertiesForSuperType() {

        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        propertyPanel.disableAttributeByLabel(NATIONALITY_LABEL);

        inventoryViewPage.saveConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    public void setDefaultConfigurationForProperties() {

        inventoryViewPage.applyConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, DEFAULT_CONFIGURATION);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        Assertions.assertThat(propertyPanel.getPropertyLabels()).contains(NATIONALITY_LABEL);

    }

    @Test(priority = 3)

    public void chooseConfigurationForProperties() {

        inventoryViewPage.applyConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);
        Assertions.assertThat(propertyPanel.getPropertyLabels()).isNotEmpty().doesNotContain(NATIONALITY_LABEL);

    }

    @Test(priority = 4)
    public void updateConfigurationForProperties() {

        inventoryViewPage.changePropertiesOrder(ROW_ID_0, PROPERTY_PANEL_ID, FIRST_NAME_ID, 0);

        inventoryViewPage.updateConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        driver.navigate().refresh();

        inventoryViewPage.applyConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> labels = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID).getPropertyLabels();

        Assertions.assertThat(labels.indexOf(NAME_LABEL)).isZero();

    }

    @Test(priority = 5)
    public void downloadConfigurationOfTabsWidget() {
        //when

        inventoryViewPage.downloadConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 6)
    public void removeConfigurationOfProperties() {
        //when
        inventoryViewPage.selectFirstRow();

        inventoryViewPage.removeConfigurationOfProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 7)
    public void saveDefaultConfigurationForPropertiesForUser() {
        //when
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        propertyPanel.disableAttributeByLabel(GENDER_LABEL);

        inventoryViewPage.saveConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_USER, createField(DEFAULT_VIEW_FOR, ME));

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        driver.navigate().refresh();
        inventoryViewPage.selectFirstRow();
        PropertyPanel propertyPanel1 = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);

        Assertions.assertThat(propertyPanel1.getPropertyLabels()).isNotEmpty().doesNotContain(GENDER_LABEL);
        inventoryViewPage.removeConfigurationOfProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_DEFAULT_FOR_USER);
    }

    @Test(priority = 8)
    public void saveDefaultConfigurationForPropertiesForGroup() {

        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        propertyPanel.enableAttributeByLabel(GENDER_LABEL);
        inventoryViewPage.changePropertiesOrder(ROW_ID_0, PROPERTY_PANEL_ID, GENDER_ID, 0);

        inventoryViewPage.saveConfigurationForProperties(ROW_ID_0, PROPERTY_PANEL_ID, CONFIGURATION_NAME_PROPERTIES_GROUP, createField(GROUPS, GROUP_NAME));

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        driver.navigate().refresh();
        inventoryViewPage.chooseGroupContext(GROUP_NAME);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> labels = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID).getPropertyLabels();

        Assertions.assertThat(labels.indexOf(GENDER_LABEL)).isZero();
    }

    @Test(priority = 9)
    public void groupAndTypeInheritanceDefaultConfigurationOfTabsWidget() {
        //when
        inventoryViewPage.changeUser(USER2, PASSWORD_2);
        inventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_DIRECTOR_TYPE);

        inventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        inventoryViewPage.selectFirstRow();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> labels = inventoryViewPage.getPropertyPanel(ROW_ID_0, PROPERTY_PANEL_ID).getPropertyLabels();

        Assertions.assertThat(labels.indexOf(GENDER_LABEL)).isZero();

        inventoryViewPage.changeUser(USER1, PASSWORD_1);
        inventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_PERSON_TYPE);
        inventoryViewPage.selectFirstRow();

        inventoryViewPage.removeConfigurationForTabs(CONFIGURATION_NAME_PROPERTIES_GROUP);

    }

    private SaveConfigurationWizard.Field createField(Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

}
