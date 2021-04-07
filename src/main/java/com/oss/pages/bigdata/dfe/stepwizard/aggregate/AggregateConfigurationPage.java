package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dfe.aggregatesmanager.AggregatesManagerWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AggregateConfigurationPage extends BasePage {

    public AggregateConfigurationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void addNewAggregateConfiguration(AggregatesManagerWidget aggregatesManager){
        aggregatesManager.clickAdd();
    }

    @Step("I fill Aggregates Configuration Step Aggregate")
    public void fillAggregatesConfigurationStepAggregate(String configName, String baseTableName, String selectedDimension){
        DelayUtils.waitForPageToLoad(driver, wait);

        AggregatesManagerWidget aggregatesManager = AggregatesManagerWidget.create(driver, wait);
        addNewAggregateConfiguration(aggregatesManager);

        List<AggregatesManagerWidget.AggregateSingleConfiguration> configs = aggregatesManager.getAggregateConfigurations();
        if(!configs.isEmpty()){
            AggregatesManagerWidget.AggregateSingleConfiguration configuration = configs.get(0);
            configuration.expand();
            configuration.setName(configName);
            configuration.setBaseTableName(baseTableName);
            configuration.setDimensions(selectedDimension);
        }
    }
}
