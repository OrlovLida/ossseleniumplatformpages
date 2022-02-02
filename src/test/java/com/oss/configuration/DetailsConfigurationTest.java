package com.oss.configuration;


import io.qameta.allure.Description;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.pages.platform.configuration.SaveConfigurationWizard.Property;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.untils.Constants;
import com.oss.utils.TestListener;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.TYPE;

@Listeners({TestListener.class})
public class DetailsConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private final static String GROUP_NAME= "SeleniumTests";
    private final static String CONFIGURATION_NAME_PROPERTIES= "Properties_User_Building";
    private final static String CONFIGURATION_NAME_PROPERTIES_SUPERTYPE= "Properties_User_Location";
    private final static String CONFIGURATION_NAME_PROPERTIES_GROUP= "Properties_Group";

    private static final String PROPERTY_PANEL_ID = "InventoryView_DetailsTab_Building_InventoryView_PropertyPanelWidget_Building";

    private static final String NAME_LABEL = "Name";
    private static final String IDENTIFIER_LABEL = "Identifier";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, Constants.BUILDING_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @AfterClass
    public void cleanUpConfigurations() {
        deleteConfiguration();
    }

    @Test(priority = 1)
    @Description("Saving new configuration for properties for supertype")
    public void saveNewConfigurationForPropertiesForSupertype(){

        //given
        List<String> labels = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID).getPropertyLabels();

        // set first positions for name
        int namesPosition = labels.indexOf(NAME_LABEL);
        int namesNewPosition = namesPosition == 0 ? 1 : 0;

        // set last positions for identifier
        int identifierPosition = labels.indexOf(IDENTIFIER_LABEL);
        int identifierNewPosition = identifierPosition == labels.size() - 1 ? labels.size() - 2 : labels.size() - 1;

        // when
        setNewAttributesPositions(namesNewPosition, identifierNewPosition, Constants.LOCATION_TYPE);

        //then
        assertAttributePosition(namesNewPosition, identifierNewPosition, Constants.BUILDING_TYPE);
        assertAttributePosition(namesNewPosition, identifierNewPosition, Constants.SITE_TYPE);
    }


    @Test(priority = 2)
    @Description("Saving new configuration for properties for type")
    public void saveNewConfigurationForPropertiesForType(){

        //given
        List<String> labels = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID).getPropertyLabels();

        // set last position for name
        int namesPosition = labels.indexOf(NAME_LABEL);
        int namesNewPosition = namesPosition == labels.size() - 1 ? labels.size() - 2 : labels.size() - 1;

        // set first position for identifier
        int identifierPosition = labels.indexOf(IDENTIFIER_LABEL);
        int identifierNewPosition = identifierPosition ==  0 ? 1 : 0;

        // when
        setNewAttributesPositions(namesNewPosition, identifierNewPosition, Constants.BUILDING_TYPE);

        //then
        assertAttributePosition(namesNewPosition, identifierNewPosition, Constants.BUILDING_TYPE);
        assertAttributePosition(namesPosition, identifierPosition, Constants.SITE_TYPE);
    }

    private void deleteConfiguration() {


    }

    private void assertAttributePosition(int namePosition, int identifierPosition, String type) {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, type);
        List<String> labels = newInventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID).getPropertyLabels();
        Assertions.assertThat(labels.indexOf(NAME_LABEL)).isEqualTo(namePosition);
        Assertions.assertThat(labels.indexOf(IDENTIFIER_LABEL)).isEqualTo(identifierPosition);
    }

    private void setNewAttributesPositions(int namePosition, int identifierPosition, String type) {
        newInventoryViewPage.changePropertiesOrder(0, PROPERTY_PANEL_ID, Constants.COMMON_NAME_ATTRIBUTE, namePosition);
        newInventoryViewPage.changePropertiesOrder(0, PROPERTY_PANEL_ID, Constants.COMMON_IDENTIFIER_ATTRIBUTE, identifierPosition);
        newInventoryViewPage.saveConfigurationForProperties(0, PROPERTY_PANEL_ID,
                CONFIGURATION_NAME_PROPERTIES, createField(DEFAULT_VIEW_FOR, SaveConfigurationWizard.DEFAULT_ME_VALUE), createField(TYPE, type));

        assertSuccessMessage();
    }

    private SaveConfigurationWizard.Field createField(Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }

    private void assertSuccessMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
    }
}
