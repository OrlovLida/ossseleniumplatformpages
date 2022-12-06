package com.oss.iaa.bigdata.dfe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.AggregatePage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.aggregate.AggregateConfigurationPage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.aggregate.AggregateProcessSettingsPage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class AggregateViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AggregateViewTest.class);

    private AggregatePage aggregatePage;
    private String aggregateName;
    private String updatedAggregateName;
    private String tableName;

    private static final String ETL_PROCESS_NAME = "t:CRUD#ETLforAggr";
    private static final String AGGREGATE_CONFIGURATION_DIMENSION_NAME = "t:SMOKE#D_HOST (HOST_NM)";
    private static final String AGGREGATE_CONFIGURATION_NAME = "Selenium_Aggregate_Test";
    private static final String AGGREGATE_WIZARD_ID = "aggregatesWizardWindow";

    @BeforeClass
    public void goToAggregateView() {
        aggregatePage = AggregatePage.goToPage(driver, BASIC_URL);

        aggregateName = ConstantsDfe.createName() + "_AggrTest";
        updatedAggregateName = aggregateName + "_updated";
        tableName = ConstantsDfe.createName();
    }

    @Test(priority = 1, testName = "Add new Aggregate", description = "Add new Aggregate")
    @Description("Add new Aggregate")
    public void addAggregate() {
        aggregatePage.clickAddNewAggregate();
        BasicInformationPage aggrBasicInfoWizard = new BasicInformationPage(driver, webDriverWait, AGGREGATE_WIZARD_ID);
        aggrBasicInfoWizard.fillName(aggregateName);
        aggrBasicInfoWizard.fillProcess(ETL_PROCESS_NAME);
        aggrBasicInfoWizard.clickNextStep();

        AggregateProcessSettingsPage processSettingsWizard = new AggregateProcessSettingsPage(driver, webDriverWait);
        processSettingsWizard.clickNextStep();

        AggregateConfigurationPage aggrConfWizard = new AggregateConfigurationPage(driver, webDriverWait);
        aggrConfWizard.fillAggregatesConfigurationStepAggregate(AGGREGATE_CONFIGURATION_NAME, tableName, AGGREGATE_CONFIGURATION_DIMENSION_NAME);
        aggrConfWizard.clickAccept();

        boolean aggregateIsCreated = aggregatePage.aggregateExistsIntoTable(aggregateName);

        if (!aggregateIsCreated) {
            log.info("Cannot find created aggregate configuration");
        }
        Assert.assertTrue(aggregateIsCreated);
    }

    @Test(priority = 2, testName = "Edit Aggregate", description = "Edit Aggregate")
    @Description("Edit Aggregate")
    public void editAggregate() {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.clickEditAggregate();

            BasicInformationPage aggrBasicInfoWizard = new BasicInformationPage(driver, webDriverWait, AGGREGATE_WIZARD_ID);
            aggrBasicInfoWizard.fillName(updatedAggregateName);
            aggrBasicInfoWizard.clickNextStep();

            AggregateProcessSettingsPage processSettingsWizard = new AggregateProcessSettingsPage(driver, webDriverWait);
            processSettingsWizard.clickNextStep();

            AggregateConfigurationPage aggrConfWizard = new AggregateConfigurationPage(driver, webDriverWait);
            aggrConfWizard.clickAccept();

            boolean aggregateIsEdited = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
            if (!aggregateIsEdited) {
                log.info("Cannot find existing edited aggregate {}", updatedAggregateName);
            }
            Assert.assertTrue(aggregateIsEdited);
        } else {
            Assert.fail(failMessage(aggregateName));
        }
    }

    @Test(priority = 3, testName = "Delete Aggregate", description = "Delete Aggregate")
    @Description("Delete Aggregate")
    public void deleteAggregate() {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.clickDeleteAggregate();
            aggregatePage.confirmDelete();
            boolean aggregateDeleted = !aggregatePage.aggregateExistsIntoTable(updatedAggregateName);

            Assert.assertTrue(aggregateDeleted);
        } else {
            Assert.fail(failMessage(updatedAggregateName));
        }
    }

    private String failMessage(String aggregateName) {
        return String.format("Cannot find existing aggregate %s", aggregateName);
    }
}
