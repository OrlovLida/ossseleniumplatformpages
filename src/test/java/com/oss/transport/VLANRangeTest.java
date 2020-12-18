package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filterpanel.FilterPanelPage;
import com.oss.pages.transport.VLANRangeWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners({ TestListener.class })
public class VLANRangeTest extends BaseTestCase {

    private VLANRangeWizardPage vLANRangeWizardPage;
    private NewInventoryViewPage newInventoryViewPage;
    private FilterPanelPage filterPanel;

    //TODO: assertions, redo delays

    @BeforeClass
    public void openVLANRangeWizardPage() {
        vLANRangeWizardPage = vLANRangeWizardPage.goToVLANRangeWizardPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Open Roles")
    public void SetFields() {
        vLANRangeWizardPage.SetName()
                            .SetRange()
                            .SetDescription()
                            .Save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Check if VLAN Range is created")
    public void CheckIV() {
        newInventoryViewPage = newInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "VLANRange");
        newInventoryViewPage.openFilterPanel();
        DelayUtils.sleep();
        filterPanel = new FilterPanelPage(driver);
        filterPanel.changeValueInLocationNameInput("CreateVLANRangeSeleniumTest1");
        filterPanel.applyFilter();
        newInventoryViewPage.selectFirstRow();
        driver.navigate().refresh();

    }

    @Test(priority = 3)
    @Description("Edit VLAN Range")
    public void EditVLANRange(){
        newInventoryViewPage = newInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "VLANRange");
        newInventoryViewPage.openFilterPanel();
        DelayUtils.sleep();
        filterPanel = new FilterPanelPage(driver);

        filterPanel.changeValueInLocationNameInput("CreateVLANRangeSeleniumTest1");

        filterPanel.applyFilter();

        newInventoryViewPage.selectFirstRow();

        newInventoryViewPage.callAction("EDIT","EditVLANRangeContextAction");
        DelayUtils.sleep();

        newInventoryViewPage.editTextFields("VLAN_RANGE_NAME_ATTRIBUTE_ID", Input.ComponentType.TEXT_FIELD, "EditVLANRangeSeleniumTest2")
                .editTextFields("VLAN_RANGE_VLAN_ID_RANGE_ATTRIBUTE_ID", Input.ComponentType.TEXT_FIELD, "1, 2, 5-9")
                .editTextFields("VLAN_RANGE_DESCRIPTION_ATTRIBUTE_ID", Input.ComponentType.TEXT_AREA, "Description2");

        Button.createBySelectorAndId(driver, "a", "VLAN_RANGE_BUTTON_PANEL_ID-0").click();
        DelayUtils.sleep();
        driver.navigate().refresh();
    }

    @Test(priority = 4)
    @Description("Delete VLAN Range")
    public void DeleteVLANRange(){
        newInventoryViewPage = newInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "VLANRange");
        DelayUtils.sleep();
        newInventoryViewPage.openFilterPanel();
        DelayUtils.sleep(5000);

        DelayUtils.sleep();
        filterPanel = new FilterPanelPage(driver);
        DelayUtils.sleep(5000);

        filterPanel.changeValueInLocationNameInput("EditVLANRangeSeleniumTest2");
        DelayUtils.sleep(5000);

        filterPanel.applyFilter();
        DelayUtils.sleep(5000);

        newInventoryViewPage.selectFirstRow();
        DelayUtils.sleep(5000);

        newInventoryViewPage.deleteObject();

    }

}
