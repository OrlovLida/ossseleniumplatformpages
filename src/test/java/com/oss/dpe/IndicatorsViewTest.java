package com.oss.dpe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.oss.utils.AttachmentsManager.attachConsoleLogs;

@Listeners({TestListener.class})
public class IndicatorsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(IndicatorsViewTest.class);
    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView(){
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 1)
    @Description("I verify if KPI View for DPE data works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("LOADED,DBTIME") String indicatorNodesToSelect,
//            @Optional("Managed Objects") String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ){
        try{
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);

            //TODO move mouse over point
            kpiViewPage.exportChart();
            kpiViewPage.attachExportedChartToReport();
            attachConsoleLogs(driver);
        } catch(Exception e){
            log.error(e.getMessage());
        }

    }

    private void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToSelect) {
        kpiViewPage.setFilters(Collections.singletonList("Data Collection Statistics"));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        kpiViewPage.selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);

//        List<String> dimensionNodesToExpandList = Arrays.asList(dimensionNodesToExpand.split(","));
        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));
        kpiViewPage.selectUnfoldedDimension(dimensionNodesToSelectList);

        kpiViewPage.applyChanges();
        kpiViewPage.seeChartIsDisplayed();
    }
}
