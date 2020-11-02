package com.oss.radio;

import com.oss.BaseTestCase;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.radio.CellSiteConfigurationPage;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.lang.String.format;

public class HostingRelationCreationTest extends BaseTestCase {
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private String eNodeBName = "SeleniumENodeBTest";
    private String locationName = "SeleniumLocationTest";
    private String hostingTabName = "Hosting";

    public static void goToCellSiteConfiguration(WebDriver driver, String basicURL) {
        DelayUtils.sleep(1000);
        driver.get(format("%s#/view/radio/cellsite/xid?ids=75521556&perspective=LIVE", basicURL));
    }

    @BeforeClass
    @Description("Open Cell Site Configuration View")
    public void openCellSiteConfigurationView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        goToCellSiteConfiguration(driver, BASIC_URL);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
    }

    @Test(priority = 1)
    @Description("Select eNodeB from tree")
    public void selectBaseStation() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", locationName, eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Go to Hosting tab and create hosting relation")
    public void createHostingRelation() {
        cellSiteConfigurationPage.selectTab(hostingTabName);
        cellSiteConfigurationPage.clickPlusIconByLabel("Host on Device");
    }


}
