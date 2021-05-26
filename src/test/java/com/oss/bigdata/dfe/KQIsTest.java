package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.KQIs.KQIsPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KQIsTest extends BaseTestCase {

    private final static Logger log = LoggerFactory.getLogger(KQIsTest.class);
    private final String VALUE_TYPE = "Number";
    private final String UNIT_TYPE = "Minutes";
    private final String FORMULA = "$[COUNT(t:SMOKE#ETLforKqis.ATTEMPTS_LONG)]";

    private KQIsPage kqIsPage;
    private String kQIsName;
    private String updatedKQIsName;


    @BeforeClass
    public void goToKQIsView() {
        kqIsPage = KQIsPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        kQIsName = "Selenium_" + date + "_kqiTest";
        updatedKQIsName = kQIsName + "_updated";
    }

    @Test(priority = 1, testName = "Add new KQI", description = "Add new KQI")
    @Description("Add new KQI")
    public void addKQI() {
        kqIsPage.clickAddNewKQI();
        kqIsPage.getKqiWizardPage().fillKQIWizard(kQIsName, VALUE_TYPE, UNIT_TYPE, FORMULA);
        kqIsPage.getKqiWizardPage().clickAccept();
        Boolean kqiIsCreated = kqIsPage.kqiExistIntoTable(kQIsName);

        Assert.assertTrue(kqiIsCreated);
    }

    @Test(priority = 2, testName = "Edit KQI", description = "Edit KQI")
    @Description("Edit KQI")
    public void editKQI() {
        Boolean kqiExists = kqIsPage.kqiExistIntoTable(kQIsName);
        if (kqiExists) {
            kqIsPage.selectFoundKQI();
            kqIsPage.clickEditKQI();
            kqIsPage.getKqiWizardPage().fillName(updatedKQIsName);
            kqIsPage.getKqiWizardPage().clickAccept();
            Boolean kqiIsEdited = kqIsPage.kqiExistIntoTable(updatedKQIsName);

            Assert.assertTrue(kqiIsEdited);
        } else {
            log.error("KQI with name: {} doesn't exist", kQIsName);
            Assert.fail();
        }
    }

}
