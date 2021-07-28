package com.oss.bigdata.dfe.SMOKE;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.KQIs.KQIsPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class KqiSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(KqiSmokeTest.class);
    private KQIsPage kqIsPage;
    private static final String KQI_NAME = "t:SMOKE#Attempts";
    private final String NAME_PROPERTY = "Name";
    private final String FORMULA_PROPERTY = "Formula";
    private static final String FORMULA_SMOKE_KQI = "$[M:COUNT(t:SMOKE#ETLforKqis.ATTEMPTS_LONG)]";

    @BeforeClass
    public void goToKqiView() {
        kqIsPage = KQIsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check KQI Details Tab", description = "Check if KQI Details Tab display proper name and formula")
    @Description("Check if KQI Details Tab display proper name and formula")
    public void checkKqiDetailsTab() {
        boolean kqiExists = kqIsPage.kqiExistIntoTable(KQI_NAME);
        if (kqiExists) {
            kqIsPage.selectFoundKQI();
            kqIsPage.selectDetailsTab();

            Assert.assertEquals(kqIsPage.checkValueForPropertyInDetails(NAME_PROPERTY), KQI_NAME);
            Assert.assertEquals(kqIsPage.checkValueForPropertyInDetails(FORMULA_PROPERTY), FORMULA_SMOKE_KQI);

        } else {
            log.info("Cannot find existing KQI {}", KQI_NAME);
            Assert.fail("Cannot find existing KQI " + KQI_NAME);
        }
    }

}
