package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.kqi.KqiPage;
import com.oss.pages.iaa.bigdata.dfe.kqi.KqiWizardPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class KQIsTest extends BaseTestCase {

    private static final String VALUE_TYPE = "Number";
    private static final String UNIT_TYPE = "Minutes";
    private static final String FORMULA = "$[M:COUNT(t:SMOKE#ETLforKqis.ATTEMPTS_LONG)]";

    private KqiPage kqisPage;
    private String kqisName;
    private String updatedKQIsName;

    @BeforeClass
    public void goToKQIsView() {
        kqisPage = KqiPage.goToPage(driver, BASIC_URL);

        kqisName = ConstantsDfe.createName() + "_kqiTest";
        updatedKQIsName = kqisName + "_updated";
    }

    @Test(priority = 1, testName = "Add new KQI", description = "Add new KQI")
    @Description("Add new KQI")
    public void addKQI() {
        kqisPage.clickAddNewKQI();
        KqiWizardPage kqiWizard = new KqiWizardPage(driver, webDriverWait);
        kqiWizard.fillKQIWizard(kqisName, VALUE_TYPE, UNIT_TYPE, FORMULA);
        kqiWizard.clickAccept();
        boolean kqiIsCreated = kqisPage.kqiExistIntoTable(kqisName);
        Assert.assertTrue(kqiIsCreated);
    }

    @Test(priority = 2, testName = "Edit KQI", description = "Edit KQI")
    @Description("Edit KQI")
    public void editKQI() {
        boolean kqiExists = kqisPage.kqiExistIntoTable(kqisName);
        if (kqiExists) {
            kqisPage.selectFoundKQI();
            kqisPage.clickEditKQI();
            KqiWizardPage kqiWizard = new KqiWizardPage(driver, webDriverWait);
            kqiWizard.fillName(updatedKQIsName);
            kqiWizard.clickAccept();
            boolean kqiIsEdited = kqisPage.kqiExistIntoTable(updatedKQIsName);

            Assert.assertTrue(kqiIsEdited);
        } else {
            Assert.fail(String.format("KQI with name: %s doesn't exist", kqisName));
        }
    }

    @Test(priority = 3, testName = "Delete KQI", description = "Delete KQI")
    @Description("Delete KQI")
    public void deleteKQI() {
        boolean kqiExists = kqisPage.kqiExistIntoTable(kqisName);
        if (kqiExists) {
            kqisPage.selectFoundKQI();
            kqisPage.clickDeleteKQI();
            kqisPage.clickConfirmDelete();
            boolean kqiIsDeleted = !kqisPage.kqiExistIntoTable(kqisName);

            Assert.assertTrue(kqiIsDeleted);
        } else {
            Assert.fail(String.format("KQI with name: %s doesn't exist, cannot perform delete action", kqisName));
        }
    }
}
