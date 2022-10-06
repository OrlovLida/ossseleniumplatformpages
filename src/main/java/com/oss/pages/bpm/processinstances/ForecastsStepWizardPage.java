package com.oss.pages.bpm.processinstances;

import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.bpm.forecasts.Forecast;
import com.oss.pages.bpm.forecasts.ForecastAttributes;
import com.oss.pages.bpm.forecasts.ForecastWizardPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class ForecastsStepWizardPage extends ProcessWizardPage {

    private static final String PROCESS_FORECAST_LIST = "processActivitiesComponentId";
    private static final String ADD_FORECAST_LIST = "addActivitiesComponentId";


    public ForecastsStepWizardPage(WebDriver driver) {
        super(driver);
    }

    public EditableList getProcessForecastList() {
        return EditableList.createById(driver, wait, PROCESS_FORECAST_LIST);
    }

    public ForecastAttributes addForecastRow(Forecast forecast) {
        ForecastWizardPage forecastWizardPage = new ForecastWizardPage(driver, ADD_FORECAST_LIST);
        return forecastWizardPage.addForecastRow(forecast);
    }

    public List<ForecastAttributes> addForecasts(List<Forecast> forecasts) {
        ForecastWizardPage forecastWizardPage = new ForecastWizardPage(driver, ADD_FORECAST_LIST);
        return forecasts.stream().map(forecastWizardPage::addForecastRow).collect(Collectors.toList());
    }

    public ForecastAttributes setProcessForecast(Forecast forecast) {
        ForecastWizardPage forecastWizardPage = new ForecastWizardPage(driver, PROCESS_FORECAST_LIST);
        return forecastWizardPage.editForecastRow(forecast, 1);
    }
}
