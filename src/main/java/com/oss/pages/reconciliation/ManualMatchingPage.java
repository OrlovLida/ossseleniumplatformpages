package com.oss.pages.reconciliation;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class ManualMatchingPage extends BasePage {

    private static final String TABLE_ID = "ActiveMatchingTableTabApp";
    private static final String NETWORK_NAME_ID = "networkName";
    private static final String CREATE_BUTTON_ID = "createMatchingApplicationActionId";
    private static final String DELETE_BUTTON_ID = "deleteMatchingApplicationActionId";
    private static final String EDIT_BUTTON_ID = "editMatchingApplicationActionId";
    private static final String TECHNICAL_OBJECT_STRUCTURE_ID = "networkInventoryMatchingNetworkDiscoveryControlViewActionId";
    private static final String TECHNICAL_OBJECT_VIEWER_ID = "networkInventoryMatchingTechnicalObjectViewActionId";
    private static final String INVENTORY_VIEW_ID = "networkInventoryMatchingNetworkInventoryViewActionId";
    private static final String TABLE_WIDGET_ID = "networkInventoryMatchingWindowId";
    private static final String DELETE_WIZARD_ID = "DeleteMatchingWizardConfig";
    private static final String DELETE_BUTTON_FROM_WIZARD_ID = "wizard-submit-button-DeleteMatchingWizardConfig";

    public ManualMatchingPage(WebDriver driver) {
        super(driver);
    }

    public static ManualMatchingPage goToManualMatchingPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/nar/inventory/integration/network-inventory-matching-view" +
                "?perspective=LIVE&withRemoved=true", basicURL));
        return new ManualMatchingPage(driver);
    }

    public static ManualMatchingPage getManualMatchingPage(WebDriver driver) {
        return new ManualMatchingPage(driver);
    }

    public void clickCreate() {
        getOldTable().callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_BUTTON_ID);
    }

    public void searchByNetworkName(String networkName) {
        getOldTable().searchByAttribute(NETWORK_NAME_ID, networkName);
    }

    public boolean isTableEmpty() {
        return getOldTable().hasNoData();
    }

    public void selectRow(int index) {
        getOldTable().selectRow(index);
    }

    public List<String> getColumnHeaders() {
        return getOldTable().getColumnsHeaders();
    }

    public void deleteMatching() {
        getOldTable().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_BUTTON_ID);
        Wizard wizard = Wizard.createByComponentId(driver, wait, DELETE_WIZARD_ID);
        wizard.clickButtonById(DELETE_BUTTON_FROM_WIZARD_ID);
    }

    public void editMatching() {
        getOldTable().callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_BUTTON_ID);
    }

    public String getCellValue(int index, String attributeLabel) {
        return getOldTable().getCellValue(index, attributeLabel);
    }

    public void showOnTechnicalObjectStructure() {
        getOldTable().callAction(ActionsContainer.SHOW_ON_GROUP_ID, TECHNICAL_OBJECT_STRUCTURE_ID);
    }

    public void showOnTechnicalObjectViewer() {
        getOldTable().callAction(ActionsContainer.SHOW_ON_GROUP_ID, TECHNICAL_OBJECT_VIEWER_ID);
    }

    public void showOnInventoryView() {
        getOldTable().callAction(ActionsContainer.SHOW_ON_GROUP_ID, INVENTORY_VIEW_ID);
    }

    private OldTable getOldTable() {
        return OldTable.createById(driver, wait, TABLE_ID);
    }
}
