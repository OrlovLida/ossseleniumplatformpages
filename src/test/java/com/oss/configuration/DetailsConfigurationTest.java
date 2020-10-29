package com.oss.configuration;


import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.components.portals.SaveConfigurationWizard.Property;
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
public class DetailsConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private final static String GROUP_NAME= "SeleniumTests";
    private final static String CONFIGURATION_NAME_PROPERTIES= "Properties_User_Building";
    private final static String CONFIGURATION_NAME_PROPERTIES_SUPERTYPE= "Properties_User_Location";
    private final static String CONFIGURATION_NAME_PROPERTIES_GROUP= "Properties_Group";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Building");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Saving new configuration for properties for type")
    public void saveNewConfigurationForPropertiesForType(){
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage
                .saveConfigurationForProperties(
                        CONFIGURATION_NAME_PROPERTIES,
                        createField(TYPE, "Building"));

        //then
        assertSuccessMessage();
    }

    @Test(priority = 2)
    @Description("Saving new configuration for properties for supertype")
    public void saveNewConfigurationForPropertiesForSupertype(){
        //when
        newInventoryViewPage
                .selectFirstRow();
        newInventoryViewPage
                .getPropertyPanel()
                .changePropertyOrder("name", 3);
        newInventoryViewPage
                .saveConfigurationForProperties(
                        CONFIGURATION_NAME_PROPERTIES_SUPERTYPE,
                        createField(TYPE, "Location"));

        //then
        assertSuccessMessage();
    }

    @Test(priority = 3)
    @Description("Saving new configuration for properties for group")
    public void saveNewConfigurationForPropertiesForGroup(){
        //when
        newInventoryViewPage
                .selectFirstRow();
        newInventoryViewPage
                .getPropertyPanel()
                .changePropertyOrder("name", 4);
        newInventoryViewPage
                .saveConfigurationForProperties(
                        CONFIGURATION_NAME_PROPERTIES_GROUP,
                        createField(TYPE, "Location"),
                        createField(GROUPS, GROUP_NAME));

        //then
        assertSuccessMessage();
    }

    @Test(priority = 4)
    @Description("")
    public void checkingConfigurationForPropertiesForType(){
        //given
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Building");
        //when
        newInventoryViewPage.selectFirstRow();
        //then
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyLabels().get(2), "Name");
    }

    @Test(priority = 5)
    @Description("")
    public void checkingConfigurationForPropertiesForSupertype(){
        //given
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Site");
        //when
        newInventoryViewPage.selectFirstRow();
        //then
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyLabels().get(3), "Name");
    }


    @Test(priority = 6)
    @Description("")
    public void checkingConfigurationForPropertiesForTypeInHierarchyView(){
        //given
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Building");
        newInventoryViewPage.selectFirstRow();
        //when
        HierarchyViewPage hierarchyViewPage = newInventoryViewPage.goToHierarchyViewForSelectedObject();
        hierarchyViewPage.selectFirstObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //then
        Assert.assertEquals(hierarchyViewPage.getBottomPropertyPanel("Building").getPropertyLabels().get(2), "Name");
    }


    @Test(priority = 7)
    @Description("")
    public void checkingConfigurationForPropertiesForGroup(){
        //given
        newInventoryViewPage.changeUser("webseleniumtests2","webtests");
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        //when
        newInventoryViewPage.selectFirstRow();
        //then
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyLabels().get(4), "Name");
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
