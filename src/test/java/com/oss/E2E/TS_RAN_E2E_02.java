package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.filterpanel.FilterPanelPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.pages.radio.RanAntennaWizardPage;
import com.oss.web.InventoryViewTest;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class TS_RAN_E2E_02 extends BaseTestCase {

    private String LOCATION_NAME = "Poznan-BU1";
    private String ANTENNA_NAME = "TS_RAN_E2E_02_RANAntenna";
    private String RAN_ANTENNA_MODEL = "Generic 3-Array Antenna";
    private String GNODEB_NAME = "TS_RAN_E2E_02_gNodeB";
    private String CELL5G_NAME = "TS_RAN_E2E_02_Cell5G";
    private String ANTENNA_ARRAY_NAME = "TS_RAN_E2E_02_RANAntenna/3-Array Antenna_Array 1/Freq(0-80000)";

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @BeforeClass
    public void openCellSiteConfigurationForLocation() {
        new HomePage(driver).setAndSelectObjectType("Location");
        new OldInventoryViewPage(driver)
                .filterObject("Name", LOCATION_NAME, "Location")
                .expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Create RAN Antenna")
    public void createRANAntenna() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.selectTab("Devices");
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create RAN Antenna");
        RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ranAntennaWizardPage.setName(ANTENNA_NAME);
        ranAntennaWizardPage.setModel(RAN_ANTENNA_MODEL);
        DelayUtils.sleep(1000);
        ranAntennaWizardPage.setPreciseLocation(LOCATION_NAME);
        ranAntennaWizardPage.clickAccept();
        AntennaArrayWizardPage antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
        antennaArrayWizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
    }

    @Test(priority = 2)
    @Description("Create Hosting between Cell5G and RAN Antenna")
    public void createHostingBetweenCell5GAndRANAntenna() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        cellSiteConfigurationPage.selectTreeRow(CELL5G_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.selectArray(ANTENNA_ARRAY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}