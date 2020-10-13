package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.aggregatedEthernetInterface.AEIInventoryViewPage;
import com.oss.pages.transport.aggregatedEthernetInterface.AEIWizardPage;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kamil Jacko
 */
public class CreateAEITest extends BaseTestCase {

    private static final String WIZARDS = "Wizards";
    private static final String TRANSPORT = "Transport";
    private static final String AGGREGATED_ETHERNET_INTERFACE = "Aggregated Ethernet Interface";

    private static final String NUMBER = "22";
    private static final String DEVICE_NAME = "aeiTest";
    private static final String DESCRIPTION = "description";
    private static final String AGGREGATION_PROTOCOL = "None";
    private static final String LACP_MODE = "Active";
    private static final String LACP_SHORT_PERIOD = "True";
    private static final String MINIMUM_ACTIVE_LINKS = "12";
    private static final String MINIMUM_BANDWIDTH = "34";
    private static final String MTU = "56";
    private static final String MAC_ADDRESS = "00:0A:E6:3E:FD:E1";
    private static final String ADMINISTRATIVE_STATE = "Disabled";
    private static final String ENCAPSULATION = "ETH";
    private static final String INTERFACE1_NAME = "CLUSTER 0";
    private static final String INTERFACE2_NAME = "MGT LAN 0";

    private static final String NUMBER2 = "33";
    private static final String DEVICE_NAME2 = "aeiTest2";
    private static final String DESCRIPTION2 = "description2";
    private static final String AGGREGATION_PROTOCOL2 = "None";
    private static final String LACP_MODE2 = "Active";
    private static final String LACP_SHORT_PERIOD2 = "False";
    private static final String MINIMUM_ACTIVE_LINKS2 = "10";
    private static final String MINIMUM_BANDWIDTH2 = "5";
    private static final String MTU2 = "19";
    private static final String MAC_ADDRESS2 = "00:0A:E6:3E:FD:E2";
    private static final String ADMINISTRATIVE_STATE2 = "Enabled";
    private static final String ENCAPSULATION2 = "ETH";
    private static final String INTERFACE3_NAME = "CLUSTER 1";
    private static final String INTERFACE4_NAME = "MGT LAN 1";

    @Test(priority = 1)
    public void createAEI() {
        AEIAttributes aeiAttributes = getAEIAttributesForCreate();

        AEIWizardPage aeiWizard = goToAEIWizardPage();
        fillAEIWizardToCreate(aeiAttributes, aeiWizard);
        AEIInventoryViewPage aeiInventoryViewPage = aeiWizard.clickAccept();
        aeiInventoryViewPage.selectAEI();

        assertAEIAttributes(aeiAttributes, aeiInventoryViewPage);
        assertAssignedInterfaces(aeiInventoryViewPage, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME));
    }

    @Test(priority = 2)
    public void updateAEI() {
        AEIAttributes aeiAttributes = getAEIAttributesForUpdate();

        AEIInventoryViewPage inventoryViewBeforeUpdate = new AEIInventoryViewPage(driver);
        AEIWizardPage aeiWizard = inventoryViewBeforeUpdate.editAEI();
        fillAEIWizardToUpdate(aeiAttributes, aeiWizard);
        AEIInventoryViewPage inventoryViewAfterUpdate = aeiWizard.clickAccept();
        inventoryViewAfterUpdate.selectAEI();

         assertAEIAttributes(aeiAttributes, inventoryViewAfterUpdate);
         assertAssignedInterfaces(inventoryViewAfterUpdate, Arrays.asList(INTERFACE3_NAME, INTERFACE4_NAME));
    }

    @Test(priority = 3)
    public void removeAEI() {
        AEIInventoryViewPage aeiInventoryViewPage = new AEIInventoryViewPage(driver);
        aeiInventoryViewPage.removeAEI();

        Assert.assertTrue(aeiInventoryViewPage.isRemoveMessageCorrect());
    }

    private AEIAttributes getAEIAttributesForCreate() {
        AEIAttributes aeiAttributes = new AEIAttributes();
        aeiAttributes.number = NUMBER;
        aeiAttributes.deviceName = DEVICE_NAME;
        aeiAttributes.description = DESCRIPTION;
        aeiAttributes.aggregationProtocol = AGGREGATION_PROTOCOL;
        aeiAttributes.LACPMode = LACP_MODE;
        aeiAttributes.LACPShortPeriod = LACP_SHORT_PERIOD;
        aeiAttributes.minimumActiveLinks = MINIMUM_ACTIVE_LINKS;
        aeiAttributes.minimumBandwidth = MINIMUM_BANDWIDTH;
        aeiAttributes.MTU = MTU;
        aeiAttributes.MACAddress = MAC_ADDRESS;
        aeiAttributes.administrativeState = ADMINISTRATIVE_STATE;
        aeiAttributes.encapsulation = ENCAPSULATION;

        return aeiAttributes;
    }

    private AEIAttributes getAEIAttributesForUpdate() {
        AEIAttributes aeiAttributes = new AEIAttributes();
        aeiAttributes.number = NUMBER2;
        aeiAttributes.deviceName = DEVICE_NAME2;
        aeiAttributes.description = DESCRIPTION2;
        aeiAttributes.aggregationProtocol = AGGREGATION_PROTOCOL2;
        aeiAttributes.LACPMode = LACP_MODE2;
        aeiAttributes.LACPShortPeriod = LACP_SHORT_PERIOD2;
        aeiAttributes.minimumActiveLinks = MINIMUM_ACTIVE_LINKS2;
        aeiAttributes.minimumBandwidth = MINIMUM_BANDWIDTH2;
        aeiAttributes.MTU = MTU2;
        aeiAttributes.MACAddress = MAC_ADDRESS2;
        aeiAttributes.administrativeState = ADMINISTRATIVE_STATE2;
        aeiAttributes.encapsulation = ENCAPSULATION2;

        return aeiAttributes;
    }

    private AEIWizardPage goToAEIWizardPage() {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sidemenu.callActionByLabel(AGGREGATED_ETHERNET_INTERFACE, WIZARDS, TRANSPORT);

        return new AEIWizardPage(driver);
    }

    private void fillAEIWizardToCreate(AEIAttributes aeiAttributes, AEIWizardPage aeiWizard) {
        fulfillFirstStep(aeiWizard, aeiAttributes);
        aeiWizard.clickNextStep();
        fulfillSecondStep(aeiWizard, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME));
    }

    private void fillAEIWizardToUpdate(AEIAttributes aeiAttributes, AEIWizardPage aeiWizard) {
        setAttributesOnFirstStep(aeiWizard, aeiAttributes);
        aeiWizard.setLACPShortPeriod("False");
        aeiWizard.clickNextStep();

        setInterfacesForUpdate(aeiWizard, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME, INTERFACE3_NAME, INTERFACE4_NAME));
    }

    private void fulfillFirstStep(AEIWizardPage aeiWizard, AEIAttributes aeiAttributes) {
        setAttributesOnFirstStep(aeiWizard, aeiAttributes);
        aeiWizard.setLACPShortPeriod("True");
        aeiWizard.setEncapsulation(aeiAttributes.encapsulation);
    }

    private void fulfillSecondStep(AEIWizardPage aeiWizard, List<String> interfacesNamesToSelect) {
        aeiWizard.searchLocationAndDevice();
        aeiWizard.selectInterfacesInResourceTree(interfacesNamesToSelect);
    }

    private void setAttributesOnFirstStep(AEIWizardPage aeiWizard, AEIAttributes aeiAttributes) {
        aeiWizard.setDeviceNumber(aeiAttributes.number);
        aeiWizard.setName(aeiAttributes.deviceName);
        aeiWizard.setDescription(aeiAttributes.description);
        aeiWizard.setAggregationProtocol(aeiAttributes.aggregationProtocol);
        aeiWizard.setLACPMode(aeiAttributes.LACPMode);
        aeiWizard.setMinimumActiveLinks(aeiAttributes.minimumActiveLinks);
        aeiWizard.setMinimumBandwidth(aeiAttributes.minimumBandwidth);
        aeiWizard.setMTU(aeiAttributes.MTU);
        aeiWizard.setMACAddress(aeiAttributes.MACAddress);
        aeiWizard.setAdministrativeState(aeiAttributes.administrativeState);
    }

    private void setInterfacesForUpdate(AEIWizardPage aeiWizard, List<String> interfacesNamesToSelect) {
        aeiWizard.selectInterfacesInResourceTree(interfacesNamesToSelect);
    }

    private void assertAssignedInterfaces(AEIInventoryViewPage aeiInventoryViewPage, List<String> assignedInterfacesNames) {
        aeiInventoryViewPage.openInterfaceAssignmentTab();
        for (String assignedInterfaceName : assignedInterfacesNames) {
            String interfaceName = aeiInventoryViewPage.getInterface(assignedInterfaceName);
            Assertions.assertThat(interfaceName).isEqualTo(assignedInterfaceName);
        }
    }

    private void assertAEIAttributes(AEIAttributes aeiAttributes, AEIInventoryViewPage aeiInventoryViewPage) {
        aeiInventoryViewPage.openPropertiesTab();
        aeiInventoryViewPage.obtainPropertyValues();
        String numberValue = aeiInventoryViewPage.getNumberValue();
        Assertions.assertThat(numberValue).isEqualTo(aeiAttributes.number);
        String deviceNameValue = aeiInventoryViewPage.getDeviceNameValue();
        Assertions.assertThat(deviceNameValue).isEqualTo(aeiAttributes.deviceName);
        String descriptionValue = aeiInventoryViewPage.getDescriptionValue();
        Assertions.assertThat(descriptionValue).isEqualTo(aeiAttributes.description);
        String aggregationProtocolValue = aeiInventoryViewPage.getAggregationProtocolValue();
        Assertions.assertThat(aggregationProtocolValue).isEqualTo(aeiAttributes.aggregationProtocol);
        String LacpModeValue = aeiInventoryViewPage.getLacpModeValue();
        Assertions.assertThat(LacpModeValue).isEqualTo(aeiAttributes.LACPMode);
        String LacpShortPeriodValue = aeiInventoryViewPage.getLacpShortPeriodValue();
        Assertions.assertThat(LacpShortPeriodValue).isEqualTo(aeiAttributes.LACPShortPeriod);
        String minimumActiveLinksValue = aeiInventoryViewPage.getMinimumActiveLinksValue();
        Assertions.assertThat(minimumActiveLinksValue).isEqualTo(aeiAttributes.minimumActiveLinks);
        String minimumBandwidthValue = aeiInventoryViewPage.getMinimumBandwidthValue();
        Assertions.assertThat(minimumBandwidthValue).isEqualTo(aeiAttributes.minimumBandwidth);
        String MTUValue = aeiInventoryViewPage.getMTUValue();
        Assertions.assertThat(MTUValue).isEqualTo(aeiAttributes.MTU);
        String MACAddressValue = aeiInventoryViewPage.getMacAddressValue();
        Assertions.assertThat(MACAddressValue).isEqualTo(aeiAttributes.MACAddress);
        String administrativeState = aeiInventoryViewPage.getAdministrativeStateValue();
        Assertions.assertThat(administrativeState).isEqualTo(aeiAttributes.administrativeState);
        String encapsulation = aeiInventoryViewPage.getEncapsulationValue();
        Assertions.assertThat(encapsulation).isEqualTo(aeiAttributes.encapsulation);
    }

    private static class AEIAttributes {
        private String number;
        private String deviceName;
        private String description;
        private String aggregationProtocol;
        private String LACPMode;
        private String LACPShortPeriod;
        private String minimumActiveLinks;
        private String minimumBandwidth;
        private String MTU;
        private String MACAddress;
        private String administrativeState;
        private String encapsulation;
    }
}
