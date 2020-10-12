package com.oss;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.ColumnsManagement;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.platform.NewInventoryViewPage;

public class InventoryViewTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {

        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
    }

    @Test
    public void searchByType() {
        //given
        TableInterface tableWidget = inventoryViewPage.getMainTable();

        //when
        tableWidget.searchByAttribute("type", ComponentType.COMBOBOX, "PoP");
        Multimap<String,String> filterValues = tableWidget.getAppliedFilters();

        //then
        Assertions.assertThat(filterValues.keys()).hasSize(1);
        Assertions.assertThat(Lists.newArrayList(filterValues.get("Object Type")).get(0)).startsWith("PoP");
    }

    @Test
    public void selectFirstRow() {
        //given
        TableInterface tableWidget = inventoryViewPage.getMainTable();

        //when
        tableWidget.selectRow(0);
        List<TableRow> selectedRows = tableWidget.getSelectedRows();

        //then
        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedRows.get(0).getIndex()).isEqualTo(1);
    }


    @Test
    public void resizeColumn() {
        TableInterface tableWidget = inventoryViewPage.getMainTable();
        int defaultSize = tableWidget.getFirstColumnSize();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        Assertions.assertThat(defaultSize).isEqualTo(200);

        int offset = 400;
        tableWidget.resizeFirstColumn(offset);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);

        int newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize + offset).isEqualTo(newSize);
    }

    @Test
    public void removeSecondColumn() {
        TableInterface tableWidget = inventoryViewPage.getMainTable();
        List<String> columnHeaders = tableWidget.getActiveColumnHeaders();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);

        tableWidget.disableColumnByLabel(secondHeader);

        List<String> newColumnHeaders = tableWidget.getActiveColumnHeaders();
        String newFirstHeader = newColumnHeaders.get(0);
        String newSecondHeader = newColumnHeaders.get(1);

        Assert.assertEquals(firstHeader, newFirstHeader);
        Assert.assertEquals(thirdHeader, newSecondHeader);
    }

    @Test
    public void addFirstUnselectedColumn() {
        //given
        TableInterface tableWidget = inventoryViewPage.getMainTable();
        List<String> columnHeaders = tableWidget.getActiveColumnHeaders();
        String firstHeader = columnHeaders.get(2);

        //when
        tableWidget.disableColumnByLabel(firstHeader);
        columnHeaders = tableWidget.getActiveColumnHeaders();
        Assertions.assertThat(columnHeaders).doesNotContain(firstHeader);

        tableWidget.enableColumnByLabel(firstHeader);

        //then
        columnHeaders = tableWidget.getActiveColumnHeaders();
        Assertions.assertThat(columnHeaders).contains(firstHeader);
    }

    @Test
    public void expandLabel() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstRowID = tableWidget.getTableCells().get(0).getText();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.resizeFirstColumn(-145);
        tableWidget.clickOnFirstExpander();
        Assert.assertEquals(firstRowID, tableWidget.getExpandedText());
    }

    @Test
    public void changeColumnsOrder() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        ColumnsManagement columnsManagement = tableWidget.getColumnsManagement();

        String firstHeader = tableWidget.getFirstColumnLabel();
        String secondHeader = tableWidget.getActiveColumnLabel(1);

        columnsManagement.changeColumnPosition("ID", -120, 0);
        columnsManagement.clickApply();

        String newFirstHeader = tableWidget.getFirstColumnLabel();
        String newSecondHeader = tableWidget.getActiveColumnLabel(1);
        Assert.assertEquals(firstHeader, newSecondHeader);
        Assert.assertEquals(secondHeader, newFirstHeader);
    }

    @Test
    public void checkDefaultSettingsOfColumnsManagement() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        ColumnsManagement columnsManagement = tableWidget.getColumnsManagement();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String defaultFirstLabel = columnsManagement.getColumnLabels().get(0);
        String defaultSecondLabel = columnsManagement.getColumnLabels().get(1);
        boolean defaultModelIDChbxStatus = columnsManagement.isColumnEnable("Model ID");
        boolean defaultDescriptionChbxStatus = columnsManagement.isColumnEnable("Description");
        columnsManagement.toggleColumn("Model ID");
        columnsManagement.toggleColumn("Description");
        boolean modelIDChbxStatus = columnsManagement.isColumnEnable("Model ID");
        boolean descriptionChbxStatus = columnsManagement.isColumnEnable("Description");
        Assert.assertEquals(defaultModelIDChbxStatus, !modelIDChbxStatus);
        Assert.assertEquals(defaultDescriptionChbxStatus, !descriptionChbxStatus);

        columnsManagement.changeColumnPosition("ID", -120, 0);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstLabel = columnsManagement.getColumnLabels().get(0);
        String secondLabel = columnsManagement.getColumnLabels().get(1);
        Assert.assertEquals(firstLabel, defaultSecondLabel);
        Assert.assertEquals(secondLabel, defaultFirstLabel);

        columnsManagement.clickApply();
        String firstHeader = tableWidget.getActiveColumnLabel(0);
        Assert.assertEquals(firstHeader, defaultSecondLabel);

        columnsManagement = tableWidget.getColumnsManagement();
        columnsManagement.clickDefaultSettings();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        firstHeader = tableWidget.getActiveColumnLabel(0);
        Assert.assertEquals(firstHeader, defaultFirstLabel);

        columnsManagement=tableWidget.getColumnsManagement();
        firstLabel = columnsManagement.getColumnLabels().get(0);
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        secondLabel = columnsManagement.getColumnLabels().get(1);
        modelIDChbxStatus = columnsManagement.isColumnEnable("Model ID");
        descriptionChbxStatus = columnsManagement.isColumnEnable("Description");
        Assert.assertEquals(firstLabel, defaultFirstLabel);
        Assert.assertEquals(secondLabel, defaultSecondLabel);
        Assert.assertEquals(defaultModelIDChbxStatus, modelIDChbxStatus);
        Assert.assertEquals(defaultDescriptionChbxStatus, descriptionChbxStatus);
    }

    @Test
    public void checkPagination() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        Assert.assertEquals(tableWidget.isFirstPageActive(), true);
        Assert.assertEquals(tableWidget.isPreviousPageClicable(), false);
        if (tableWidget.isMoreThanOnePage()) {
            tableWidget.clickNextPage();
            DelayUtils.sleep(500);
            Assert.assertEquals(tableWidget.getLabelOfActivePageBtn(), "2");
            tableWidget.clickPreviousPage();
            DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
            Assert.assertEquals(tableWidget.isFirstPageActive(), true);
            tableWidget.clickLastPage();
        }
        Assert.assertEquals(tableWidget.isNextPageClicable(), false);
    }

    @Test
    public void checkHorizontalScroller() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.scrollHorizontally(1000);
        List<String> headers = tableWidget.getActiveColumns();
        Assert.assertEquals(headers.get(headers.size() - 1), "Calculated Parents");
        tableWidget.scrollHorizontally(-1000);
        headers = tableWidget.getActiveColumns();
        Assert.assertEquals(headers.get(0), "ID");
    }

    @Test
    public void checkVerticalScroller() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        tableWidget.scrollVertically(1000);
        String lastVisibleID = tableWidget.getValueFromNthRow("ID", "last()");
        String lastIdOnThePage = tableWidget.getValueFromRowWithID("ID", lastVisibleID);
        Assert.assertEquals(lastVisibleID.equals(lastIdOnThePage), true);
        tableWidget.scrollVertically(-1000);
        lastVisibleID = tableWidget.getValueFromNthRow("ID", "last()");
        Assert.assertEquals(lastVisibleID.equals(lastIdOnThePage), false);
    }

    @Test
    public void findByText() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String secondID = tableWidget.getValueFromNthRow("ID", "2");
        tableWidget.typeIntoSearch(secondID);
        DelayUtils.sleep(500);
        String newFirstID = tableWidget.getValueFromNthRow("ID", "1");
        Assert.assertEquals(secondID, newFirstID);
        Assert.assertEquals(tableWidget.getNumberOfAllRowsInTable(), "1");
    }

    @Test
    public void clickExport() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(750);
        String numberOfExports = homePage.getNumberOfNotifications();
        tableWidget.clickOnKebabMenu();
        tableWidget.clickOnAction("Export");
        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String newNumberOfExports = homePage.getNumberOfNotifications();
        Assert.assertEquals(Integer.valueOf(newNumberOfExports), Integer.valueOf(1 + Integer.valueOf(numberOfExports)));
    }

    @Test
    public void changeTab() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();

        tableWidget.selectFirstRow();
        TabsWidget tabsWidget = inventoryViewPage.getTabsWidget();

        String activeTabLabel = tabsWidget.getActiveTabLabel();
        String secondTabLabel = tabsWidget.getTabLabel(1);
        Assertions.assertThat(activeTabLabel).isNotEqualTo(secondTabLabel);

        tabsWidget.selectTabByLabel(secondTabLabel);
        String newActiveLabel = tabsWidget.getActiveTabLabel();
        Assertions.assertThat(newActiveLabel).isEqualTo(secondTabLabel);
    }

    @Test
    public void clickRefresh() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        DelayUtils.sleep(500);
        tableWidget.clickOnKebabMenu();
        tableWidget.clickOnAction("Refresh");
        Assert.assertTrue(inventoryViewPage.isLoadBarDisplayed());
    }

    @Test
    public void wizardTest() {
        //TODO: problem with click Next and then Cancel button
        TableWidget tableWidget = inventoryViewPage.getMainTable();
//        tableWidget.takeAction("Create", "Location");
        DelayUtils.waitForComponent(webDriverWait,"//div[contains(@id,'CREATE')]");
        driver.findElement(By.id("CREATE")).click();
        DelayUtils.waitForComponent(webDriverWait,"(//a[contains(text(),'Create Location')])[1]");
        driver.findElement(By.xpath("(//a[contains(text(),'Create Location')])[1]")).click();
        DelayUtils.sleep(600);
        Wizard locationWizard = inventoryViewPage.getWizard();
        Input type = locationWizard.getComponent("type", ComponentType.COMBOBOX);
        type.setSingleStringValue("Building");
        DelayUtils.sleep(1000);
        locationWizard.clickNext();
        DelayUtils.sleep(2000);
        locationWizard.cancel(); //something is NO YES
        DelayUtils.sleep(4000);
    }

    @Test
    public void changeTabAndSelectFirstRow() { //
        //TODO: uwzględnić notatki z AdvancedSearch.java, nie działa dopóki nie bedzei widgetID - wtedy poprawiać
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        tableWidget.selectFirstRow();

        TabsWidget tabsWidget = inventoryViewPage.getTabsWidget();
        tabsWidget.callActionByLabel("Locations");

        DelayUtils.sleep(500);
//        tabsWidget
//                .getCurrentTabTable()
//                .clickFirstRow();
        DelayUtils.sleep(50);
    }

    @Test
    public void checkDisplayingOfPropertyValue(){
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        String idNumberFromTableWidget = tableWidget.getValueFromNthRow("ID", "1");
        tableWidget.selectFirstRow();
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel();
        String idNumberFromPropertiesTab = propertyPanel.getPropertyValue("id");
        Assert.assertEquals(idNumberFromTableWidget, idNumberFromPropertiesTab);
    }

    @Test
    public void setProperties(){
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        tableWidget.selectFirstRow();
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel();
        PropertiesFilter propertiesFilter = inventoryViewPage.getPropertiesFilter();
        String defaultFirstPropertyLabel = propertyPanel.getNthPropertyLabel(1);
        String defaultSecondPropertyLabel = propertyPanel.getNthPropertyLabel(2);
        propertiesFilter.clickOnFilterIcon();
        boolean defaultChbxStatusForFirstLabel = propertiesFilter.isCheckboxChecked(defaultFirstPropertyLabel);
        propertiesFilter.clickCheckbox(defaultFirstPropertyLabel);
        boolean chbxStatusForFirstLabel = propertiesFilter.isCheckboxChecked(defaultFirstPropertyLabel);
        Assert.assertEquals(defaultChbxStatusForFirstLabel, !chbxStatusForFirstLabel);
        propertiesFilter.clickOnSave();
        String firstLabel = propertyPanel.getNthPropertyLabel(1);
        Assert.assertEquals(firstLabel, defaultSecondPropertyLabel);
        propertiesFilter.clickOnFilterIcon();
        propertiesFilter.clickCheckbox(defaultFirstPropertyLabel);
        chbxStatusForFirstLabel = propertiesFilter.isCheckboxChecked(defaultFirstPropertyLabel);
        Assert.assertEquals(defaultChbxStatusForFirstLabel, chbxStatusForFirstLabel);
        propertiesFilter.clickOnSave();
        firstLabel = propertyPanel.getNthPropertyLabel(1);
        Assert.assertEquals(firstLabel, defaultFirstPropertyLabel);
    }

    @Test
    public void searchProperty(){
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        tableWidget.selectFirstRow();
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel();
        PropertiesFilter propertiesFilter = inventoryViewPage.getPropertiesFilter();
        String defaultFirstPropertyLabel = propertyPanel.getNthPropertyLabel(1);
        String defaultSecondPropertyLabel = propertyPanel.getNthPropertyLabel(2);
        propertiesFilter.clickOnFilterIcon();
        String defaultFirstPropertyLabelOnPopup = propertiesFilter.getNthPropertyChbxLabelFromPopup(1);
        String defaultSecondPropertyLabelOnPopup = propertiesFilter.getNthPropertyChbxLabelFromPopup(2);
        Assert.assertEquals(defaultFirstPropertyLabel,defaultFirstPropertyLabelOnPopup);
        Assert.assertEquals(defaultSecondPropertyLabel,defaultSecondPropertyLabelOnPopup);
        propertiesFilter.typeIntoSearch(defaultSecondPropertyLabelOnPopup);
        String firstPropertyLabelOnPopup = propertiesFilter.getNthPropertyChbxLabelFromPopup(1);
        Assert.assertEquals(firstPropertyLabelOnPopup, defaultSecondPropertyLabelOnPopup);
    }
}
