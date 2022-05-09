package com.oss.pages.reconciliation;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class VS20Page extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(VS20Page.class);

    private static final String TABLE_WIDGET = "InventoryView_MainWidget_VS_Object";
    private static final String CM_DOMAIN_NAME = "cmDomainName";
    private static final String DISTINGUISH_NAME = "distinguishName";
    private static final String NATIVE_TYPE = "nativeType";
    private static final String PROPERTY_PANEL_ID = "PropertyPanel";
    private static final String NAVIGATION_ID = "NAVIGATION";
    SoftAssert softAssert = new SoftAssert();


    public VS20Page(WebDriver driver) {
        super(driver);
    }

    public static VS20Page goToVS20Page(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/VS_Object" +
                "?perspective=NETWORK", basicURL));
        return new VS20Page(driver);
    }

    @Step("Search for item by CMDomain name")
    public void searchItemByCMDomainName(String cmDomainName) {
        TableWidget tableWidget = getTableWidget();
        tableWidget.searchByAttribute(CM_DOMAIN_NAME, Input.ComponentType.TEXT_FIELD, cmDomainName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Search for item by distinguish name")
    public void searchItemByDistinguishName(String distName) {
        TableWidget tableWidget = getTableWidget();
        tableWidget.searchByAttribute(DISTINGUISH_NAME, Input.ComponentType.TEXT_FIELD, distName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Search for item by type")
    public void searchItemByType(String objectType) {
        TableWidget tableWidget = getTableWidget();
        tableWidget.searchByAttribute(NATIVE_TYPE, Input.ComponentType.TEXT_FIELD, objectType);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickFirstItem() {
        TableWidget tableWidget = getTableWidget();
        tableWidget.clickRow(0);
    }

    @Step("Go to Network Discovery Control View")
    public void goToNDCV() {
        TableWidget tableWidget = getTableWidget();
        tableWidget.callAction(NAVIGATION_ID);
    }

    @Step("Get value of property")
    public String getPropertyValue() {
        PropertyPanel propertyPanel = getPropertyPanel();
        return propertyPanel.getPropertyValue(CM_DOMAIN_NAME);
    }

    @Step("Assert all properties of VS")
    public void assertProperties(List<String> assertionList) {
        List<String> attributes = getPropertiesToList();
        System.out.println("Przed asercja");
        System.out.println(attributes.size());
        System.out.println(assertionList.size());
        Assert.assertEquals(attributes.size(), assertionList.size());
        System.out.println("Po asercja");
        for (int i=0; i < attributes.size(); i++) {
            log.info("Checking attribute with index: " + i + ", which equals: " + assertionList.get(i) + " on declared assertionList");
            System.out.println("Checking attribute with index: " + i + ", which equals: " + assertionList.get(i) + " on declared assertionList");
            softAssert.assertEquals(getPropertyValue(attributes.get(i)), assertionList.get(i));
        }
        softAssert.assertAll();
    }

    @Step("Get all properties to list")
    public List<String> getPropertiesToList() {
        PropertyPanel propertyPanel = getPropertyPanel();
        return propertyPanel.getVisibleAttributes();
    }

    @Step("Get property value")
    public String getPropertyValue(String propertyName) {
        PropertyPanel propertyPanel = getPropertyPanel();
        return propertyPanel.getPropertyValue(propertyName);
    }

    private PropertyPanel getPropertyPanel() {
        return PropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, TABLE_WIDGET, wait);
    }

}
