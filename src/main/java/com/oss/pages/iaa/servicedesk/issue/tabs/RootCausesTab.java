package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;

public class RootCausesTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RootCausesTab.class);

    private static final String ROOT_CAUSES_TABLE_ID = "_rootCausesApp";
    private static final String ROOT_CAUSES_SERVICES_TABLE_ID = "_rootCausesAppTreeTable";
    private static final String ADD_ROOT_CAUSE_ID = "_addRootCause";
    private static final String ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID = "MO Identifier";

    public RootCausesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Check Root Cause Tab")
    public boolean isRootCauseTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getRootCauseTable().hasNoData();
    }

    @Step("Select Mo in Root Causes Tab")
    public void selectMOInRootCausesTab(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Selecting MO in Root Cause Table");
        getRootCauseTable().selectRow(index);
    }

    @Step("Check if Root Causes Tree Table is not empty")
    public boolean checkIfRootCausesTreeTableIsNotEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check if Root Causes Tree Table is not empty");
        return !OldTable.createById(driver, wait, ROOT_CAUSES_SERVICES_TABLE_ID).hasNoData();
    }

    @Step("I open add root cause wizard")
    public SDWizardPage openAddRootCauseWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(ADD_ROOT_CAUSE_ID);
        log.info("Add Root Cause Wizard is opened");
        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    public boolean isAddRootCauseButtonActive() {
        return isActionPresent(ADD_ROOT_CAUSE_ID);
    }

    @Step("I check if MO Identifier is present on Root Causes tab")
    public boolean checkIfMOIdentifierIsPresentOnRootCauses(String moIdentifier) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if MO Identifier is present on Root Causes tab");
        for (int row = 0; row < getNumberOfRowsInRootCauseTable(); row++) {
            if (checkRootCauseMOIdentifier(row).equals(moIdentifier)) {
                return true;
            }
        }
        return false;
    }

    private int getNumberOfRowsInRootCauseTable() {
        return getRootCauseTable().countRows(ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID);
    }

    private String checkRootCauseMOIdentifier(int row) {
        return getRootCauseTable().getCellValue(row, ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID);
    }

    private OldTable getRootCauseTable() {
        return OldTable.createById(driver, wait, ROOT_CAUSES_TABLE_ID);
    }
}
