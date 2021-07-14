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
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Movie");
    }

    @Test(priority = 1)
    public void checkDisplayingOfPropertyValue() {
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(0, PROPERTY_PANEL_ID);

        String idNumberFromPropertiesTab = propertyPanel.getPropertyValue("identifier");
        String idNumberFromTableWidget = inventoryViewPage.getAttributeValue("identifier", 0);
        Assert.assertEquals(idNumberFromTableWidget, idNumberFromPropertiesTab);

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test(priority = 2)
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

    @Test(priority = 3)
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

    @Test(priority = 4)
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