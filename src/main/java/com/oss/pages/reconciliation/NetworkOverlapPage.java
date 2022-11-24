package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NetworkOverlapPage extends BasePage {

    private static final String RESOLVE = "cmInventoryIntegration_CONFLICTActionRESOLVEId";
    private static final String REOPEN = "cmInventoryIntegration_CONFLICTActionREOPENId";
    private static final String CONFLICTED_OBJECTS_TAB = "ConflictTabViewApp";
    private static final String EDITABLE_LIST_ID = "ExtendedList-ConflictedObjectEditableListTabApp";
    private static final String NETWORK_ELEMENT_NAME = "name";
    private static final String STATUS_LABEL = "Status";
    private static final String COMMENT_LABEL = "Comment";
    private static final String DOMAIN_HEADER_ID = "overlapCmDomain";
    private static final String ARCHIVE_CONFLICT_ID = "ARCHIVE_CONFLICT";
    private static final String CM_DOMAIN_VIEW_ACTION_ID = "overlapCmDomainViewActionId";
    private String conflictTab = "CurrentConflictTableTabApp";

    protected NetworkOverlapPage(WebDriver driver) {
        super(driver);
    }

    public static NetworkOverlapPage goToNetworkOverlapPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/nar/inventory/integration/network-overlap-view" +
                "?perspective=LIVE&withRemoved=true", basicURL));
        return new NetworkOverlapPage(driver);
    }

    @Step("Get conflict status for given rowIndex")
    public String getConflictStatus(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, STATUS_LABEL);
    }

    @Step("Get conflict comment")
    public String getConflictComment(int rowIndex) {
        return getOldTable().getCellValue(rowIndex, COMMENT_LABEL);
    }

    @Step("Select row with given index")
    public void selectConflict(int rowIndex) {
        getOldTable().selectRow(rowIndex);
    }

    @Step("click Resolve button")
    public void clickResolve() {
        getTabsInterface().callActionById(RESOLVE);
    }

    public void goToCmDomainView() {
        EditableList.Row getFirstRow = getConflictedObjects().getRow(0);
        getFirstRow.click();
        getFirstRow.callAction(ActionsContainer.SHOW_ON_GROUP_ID, CM_DOMAIN_VIEW_ACTION_ID);
    }

    @Step("click Reopen button")
    public void reopenConflict() {
        getTabsInterface().callActionById(REOPEN);
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel("Accept");
    }

    @Step("Get Domain Name from Conflicted Objects tab")
    public String getDomainFromConflictedObjectsTab(int rowIndex) {
        return getConflictedObjects().getRow(rowIndex).getCellValue(DOMAIN_HEADER_ID);
    }

    @Step("Search for object by name")
    public void searchByObjectName(String name) {
        getOldTable().searchByAttribute(NETWORK_ELEMENT_NAME, name);
    }

    @Step("Open archive tab")
    public void openArchiveTab() {
        getTabsInterface().selectTabById(ARCHIVE_CONFLICT_ID);
        conflictTab = "ArchiveConflictTableTabApp";
    }

    private EditableList getConflictedObjects() {
        return EditableList.createById(driver, wait, EDITABLE_LIST_ID);
    }

    private OldTable getOldTable() {
        return OldTable.createById(driver, wait, conflictTab);
    }

    private TabsInterface getTabsInterface() {
        return TabsWidget.createById(driver, wait, CONFLICTED_OBJECTS_TAB);
    }
}
