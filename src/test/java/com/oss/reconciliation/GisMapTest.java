package com.oss.reconciliation;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.gisView.GisViewPage;

public class GisMapTest extends BaseTestCase {

    private GisViewPage gisViewPage;

    @BeforeClass
    public void openGisViewByLink() {
        gisViewPage = GisViewPage.goToGisViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void test() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.searchFirstResult("gliwice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        gisViewPage.clickOnMapByCoordinates(2, 4);  //click on empty field to uncheck building
        gisViewPage.useContextAction("Draw Single Location", "Manhole");
        gisViewPage.clickOnMapByCoordinates(4, 2);

        //handle Manhole wizard

        gisViewPage.clickOnMapByCoordinates(2, 4); //click on empty field to uncheck manhole
        gisViewPage.useContextActionById("Draw New Duct");
        gisViewPage.clickOnMapByCoordinates(2, 2); //click on midle of the screen (building)
        gisViewPage.clickOnMapByCoordinates(4, 2); //click on manhole location
        gisViewPage.doubleClickOnMapByCoordinates(4, 2);  //doubleclick doesn't work correctly if manhole isn't clicked before

        //handle Duct wizard

        DelayUtils.sleep(10000);
    }

}
