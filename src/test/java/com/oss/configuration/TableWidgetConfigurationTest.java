package com.oss.configuration;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static com.oss.framework.components.portals.SaveConfigurationWizard.Property.GROUPS;

@Listeners({TestListener.class})
public class TableWidgetConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private final static String GROUP_NAME= "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET= "Table_Widget_User";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_GROUP= "Table_Widget_Group";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


    @Test(priority = 1)
    @Description("Saving new configuration for table widget for user")
    public void saveNewConfigurationForTableWidgetForUser() {
        //when
        newInventoryViewPage.enableColumn("Depth");
        newInventoryViewPage.disableColumnAndApply("Name");
        newInventoryViewPage.changeLayoutToVertical()
                .changeColumnsOrderInMainTable("XId", 3)
                .openFilterPanel()
                .setValueOnComboWithTags("type", "type-dropdown-search", "Building")
                .applyFilter();
        newInventoryViewPage.saveConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    @Description("Checking that configuration is saved properly in this Table Widget")
    public void isConfigurationForTableWidgetWorks() {}

    @Test(priority = 3)
    @Description("Saving new configuration for table widget for group")
    public void saveNewConfigurationForTableWidgetForGroup() {
        //when
        newInventoryViewPage.enableColumn("Height");
        newInventoryViewPage.disableColumnAndApply("Latitude");
        newInventoryViewPage.changeColumnsOrderInMainTable("XId", 2)
                .openFilterPanel()
                .setValueOnComboWithTags("type", "type-dropdown-search", "Site")
                .applyFilter();
        newInventoryViewPage.saveConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_GROUP, createField(GROUPS, GROUP_NAME));
        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }
}
