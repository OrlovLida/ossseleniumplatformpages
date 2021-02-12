package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.physical.*;
import com.oss.pages.platform.ConnectionsViewPage;
import com.oss.pages.platform.OldInventoryViewPage;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import com.oss.framework.widgets.tablewidget.OldTable;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static java.lang.String.format;

public class LocationBlockade extends BaseTestCase {

    private static final String BUILDING_NAME = "Blockade_Test_Building";
    private static final String CHP_NAME = "Blockade_Test_CHP";
    private static final String POP_NAME = "Blockade_Test_PoP";
    private static final String BLOCKADE_ACTION = "LocationBlockadeWizardAction";
    private static final String CREATE_LOCATION_IN_LOCATION_ACTION = "CreateLocationInLocationWizardAction";
    private static final String CREATE_POP_ACTION = "CreatePoPInLocationWizardAction";
    private static final String BUILDING_ALL_LOCATIONS_TAB = "Tab:DetailAllLocations(Building)";
    private static final String BUILDING_ALL_LOCATIONS_TAB_TAB = "tab_Tab:DetailAllLocations(Building)";
    private static final String BUILDING_ALL_LOCATIONS_TABLE = "DetailAllLocations(Building)";
    private static final String CHP_DETAIL_LOCATIONS_TABLE = "DetailLocationsInLocation(CharacteristicPointLocation)";
    private static final String CHP_DETAIL_LOCATIONS_TAB = "Tab:DetailLocationsInLocation(CharacteristicPointLocation)";
    public static final String BASIC_URL = CONFIGURATION.getUrl();

    private LocationWizardPage locationWizardPage;
    private LocationOverviewPage locationOverviewPage;
    private OldInventoryViewPage oldInventoryViewPage;
    private LocationBlockadeWizardPage locationBlockadeWizardPage;
    private WebElement webElement;
    private OldTable oldTable;

    public static void goToLink(WebDriver driver, String basicURL) {
        DelayUtils.sleep(1000);
        //driver.get(format("%s#/view/location-inventory/wizard/physicallocation/create?closeAsModal=true&perspective=LIVE", basicURL));
        driver.get(format("%s#/view/inventory/view/type/PoP/104063967?perspective=LIVE", basicURL));

    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }

    @BeforeClass
    public void openCreateLocationWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //homePage.chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        goToLink(driver, BASIC_URL);
        locationWizardPage = new LocationWizardPage(driver);
        locationOverviewPage = new LocationOverviewPage(driver);
        oldInventoryViewPage = new OldInventoryViewPage(driver);
        locationBlockadeWizardPage = new LocationBlockadeWizardPage(driver);
    }


    /*@Test(priority = 1)
    @Description("Create Building")
    public void createBuilding() {
        locationWizardPage.createLocationStepWizard("Building", BUILDING_NAME);
        checkPopup();
    }

    @Test(priority = 2)
    @Description("Open Building in Location Overview")
    public void openLocationOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
    }

    @Test(priority = 3)
    @Description("Open Building in Inventory View")
    public void openBuildingInInventoryView() {
        locationOverviewPage.clickButton("Inventory View");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Location in Location")
    public void createCHPinBuilding() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        DelayUtils.sleep(10000);
        oldInventoryViewPage.expandCreateAndChooseAction(CREATE_LOCATION_IN_LOCATION_ACTION);
        DelayUtils.sleep(10000);
        locationWizardPage.createLocationPopupWizard("Characteristic Point", CHP_NAME);
        DelayUtils.sleep(10000);
    }

    @Test(priority = 5)
    @Description("Create PoP")
    public void createPoP() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        oldInventoryViewPage.expandCreateAndChooseAction(CREATE_POP_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.createPoP(CHP_NAME);
    }

    @Test(priority = 6)
    @Description("Open Location Blockade Wizard")
    public void openLocationBlockadeWizard() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        oldInventoryViewPage.expandOtherAndChooseAction(BLOCKADE_ACTION);
        DelayUtils.sleep(1000);
    }

    @Test(priority = 7)
    @Description("Block Location")
    public void blockLocation() {
        locationBlockadeWizardPage.enableLocationBlockade("Damage");
        DelayUtils.sleep(1000);
    }

    @Test(priority = 8)
    @Description("Check Building blockade")
    public void checkBuildingBlockade() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        Map<String, String> properties = oldInventoryViewPage.getProperties("properties(Building)");
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 9)
    @Description("Open IV for CHP from All Locations tab")
    public void openCHPfromAllLocationsTab() {
        oldInventoryViewPage.navigateToBottomTabById(BUILDING_ALL_LOCATIONS_TAB);
        oldInventoryViewPage.selectRow(BUILDING_ALL_LOCATIONS_TABLE, "Object Type", "Characteristic Point");
        TabsInterface tab = TabWindowWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tab.callActionById("NAVIGATION", "InventoryViewCharacteristicPointLocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Check CHP blockade")
    public void checkCHPBlockade() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        Map<String, String> properties = oldInventoryViewPage.getProperties("properties(CharacteristicPointLocation)");
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 11)
    @Description("Open IV for PoP from All Locations tab")
    public void openPoPfromAllLocationsTab() {
        oldInventoryViewPage.navigateToBottomTabById(CHP_DETAIL_LOCATIONS_TAB);
        oldInventoryViewPage.selectRow(CHP_DETAIL_LOCATIONS_TABLE, "Object Type", "PoP");
        TabsInterface tab = TabWindowWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tab.callActionById("NAVIGATION", "InventoryViewPoP");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 12)
    @Description("Check PoP blockade")
    public void checkPoPBlockade() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        Map<String, String> properties = oldInventoryViewPage.getProperties("properties(PoP)");
        Assert.assertEquals(properties.get("Blockade"), "True");
    }*/

    @Test(priority = 13)
    @Description("Open IV for Building")
    public void openBuildingIV() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //driver.findElement(By.xpath("(//a[contains(text(), 'Blockade_Test_Building')])[1]")).click();
        //LocatingUtils.clickUsingXpath("(//text()[contains(.,'Blockade_Test_Building')])[1]", driver);
        //driver.findElement(By.linkText("#/view/inventory/view/type/Building*")).click();
        DelayUtils.sleep(10000);
        oldTable = OldTable.createForOldInventoryView(driver, webDriverWait);
        oldTable.selectLinkInSpecificColumn("Location");
        //webElement.findElement(By.xpath("(//a[contains(text(), 'Blockade_Test_Building')])[1]/@href")).click();
    }


}
