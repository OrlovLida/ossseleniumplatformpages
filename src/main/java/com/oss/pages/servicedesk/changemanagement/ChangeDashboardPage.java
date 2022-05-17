package com.oss.pages.servicedesk.changemanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.servicedesk.issue.BaseDashboardPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_DASHBOARD;

public class ChangeDashboardPage extends BaseDashboardPage {

    private static final String CHANGE_WIZARD_ID = "TT_WIZARD";
    private static final String NORMAL_BUTTON_ID = "Normal";
    private static final String CREATE_CHANGE_BUTTON_ID = "CM_WIZARD_CREATE_TITLE";
    private static final String CHANGES_TABLE_ID = "_tableChanges";
    private static final String REQUESTER_ATTRIBUTE = "Requester";
    private static final String DESCRIPTION_ATTRIBUTE = "Incident Description";

    public ChangeDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public ChangeDashboardPage goToPage(WebDriver driver, String basicURL) {
        openPage(driver, getDashboardURL(basicURL, CHANGE_DASHBOARD));

        return new ChangeDashboardPage(driver, wait);
    }

    public SDWizardPage openCreateChangeWizard() {
        return openCreateWizard(NORMAL_BUTTON_ID, CREATE_CHANGE_BUTTON_ID, CHANGE_WIZARD_ID);
    }

    @Step("Check if change with {description} is in the table")
    public boolean isChangeCreated(String description) {
        return getTable().getRowNumber(description, DESCRIPTION_ATTRIBUTE) >= 0;
    }

    public String getRequesterFromNthRow(int row) {
        return getAttributeFromTable(row, REQUESTER_ATTRIBUTE);
    }

    @Override
    protected String getTableID() {
        return CHANGES_TABLE_ID;
    }
}
