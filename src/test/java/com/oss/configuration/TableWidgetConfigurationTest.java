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
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.configuration.SaveConfigurationWizard;
import com.oss.utils.TestListener;

import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.DEFAULT_VIEW_FOR;
import static com.oss.pages.platform.configuration.SaveConfigurationWizard.Property.GROUPS;

@Listeners({TestListener.class})
public class TableWidgetConfigurationTest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;

    private final static String GROUP_NAME = "SeleniumTests";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET = "Table_Widget_Selenium_Test_Configuration";
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_GROUP = "Table_Widget_Group";
    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private final static String TYPE_LABEL = "DM_TestPerson.type";
    private final static String GENDER_LABEL = "DM_TestPerson.gender";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "TestPerson");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForTableWidget() {

        //when
        newInventoryViewPage.disableColumnAndApply(GENDER_LABEL);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(0);

        newInventoryViewPage.changeColumnsOrderInMainTable(firstHeader, 2);

        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
    }

    @Test(priority = 2)
    public void setDefaultConfigurationForTableWidget() {

        setDefaultConfigurationForTable();

        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();

        Assertions.assertThat(columnHeaders).contains(GENDER_LABEL);

    }

    @Test(priority = 3)
    public void chooseConfigurationForTableWidget() {
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();

        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);

        newInventoryViewPage.applyConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        List<String> columnHeadersAfterChangeConfig = newInventoryViewPage.getActiveColumnsHeaders();

        Assertions.assertThat(columnHeadersAfterChangeConfig).isNotEmpty().doesNotContain(GENDER_LABEL);

        Assertions.assertThat(columnHeadersAfterChangeConfig.indexOf(firstHeader)).isEqualTo(2);
        Assertions.assertThat(columnHeadersAfterChangeConfig.indexOf(secondHeader)).isZero();
        Assertions.assertThat(columnHeadersAfterChangeConfig.indexOf(thirdHeader)).isEqualTo(1);

    }

    @Test(priority = 4)
    public void updateConfigurationForTableWidget() {
        com.oss.framework.widgets.table.TableWidget tableWidget = newInventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);

        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);

        newInventoryViewPage.updateConfigurationForMainTable();

        setDefaultConfigurationForTable();
        newInventoryViewPage.applyConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);

        int newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize + offset).isEqualTo(newSize);
    }

    @Test(priority = 5)
    public void downloadConfigurationOfTableWidget() {

        newInventoryViewPage.downloadConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 6)
    public void removeConfigurationOfTableWidget() {

        newInventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
    }

    @Test(priority = 7)
    public void saveDefaultConfigurationOfTableWidgetForGroup() {
        newInventoryViewPage.enableColumn(TYPE_LABEL);
        newInventoryViewPage.changeColumnsOrderInMainTable(TYPE_LABEL, 0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_GROUP, createField(GROUPS, GROUP_NAME));
        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        setDefaultConfigurationForTable();



        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders.indexOf(TYPE_LABEL)).isZero();

    }

    private void setDefaultConfigurationForTable() {
        newInventoryViewPage.applyConfigurationForMainTable(DEFAULT_CONFIGURATION);
        newInventoryViewPage.setDefaultSettings();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }



   /*@Test(priority = 1)
    @Description("Saving new configuration for table widget for user")
    public void saveNewConfigurationForTableWidgetForUser() {
        //when
        newInventoryViewPage.enableColumn("Depth");
        newInventoryViewPage.disableColumnAndApply("Name");
        newInventoryViewPage.setVerticalLayout();
        newInventoryViewPage.changeColumnsOrderInMainTable("XId", 3)
                .openFilterPanel()
                .setValueOnComboWithTags("type", "type-dropdown-search", "Building")
                .applyFilter();
        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    @Description("Checking that configuration is saved properly in this Table Widget")
    public void isConfigurationForTableWidgetWorks() {
    }

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
        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_GROUP, createField(GROUPS, GROUP_NAME));
        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }*/

    private SaveConfigurationWizard.Field createField(SaveConfigurationWizard.Property property, String... values) {
        return SaveConfigurationWizard.create(driver, webDriverWait).createField(property, values);
    }
}
