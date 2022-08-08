package com.oss.iaa.dpe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.kqiview.KpiViewPage;
import com.oss.pages.iaa.bigdata.kqiview.KpiViewSetupPage;
import com.oss.pages.iaa.dpe.qvp.GlobalQuickViewPolicyPage;
import com.oss.pages.iaa.dpe.qvp.QVPPrompt;

import io.qameta.allure.Description;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class GlobalQuickViewPolicyTest extends BaseTestCase {

    private static final String QVP_NAME = "Selenium Test Global QVP";
    private static final String QVP_NAME_EDITED = "Selenium Test Global QVP Edited";
    private static final String POSITION_PROPERTY_NAME = "Position";
    private static final String NAME_ON_VIEW_COLUMN_LABEL = "Name on View";

    private KpiViewPage kpiViewPage;
    private KpiViewSetupPage kpiViewSetup;
    private GlobalQuickViewPolicyPage globalQuickViewPolicyPage;
    private QVPPrompt qvpPrompt;

    @BeforeClass
    public void goToKpiView() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
        kpiViewSetup = new KpiViewSetupPage(driver, webDriverWait);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1, testName = "Create Global QVP", description = "Create Global QVP")
    @Description("Create Global QVP")
    public void createGlobalQVP(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {

        kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
        assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

        kpiViewPage.clickShare();
        String linkToQVP = kpiViewPage.copyLink();
        kpiViewPage.clickCloseShare();
        globalQuickViewPolicyPage = GlobalQuickViewPolicyPage.openGlobalQVPPage(driver, BASIC_URL, webDriverWait);
        int countQVP = globalQuickViewPolicyPage.countQVP();
        qvpPrompt = globalQuickViewPolicyPage.clickCreateGlobalQVP();
        qvpPrompt.setLinkToIndicatorsView(linkToQVP);
        qvpPrompt.setQVPName(QVP_NAME);
        qvpPrompt.clickAccept();
        assertEquals(globalQuickViewPolicyPage.countQVP(), countQVP + 1);
        assertEquals(globalQuickViewPolicyPage.getCreatedQVPName(), QVP_NAME);
    }

    @Test(priority = 2, testName = "Edit Global QVP", description = "Edit Global QVP")
    @Description("Edit Global QVP")
    public void editGlobalQVP() {
        globalQuickViewPolicyPage.selectQVP(QVP_NAME);
        qvpPrompt = globalQuickViewPolicyPage.clickEditGlobalQVP();
        qvpPrompt.setQVPName(QVP_NAME_EDITED);
        qvpPrompt.clickAccept();
        assertEquals(globalQuickViewPolicyPage.getCreatedQVPName(), QVP_NAME_EDITED);
    }

    @Test(priority = 3, testName = "Check Move buttons", description = "Check Move buttons")
    @Description("Check Move buttons")
    public void checkMoveButtons() {
        int countQVP = globalQuickViewPolicyPage.countQVP();

        if (countQVP >= 3) {
            globalQuickViewPolicyPage.clickMoveUp(QVP_NAME_EDITED);
            assertEquals(globalQuickViewPolicyPage.getQVPNameInRow(countQVP - 2), QVP_NAME_EDITED);

            globalQuickViewPolicyPage.clickMoveTop(QVP_NAME_EDITED);
            assertEquals(globalQuickViewPolicyPage.getQVPNameInRow(0), QVP_NAME_EDITED);

            globalQuickViewPolicyPage.clickMoveDown(QVP_NAME_EDITED);
            assertEquals(globalQuickViewPolicyPage.getQVPNameInRow(1), QVP_NAME_EDITED);

            globalQuickViewPolicyPage.clickMoveBottom(QVP_NAME_EDITED);
            assertEquals(globalQuickViewPolicyPage.getQVPNameInRow(countQVP - 1), QVP_NAME_EDITED);
        } else {
            Assert.fail("No sufficient number of QVP to check buttons actions");
        }
    }

    @Test(priority = 4, testName = "Check lower panel", description = "Check lower panel with QVP details")
    @Description("Check lower panel with QVP details")
    public void checkLowerPanel() {
        globalQuickViewPolicyPage.selectQVP(QVP_NAME_EDITED);
        assertEquals(globalQuickViewPolicyPage.getPropertyFromPropertyTable(POSITION_PROPERTY_NAME), String.valueOf(globalQuickViewPolicyPage.countQVP()));
        assertEquals(globalQuickViewPolicyPage.getPropertyFromPropertyTable(NAME_ON_VIEW_COLUMN_LABEL), QVP_NAME_EDITED);
    }

    @Test(priority = 5, testName = "Delete QVP", description = "Delete Global QVP")
    @Description("Delete Global QVP")
    public void deleteQVP() {
        int countQVP = globalQuickViewPolicyPage.countQVP();
        globalQuickViewPolicyPage.selectQVP(QVP_NAME_EDITED);
        globalQuickViewPolicyPage.clickDeleteQVP();
        globalQuickViewPolicyPage.confirmDelete();
        assertEquals(countQVP - 1, globalQuickViewPolicyPage.countQVP());
    }
}
