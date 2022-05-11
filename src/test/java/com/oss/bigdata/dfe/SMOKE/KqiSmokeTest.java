package com.oss.bigdata.dfe.SMOKE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.kqi.KqiPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class KqiSmokeTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(KqiSmokeTest.class);
    private static final String FORMULA_SMOKE_KQI = "$[M:COUNT(t:SMOKE#ETLforKqis.ATTEMPTS_LONG)]";
    
    private KqiPage kqiPage;

    @BeforeClass
    public void goToKqiView() {
        kqiPage = KqiPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"kqiName"})
    @Test(priority = 1, testName = "Check KQI Details Tab", description = "Check if KQI Details Tab display proper name and formula")
    @Description("Check if KQI Details Tab display proper name and formula")
    public void checkKqiDetailsTab(
            @Optional("t:SMOKE#Attempts") String kqiName
    ) {
        boolean kqiExists = kqiPage.kqiExistIntoTable(kqiName);
        if (kqiExists) {
            kqiPage.selectFoundKQI();
            kqiPage.selectDetailsTab();

            Assert.assertEquals(kqiPage.checkNameInPropertyPanel(), kqiName);
            Assert.assertEquals(kqiPage.checkFormulaInPropertyPanel(), FORMULA_SMOKE_KQI);
        } else {
            log.info("Cannot find existing KQI {}", kqiName);
            Assert.fail("Cannot find existing KQI " + kqiName);
        }
    }

    @Parameters({"kqiName"})
    @Test(priority = 2, testName = "Check KQI Parameters Tab", description = "Check if Parameters Table is not empty")
    @Description("Check if Parameters Table is not empty")
    public void checkKqiParametersTab(
            @Optional("t:SMOKE#Attempts") String kqiName
    ) {
        boolean kqiExists = kqiPage.kqiExistIntoTable(kqiName);
        if (kqiExists) {
            kqiPage.selectFoundKQI();
            kqiPage.selectParametersTab();

            Assert.assertTrue(kqiPage.parametersTableHasData());
        } else {
            log.info("Cannot find existing KQI {}", kqiName);
            Assert.fail("Cannot find existing KQI " + kqiName);
        }
    }
}
