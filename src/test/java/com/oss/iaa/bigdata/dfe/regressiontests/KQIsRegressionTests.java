package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.kqi.KqiPage;

import io.qameta.allure.Description;

public class KQIsRegressionTests extends BaseTestCase {

    private static final String KQIS_VIEW_TITLE = "KQIs";
    private KqiPage kqiPage;

    @BeforeMethod
    public void openPage() {
        kqiPage = KqiPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Opening KQIs View", description = "Opening KQIs View")
    @Description("Opening KQIs View")
    public void openKQIsViewTest() {
        Assert.assertEquals(kqiPage.getViewTitle(), KQIS_VIEW_TITLE);
        Assert.assertFalse(kqiPage.isTableEmpty());
    }

    @Parameters({"category"})
    @Test(priority = 2, testName = "Category search", description = "Check Category search")
    @Description("Check Category search")
    public void categorySearch(
            @Optional("Selenium Tests") String category
    ) {
        kqiPage.searchCategories(category);
        Assert.assertEquals(kqiPage.getCategoryName(0), category);
    }
}
