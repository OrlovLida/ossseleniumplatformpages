package com.oss.physical.locations;

import com.comarch.oss.web.pages.OldInventoryView.OldInventoryViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.*;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static java.lang.String.format;
import static com.comarch.oss.web.pages.OldInventoryView.helper.OldInventoryViewConstants.*;

public class LocationBlockadeTest extends BaseTestCase {

    private static final String BUILDING_NAME = "Blockade_Test_Building";
    private static final String CHP_NAME = "Blockade_Test_CHP";
    private static final String BLOCKADE_ACTION = "LocationBlockadeWizardAction";
    private static final String CREATE_LOCATION_IN_LOCATION_ACTION = "CreateLocationInLocationWizardAction";
    private static final String CREATE_POP_ACTION = "CreatePoPInLocationWizardAction";
    private static final String REMOVE_BUILDING_ACTION = "RemoveLocationWizardAction";
    public static final String BASIC_URL = CONFIGURATION.getUrl();

    private OldInventoryViewPage oldInventoryViewPage;

    public static void goToLink(WebDriver driver, String basicURL) {
        driver.get(format("%s#/view/location-inventory/wizard/physicallocation/create?closeAsModal=true&perspective=LIVE",
                basicURL));
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty"))
                        .getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
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
        goToLink(driver, BASIC_URL);
        oldInventoryViewPage = new OldInventoryViewPage(driver);
    }

    @Test(priority = 1)
    @Description("Create Building")
    public void createBuilding() {
        new LocationWizardPage(driver).createLocationInAnyWizard("Building", BUILDING_NAME);
        checkPopup();
    }

    @Test(priority = 2)
    @Description("Open Building in Location Overview")
    public void openLocationOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
    }

    @Test(priority = 3)
    @Description("Open Building in Old Inventory View")
    public void openBuildingInInventoryView() {
        new LocationOverviewPage(driver).clickButton("Inventory View");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        new NewInventoryViewPage(driver, webDriverWait).selectFirstRow()
                .callAction(ActionsContainer.SHOW_ON_GROUP_ID, IV_BUILDING_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Location in Location")
    public void createCHPinBuilding() {
        oldInventoryViewPage.selectRow("Name", BUILDING_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandCreateAndChooseAction(CREATE_LOCATION_IN_LOCATION_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        new LocationWizardPage(driver).createLocationInAnyWizard("Characteristic Point", CHP_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5)
    @Description("Create PoP")
    public void createPoP() {
        oldInventoryViewPage.selectRow("Name", BUILDING_NAME);
        oldInventoryViewPage.expandCreateAndChooseAction(CREATE_POP_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        new LocationWizardPage(driver).createPoP(CHP_NAME);
    }

    @Test(priority = 6)
    @Description("Open Location Blockade Wizard")
    public void openLocationBlockadeWizard() {
        oldInventoryViewPage.selectRow("Name", BUILDING_NAME);
        oldInventoryViewPage.expandOtherAndChooseAction(BLOCKADE_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    @Description("Block Location")
    public void blockLocation() {
        new LocationBlockadeWizardPage(driver).enableLocationBlockade("Damage");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Check Building blockade")
    public void checkBuildingBlockade() {
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.selectRow("Name", BUILDING_NAME);
        Map<String, String> properties = oldInventoryViewPage.getProperties(BUILDING_PROPERTIES_TABLE_TEST_ID);
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 9)
    @Description("Open IV for CHP from All Locations tab")
    public void openCHPfromAllLocationsTab() {
        oldInventoryViewPage.navigateToBottomTabById(BUILDING_ALL_LOCATIONS_TAB);
        oldInventoryViewPage.selectRow(BUILDING_ALL_LOCATIONS_TABLE, "Object Type","Characteristic Point");
        oldInventoryViewPage.useContextActionInCurrentTab(ActionsContainer.SHOW_ON_GROUP_ID, IV_CHP_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10)
    @Description("Check CHP blockade")
    public void checkCHPBlockade() {
        oldInventoryViewPage.selectRow("Name", CHP_NAME);
        Map<String, String> properties = oldInventoryViewPage.getProperties(CHP_PROPERTIES_TABLE_TEST_ID);
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 11)
    @Description("Open IV for PoP from All Locations tab")
    public void openPoPfromAllLocationsTab() {
        oldInventoryViewPage.navigateToBottomTabByLabel("Locations in Location");
        oldInventoryViewPage.selectRow(CHP_DETAIL_LOCATIONS_TABLE, "Object Type", "PoP");
        oldInventoryViewPage.useContextActionInCurrentTab(ActionsContainer.SHOW_ON_GROUP_ID,IV_POP_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 12)
    @Description("Check PoP blockade")
    public void checkPoPBlockade() {
        oldInventoryViewPage.selectRow("Name", "PoP");
        Map<String, String> properties = oldInventoryViewPage.getProperties(POP_PROPERTIES_TABLE_TEST_ID);
        Assert.assertEquals(properties.get("Blockade"), "True");
    }

    @Test(priority = 13)
    @Description("Open IV for Building")
    public void openBuildingIV() {
        oldInventoryViewPage.clickLinkInColumn("Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.selectRow("Name", BUILDING_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 14)
    @Description("Delete Location")
    public void deleteLocation() {
        oldInventoryViewPage.expandEditAndChooseAction(REMOVE_BUILDING_ACTION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ConfirmationBox.create(driver, webDriverWait).clickButtonByLabel("Delete");
        checkMessageText("Location has been removed.");
    }

}