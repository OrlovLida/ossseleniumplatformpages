package com.oss.web;

import com.oss.BaseTestCase;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.platform.NewInventoryViewPage;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class PropertyPanelTest extends BaseTestCase {
    private final static String PROPERTY_PANEL_ID = "PropertyPanelWidget";
    private final static int ROW_ID = 0;

    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        String TYPE = "TestMovie";
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TYPE);
    }

    @Test(priority = 1)
    public void checkDisplayingOfPropertyValue() {
        String propertyName = "id";

        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        String idNumberFromPropertiesTab = propertyPanel.getPropertyValue(propertyName);
        String idNumberFromTableWidget = inventoryViewPage.getAttributeValue(propertyName, ROW_ID);
        Assert.assertEquals(idNumberFromTableWidget, idNumberFromPropertiesTab);

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test(priority = 2)
    public void propertyPanelAttributesChooserCheck() {
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        List<String> labels = propertyPanel.getPropertyLabels();
        String firstProperty = labels.get(0);

        propertyPanel.disableAttributeByLabel(firstProperty);

        List<String> labelsAfterDisable = propertyPanel.getPropertyLabels();
        Assertions.assertThat(labelsAfterDisable).doesNotContain(firstProperty);

        propertyPanel.enableAttributeByLabel(firstProperty);

        List<String> labelsAfterEnable = propertyPanel.getPropertyLabels();
        Assertions.assertThat(labelsAfterEnable).contains(firstProperty);

        inventoryViewPage.unselectObjectByRowId(ROW_ID);
    }

    @Test(priority = 3)
    public void searchProperty() {
        String propertyName = "id";

        String name = inventoryViewPage.getAttributeValue(propertyName, ROW_ID);
        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(ROW_ID, PROPERTY_PANEL_ID);
        propertyPanel.fullTextSearch(name);
        Assert.assertTrue(propertyPanel.getVisibleAttributes().contains(propertyName));
        for (String attribute : propertyPanel.getVisibleAttributes()) {
            Assert.assertTrue(propertyPanel.getPropertyValue(attribute).contains(name));
        }

        inventoryViewPage.unselectObjectByRowId(0);
    }

    @Test(priority = 4)
    public void hideEmpty() {
        String emptyValue = "—";

        PropertyPanel propertyPanel = inventoryViewPage.getPropertyPanel(10, PROPERTY_PANEL_ID);
        propertyPanel.hideEmpty();

        for (String attribute : propertyPanel.getVisibleAttributes()) {
            Assertions.assertThat(propertyPanel.getPropertyValue(attribute)).doesNotContain(emptyValue);
        }

        propertyPanel.showEmpty();
        inventoryViewPage.unselectObjectByRowId(0);
    }
}
