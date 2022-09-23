package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkElementsDiscoveryPage extends BasePage {

    private static final String NE_DISCOVERY_OPERATION_WINDOW_ID = "neDiscoveryOperationWindowId";
    private static final String CREATE_NEW_OPERATION_ID = "neDiscovery_NE_DISCOVERY_OPERATIONActionCREATEId";
    private static final String NE_DISCOVERY_OPERATION_ENTRY_TABLE_APP = "neDiscoveryOperationEntryTableApp";
    private static final String DELETE_OPERATION_ID = "neDiscovery_NE_DISCOVERY_OPERATIONActionDELETEId";
    private static final String CONNECTION = "Connection";

    public NetworkElementsDiscoveryPage(WebDriver driver) {
        super(driver);
    }

    public static NetworkElementsDiscoveryPage goToNetworkElementsDiscoveryPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-element-discovery-view" +
                "?perspective=LIVE&withRemoved=true", basicURL));
        return new NetworkElementsDiscoveryPage(driver);
    }

    @Step("Search for element and select it by {elementName}")
    public void queryAndSelectObjectByName(String elementName) {
        searchForElement(elementName);
        DelayUtils.waitForPageToLoad(driver, wait);
        selectElement(elementName);
    }

    @Step("Check if operation exists")
    public boolean isOperationPresent(String operationName) {
        return getTreeView().isRowPresent(operationName);
    }

    @Step("Search for element by {elementName}")
    public void searchForElement(String elementName) {
        getTreeView().search(elementName);
    }

    @Step("Select element by {elementName}")
    public void selectElement(String elementName) {
        getTreeView().selectTreeRow(elementName);
    }

    @Step("Create new operation")
    public void createNewOperation() {
        getTreeView().callActionById(CREATE_NEW_OPERATION_ID);
    }

    @Step("Get IPAddress value by {rowIndex}")
    public String getIPAddressByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "IP Address");
    }

    @Step("Get Port value by {rowIndex}")
    public String getPortByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "Port");
    }

    @Step("Get State value by {rowIndex}")
    public String getStateByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "State");
    }

    @Step("Get Connection value by {rowIndex}")
    public String getConnectionByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, CONNECTION);
    }

    @Step("Go to connection")
    public void goToConnection() {
        getOldTable().clickLink(CONNECTION);
    }

    @Step("Delete entry")
    public void deleteOperation() {
        getTreeView().callActionById(DELETE_OPERATION_ID);
        ConfirmationBox.create(driver, wait).clickButtonByLabel("Delete");
    }

    public boolean isConnectionSetByRowIndex(int rowIndex) {
        String connection = getOldTable().getCellValue(rowIndex, CONNECTION);
        return !connection.isEmpty();
    }

    private TreeWidget getTreeView() {
        return TreeWidget.createById(driver, wait, NE_DISCOVERY_OPERATION_WINDOW_ID);
    }

    private OldTable getOldTable() {
        return OldTable.createById(driver, wait, NE_DISCOVERY_OPERATION_ENTRY_TABLE_APP);
    }

}
