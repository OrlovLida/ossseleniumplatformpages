package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.pages.bookmarkManager.BookmarkManagerPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BookmarkTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(BookmarkTest.class);
    private KpiViewPage kpiViewPage;
    private final String BOOKMARK_NAME = ConstantsDfe.createName() + "_bookmark";
    private BookmarkManagerPage bookmarkManagerPage;

    @BeforeClass
    public void goKpiViewPage() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 1, testName = "Creating Bookmark", description = "Creating Bookmark")
    @Description("Creating Bookmark")
    public void createBookmark(
            @Optional("DFE Tests,DFE Product Tests,Test Simple Random") String indicatorNodesToExpand,
            @Optional("SUCCESS_LONG") String indicatorNodesToSelect,
            @Optional("t:MANUAL#d4.TSRandom") String dimensionNodesToExpand,
            @Optional("D4_01") String dimensionNodesToSelect,
            @Optional("DFE Tests") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.setValueInTimePeriodChooser(10, 0, 0);
            kpiViewPage.applyChanges();

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickSaveBookmark();
            kpiViewPage.fillBookmarkWizard(BOOKMARK_NAME);

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(BOOKMARK_NAME);


        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}
