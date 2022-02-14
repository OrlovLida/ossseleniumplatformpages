package com.oss.bigdata.dfe.SMOKE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.kqi.KqiPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class KqiSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(KqiSmokeTest.class);
    private KqiPage kqiPage;
    private static final String KQI_NAME = "t:SMOKE#Attempts";
    private final String NAME_PROPERTY = "Name";
    private final String FORMULA_PROPERTY = "Formula";
    private static final String FORMULA_SMOKE_KQI = "$[M:COUNT(t:SMOKE#ETLforKqis.ATTEMPTS_LONG)]";

    @BeforeClass
    public void goToKqiView() {
        kqiPage = KqiPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check KQI Details Tab", description = "Check if KQI Details Tab display proper name and formula")
    @Description("Check if KQI Details Tab display proper name and formula")
    public void checkKqiDetailsTab() {
        boolean kqiExists = kqiPage.kqiExistIntoTable(KQI_NAME);
        if (kqiExists) {
            kqiPage.selectFoundKQI();
            kqiPage.selectDetailsTab();

            Assert.assertEquals(kqiPage.checkValueForPropertyInDetails(NAME_PROPERTY), KQI_NAME);
            Assert.assertEquals(kqiPage.checkValueForPropertyInDetails(FORMULA_PROPERTY), FORMULA_SMOKE_KQI);
        } else {
            log.info("Cannot find existing KQI {}", KQI_NAME);
            Assert.fail("Cannot find existing KQI " + KQI_NAME);
        }
    }

    @Test(priority = 2, testName = "Check KQI Parameters Tab", description = "Check if Parameters Table is not empty")
    @Description("Check if Parameters Table is not empty")
    public void checkKqiParametersTab() {
        boolean kqiExists = kqiPage.kqiExistIntoTable(KQI_NAME);
        if (kqiExists) {
            kqiPage.selectFoundKQI();
            kqiPage.selectParametersTab();

            Assert.assertTrue(kqiPage.parametersTableHasData());
        } else {
            log.info("Cannot find existing KQI {}", KQI_NAME);
            Assert.fail("Cannot find existing KQI " + KQI_NAME);
        }
    }
}
