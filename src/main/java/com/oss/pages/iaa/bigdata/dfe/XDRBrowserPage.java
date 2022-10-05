package com.oss.pages.iaa.bigdata.dfe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.widgets.table.TableWidget;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.sleep;
import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class XDRBrowserPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(XDRBrowserPage.class);

    private static final String ETL_NAME_COMBOBOX_ID = "etlProcessId";
    private static final String TIME_PERIOD_ID = "input_etlTime";
    private static final String SEARCH_BUTTON_ID = "Search_Button-0";
    private static final String XDR_TABLE_ID = "xdrTableId";
    private static final String EXPORT_BUTTON = "tableExportButton";

    public XDRBrowserPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open XDR Browser View")
    public static XDRBrowserPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        openDfePage(driver, basicURL, wait, "xdr-browser");
        return new XDRBrowserPage(driver, wait);
    }

    @Step("I select ETL name from Combobox")
    public void selectETLName(String etlName) {
        ComponentFactory.create(ETL_NAME_COMBOBOX_ID, driver, wait)
                .setSingleStringValue(etlName);
        log.info("I select ETL: {}", etlName);
    }

    @Step("I check value of ETL name in XDR Browser search Combobox")
    public String getETLName() {
        return ComponentFactory.create(ETL_NAME_COMBOBOX_ID, driver, wait).getStringValue();
    }

    @Step("I set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        waitForPageToLoad(driver, wait);
        getTimePeriodChooser().clickClearValue();
        getTimePeriodChooser().chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        getTimePeriodChooser().setLastPeriod(days, hours, minutes);
    }

    private TimePeriodChooser getTimePeriodChooser() {
        return TimePeriodChooser.create(driver, wait, TIME_PERIOD_ID);
    }

    @Step("I click Search")
    public void clickSearch() {
        waitForPageToLoad(driver, wait);
        Button.createById(driver, SEARCH_BUTTON_ID).click();
        waitForPageToLoad(driver, wait);
        sleep(3000);
        log.info("Searching for ETL");
    }

    @Step("I check if XDR Table is empty")
    public boolean checkIfTableIsEmpty() {
        return getXDRTable().hasNoData();
    }

    @Step("I click on kebab menu and Export button")
    public void clickExport() {
        getXDRTable().callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON);
    }

    @Step("I check if active filter contain {filter}")
    public boolean checkIfFilterExist(String filter) {
        Multimap<String, String> activeFilters = AdvancedSearch.createByWidgetId(driver, wait, getTableId()).getAppliedFilters();
        log.info("Checking if filter: {} is on the map", filter);
        return activeFilters.toString().contains(filter);
    }

    private TableWidget getXDRTable() {
        return TableWidget.createById(driver, getTableId(), wait);
    }

    @Override
    public String getTableId() {
        return XDR_TABLE_ID;
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
