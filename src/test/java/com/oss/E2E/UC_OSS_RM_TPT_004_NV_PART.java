package com.oss.E2E;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.EthernetLinkWizardPage;
import com.oss.pages.transport.trail.MicrowaveLinkWizardPage;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class UC_OSS_RM_TPT_004_NV_PART extends BaseTestCase {
    private final static String LOCATION_1_NAME = "ABC";
    private final static String LOCATION_2_NAME = "XYZ";
    private final static String MICROWAVE_LINK_NAME = "UC_OSS_RM_TPT_004_MICROWAVE_LINK";
    private final static String IDU_MODEL = "HUAWEI Technology Co.,Ltd RTN950";
    private final static String IDU_ABC_NAME = "UC_OSS_RM_TPT_004_IDU_ABC";
    private final static String IDU_XYZ_NAME = "UC_OSS_RM_TPT_004_IDU_XYZ";
    private final static String ODU_MODEL = "HUAWEI Technology Co.,Ltd ODU RTN XMC-1 07G HI";
    private final static String ODU_ABC_NAME = "UC_OSS_RM_TPT_004_ODU_ABC";
    private final static String ODU_XYZ_NAME = "UC_OSS_RM_TPT_004_ODU_XYZ";
    private final static String ENODEB_NAME = "UC_OSS_RM_TPT_004_eNodeB";
    private final static String BBU_NAME = "UC_OSS_RM_TPT_004_BBU";
    private final static String ROUTER_NAME = "UC_OSS_RM_TPT_004_Router";
    private final static String ETHERNET_LINK_NAME = "UC_OSS_RM_TPT_004_Ethernet_Link";
    private HomePage homePage;
    private NetworkViewPage networkViewPage;

    @Test(priority = 1)
    @Description("Open network view")
    public void openNetworkView() {
        homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu("Network View", "Views", "Transport");
        networkViewPage = new NetworkViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Add locations")
    public void addLocations() {
        networkViewPage.useContextAction("add_to_view_group", "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, LOCATION_1_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        networkViewPage.useContextAction("add_to_view_group", "Location");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, LOCATION_2_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.unselectObject(LOCATION_2_NAME);
    }

    @Test(priority = 3)
    @Description("Create MW IDU on location")
    public void createIDUonABC() {
        createDevice(LOCATION_1_NAME, "IDU", IDU_MODEL, IDU_ABC_NAME, LOCATION_1_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create MW IDU on location")
    public void createIDUonXYZ() {
        createDevice(LOCATION_2_NAME, "IDU", IDU_MODEL, IDU_XYZ_NAME, LOCATION_2_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 5)
    @Description("Create MW IDU on location")
    public void createODUonABC() {
        createDevice(LOCATION_1_NAME, "ODU", ODU_MODEL, ODU_ABC_NAME, LOCATION_1_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Create MW IDU on location")
    public void createODUonXYZ() {
        createDevice(LOCATION_2_NAME, "ODU", ODU_MODEL, ODU_XYZ_NAME, LOCATION_2_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 7)
    @Description("Create Microwave Link between locations")
    public void addMicrowaveLink() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", LOCATION_1_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", LOCATION_2_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Connection");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectTrailType("MicrowaveLink");
        networkViewPage.acceptTrailType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        MicrowaveLinkWizardPage microwaveLinkWizardPage = new MicrowaveLinkWizardPage(driver);
        microwaveLinkWizardPage.selectWizardMode("Create in OSS");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.setUserLabel(MICROWAVE_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.setCapacityUnit("Gbps");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.setCapacityValue("1");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.setStartTerminationNetworkElement(IDU_XYZ_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.setEndTerminationNetworkElement(IDU_ABC_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        microwaveLinkWizardPage.accept();
    }

    @Test(priority = 8)
    @Description("Add eNodeB and router to view")
    public void addToView() {
        networkViewPage.useContextAction("add_to_view_group", "Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, BBU_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        networkViewPage.useContextAction("add_to_view_group", "Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.queryElementAndAddItToView("name", TEXT_FIELD, ROUTER_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    // TODO Add 2 ethernet links with termination
    @Test(priority = 9)
    @Description("Create Ethernet Link between MW IDU and eNodeB Ethernet interface")
    public void createEthernetLinkInABC() {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", IDU_ABC_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", ENODEB_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Connection");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectTrailType("Ethernet Link");
        networkViewPage.acceptTrailType();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        EthernetLinkWizardPage ethernetLinkWizardPage = new EthernetLinkWizardPage(driver);
        ethernetLinkWizardPage.setName(ETHERNET_LINK_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ethernetLinkWizardPage.setSpeed("10M");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Create Ethernet Link between MW IDU and SFP+ 0 Ethernet interface")
    public void createEthernetLinkInXYZ() {

    }

    private void createDevice(String locationName, String equipmentType, String model, String name, String preciseLocation) {
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        networkViewPage.useContextAction("CREATE", "Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setEquipmentType(equipmentType);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setModel(model);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(name);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setPreciseLocation(preciseLocation);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.accept();
        //TODO uncomment after OSSWEB-10522
        //checkPopupText("Device test has been created successfully, click here to open Hierarchy View.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        networkViewPage.unselectObject(name);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkPopupText(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }
}
