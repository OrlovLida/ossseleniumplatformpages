package com.oss.pages.iaa.bigdata.kqiview;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.iaa.widgets.dpe.toolbarpanel.ExportPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.KpiToolbarPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsSidePanel;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel.MiscellaneousOption.DATA_COMPLETENESS;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel.MiscellaneousOption.LAST_SAMPLE_TIME;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel.MiscellaneousOption.SHOW_TIME_ZONE;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel.TimePeriodChooserOption.LATEST;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel.TimePeriodChooserOption.SMART;
import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class KpiToolbarPanelPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KpiToolbarPanelPage.class);
    private static final String MANUAL_MODE = "Manual";

    private final KpiToolbarPanel kpiToolbarPanel;

    public KpiToolbarPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        kpiToolbarPanel = KpiToolbarPanel.create(driver, wait);
    }

    @Step("I apply changes")
    public void applyChanges() {
        log.info("Apply changes");
        kpiToolbarPanel.clickApply();
        waitForPageToLoad(driver, wait);
    }

    @Step("I export chart")
    public void exportChart() {
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportPanel.ExportType.JPG);
        log.info("Exporting chart to JPG");
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportPanel.ExportType.PNG);
        log.info("Exporting chart to PNG");
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportPanel.ExportType.PDF);
        log.info("Exporting chart to PDF");
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportPanel.ExportType.XLSX);
        log.info("Exporting chart to XLSX");
    }

    @Step("I change layout")
    public void changeLayout(LayoutPanel.LayoutType layoutType) {
        kpiToolbarPanel.openLayoutPanel().changeLayout(layoutType);
    }

    @Step("I set topN dimension in TopN panel")
    public void setTopNDimension(String dimensionId) {
        kpiToolbarPanel.openTopNPanel().setDimension(dimensionId);
    }

    @Step("I set topN level in TopN panel")
    public void setTopNLevel(String levelId) {
        kpiToolbarPanel.openTopNPanel().setLevel(levelId);
    }

    @Step("I click Perform in TopN panel")
    public void clickPerformTopN() {
        kpiToolbarPanel.openTopNPanel().clickPerform();
        waitForPageToLoad(driver, wait);
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setLastPeriodOption(days, hours, minutes);
    }

    @Step("Set SMART option in time period chooser")
    public void chooseSmartOptionInTimePeriodChooser() {
        log.info("Setting smart option in time period chooser");
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().chooseTimePeriodOption(SMART);
    }

    @Step("Set LATEST option in time period chooser")
    public void chooseLatestOptionInTimePeriodChooser() {
        log.info("Setting latest option in time period chooser");
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().chooseTimePeriodOption(LATEST);
    }

    @Step("I select Aggregation Method")
    public void selectAggregationMethod(OptionsPanel.AggregationMethodOption option) {
        log.info("Setting: {} option in aggregation method", option);
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().chooseAggregationMethodOption(option);
    }

    public void unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption option) {
        List<OptionsPanel.AggregationMethodOption> toUnselect = kpiToolbarPanel.openOptionsPanel().getActiveAggregationMethods();
        toUnselect.remove(option);
        for (OptionsPanel.AggregationMethodOption aggOption : toUnselect) {
            kpiToolbarPanel.openOptionsPanel().chooseAggregationMethodOption(aggOption);
        }
    }

    @Step("I enable Data Completeness option")
    public void enableDataCompleteness() {
        log.info("Enabling Data Completeness visibility");
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setMiscellaneousOption(DATA_COMPLETENESS);
    }

    @Step("I enable Last Sample Time option")
    public void enableLastSampleTime() {
        log.info("Enabling Last Sample Time visibility");
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setMiscellaneousOption(LAST_SAMPLE_TIME);
    }

    @Step("I enable Show Time Zone option")
    public void enableShowTimeZone() {
        log.info("Enabling Show Time Zone");
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setMiscellaneousOption(SHOW_TIME_ZONE);
    }

    @Step("I enable Compare with Other Period option")
    public void enableCompareWithOtherPeriod() {
        log.info("Enabling Compare with Other Period option");
        waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setOtherPeriodOption();
    }

    @Step("I check status of chosen layout button")
    public String layoutButtonStatus(LayoutPanel.LayoutType layout) {
        waitForPageToLoad(driver, wait);
        return kpiToolbarPanel.openLayoutPanel().chartLayoutButtonStatus(layout);
    }

    public String activeAggMethod() {
        waitForPageToLoad(driver, wait);
        return kpiToolbarPanel.openOptionsPanel().getActiveAggregationMethods().get(0).toString();
    }

    public int numberOfActiveAggMethods() {
        waitForPageToLoad(driver, wait);
        return kpiToolbarPanel.openOptionsPanel().getActiveAggregationMethods().size();
    }

    @Step("I close Options Panel")
    public void closeOptionsPanel() {
        kpiToolbarPanel.closeOptionsPanel();
    }

    @Step("I select display type from toolbar panel")
    public void setDisplayType(String displayTypeId) {
        kpiToolbarPanel.selectDisplayType(displayTypeId);
        waitForPageToLoad(driver, wait);
        log.info("Setting display type to: {}", displayTypeId);
    }

    private OptionsSidePanel getOptionsSidePanel() {
        return kpiToolbarPanel.openOptionsSidePanel();
    }

    @Step("Select Y axis: {yaxisName}")
    public void selectYaxis(String yaxisName) {
        getOptionsSidePanel().selectYAxis(yaxisName);
        log.info("Selecting Y axis: {}", yaxisName);
    }

    @Step("Select Y axis mode: {mode}")
    public void selectManualYaxisParameters(String maxValue, String minValue, boolean softLimit) {
        getOptionsSidePanel().setYaxisMode(MANUAL_MODE);
        log.info("Selecting Y axis mode to: Manual");
        getOptionsSidePanel().setYaxisMaxAndMinValues(maxValue, minValue);
        if (softLimit) {
            getOptionsSidePanel().setOnSoftLimits();
        }
    }
}
