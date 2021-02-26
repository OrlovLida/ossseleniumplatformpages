package com.oss.physical;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.physical.*;
import com.oss.pages.platform.OldInventoryViewPage;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static java.lang.String.format;

public class LocationBlockade extends BaseTestCase {

    private static final String BUILDING_NAME = "Blockade_Test_Building";
    private static final String CHP_NAME = "Blockade_Test_CHP";
    private static final String BLOCKADE_ACTION = "LocationBlockadeWizardAction";
    private static final String CREATE_LOCATION_IN_LOCATION_ACTION = "CreateLocationInLocationWizardAction";
    private static final String CREATE_POP_ACTION = "CreatePoPInLocationWizardAction";
    private static final String REMOVE_BUILDING_ACTION = "RemoveLocationWizardAction";
    private static final String BUILDING_ALL_LOCATIONS_TAB = "Tab:DetailAllLocations(Building)";
    private static final String BUILDING_ALL_LOCATIONS_TABLE = "DetailAllLocations(Building)";
    private static final String CHP_DETAIL_LOCATIONS_TABLE = "DetailLocationsInLocation(CharacteristicPointLocation)";
    public static final String BASIC_URL = CONFIGURATION.getUrl();

    private LocationWizardPage locationWizardPage;
    private LocationOverviewPage locationOverviewPage;
    private OldInventoryViewPage oldInventoryViewPage;
    private LocationBlockadeWizardPage locationBlockadeWizardPage;
    private OldTable oldTable;

    public static void goToLink(WebDriver driver, String basicURL) {
        driver.get(format("%s#/view/location-inventory/wizard/physicallocation/create?closeAsModal=true&perspective=LIVE",
                basicURL));
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty"))
                        .getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }

    private void checkMessageText(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }

    @BeforeClass
    public void openCreateLocationWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        //goToLink(driver, BASIC_URL);
        locationWizardPage = new LocationWizardPage(driver);
        locationOverviewPage = new LocationOverviewPage(driver);
        oldInventoryViewPage = new OldInventoryViewPage(driver);
        locationBlockadeWizardPage = new LocationBlockadeWizardPage(driver);
    }

    @Test(priority = 1)
    @Description("Create Building")
    public void createBuilding() {
        locationWizardPage.createLocationAnyWizard("Building", BUILDING_NAME);
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
        DelayUtils.sleep(1000);
        oldInventoryViewPage.expandCreateAndChooseAction(CREATE_LOCATION_IN_LOCATION_ACTION);
        DelayUtils.sleep(1000);
        locationWizardPage.createLocationPopupWizard("Characteristic Point", CHP_NAME);
        DelayUtils.sleep(1000);
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
        oldInventoryViewPage.selectRow(BUILDING_ALL_LOCATIONS_TABLE, "Object Type",
                "Characteristic Point");
        TabsInterface tab = TabWindowWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tab.callActionById("NAVIGATION", "InventoryViewCharacteristicPointLocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Check CHP blockade")
    public void checkCHPBlockade() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        Map<String, String> properties = oldInventoryViewPage
                .getProperties("properties(CharacteristicPointLocation)");
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 11)
    @Description("Open IV for PoP from All Locations tab")
    public void openPoPfromAllLocationsTab() {
        TabsInterface tab = TabWindowWidget.create(driver, webDriverWait);
        tab.selectTabByLabel("Locations in Location");
        oldInventoryViewPage.selectRow(CHP_DETAIL_LOCATIONS_TABLE, "Object Type", "PoP");
        tab.callActionById("NAVIGATION", "InventoryViewPoP");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 12)
    @Description("Check PoP blockade")
    public void checkPoPBlockade() {
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        Map<String, String> properties = oldInventoryViewPage.getProperties("properties(PoP)");
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 13)
    @Description("Open IV for Building")
    public void openBuildingIV() {
        oldTable = OldTable.createForOldInventoryView(driver, webDriverWait);
        oldTable.selectLinkInSpecificColumn("Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.selectRowInTableAtIndex(0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 14)
    @Description("Delete Location")
    public void deleteLocation() {
        oldInventoryViewPage.expandEditAndChooseAction(REMOVE_BUILDING_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBox popup = ConfirmationBox.create(driver, webDriverWait);
        popup.clickButtonByLabel("Delete");
        checkMessageText("Location removed successfully");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}