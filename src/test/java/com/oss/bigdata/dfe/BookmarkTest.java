package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.pages.bookmarkmanager.BookmarkManagerPage;
import com.oss.pages.bookmarkmanager.BookmarkManagerWizards.BookmarkWizardPage;
import com.oss.pages.bookmarkmanager.BookmarkManagerWizards.CategoryWizardPage;
import com.oss.pages.platform.HomePage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(BookmarkTest.class);

    private final String BOOKMARK_NAME = ConstantsDfe.createName() + "_bookmark";
    private final String EDITED_BOOKMARK_TITLE_ON_PAGE = BOOKMARK_NAME + " [Edited]";
    private final String EDITED_BOOKMARK_NAME = BOOKMARK_NAME + "_updated";
    private final String CATEGORY_NAME = "Selenium Test";

    private final List<String> DIMENSION_TO_SELECT;

    {
        DIMENSION_TO_SELECT = new ArrayList<>(Collections.singletonList("D3_02"));
    }

    private BookmarkManagerPage bookmarkManagerPage;
    private KpiViewPage kpiViewPage;

    @BeforeClass
    public void checkIfCategoryExist() {
        bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
        bookmarkManagerPage.searchForBookmark(CATEGORY_NAME);
        boolean categoryExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (!categoryExist) {
            bookmarkManagerPage.clickCreateNewCategory();
            CategoryWizardPage categoryWizardPage = new CategoryWizardPage(driver, webDriverWait);
            categoryWizardPage.fillCategoryName(CATEGORY_NAME);
            categoryWizardPage.clickSave();
        }
    }

    @BeforeMethod
    public void goKpiViewPage() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 1, testName = "Creating Bookmark", description = "Creating Bookmark")
    @Description("Creating Bookmark")
    public void createBookmark(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("SUCCESS_LONG") String indicatorNodesToSelect,
            @Optional("t:SMOKE#DimHierSelenium") String dimensionNodesToExpand,
            @Optional("D3_01") String dimensionNodesToSelect,
            @Optional("DFE Tests") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.applyChanges();

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickSaveBookmark();
            BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
            bookmarkWizardPage.fillBookmarkWizard(BOOKMARK_NAME, CATEGORY_NAME);

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(BOOKMARK_NAME);
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(BOOKMARK_NAME);
            HomePage homePage = new HomePage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);

            Assert.assertEquals(homePage.getPageTitle(), BOOKMARK_NAME);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 2, testName = "Editing Bookmark", description = "Editing Bookmark")
    @Description("Editing Bookmark")
    public void editBookmark() {
        bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
        bookmarkManagerPage.searchForBookmark(BOOKMARK_NAME);
        boolean bookmarkExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (bookmarkExist) {
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(BOOKMARK_NAME);
            kpiViewPage.selectUnfoldedDimension(DIMENSION_TO_SELECT);
            kpiViewPage.applyChanges();

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            HomePage homePage = new HomePage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);

            Assert.assertEquals(homePage.getPageTitle(), EDITED_BOOKMARK_TITLE_ON_PAGE);

            kpiViewPage.clickSaveBookmark();
            BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
            bookmarkWizardPage.fillBookmarkWizard(EDITED_BOOKMARK_NAME, CATEGORY_NAME);
            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(EDITED_BOOKMARK_NAME);
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(EDITED_BOOKMARK_NAME);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);

            Assert.assertEquals(homePage.getPageTitle(), EDITED_BOOKMARK_NAME);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
        } else {
            log.error("There is no bookmarks on the search list.");
            Assert.fail();
        }
    }

    // TODO poczekać na zmiany we frameworku (OSSWEB-13881)
    @Test(priority = 3)
    public void deleteBookmark() {
        bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
        bookmarkManagerPage.searchForBookmark(BOOKMARK_NAME);
        boolean bookmarkExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (bookmarkExist) {
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
        } else {
            log.error("There is no bookmarks on the search list.");
            Assert.fail();
        }
    }
}
