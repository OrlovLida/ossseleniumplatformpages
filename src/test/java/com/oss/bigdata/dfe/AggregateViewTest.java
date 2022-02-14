package com.oss.bigdata.dfe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.AggregatePage;
import com.oss.pages.bigdata.dfe.stepwizard.aggregate.AggregateConfigurationPage;
import com.oss.pages.bigdata.dfe.stepwizard.aggregate.AggregateProcessSettingsPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class AggregateViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AggregateViewTest.class);

    private AggregatePage aggregatePage;
    private String aggregateName;
    private String updatedAggregateName;
    private String tableName;

    private final static String ETL_PROCESS_NAME = "t:CRUD#ETLforAggr";
    private final static String AGGREGATE_CONFIGURATION_DIMENSION_NAME = "t:SMOKE#D_HOST (HOST_NM)";
    private final static String AGGREGATE_CONFIGURATION_NAME = "Selenium_Aggregate_Test";
    private final static String AGGREGATE_WIZARD_ID = "aggregatesWizardWindow";

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

        Boolean aggregateIsCreated = aggregatePage.aggregateExistsIntoTable(aggregateName);

        if (!aggregateIsCreated) {
            log.info("Cannot find created aggregate configuration");
        }
        Assert.assertTrue(aggregateIsCreated);
    }

    @Test(priority = 2, testName = "Edit Aggregate", description = "Edit Aggregate")
    @Description("Edit Aggregate")
    public void editAggregate() {
        Boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if (aggregateExists) {
            aggregatePage.selectFoundAggregate();
            aggregatePage.clickEditAggregate();

            BasicInformationPage aggrBasicInfoWizard = new BasicInformationPage(driver, webDriverWait, AGGREGATE_WIZARD_ID);
            aggrBasicInfoWizard.fillName(updatedAggregateName);
            aggrBasicInfoWizard.clickNextStep();

            AggregateProcessSettingsPage processSettingsWizard = new AggregateProcessSettingsPage(driver, webDriverWait);
            processSettingsWizard.clickNextStep();

            AggregateConfigurationPage aggrConfWizard = new AggregateConfigurationPage(driver, webDriverWait);
            aggrConfWizard.clickAccept();

            Boolean aggregateIsEdited = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
            if (!aggregateIsEdited) {
                log.info("Cannot find existing edited aggregate {}", updatedAggregateName);
            }
            Assert.assertTrue(aggregateIsEdited);
        } else {
            log.info("Cannot find existing aggregate {}", aggregateName);
            Assert.fail("Cannot find existing aggregate " + aggregateName);
        }
    }

    @Test(priority = 3, testName = "Delete Aggregate", description = "Delete Aggregate")
    @Description("Delete Aggregate")
    public void deleteAggregate() {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
        if (aggregateExists) {
            aggregatePage.selectFoundAggregate();
            aggregatePage.clickDeleteAggregate();
            aggregatePage.confirmDelete();
            boolean aggregateDeleted = !aggregatePage.aggregateExistsIntoTable(updatedAggregateName);

            Assert.assertTrue(aggregateDeleted);
        } else {
            log.info("Cannot find existing aggregate {}", updatedAggregateName);
            Assert.fail("Cannot find existing aggregate " + updatedAggregateName);
        }
    }
}
