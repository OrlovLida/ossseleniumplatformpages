package com.oss;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.MULTI_COMBOBOX;

@Listeners({TestListener.class})
public class ConfigurationTest extends BaseTestCase{

    private NewInventoryViewPage newInventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Saving new configuration for table widget")
    public void saveNewConfigurationForTableWidget() {
        newInventoryViewPage.openLoginPanel().changeSwitcherForAlphaMode().closeLoginPanel();
        newInventoryViewPage.openColumnsManagement().checkTheCheckbox("XId").uncheckTheCheckbox("Depth").applyChanges();
        newInventoryViewPage.changeColumnsOrder("XId", 3)
                .changeLayoutToVertical()
                .openFilterPanel()
                .setValueOnComboWithTags("type", "type-dropdown-search", "Building")
                .applyFilter();
        newInventoryViewPage.changeUser("webseleniumtests2", "webtests");
        newInventoryViewPage.checkFirstCheckbox().openSaveWidgetConfigurationWizard().typeName("213").setAsDefaultFor("Me");
        DelayUtils.sleep(10000);
    }

    @Test
    @Description("Saving new configuration for table widget")
    public void isConfigurationWorking() {
        // tabs order/ tabs visibility/ layout/ filter/
        // another table/ another user/ another type/
    }
}