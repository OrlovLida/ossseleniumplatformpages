package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicensesListPage extends BasePage {

    private static final String REGULATORY_LICENSES_TABLE_ID = "tableApp";
    private static final String CREATE_BUTTON_DATA_ATTRIBUTENAME = "create";

    public RegulatoryLicensesListPage(WebDriver driver) {
        super(driver);
    }

    private OldTable getTableWidget(String tableId) {
        return OldTable.createById(driver, wait, tableId);
    }

    @Step("Click create button")
    public RegulatoryLicenseWizardPage clickCreate() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable regulatoryLicensesTable = getTableWidget(REGULATORY_LICENSES_TABLE_ID);
        regulatoryLicensesTable.callAction(CREATE_BUTTON_DATA_ATTRIBUTENAME);

        return new RegulatoryLicenseWizardPage(driver);
    }

    @Step("Redirect to Regulatory License Overview")
    public void openRegulatoryLicenseOverview() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
