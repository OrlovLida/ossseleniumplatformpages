package com.oss.configuration;


import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
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

@Listeners({TestListener.class})
public class DetailsConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private static String GROUP_NAME= "SeleniumTests";
    private static String CONFIGURATION_NAME_PROPERTIES= "Properties_User_Building";
    private static String CONFIGURATION_NAME_PROPERTIES_SUPERTYPE= "Properties_User_Location";
    private static String CONFIGURATION_NAME_PROPERTIES_GROUP= "Properties_Group";

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
        newInventoryViewPage.saveConfigurationForPropertiesForUser(CONFIGURATION_NAME_PROPERTIES, "Building");

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    @Description("Saving new configuration for properties for supertype")
    public void saveNewConfigurationForPropertiesForSupertype(){
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.getPropertyPanel().changeOrder("name", 3);
        newInventoryViewPage.saveConfigurationForPropertiesForUser(CONFIGURATION_NAME_PROPERTIES_SUPERTYPE,"Location");

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 3)
    @Description("Saving new configuration for properties for group")
    public void saveNewConfigurationForPropertiesForGroup(){
        //when
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.getPropertyPanel().changeOrder("name", 4);
        newInventoryViewPage.saveConfigurationForPropertiesForGroup(CONFIGURATION_NAME_PROPERTIES_GROUP,"Location", GROUP_NAME);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
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
}
