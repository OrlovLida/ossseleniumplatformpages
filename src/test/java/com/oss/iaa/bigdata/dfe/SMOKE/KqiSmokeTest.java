package com.oss.iaa.bigdata.dfe.SMOKE;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.kqi.KqiPage;

import io.qameta.allure.Description;

public class KqiSmokeTest extends BaseTestCase {

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
            Assert.fail(failMessage(kqiName));
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
            Assert.fail(failMessage(kqiName));
        }
    }

    private String failMessage(String kqiName) {
        return String.format("Cannot find existing KQI %s", kqiName);
    }
}
