package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.radio.Cell4GBulkWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.ENodeBWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import io.qameta.allure.Description;
import org.testng.annotations.Test;


public class CreationEnodebCell4g extends BaseTestCase {

    private String locationName = "Poznan-BU1";
    private String eNodeBModel = "HUAWEI Technology Co.,Ltd BTS5900";
    private String eNodeBID = "1" + (int) (Math.random() * 10);
    private String eNodeBName = "SeleniumTestsEnodeB" + (int) (Math.random() * 10);
    private String MCCMNCPrimary = "DU [mcc: 424, mnc: 03]";
    private String carrier = "L800-B20-5 (6175)";
    private String useAvailableID = "true";
    private String cellNames[] = new String[] {"cell10", "cell20", "cell30"};
    private String amountOfCells = Integer.toString(cellNames.length);

    @BeforeClass
    @Description("Open Lab Network View")
    public void openNetworkView() {
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("LAB Network View","Favourites", "SeleniumTests");
    }

    @Test(priority = 1)
    @Description("Select Location Poznan-BU1")
    public void selectLocation() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", locationName);
    }

    @Test(priority = 2)
    @Description("Open Cell Site Configuration View")
    public void openCellSiteConfigurationView() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction("NAVIGATION", "Cell Site Configuration");
    }

    @Test(priority = 3)
    @Description("Create eNodeB")
    public void createENodeB() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create eNodeB");
        ENodeBWizardPage eNodeBWizard = new ENodeBWizardPage(driver);
        eNodeBWizard.createENodeB(eNodeBName, eNodeBID, eNodeBModel, MCCMNCPrimary);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created eNodeB"));
    }

    @Test(priority = 4)
    @Description("Expand tree and select eNodeB")
    public void selectEnodeBFromTree() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToENodeB("Site", locationName, eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GBulk() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTab("Cells 4G");
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Cell 4G Bulk Wizard");
        Cell4GBulkWizardPage cell4GBulkWizardPage = new Cell4GBulkWizardPage(driver);
        cell4GBulkWizardPage.createCell4GBulkWizard(amountOfCells, carrier, useAvailableID, cellNames);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Cells 4G created success"));
    }

    @Test(priority = 6)
    @Description("Delete Cell4G")
    public void deleteCell4G() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTab("Cells 4G");
        for(String cellName : cellNames) {
            cellSiteConfigurationPage.filterObject("Name", cellName);
            cellSiteConfigurationPage.clickRemoveIcon();
            Wizard deletionWizard = Wizard.createWizard(driver, webDriverWait);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deletionWizard.clickDelete();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                    .getMessages().get(0).getText().contains("Deleted Cell 4G"));
        }
    }

    @Test(priority = 7)
    @Description("Delete eNodeB")
    public void deleteENodeB() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTreeRow(locationName);
        cellSiteConfigurationPage.selectTab("Base Stations").filterObject("Name", eNodeBName);
        cellSiteConfigurationPage.clickRemoveIcon();
        Wizard deletionWizard = Wizard.createWizard(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deletionWizard.clickDelete();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Deleted eNodeBs successfully"));
    }
}
