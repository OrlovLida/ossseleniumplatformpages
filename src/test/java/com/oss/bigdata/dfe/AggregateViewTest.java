package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.AggregatePage;
import com.oss.pages.bigdata.dfe.stepwizard.aggregate.AggregateStepWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class AggregateViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AggregateViewTest.class);

    private AggregatePage aggregatePage;
    private String aggregateName;
    private String updatedAggregateName;

    private final static String ETL_PROCESS_NAME = "t:CRUD#ETLforAggr";
    private final static String AGGREGATE_CONFIGURATION_DIMENSION_NAME = "t:SMOKE#D_HOST (HOST_NM)";
    private final static String AGGREGATE_CONFIGURATION_NAME = "Selenium_Aggregate_Test";
    private final static String AGGREGATE_CONFIGURATION_TABLE_PREFIX = "Selenium_Aggregate_Test";


    @BeforeClass
    public void goToAggregateView(){
        aggregatePage = AggregatePage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        aggregateName = "Selenium_" + date + "_AggrTest";
        updatedAggregateName = aggregateName + "_updated";
    }

    @Test(priority = 1)
    @Description("Add new Aggregate")
    public void addAggregate(){
        aggregatePage.clickAddNewAggregate();
        WebDriverWait wait = new WebDriverWait(driver, 45);
        AggregateStepWizardPage aggregateStepWizard = new AggregateStepWizardPage(driver, wait);
        aggregateStepWizard.getBasicInformationStep().fillBasicInformationStep(aggregateName, ETL_PROCESS_NAME);
        aggregateStepWizard.clickNextStep();
        aggregateStepWizard.clickNextStep();
        aggregateStepWizard
                .getAggregatesConfigurationStep()
                .fillAggregatesConfigurationStepAggregate(AGGREGATE_CONFIGURATION_NAME, AGGREGATE_CONFIGURATION_TABLE_PREFIX, AGGREGATE_CONFIGURATION_DIMENSION_NAME);
        aggregateStepWizard.clickAccept();
        Boolean aggregateIsCreated = aggregatePage.aggregateExistsIntoTable(aggregateName);

        if(!aggregateIsCreated){
            log.info("Cannot find created aggregate configuration");
        }
        Assert.assertTrue(aggregateIsCreated);
    }

    @Test(priority = 2)
    @Description("Edit Aggregate")
    public void editAggregate(){
        Boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if(aggregateExists){
            aggregatePage.selectFoundAggregate();
            aggregatePage.clickEditAggregate();

            WebDriverWait wait = new WebDriverWait(driver, 45);
            AggregateStepWizardPage aggregateStepWizard = new AggregateStepWizardPage(driver, wait);
            aggregateStepWizard.getBasicInformationStep().fillName(updatedAggregateName);
            aggregateStepWizard.clickNextStep();
            aggregateStepWizard.clickNextStep();
            aggregateStepWizard.clickAccept();

            Boolean aggregateIsEdited = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
            if(!aggregateIsEdited){
                log.info("Cannot find existing edited aggregate {}", updatedAggregateName);
            }
            Assert.assertTrue(aggregateIsEdited);
        } else {
            log.info("Cannot find existing aggregate {}", aggregateName);
            Assert.fail("Cannot find existing aggregate " + aggregateName);
        }
    }

    @Test(priority = 3)
    @Description("Delete Aggregate")
    public void deleteAggregate(){
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
        if(aggregateExists){
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
