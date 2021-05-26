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

    private KQIsPage kqisPage;
    private String kQIsName;
    private String updatedKQIsName;


    @BeforeClass
    public void goToKQIsView() {
        kqisPage = KQIsPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        kQIsName = "Selenium_" + date + "_kqiTest";
        updatedKQIsName = kQIsName + "_updated";
    }

    @Test(priority = 1, testName = "Add new KQI", description = "Add new KQI")
    @Description("Add new KQI")
    public void addKQI() {
        kqisPage.clickAddNewKQI();
        kqisPage.getKqiWizardPage().fillKQIWizard(kQIsName, VALUE_TYPE, UNIT_TYPE, FORMULA);
        kqisPage.getKqiWizardPage().clickAccept();
        Boolean kqiIsCreated = kqisPage.kqiExistIntoTable(kQIsName);

        Assert.assertTrue(kqiIsCreated);
    }

    @Test(priority = 2, testName = "Edit KQI", description = "Edit KQI")
    @Description("Edit KQI")
    public void editKQI() {
        Boolean kqiExists = kqisPage.kqiExistIntoTable(kQIsName);
        if (kqiExists) {
            kqisPage.selectFoundKQI();
            kqisPage.clickEditKQI();
            kqisPage.getKqiWizardPage().fillName(updatedKQIsName);
            kqisPage.getKqiWizardPage().clickAccept();
            Boolean kqiIsEdited = kqisPage.kqiExistIntoTable(updatedKQIsName);

            Assert.assertTrue(kqiIsEdited);
        } else {
            log.error("KQI with name: {} doesn't exist", kQIsName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete KQI", description = "Delete KQI")
    @Description("Delete KQI")
    public void deleteKQI() {
        Boolean kqiExists = kqisPage.kqiExistIntoTable(kQIsName);
        if (kqiExists) {
            kqisPage.selectFoundKQI();
            kqisPage.clickDeleteKQI();
            kqisPage.clickConfirmDelete();
            Boolean kqiIsDeleted = !kqisPage.kqiExistIntoTable(kQIsName);

            Assert.assertTrue(kqiIsDeleted);
        } else {
            log.error("KQI with name: {} doesn't exist, can not perform delete action", kQIsName);
            Assert.fail();
        }
    }
}
