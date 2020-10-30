package com.oss.radio;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.radio.CellSiteConfigurationPage;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;

import static java.lang.String.format;

public class HostingRelationCreationTest extends BaseTestCase {
    private CellSiteConfigurationPage cellSiteConfigurationPage;

    public static void goToCellSiteConfiguration(WebDriver driver, String basicURL) {
        DelayUtils.sleep(1000);
        driver.get(format("#/view/radio/cellsite/xid?ids=62260378&perspective=LIVE", basicURL));
    }

    @BeforeClass
    @Description("Open Cell Site Configuration View")
    public void openCellSiteConfigurationView() {
        goToCellSiteConfiguration(driver, BASIC_URL);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
    }


}
