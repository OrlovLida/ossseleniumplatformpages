package com.oss.pages.iaa.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

public class TimeSeriesWizardPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesWizardPage.class);

    private static final String DATA_SOURCE_ID = "sourceSystem";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-addTimeSeries";
    private static final String INDICATOR_NAME_ID = "indicatorIdentifiers";
    private static final String BUSINESS_INDICATOR_TYPE_ID = "indicatorBusinessComboBoxId";
    private static final String MO_ID = "dimensionIdentifiers";
    private static final String MO_DOMAIN_ID = "monitoredObjectDomainComboBoxId";
    private static final String INDICATOR_TYPE_RADIO_BUTTON_ID = "radioButtonIndicatorTypeId";
    private static final String TREND_ANALYSIS_CHECKBOX_ID = "checkBoxTrendAnalysisId";
    private static final String FORECAST_ANALYSIS_CHECKBOX_ID = "checkBoxForecastAnalysisId";

    private final Popup timeSeriesWizardPage;

    public TimeSeriesWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        timeSeriesWizardPage = Popup.create(driver, wait);
    }

    @Step("Set Data Source")
    public void setDataSource(String dataSource) {
        timeSeriesWizardPage.setComponentValue(DATA_SOURCE_ID, dataSource);
    }

    @Step("Set Indicator Name")
    public void setIndicatorName(String indicatorName) {
        timeSeriesWizardPage.setComponentValue(INDICATOR_NAME_ID, indicatorName);
    }

    @Step("Set Indicator Business Type")
    public void setIndicatorBusinessType(String indicatorBusinessType) {
        timeSeriesWizardPage.setComponentValue(BUSINESS_INDICATOR_TYPE_ID, indicatorBusinessType);
    }

    @Step("Set MO Identifier")
    public void setMoIdentifier(String moIdentifier) {
        timeSeriesWizardPage.setComponentValue(MO_ID, moIdentifier);
    }

    @Step("Set MO Domain Identifier")
    public void setMoDomainIdentifier(String moDomainIdentifier) {
        timeSeriesWizardPage.setComponentValue(MO_DOMAIN_ID, moDomainIdentifier);
    }

    @Step("I select Indicator Technical Type")
    public void setIndicatorTechnicalType(String indicatorTechnicalType) {
        timeSeriesWizardPage.setComponentValue(INDICATOR_TYPE_RADIO_BUTTON_ID, indicatorTechnicalType);
        log.info("I select Indicator Technical Type");
    }

    @Step("I select Trend Analysis checkbox")
    public void selectTrendAnalysis() {
        timeSeriesWizardPage.setComponentValue(TREND_ANALYSIS_CHECKBOX_ID, "true");
        log.info("I select Trend Analysis checkbox");
    }

    @Step("I select Forecast Analysis checkbox")
    public void selectForecastAnalysis() {
        timeSeriesWizardPage.setComponentValue(FORECAST_ANALYSIS_CHECKBOX_ID, "true");
        log.info("I select Forecast Analysis checkbox");
    }

    @Step("Click accept button")
    public void clickAcceptButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        timeSeriesWizardPage.clickButtonById(ACCEPT_BUTTON_ID);
    }
}