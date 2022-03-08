package com.oss.bigdata.dfe;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.pages.bookmarkmanager.BookmarkManagerPage;
import com.oss.pages.bookmarkmanager.bookmarkmanagerwizards.BookmarkWizardPage;
import com.oss.pages.bookmarkmanager.bookmarkmanagerwizards.CategoryWizardPage;

import io.qameta.allure.Description;

public class BookmarkTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(BookmarkTest.class);

    private String product = "_DPE";
    private final String bookmarkName = ConstantsDfe.createName() + "_bookmark" + product;
    private final String EDITED_BOOKMARK_TITLE_ON_PAGE = bookmarkName + " [Edited]";
    private final String EDITED_BOOKMARK_NAME = bookmarkName + "_updated";
    private final String CATEGORY_NAME = "Selenium Test";

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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName", "productName"})
    @Test(priority = 1, testName = "Creating Bookmark", description = "Creating Bookmark")
    @Description("Creating Bookmark")
    public void createBookmark(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName,
            @Optional("_DPE") String productName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.applyChanges();
            product = productName;

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickSaveBookmark();
            BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
            bookmarkWizardPage.fillBookmarkWizard(bookmarkName, CATEGORY_NAME);

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(bookmarkName);
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(bookmarkName);

            Assert.assertEquals(bookmarkName, kpiViewPage.getBookmarkTitle());
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"anotherDimensionToSelect", "product"})
    @Test(priority = 2, testName = "Editing Bookmark", description = "Editing Bookmark")
    @Description("Editing Bookmark")
    public void editBookmark(
            @Optional("DC Type: PM_DC") String anotherDimensionToSelect,
            @Optional("_DPE") String productName
    ) {
        product = productName;
        bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
        bookmarkManagerPage.searchForBookmark(bookmarkName);
        boolean bookmarkExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (bookmarkExist) {
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(bookmarkName);
            kpiViewPage.selectUnfoldedDimension(new ArrayList<>(Collections.singletonList(anotherDimensionToSelect)));
            kpiViewPage.applyChanges();

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
            Assert.assertEquals(EDITED_BOOKMARK_TITLE_ON_PAGE, kpiViewPage.getBookmarkTitle());

            kpiViewPage.clickSaveBookmark();
            BookmarkWizardPage bookmarkWizardPage = new BookmarkWizardPage(driver, webDriverWait);
            bookmarkWizardPage.fillBookmarkWizard(EDITED_BOOKMARK_NAME, CATEGORY_NAME);
            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(EDITED_BOOKMARK_NAME);
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(EDITED_BOOKMARK_NAME);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);

            Assert.assertEquals(kpiViewPage.getBookmarkTitle(), EDITED_BOOKMARK_NAME);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
        } else {
            log.error("There is no bookmarks on the search list.");
            Assert.fail();
        }
    }

    @Parameters({"product"})
    @Test(priority = 3)
    public void deleteBookmark(
            @Optional("_DPE") String productName
    ) {
        product = productName;
        bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
        bookmarkManagerPage.searchForBookmark(EDITED_BOOKMARK_NAME);
        boolean bookmarkExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (bookmarkExist) {
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.clickDeleteBookmark(EDITED_BOOKMARK_NAME);
            bookmarkManagerPage.searchForBookmark(EDITED_BOOKMARK_NAME);
            Assert.assertTrue(bookmarkManagerPage.isBookmarkDeleted(EDITED_BOOKMARK_NAME));
        } else {
            log.error("There is no bookmarks on the search list.");
            Assert.fail();
        }
    }
}
