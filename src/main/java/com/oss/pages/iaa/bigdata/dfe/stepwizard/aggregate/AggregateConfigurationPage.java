package com.oss.pages.iaa.bigdata.dfe.stepwizard.aggregate;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.data.Data;
import com.oss.framework.iaa.widgets.dfe.aggregatesmanager.AggregatesManagerWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class AggregateConfigurationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(AggregateConfigurationPage.class);
    private static final String AGGREGATES_WIZARD_ID = "aggregatesWizardWindow";
    private static final String HASH_PARTITION_CHECKBOX = "confirmation-checkbox-id";
    private final Wizard aggrConfWizard;

    public AggregateConfigurationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        aggrConfWizard = Wizard.createByComponentId(driver, wait, AGGREGATES_WIZARD_ID);
    }

    @Step("I fill Aggregates Configuration Step Aggregate")
    public void fillAggregatesConfigurationStepAggregate(String configName, String baseTableName, String selectedDimension) {
        waitForPageToLoad(driver, wait);

        AggregatesManagerWidget aggregatesManager = AggregatesManagerWidget.create(driver, wait);
        addNewAggregateConfiguration(aggregatesManager);

        List<AggregatesManagerWidget.AggregateSingleConfiguration> configs = aggregatesManager.getAggregateConfigurations();
        if (!configs.isEmpty()) {
            AggregatesManagerWidget.AggregateSingleConfiguration configuration = configs.get(0);
            log.debug("Getting configuration at index 0");
            configuration.expand();
            configuration.setName(configName);
            log.debug("Setting configuration name: {}", configName);
            configuration.setBaseTableName(baseTableName);
            log.debug("Setting configuration base table name: {}", baseTableName);
            configuration.setDimensions(selectedDimension);
            log.debug("Setting configuration dimension: {}", selectedDimension);
            clickHashPartitionCheckbox();
        }
        log.info("Filled Aggregates Configuration Step");
    }

    @Step("Click Accept")
    public void clickAccept() {
        aggrConfWizard.clickAccept();
        waitForPageToLoad(driver, wait);
        log.info("Finishing Step Wizard by clicking 'Accept'");
    }

    private void addNewAggregateConfiguration(AggregatesManagerWidget aggregatesManager) {
        aggregatesManager.clickAdd();
        log.debug("Adding new aggregate configuration");
    }

    private void clickHashPartitionCheckbox() {
        if (aggrConfWizard.isElementPresentById(HASH_PARTITION_CHECKBOX)) {
            aggrConfWizard.getComponent(HASH_PARTITION_CHECKBOX).setValue(Data.createSingleData("true"));
            log.info("Clicking configuration checkbox: changed Hash partitioning parameters ");
        }
    }
}
