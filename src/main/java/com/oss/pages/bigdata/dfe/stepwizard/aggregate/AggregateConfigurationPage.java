package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.components.dfe.aggegatesmanager.AggregateSingleConfiguration;
import com.oss.framework.data.Data;
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

    private void addNewAggregateConfiguration(AggregatesManagerWidget aggregatesManager){
        aggregatesManager.clickAdd();
    }


    @Step("I fill Aggregates Configuration Step Aggregate")
    public void fillAggregatesConfigurationStepAggregate(String configName, String baseTableName, String selectedDimension){
        DelayUtils.waitForPageToLoad(driver, wait);

        AggregatesManagerWidget aggregatesManager = AggregatesManagerWidget.create(driver, wait);
        addNewAggregateConfiguration(aggregatesManager);

        List<AggregateSingleConfiguration> configs = aggregatesManager.getAggregateConfigurations();
        if(!configs.isEmpty()){
            AggregateSingleConfiguration configuration = configs.get(0);
            configuration.expand();
            configuration.getNameInput().setValue(Data.createSingleData(configName));
            configuration.getBaseTableNameInput().setValue(Data.createSingleData(baseTableName));
            configuration.getDimensionsInput().setSingleStringValue(selectedDimension);
        }
    }
}
