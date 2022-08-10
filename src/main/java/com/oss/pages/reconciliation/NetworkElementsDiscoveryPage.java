package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkElementsDiscoveryPage extends BasePage {

    private static final String NE_DISCOVERY_OPERATION_WINDOW_ID = "neDiscoveryOperationWindowId";
    private static final String CREATE_NEW_ENTRY_ID = "neDiscovery_NE_DISCOVERY_OPERATIONActionCREATEId";
    private static final String NE_DISCOVERY_OPERATION_ENTRY_TABLE_APP = "neDiscoveryOperationEntryTableApp";
    private static final String DELETE_ENTRY_ID = "neDiscovery_NE_DISCOVERY_OPERATIONActionDELETEId";

    public NetworkElementsDiscoveryPage(WebDriver driver) {
        super(driver);
    }

    public static NetworkElementsDiscoveryPage goToNetworkElementsDiscoveryPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-element-discovery-view" +
        "?perspective=LIVE&withRemoved=true", basicURL));
        return new NetworkElementsDiscoveryPage(driver);
    }

    @Step("Search for element by {elementName}")
    public void searchForElement(String elementName) {
        getTreeView().search(elementName);
    }

    @Step("Select element by {elementName}")
    public void selectElement(String elementName) {
        getTreeView().selectTreeRow(elementName);
    }

    @Step("Create new entry")
    public void createNewEntry() {
        getTreeView().callActionById(CREATE_NEW_ENTRY_ID);
    }

    @Step("Get IPAddress value by {rowIndex}")
    public String getIPAddressByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "IP Address");
    }

    @Step("Get Port value by {rowIndex}")
    public String getPortByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "Port");
    }

    @Step("Get Pingable value by {rowIndex}")
    public String getPingableByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "Pingable");
    }

    @Step("Get State value by {rowIndex}")
    public String getStateByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "State");
    }

    @Step("Get Connection value by {rowIndex}")
    public String getConnectionByRowIndex(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, "Connection");
    }

    @Step("Go to connection")
    public void goToConnection() {
        getOldTable().clickLink("Connection");
    }

    @Step("Delete entry")
    public void deleteEntry() {
        getTreeView().callActionById(DELETE_ENTRY_ID);
        ConfirmationBox.create(driver, wait).clickButtonByLabel("Delete");
    }

    public boolean isConnectionSetByRowIndex(int rowIndex) {
        String connection = getOldTable().getCellValue(rowIndex, "Connection");
        return !connection.isEmpty();
    }

    private TreeWidget getTreeView() {
        return TreeWidget.createById(driver, wait, NE_DISCOVERY_OPERATION_WINDOW_ID);
    }

    private OldTable getOldTable() {
        return OldTable.createById(driver, wait, NE_DISCOVERY_OPERATION_ENTRY_TABLE_APP);
    }


}
