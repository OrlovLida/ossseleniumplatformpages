package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tablewidget.TableRow;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;

public class InventoryViewTest extends BaseTestCase {

    private final static String PROPERTY_PANEL_ID = "PropertyPanelWidget";

    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
    }

    @Test(priority = 1)
    public void searchByType() {
        // when
        Multimap<String, String> filterValues = inventoryViewPage.searchByAttributeValue("type", "PoP", ComponentType.TEXT_FIELD);

        // then
        Assertions.assertThat(filterValues.keys()).hasSize(1);
        Assertions.assertThat(Lists.newArrayList(filterValues.get("Object Type")).get(0)).startsWith("PoP");

        inventoryViewPage.clearFilters();
    }

    @Test(priority = 2)
    public void selectFirstRow() {
        // when
        inventoryViewPage.selectObjectByRowId(0);
        List<TableRow> selectedRows = inventoryViewPage.getSelectedRows();

        // then
        Assertions.assertThat(selectedRows).hasSize(1);
        Assertions.assertThat(selectedRows.get(0).getIndex()).isEqualTo(0);

        inventoryViewPage.unselectObjectByRowId(0);
        selectedRows = inventoryViewPage.getSelectedRows();
        Assertions.assertThat(selectedRows).hasSize(0);
    }

    @Test(priority = 3)
    public void findByText() {
        String secondID = inventoryViewPage.getAttributeValue("id", 1);
        inventoryViewPage.searchObject(secondID);
        String newFirstID = inventoryViewPage.getAttributeValue("id", 0);
        Assert.assertEquals(secondID, newFirstID);
        Assert.assertEquals(inventoryViewPage.getRowsNumber(), 1);

        inventoryViewPage.clearFilters();
    }

    @Test(priority = 4)
    public void checkDisplayingOfPropertyValue() {
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID);

        String idNumberFromPropertiesTab = propertyPanel.getPropertyValue("identifier");
        String idNumberFromTableWidget = inventoryViewPage.getAttributeValue("identifier", 0);
        Assert.assertEquals(idNumberFromTableWidget, idNumberFromPropertiesTab);

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test(priority = 5)
    public void propertyPanelAttributesChooserCheck() {
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

    @Test(priority = 6)
    public void searchProperty() {
        inventoryViewPage.selectObjectByRowId(0);
        String name = inventoryViewPage.getAttributeValue("identifier", 0);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID);
        propertyPanel.fullTextSearch(name);
        Assert.assertTrue(propertyPanel.getPropertyLabels().contains("Identifier"));
        for (String attribute : propertyPanel.getVisibleAttributes()) {
            Assert.assertTrue(propertyPanel.getPropertyValue(attribute).contains(name));
        }

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test(priority = 7)
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

        tableWidget.resizeFirstColumn(-offset);
        newSize = tableWidget.getFirstColumnSize();
        Assertions.assertThat(defaultSize).isEqualTo(newSize);
    }

    @Test(priority = 9)
    public void addFirstUnselectedColumn() {
        // given
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        String firstHeader = columnHeaders.get(1);

        //when
        inventoryViewPage.removeColumn(firstHeader);
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).doesNotContain(firstHeader);

        inventoryViewPage.enableColumnAndApply(firstHeader);

        // then
        columnHeaders = inventoryViewPage.getActiveColumnsHeaders();
        Assertions.assertThat(columnHeaders).contains(firstHeader);
    }

    @Test(priority = 10)
    public void changeColumnsOrder() {
        List<String> columnHeaders = inventoryViewPage.getActiveColumnsHeaders();

        String firstHeader = columnHeaders.get(0);
        String secondHeader = columnHeaders.get(1);
        String thirdHeader = columnHeaders.get(2);

        inventoryViewPage.changeColumnsOrderInMainTable(firstHeader, 2);

        List<String> newHeaders = inventoryViewPage.getActiveColumnsHeaders();

        Assertions.assertThat(newHeaders.indexOf(firstHeader)).isEqualTo(2);
        Assertions.assertThat(newHeaders.indexOf(secondHeader)).isEqualTo(0);
        Assertions.assertThat(newHeaders.indexOf(thirdHeader)).isEqualTo(1);

    }

    @Test(priority = 11)
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

}