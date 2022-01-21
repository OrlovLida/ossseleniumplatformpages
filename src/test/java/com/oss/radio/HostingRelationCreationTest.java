package com.oss.radio;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.HostingWizardPage;

import io.qameta.allure.Description;

import static java.lang.String.format;

public class HostingRelationCreationTest extends BaseTestCase {
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private String eNodeBName = "SeleniumENodeBTest";
    private String locationName = "SeleniumLocationTest";
    private String hostingTabName = "Hosting";
    private String cell4GName = "cell4";
    private String showOnlyCompatible = "false";
    private String nameOfBBU = "SeleniumTestBBU";
    private String nameOfRRU = "SeleniumTestRadioUnit";
    private String antennaArrayName = "APE4518R14V06_Lr1";

    public static void goToCellSiteConfiguration(WebDriver driver, String basicURL) {
        DelayUtils.sleep(1000);
        driver.get(format("%s#/view/radio/cellsite/xid?ids=75521556&perspective=LIVE", basicURL));
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @BeforeClass
    @Description("Open Cell Site Configuration View")
    public void openCellSiteConfigurationView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        goToCellSiteConfiguration(driver, BASIC_URL);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
    }

    @Test(priority = 1)
    @Description("Select eNodeB from tree")
    public void selectBaseStation() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", locationName, eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Create hosting relation between eNodeB and BBU")
    public void hostENodeBOnBBU() {
        cellSiteConfigurationPage.selectTab(hostingTabName);
        cellSiteConfigurationPage.useTableContextActionByLabel("Host on Device");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.onlyCompatible(showOnlyCompatible);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.setDevice(nameOfBBU);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Select Cell 4G and open Hosting tab")
    public void selectCell4G() {
        cellSiteConfigurationPage.getTree().expandTreeRow(eNodeBName);
        cellSiteConfigurationPage.selectTreeRow(cell4GName);
        cellSiteConfigurationPage.selectTab(hostingTabName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GOnRRU() {
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Device");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.setDevice(nameOfRRU);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GOnRANAntennaArray() {
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
        HostingWizardPage wizard = new HostingWizardPage(driver);
        wizard.setHostingContains(antennaArrayName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Delete hosting relation between Cell 4G and RAN Antenna Array")
    public void deleteHostingRelationCell4GRANAntennaArray() {
        cellSiteConfigurationPage.selectTab("Hosting");
        cellSiteConfigurationPage.filterObject("Hosting Resource", antennaArrayName);
        cellSiteConfigurationPage.removeObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    @Description("Delete hosting relation between Cell 4G and RRU")
    public void deleteHostingRelationCell4GRRU() {
        cellSiteConfigurationPage.selectTab("Hosting");
        cellSiteConfigurationPage.filterObject("Hosting Resource", nameOfRRU);
        cellSiteConfigurationPage.removeObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Delete hosting relation between eNodeB and BBU")
    public void deleteHostingRelationENodeBBBU() {
        cellSiteConfigurationPage.selectTreeRow(eNodeBName);
        cellSiteConfigurationPage.selectTab("Hosting");
        cellSiteConfigurationPage.filterObject("Hosting Resource", nameOfBBU);
        cellSiteConfigurationPage.removeObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}