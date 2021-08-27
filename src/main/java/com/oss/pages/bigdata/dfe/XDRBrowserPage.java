package com.oss.pages.bigdata.dfe;

import com.oss.framework.components.common.TimePeriodChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XDRBrowserPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(XDRBrowserPage.class);

    private final String ETL_NAME_COMBOBOX_ID = "etlProcessId-input";
    private final String TIME_PERIOD_ID = "etlTime";
    private final String SEARCH_LABEL = "Search";
    private final String XDR_TABLE_ID = "xdrTableId";
    private final String EXPORT_BUTTON = "tableExportButton";

    public XDRBrowserPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open XDR Browser View")
    public static XDRBrowserPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "xdr-browser");
        return new XDRBrowserPage(driver, wait);
    }

    @Step("I select ETL name from Combobox")
    public void selectETLName(String etlName) {
        ComponentFactory.create(ETL_NAME_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).
                setSingleStringValue(etlName);
        log.info("I select ETL: {}", etlName);
    }

    @Step("I set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        DelayUtils.waitForPageToLoad(driver, wait);
        TimePeriodChooser timePeriodChooser = TimePeriodChooser.create(driver, wait, TIME_PERIOD_ID);
        timePeriodChooser.clickClearValue();
        timePeriodChooser.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        timePeriodChooser.setLastPeriod(days, hours, minutes);
    }

    @Step("I click Search")
    public void clickSearch() {
        ButtonContainer.create(driver, wait).callActionByLabel(SEARCH_LABEL);
        log.info("Searching for ETL");
    }

    @Step("I check if XDR Table is empty")
    public boolean checkIfTableIsEmpty() {
        return TableWidget.createById(driver, XDR_TABLE_ID, wait).hasNoData();
    }

    @Step("I click on kebab menu and Export button")
    public void clickExport() {
        TableWidget.createById(driver, XDR_TABLE_ID, wait).callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON);
    }


    @Step("I click on notification icon")
    public void clickOnNotificationIcon() {
        //Notifications.create(driver, wait).
    }

    @Override
    public String getTableId() {
        return null;
    }

    @Override
    public String getContextActionAddLabel() {
        return null;
    }

    @Override
    public String getContextActionEditLabel() {
        return null;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return null;
    }

    @Override
    public String getSearchId() {
        return null;
    }
}
