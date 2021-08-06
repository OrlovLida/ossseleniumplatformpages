package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.transport.aei.AEIWizardPage;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * @author Kamil Jacko
 */
public class AEITest extends BaseTestCase {

    private static final String PRE_CREATED_LOCATION = "Gliwice-BU1";
    private static final String PRE_CREATED_DEVICE = "SeleniumTestDeviceAEI";

    private static final String INTERFACE1_NAME = "CLUSTER 0";
    private static final String INTERFACE2_NAME = "MGT LAN 0";
    private static final String INTERFACE3_NAME = "CLUSTER 1";
    private static final String INTERFACE4_NAME = "MGT LAN 1";

    private static final String EDIT_AGGREGATED_ETHERNET_INTERFACE_CONTEXT_ACTION_ID = "EditAggregatedEthernetInterfaceContextAction";
    private static final String DELETE_AGGREGATED_ETHERNET_INTERFACE_CONTEXT_ACTION_ID = "DeleteAggregatedEthernetInterfaceContextAction";
    private static final String SERVER_TERMINATION_POINTS_TABLE_NAME_COLUMN_LABEL = "Name";
    private static final String BOTTOM_SERVER_TERMINATION_POINTS_TAB_ID = "Tab:DetailServerNodes(AggregatedEthernetInterface)";
    private static final String BOTTOM_SERVER_TERMINATION_POINTS_TABLE_TEST_ID = "DetailServerNodes(AggregatedEthernetInterface)";
    private static final String BOTTOM_PROPERTIES_TABLE_TEST_ID = "properties(AggregatedEthernetInterface)";

    private Map<String, String> propertyNamesToValues;

    @Test(priority = 1)
    @Step("Create Aggregated Ethernet Interface with set all attributes")
    public void createAEI() {
        AEIAttributes aeiAttributes = getAEIAttributesForCreate();

        AEIWizardPage aeiWizard = goToAEIWizardPage();
        fillAEIWizardToCreate(aeiAttributes, aeiWizard);
        OldInventoryViewPage inventoryViewPage = aeiWizard.clickAccept();
        inventoryViewPage.selectRowInTableAtIndex(0);
        propertyNamesToValues = inventoryViewPage.getProperties(BOTTOM_PROPERTIES_TABLE_TEST_ID);

        assertAEIAttributes(aeiAttributes);
        assertAssignedInterfaces(inventoryViewPage, INTERFACE1_NAME, INTERFACE2_NAME);
    }

    @Test(priority = 2)
    @Step("Update all Aggregated Ethernet Interface attributes to other values")
    public void updateAEI() {
        AEIAttributes aeiAttributes = getAEIAttributesForUpdate();

        AEIWizardPage aeiWizard = goToWizardAtUpdate();
        fillAEIWizardToUpdate(aeiAttributes, aeiWizard);
        OldInventoryViewPage inventoryViewPage = aeiWizard.clickAccept();
        inventoryViewPage.selectRowInTableAtIndex(0);
        propertyNamesToValues = inventoryViewPage.getProperties(BOTTOM_PROPERTIES_TABLE_TEST_ID);

        assertAEIAttributes(aeiAttributes);
        assertAssignedInterfaces(inventoryViewPage, INTERFACE3_NAME, INTERFACE4_NAME);
    }

    @Test(priority = 3)
    @Step("Remove Aggregated Ethernet Interface")
    public void removeAEI() {
        OldInventoryViewPage inventoryViewPage = new OldInventoryViewPage(driver);
        inventoryViewPage.expandEditAndChooseAction(DELETE_AGGREGATED_ETHERNET_INTERFACE_CONTEXT_ACTION_ID);
        inventoryViewPage.clickConfirmRemovalButton();

        Assert.assertTrue(isRemovalPopupCorrect());
    }

    private AEIAttributes getAEIAttributesForCreate() {
        AEIAttributes aeiAttributes = new AEIAttributes();
        aeiAttributes.number = "22";
        aeiAttributes.deviceName = "aeiTest";
        aeiAttributes.description = "description";
        aeiAttributes.aggregationProtocol = "LACP";
        aeiAttributes.lacpMode = "Active";
        aeiAttributes.lacpShortPeriod = "True";
        aeiAttributes.minimumActiveLinks = "12";
        aeiAttributes.minimumBandwidth = "34";
        aeiAttributes.mtu = "56";
        aeiAttributes.macAddress = "00:0A:E6:3E:FD:E1";
        aeiAttributes.encapsulation = "ETH";

        return aeiAttributes;
    }

    private AEIAttributes getAEIAttributesForUpdate() {
        AEIAttributes aeiAttributes = new AEIAttributes();
        aeiAttributes.number = "33";
        aeiAttributes.deviceName = "aeiTest2";
        aeiAttributes.description = "description2";
        aeiAttributes.aggregationProtocol = "NONE";
        aeiAttributes.lacpMode = "Passive";
        aeiAttributes.lacpShortPeriod = "False";
        aeiAttributes.minimumActiveLinks = "10";
        aeiAttributes.minimumBandwidth = "5";
        aeiAttributes.mtu = "19";
        aeiAttributes.macAddress = "00:0A:E6:3E:FD:E2";
        aeiAttributes.encapsulation = "ETH";

        return aeiAttributes;
    }

    private AEIWizardPage goToAEIWizardPage() {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sidemenu.callActionByLabel("Create Aggregated Ethernet Interface", "Network domains", "Transport & IP");

        return new AEIWizardPage(driver);
    }

    private void fillAEIWizardToCreate(AEIAttributes aeiAttributes, AEIWizardPage aeiWizard) {
        fulfillFirstStep(aeiWizard, aeiAttributes);
        aeiWizard.clickNextStep();
        fulfillSecondStep(aeiWizard, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME));
    }

    private void fillAEIWizardToUpdate(AEIAttributes aeiAttributes, AEIWizardPage aeiWizard) {
        setAttributesOnFirstStep(aeiWizard, aeiAttributes);
        aeiWizard.clickNextStep();
        fulfillSecondStep(aeiWizard, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME, INTERFACE3_NAME, INTERFACE4_NAME));
    }

    private void fulfillFirstStep(AEIWizardPage aeiWizard, AEIAttributes aeiAttributes) {
        setAttributesOnFirstStep(aeiWizard, aeiAttributes);
        aeiWizard.setEncapsulation(aeiAttributes.encapsulation);
    }

    private void fulfillSecondStep(AEIWizardPage aeiWizard, List<String> interfacesNamesToSelect) {
        aeiWizard.assignEthernetInterfaces(interfacesNamesToSelect, PRE_CREATED_LOCATION, PRE_CREATED_DEVICE);
    }

    private void setAttributesOnFirstStep(AEIWizardPage aeiWizard, AEIAttributes aeiAttributes) {
        aeiWizard.setNumber(aeiAttributes.number);
        aeiWizard.setName(aeiAttributes.deviceName);
        aeiWizard.setDescription(aeiAttributes.description);
        aeiWizard.setAggregationProtocol(aeiAttributes.aggregationProtocol);
        aeiWizard.setLACPMode(aeiAttributes.lacpMode);
        aeiWizard.setLACPShortPeriod(aeiAttributes.lacpShortPeriod);
        aeiWizard.setMinimumActiveLinks(aeiAttributes.minimumActiveLinks);
        aeiWizard.setMinimumBandwidth(aeiAttributes.minimumBandwidth);
        aeiWizard.setMTU(aeiAttributes.mtu);
        aeiWizard.setMACAddress(aeiAttributes.macAddress);
    }

    private void assertAEIAttributes(AEIAttributes aeiAttributes) {
        String numberValue = propertyNamesToValues.get("Number");
        String deviceNameValue = propertyNamesToValues.get("Name");
        String descriptionValue = propertyNamesToValues.get("Description");
        String aggregationProtocolValue = propertyNamesToValues.get("Aggregation Protocol");
        String LacpModeValue = propertyNamesToValues.get("LACP Mode");
        String LacpShortPeriodValue = propertyNamesToValues.get("LACP Short Period");
        String minimumActiveLinksValue = propertyNamesToValues.get("Minimum Links");
        String minimumBandwidthValue = propertyNamesToValues.get("Minimum Bandwidth [mbps]");
        String MTUValue = propertyNamesToValues.get("MTU");
        String MACAddressValue = propertyNamesToValues.get("MAC address");
        String encapsulation = propertyNamesToValues.get("Encapsulation");

        Assert.assertEquals(numberValue, aeiAttributes.number);
        Assert.assertEquals(deviceNameValue, aeiAttributes.deviceName);
        Assert.assertEquals(descriptionValue, aeiAttributes.description);
        Assert.assertEquals(aggregationProtocolValue.toUpperCase(), aeiAttributes.aggregationProtocol.toUpperCase());
        Assert.assertEquals(LacpModeValue, aeiAttributes.lacpMode);
        Assert.assertEquals(LacpShortPeriodValue, aeiAttributes.lacpShortPeriod);
        Assert.assertEquals(minimumActiveLinksValue, aeiAttributes.minimumActiveLinks);
        Assert.assertEquals(minimumBandwidthValue, aeiAttributes.minimumBandwidth);
        Assert.assertEquals(MTUValue, aeiAttributes.mtu);
        Assert.assertEquals(MACAddressValue, aeiAttributes.macAddress);
        Assert.assertEquals(encapsulation, aeiAttributes.encapsulation);
    }

    private void assertAssignedInterfaces(OldInventoryViewPage inventoryViewPage, String... expectedInterfacesNames) {
        OldTable serverTerminationPointsTable = inventoryViewPage.getTableWidgetForTab(BOTTOM_SERVER_TERMINATION_POINTS_TAB_ID, BOTTOM_SERVER_TERMINATION_POINTS_TABLE_TEST_ID);

        List<String> assignedInterfaces = new ArrayList<>();
        int numberOfRowsInTable = serverTerminationPointsTable.getNumberOfRowsInTable(SERVER_TERMINATION_POINTS_TABLE_NAME_COLUMN_LABEL);
        for (int i = 0; i < numberOfRowsInTable; i++) {
            assignedInterfaces.add(serverTerminationPointsTable.getCellValue(i, SERVER_TERMINATION_POINTS_TABLE_NAME_COLUMN_LABEL));
        }
        Assert.assertEquals(assignedInterfaces.size(), expectedInterfacesNames.length);
        Assert.assertTrue(assignedInterfaces.containsAll(Arrays.asList(expectedInterfacesNames)));
    }

    private AEIWizardPage goToWizardAtUpdate() {
        OldInventoryViewPage inventoryViewPage = new OldInventoryViewPage(driver);
        inventoryViewPage.expandEditAndChooseAction(EDIT_AGGREGATED_ETHERNET_INTERFACE_CONTEXT_ACTION_ID);
        return new AEIWizardPage(driver);
    }

    private boolean isRemovalPopupCorrect() {
        SystemMessageInterface messages = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> popups = messages.getMessages();
        return popups.stream().anyMatch(this::isPopupValidForDeletion);
    }

    private boolean isPopupValidForDeletion(SystemMessageContainer.Message popupMessage) {
        return popupMessage.getMessageType() == SystemMessageContainer.MessageType.SUCCESS;
    }

    private static class AEIAttributes {
        private String number;
        private String deviceName;
        private String description;
        private String aggregationProtocol;
        private String lacpMode;
        private String lacpShortPeriod;
        private String minimumActiveLinks;
        private String minimumBandwidth;
        private String mtu;
        private String macAddress;
        private String encapsulation;
    }
}
