package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.AggregatePage;
import com.oss.pages.bigdata.dfe.stepwizard.aggregate.AggregateStepWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class AggregateViewTest extends BaseTestCase {

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
            Assert.assertTrue(aggregateIsEdited);
        } else {
            Assert.fail();
        }
    }

    @Test(priority = 3)
    @Description("Delete Aggregate")
    public void deleteAggregate(){
        Boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(updatedAggregateName);
        if(aggregateExists){
            aggregatePage.selectFoundAggregate();
            aggregatePage.clickDeleteAggregate();
            aggregatePage.confirmDelete();
            Boolean aggregateDeleted = !aggregatePage.aggregateExistsIntoTable(updatedAggregateName);

            Assert.assertTrue(aggregateDeleted);
        } else {
            Assert.fail();
        }
    }


}
