package com.oss.floorPlan;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.floorPlan.FloorPlanLoginPage;
import com.oss.pages.floorPlan.FloorPlanPage;
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TS_PHY_INV_22 extends BaseTestCase {
    private String locationName = "TS_PHY_INV_22_TestLocation";
    private String sublocationName = "TS_PHY_INV_22_Room";
    private String filePath = "floorPlans/Testroom_EN.vsdx";
    FloorPlanPage floorPlanPage;
    LocationOverviewPage locationOverviewPage;

    @Test(priority = 1)
    @Description("Create sublocation")
    public void createSublocation() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Building");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        openLocationOverview();

        locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton("Create Sublocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType("Room");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationName(sublocationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setPreciseLocation(locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.accept();
        checkPopup("Sublocation created successfully");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Open floor plan for building")
    public void openFloorPlan() {
        locationOverviewPage.clickButton("Floor Plan");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        FloorPlanLoginPage floorPlanLoginPage = new FloorPlanLoginPage(driver);
        floorPlanPage = floorPlanLoginPage.login();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Open sublocation and import floor plan")
    public void importFloorPlan() {
        floorPlanPage.expandLocation(locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        floorPlanPage.openLocationInThisView(sublocationName + " 001");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        floorPlanPage.importFloorPlan(filePath);
    }

    @Test(priority = 4)
    @Description("Show rows layer")
    public void showRows(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        floorPlanPage.selectTab("Layers");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        floorPlanPage.showLayer("Rows");
    }

    @Test(priority = 5)
    @Description("Export floor plan")
    public void exportFloorPlan(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        floorPlanPage.exportFloorPlan();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Check summary")
    public void checkSummary() {
        floorPlanPage.selectTab("Summary");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        floorPlanPage.expandSummary("Footprints ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(floorPlanPage.getCellValue("Footprints ", 3), "0%");

        floorPlanPage.expandSummary("Cooling ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(floorPlanPage.getCellValue("Cooling ", 2), "0%");

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        floorPlanPage.expandSummary("Power ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(floorPlanPage.getCellValue("Power ", 2), "0%");
    }

    @Test(priority = 7)
    @Description("Delete sublocation")
    public void deleteSublocation() {
        HomePage homePage = floorPlanPage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Building");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        openLocationOverview();

        locationOverviewPage.selectObjectInSpecificTab(LocationOverviewPage.TabName.LOCATIONS, "Name", sublocationName + " 001");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonByLabelInSpecificTab(LocationOverviewPage.TabName.LOCATIONS, "Remove Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationOverviewPage.clickButtonInConfirmationBox("Delete");
        checkPopup("Sublocation removed successfully.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkPopup(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }

    private void openLocationOverview() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("OpenLocationOverviewAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
