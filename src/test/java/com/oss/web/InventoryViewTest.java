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

    private final static String PROPERTY_PANEL_ID = "InventoryView_DetailsTab_LocationInventoryView_PropertyPanelWidget_Building";

    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
    }

    @Test
    public void searchByType() {
        //when
        Multimap<String,String> filterValues = inventoryViewPage.searchByAttributeValue("type", "PoP", ComponentType.COMBOBOX);

        //then
        Assertions.assertThat(filterValues.keys()).hasSize(1);
        Assertions.assertThat(Lists.newArrayList(filterValues.get("Object Type")).get(0)).startsWith("PoP");

        inventoryViewPage.clearFilters();
    }

    @Test
    public void selectFirstRow() {
        //when
        inventoryViewPage.selectObjectByRowId(0);
        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();

        //then
        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedRows.get(0).getIndex()).isEqualTo(0);

        inventoryViewPage.unselectObjectByRowId(0);
        selectedRows = inventoryViewPage.getSelectedRows();
        Assertions.assertThat(selectedRows).hasSize(0);
    }

    @Test
    public void resizeColumn() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
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
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();

        DelayUtils.sleep(DelayUtils.HUMAN_REACTION_MS);
        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);

        inventoryViewPage.removeColumn(secondHeader);

        List<String> newColumnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        String newFirstHeader = newColumnHeaders.get(0);
        String newSecondHeader = newColumnHeaders.get(1);

        Assert.assertEquals(firstHeader, newFirstHeader);
        Assert.assertEquals(thirdHeader, newSecondHeader);
    }

    @Test
    public void addFirstUnselectedColumn() {
        //given
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(2);

        //when
        inventoryViewPage.removeColumn(firstHeader);
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).doesNotContain(firstHeader);

        inventoryViewPage.enableColumnAndApply(firstHeader);

        //then
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
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
        String secondID = inventoryViewPage.getAttributeValue("XId", 1);
        inventoryViewPage.searchObject(secondID);
        String newFirstID = inventoryViewPage.getAttributeValue("XId", 0);
        Assert.assertEquals(secondID, newFirstID);
        Assert.assertEquals(inventoryViewPage.getRowsNumber(), 1);

        inventoryViewPage.clearFilters();
    }


    @Test
    public void changeTab() {
        inventoryViewPage.selectObjectByRowId(0);

        String activeTabLabel = inventoryViewPage.getActiveTabLabel();
        String secondTabLabel = inventoryViewPage.getTabLabel(1);
        Assertions.assertThat(activeTabLabel).isNotEqualTo(secondTabLabel);

        inventoryViewPage.selectTabByLabel(secondTabLabel);
        String newActiveLabel = inventoryViewPage.getActiveTabLabel();
        Assertions.assertThat(newActiveLabel).isEqualTo(secondTabLabel);

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test
    public void checkDisplayingOfPropertyValue(){
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID);

        String idNumberFromPropertiesTab = propertyPanel.getPropertyValue("id");
        String idNumberFromTableWidget = inventoryViewPage.getAttributeValue("XId", 0);
        Assert.assertEquals(idNumberFromTableWidget, idNumberFromPropertiesTab);

        inventoryViewPage.unselectObjectByRowId(0);
    }
    @Test
    public void propertyPanelAttributesChooserCheck(){
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID);
        List<String> labels = propertyPanel.getPropertyLabels();
        String firstProperty = labels.get(0);

        propertyPanel.disableAttributeByLabel(firstProperty);

        List<String> labelsAfterDisable = propertyPanel.getPropertyLabels();
        Assertions.assertThat(labelsAfterDisable).doesNotContain(firstProperty);

        propertyPanel.enableAttributeByLabel(firstProperty);

        List<String> labelsAfterEnable = propertyPanel.getPropertyLabels();
        Assertions.assertThat(labelsAfterEnable).contains(firstProperty);

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test
    public void searchProperty(){
       //TODO:
    }
}