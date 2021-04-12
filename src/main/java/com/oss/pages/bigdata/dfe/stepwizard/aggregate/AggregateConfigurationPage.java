package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dfe.aggregatesmanager.AggregatesManagerWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AggregateConfigurationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(AggregateConfigurationPage.class);

    public AggregateConfigurationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void addNewAggregateConfiguration(AggregatesManagerWidget aggregatesManager){
        aggregatesManager.clickAdd();
        log.debug("Adding new aggregate configuration");
    }

    @Step("I fill Aggregates Configuration Step Aggregate")
    public void fillAggregatesConfigurationStepAggregate(String configName, String baseTableName, String selectedDimension){
        DelayUtils.waitForPageToLoad(driver, wait);

        AggregatesManagerWidget aggregatesManager = AggregatesManagerWidget.create(driver, wait);
        addNewAggregateConfiguration(aggregatesManager);

        List<AggregatesManagerWidget.AggregateSingleConfiguration> configs = aggregatesManager.getAggregateConfigurations();
        if(!configs.isEmpty()){
            AggregatesManagerWidget.AggregateSingleConfiguration configuration = configs.get(0);
            log.debug("Getting configuration at index 0");
            configuration.expand();
            configuration.setName(configName);
            log.debug("Setting configuration name: {}", configName);
            configuration.setBaseTableName(baseTableName);
            log.debug("Setting configuration base table name: {}", baseTableName);
            configuration.setDimensions(selectedDimension);
            log.debug("Setting configuration dimension: {}", selectedDimension);
        }
        log.info("Filled Aggregates Configuration Step");
    }
}
