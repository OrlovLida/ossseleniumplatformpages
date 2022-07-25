package com.oss.pages.bigdata.kqiview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.iaa.widgets.dpe.contextaction.ContextActionPanel;
import com.oss.pages.BasePage;
import com.oss.pages.bigdata.dfe.XDRBrowserPage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class ChartActionsPanelPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ChartActionsPanelPage.class);

    private static final String CHART_TYPE_BUTTON_ID = "chart-type-button";
    private static final String AREA_CHART_BUTTON_ID = "dropdown-list-button_area";
    private static final String BAR_CHART_BUTTON_ID = "dropdown-list-button_bar";
    private static final String LINE_CHART_BUTTON_ID = "dropdown-list-button_line";
    private static final String CHART_COLOR_BUTTON_ID = "chart-color-button";
    private static final String COLOR_PICKER_CLASS = "colorPickerWrapper";
    private static final String STACKED_BUTTON_ID = "stacked-button";
    private static final String CHART_ACTIONS_LINKS_ID = "external-links-button";
    private static final String LINK_TO_XDR_LABEL = "Open xDR for t:SMOKE#ETLforKqis. Time condition limited to last 1 hour(s) from chosen period.";
    private static final String LINK_TO_INDICATORS_VIEW_CHART_LABEL = "Indicators View - Chart";

    private final ContextActionPanel contextActionPanel;

    public ChartActionsPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        contextActionPanel = ContextActionPanel.create(driver, wait);
    }

    @Step("I click chart type - area")
    public void clickAreaChartType() {
        waitForPageToLoad(driver, wait);
        contextActionPanel.callAction(CHART_TYPE_BUTTON_ID, AREA_CHART_BUTTON_ID);
        log.info("Changing chart type to area");
    }

    @Step("I click chart type - bar")
    public void clickBarChartType() {
        log.info("Changing chart type to bar chart");
        contextActionPanel.callAction(CHART_TYPE_BUTTON_ID, BAR_CHART_BUTTON_ID);
    }

    @Step("I click chart type - line")
    public void clickLineChartType() {
        log.info("Changing chart type to line chart");
        contextActionPanel.callAction(CHART_TYPE_BUTTON_ID, LINE_CHART_BUTTON_ID);
    }

    @Step("I pick data series color")
    public void chooseDataSeriesColor() {
        log.info("Changing first data series color");
        contextActionPanel.callAction(CHART_COLOR_BUTTON_ID, COLOR_PICKER_CLASS, "rgb(150, 65, 54)");
    }

    @Step("Click Stacked button")
    public void clickStackedButton() {
        log.info("Clicking stacked button");
        contextActionPanel.callAction(STACKED_BUTTON_ID);
    }

    public String getStackedButtonTitle() {
        return contextActionPanel.getButtonTitle(STACKED_BUTTON_ID);
    }

    @Step("I click link to XDR Browser")
    public XDRBrowserPage clickLinkToXDRBrowser() {
        contextActionPanel.callAction(CHART_ACTIONS_LINKS_ID, LINK_TO_XDR_LABEL);
        waitForPageToLoad(driver, wait);
        log.info("Clicking on link to XDR Browser");
        return new XDRBrowserPage(driver, wait);
    }

    @Step("I click link to chart")
    public void clickLinkToChart() {
        contextActionPanel.callAction(CHART_ACTIONS_LINKS_ID, LINK_TO_INDICATORS_VIEW_CHART_LABEL);
        waitForPageToLoad(driver, wait);
        log.info("Clicking on link to Indicators View - Chart");
    }
}
