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
    private final static String CONFIGURATION_NAME_TABLE_WIDGET_USER = "Table_Widget_User";
    private final static String DEFAULT_CONFIGURATION = "DEFAULT";
    private final static String TYPE_LABEL = "Name";
    private final static String GENDER_LABEL = "Object Type";
    private final static String ID_LABEL = "Identifier";
    private final static String USER2 = "webseleniumtest2";
    private static final String PASSWORD_2 = "oss";
    private static final String LOCATION = "Location";
    private static final String BUILDING = "Building";

    @BeforeClass
    public void goToInventoryView() {
        //given
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    public void saveNewConfigurationForTableWidget() {

        //when
        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(0);

        newInventoryViewPage.disableColumnAndApply(GENDER_LABEL);

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

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        List<String> columnHeadersAfterChangeConfig = newInventoryViewPage.getActiveColumnsHeaders();

        Assertions.assertThat(columnHeadersAfterChangeConfig).doesNotContain(GENDER_LABEL);

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

        com.oss.framework.widgets.table.TableWidget tableWidget1 = newInventoryViewPage.getMainTable();
        int newSize = tableWidget1.getFirstColumnSize();
        Assertions.assertThat(defaultSize + offset).isEqualTo(newSize);
    }

    @Test(priority = 5)
    public void downloadConfigurationOfTableWidget() {

        newInventoryViewPage.downloadConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.close();
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
    public void saveDefaultConfigurationOfTableWidgetForUser() {
        newInventoryViewPage.enableColumn(ID_LABEL);
        newInventoryViewPage.changeColumnsOrderInMainTable(ID_LABEL, 0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        newInventoryViewPage.saveNewConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USER, createField(DEFAULT_VIEW_FOR, "Me"));
        //then
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);

        driver.navigate().refresh();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders.indexOf(ID_LABEL)).isZero();

        newInventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_USER);

    }

    @Test(priority = 8)
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

        newInventoryViewPage.chooseGroupContext(GROUP_NAME);

        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders.indexOf(TYPE_LABEL)).isZero();

    }

    @Test(priority = 9)
    public void groupAndTypeInheritanceDefaultConfigurationOfTableWidget() {
        newInventoryViewPage.changeUser(USER2, PASSWORD_2);
        newInventoryViewPage = com.oss.pages.platform.NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, BUILDING);

        newInventoryViewPage.chooseGroupContext(GROUP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        List<String> columnHeaders = newInventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders.indexOf(TYPE_LABEL)).isZero();

        newInventoryViewPage.deleteConfigurationForMainTable(CONFIGURATION_NAME_TABLE_WIDGET_GROUP);

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
