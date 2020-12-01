package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
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
        TableWidget tableWidget = inventoryViewPage.getMainTable();
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
        // TODO: add test for log text box
    }

    @Test
    public void changeColumnsOrder() {
        // TODO: add change columns order test
    }

    @Test
    public void checkPagination() {
      //TODO: add pagination test
    }

    @Test
    public void checkHorizontalScroller() {
//        TableWidget tableWidget = inventoryViewPage.getMainTable();
//        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
//        tableWidget.scrollHorizontally(1000);
//        List<String> headers = tableWidget.getActiveColumns();
//        Assert.assertEquals(headers.get(headers.size() - 1), "Calculated Parents");
//        tableWidget.scrollHorizontally(-1000);
//        headers = tableWidget.getActiveColumns();
//        Assert.assertEquals(headers.get(0), "ID");
    }

    @Test
    public void checkVerticalScroller() {
//        TableWidget tableWidget = inventoryViewPage.getMainTable();
//        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
//        tableWidget.scrollVertically(1000);
//        String lastVisibleID = tableWidget.getValueFromNthRow("ID", "last()");
//        String lastIdOnThePage = tableWidget.getValueFromRowWithID("ID", lastVisibleID);
//        Assert.assertEquals(lastVisibleID.equals(lastIdOnThePage), true);
//        tableWidget.scrollVertically(-1000);
//        lastVisibleID = tableWidget.getValueFromNthRow("ID", "last()");
//        Assert.assertEquals(lastVisibleID.equals(lastIdOnThePage), false);
    }

    @Test
    public void findByText() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        String secondID = tableWidget.getValueFromNthRow("XId", 2);
        tableWidget.typeIntoSearch(secondID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newFirstID = tableWidget.getValueFromNthRow("XId", 1);
        Assert.assertEquals(secondID, newFirstID);
        Assert.assertEquals(tableWidget.getRowsNumber(), 1);
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        tableWidget.callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
        Assert.assertTrue(inventoryViewPage.isLoadBarDisplayed());
    }

    @Test
    public void changeTabAndSelectFirstRow() {
       //TODO: rewrite this
    }

    @Test
    public void checkDisplayingOfPropertyValue(){
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        String idNumberFromTableWidget = tableWidget.getValueFromNthRow("XId", 1);
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