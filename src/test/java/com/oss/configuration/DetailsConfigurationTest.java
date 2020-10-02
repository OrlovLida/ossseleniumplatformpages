package com.oss.configuration;


import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners({TestListener.class})
public class DetailsConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private String GROUP_NAME= "SeleniumTests";
    private String CONFIGURATION_NAME_PROPERTIES= "Properties_User_Building";
    private String CONFIGURATION_NAME_PROPERTIES_SUPERTYPE= "Properties_User_Location";
    private String CONFIGURATION_NAME_PROPERTIES_GROUP= "Properties_Group";

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
        newInventoryViewPage.checkFirstCheckbox();
        newInventoryViewPage.openSaveConfigurationForPropertiesWizard().typeName(CONFIGURATION_NAME_PROPERTIES).setForType("Building").setAsDefaultForMe().saveAsNew();

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
        newInventoryViewPage.checkFirstCheckbox();
        newInventoryViewPage.openSaveConfigurationForPropertiesWizard().typeName(CONFIGURATION_NAME_PROPERTIES_SUPERTYPE).setForType("Location").setAsDefaultForMe().saveAsNew();

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
        newInventoryViewPage.checkFirstCheckbox();
        newInventoryViewPage.openSaveConfigurationForPropertiesWizard().typeName(CONFIGURATION_NAME_PROPERTIES_GROUP).setForType("Location").setAsDefaultForGroup(GROUP_NAME).saveAsNew();

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}
