package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;
import com.oss.pages.transport.VRF.VRFWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.configuration.Configuration.CONFIGURATION;

    public class RegulatoryLicensesListPage extends BasePage {

    public static final String BASIC_URL = CONFIGURATION.getUrl();

    private static final String REGULATORY_LICENSES_TABLE_ID = "tableApp";
    private static final String CREATE_BUTTON_DATA_ATTRIBUTENAME = "create";

    public WebDriverWait webDriverWait;

    public RegulatoryLicensesListPage(WebDriver driver) {
        super(driver);
    }

    private OldTable getTableWidget(String tableId){
        return OldTable.createByComponentDataAttributeName(driver, wait, tableId);
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
