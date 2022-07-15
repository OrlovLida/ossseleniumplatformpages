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
import com.oss.pages.administration.BookmarkManagerPage;
import com.oss.pages.administration.managerwizards.BookmarkWizardPage;
import com.oss.pages.administration.managerwizards.CategoryWizardPage;
import com.oss.pages.bigdata.kqiview.KpiToolbarPanelPage;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.pages.bigdata.kqiview.KpiViewSetupPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class BookmarkTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(BookmarkTest.class);

    private final String BOOKMARK_NAME = ConstantsDfe.createName() + "_bookmark";
    private String bookmarkForProduct;
    private String updatedBookmarkForProduct;
    private final String EDITED = " [Edited]";
    private final String UPDATED = "_updated";
    private final String CATEGORY_NAME = "Selenium Test";

    private BookmarkManagerPage bookmarkManagerPage;
    private KpiViewPage kpiViewPage;
    private KpiViewSetupPage kpiViewSetup;
    private BookmarkWizardPage bookmarkWizardPage;
    private KpiToolbarPanelPage kpiToolbarPanel;

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
    public void goToBookmarkManagerPage() {
        bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"kpiViewType", "indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName", "productName"})
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
            kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
            kpiViewSetup = new KpiViewSetupPage(driver, webDriverWait);
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiToolbarPanel = new KpiToolbarPanelPage(driver, webDriverWait);
            kpiToolbarPanel.applyChanges();
            bookmarkForProduct = BOOKMARK_NAME + productName;

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            bookmarkWizardPage = kpiViewPage.clickSaveBookmark();
            bookmarkWizardPage.fillBookmarkWizard(bookmarkForProduct, CATEGORY_NAME);

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(bookmarkForProduct);
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(bookmarkForProduct);

            Assert.assertEquals(bookmarkForProduct, kpiViewPage.getBookmarkTitle());
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"anotherDimensionToSelect"})
    @Test(priority = 2, testName = "Editing Bookmark", description = "Editing Bookmark")
    @Description("Editing Bookmark")
    public void editBookmark(
            @Optional("DC Type: PM_DC") String anotherDimensionToSelect
    ) {
        bookmarkManagerPage.searchForBookmark(bookmarkForProduct);
        boolean bookmarkExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (bookmarkExist) {
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(bookmarkForProduct);
            kpiViewSetup = new KpiViewSetupPage(driver, webDriverWait);
            kpiViewSetup.selectUnfoldedDimension(new ArrayList<>(Collections.singletonList(anotherDimensionToSelect)));
            kpiToolbarPanel = new KpiToolbarPanelPage(driver, webDriverWait);
            kpiToolbarPanel.applyChanges();
            String editedBookmarkTitle = bookmarkForProduct + EDITED;

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
            Assert.assertEquals(kpiViewPage.getBookmarkTitle(), editedBookmarkTitle);
            bookmarkWizardPage = kpiViewPage.clickSaveBookmark();
            updatedBookmarkForProduct = bookmarkForProduct + UPDATED;
            bookmarkWizardPage.fillBookmarkWizard(updatedBookmarkForProduct, CATEGORY_NAME);
            bookmarkManagerPage = BookmarkManagerPage.goToPage(driver, BASIC_URL);
            bookmarkManagerPage.searchForBookmark(updatedBookmarkForProduct);
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.openBookmark(updatedBookmarkForProduct);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);

            Assert.assertEquals(kpiViewPage.getBookmarkTitle(), updatedBookmarkForProduct);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
        } else {
            log.error("There is no bookmarks on the search list.");
            Assert.fail();
        }
    }

    @Test(priority = 3)
    public void deleteBookmark() {
        bookmarkManagerPage.searchForBookmark(updatedBookmarkForProduct);
        boolean bookmarkExist = bookmarkManagerPage.isAnyBookmarkInList();
        if (bookmarkExist) {
            bookmarkManagerPage.expandBookmarkList(CATEGORY_NAME);
            bookmarkManagerPage.clickDeleteBookmark(updatedBookmarkForProduct);
            bookmarkManagerPage.searchForBookmark(updatedBookmarkForProduct);
            Assert.assertTrue(bookmarkManagerPage.isBookmarkDeleted(updatedBookmarkForProduct));
        } else {
            log.error("There is no bookmarks on the search list.");
            Assert.fail();
        }
    }
}
