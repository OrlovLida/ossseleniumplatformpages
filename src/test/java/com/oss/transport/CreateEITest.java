package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.ethernetInterface.EIInventoryViewPage;
import com.oss.pages.transport.ethernetInterface.EIWizardPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

/**
 * @author Kamil Jacko
 */
public class CreateEITest extends BaseTestCase {

    private static final String EI_INVENTORY_VIEW_URL =
            "http://10.132.58.17:25080/#/view/inventory/view/type/EthernetInterface/44427880?perspective=LIVE";

    private static final String ADMINISTRATIVE_STATE = "Enabled";
    private static final String AUTO_NEGOTIATION = "Disabled";
    private static final String AUTO_NEGOTIATION_MANDATORY_CLEAR_CASE = "Enabled";
    private static final String ADMINISTRATIVE_SPEED = "10M";
    private static final String ADMINISTRATIVE_DUPLEX_MODE = "Half Duplex";
    private static final String ADMINISTRATIVE_DUPLEX_MODE_PROPERTY_TABLE = "Half-Duplex";
    private static final String ADMINISTRATIVE_DUPLEX_MODE_PROPERTY_TABLE_EMPTY = null;
    private static final String MAXIMUM_FRAME_SIZE = "200";
    private static final String FLOW_CONTROL = "Ingress";
    private static final String FLOW_CONTROL_EMPTY_VALUE = "None";
    private static final String MTU = "10";
    private static final String ENCAPSULATION = "ETH";
    private static final String BANDWIDTH = "70";
    private static final String SWITCH_PORT = "No";
    private static final String SWITCH_PORT_EMPTY_VALUE = "Undefined";
    private static final String SWITCH_MODE = "Trunk";
    private static final String ACCESS_FUNCTION = "Downlink";
    private static final String DESCRIPTION = "Opis";

    @Test(priority = 1)
    public void updateEI() {
        EIAttributes eiAttributes = getEIAttributesForUpdate();

        EIInventoryViewPage inventoryViewBeforeUpdate = navigateToEIInventoryViewPage();
        inventoryViewBeforeUpdate.selectEI();
        EIWizardPage eiWizard = inventoryViewBeforeUpdate.editEI();
        fillEIWizardForUpdate(eiAttributes, eiWizard);
        EIInventoryViewPage inventoryViewAfterUpdate = eiWizard.clickAccept();
        inventoryViewAfterUpdate.selectEI();

        assertEIAttributes(eiAttributes, inventoryViewAfterUpdate);
        assertAdministrativeDuplexMode(inventoryViewAfterUpdate);
    }

    @Test(priority = 2)
    public void clearEI() {
        EIAttributes eiAttributes = getEIAttributesForClearing();

        EIInventoryViewPage inventoryViewBeforeClearing = new EIInventoryViewPage(driver);
        EIWizardPage eiWizard = inventoryViewBeforeClearing.editEI();
        fillEIWizardEmptyAttributes(eiAttributes, eiWizard);
        EIInventoryViewPage inventoryViewAfterClearing = eiWizard.clickAccept();
        inventoryViewAfterClearing.selectEI();

        assertEIAttributes(eiAttributes, inventoryViewAfterClearing);
        assertAdministrativeDuplexModeClear(eiAttributes, inventoryViewAfterClearing);
    }

    private EIAttributes getEIAttributesForUpdate() {
        EIAttributes eiAttributes = new EIAttributes();
        eiAttributes.administrativeState = ADMINISTRATIVE_STATE;
        eiAttributes.autoNegotiation = AUTO_NEGOTIATION;
        eiAttributes.administrativeSpeed = ADMINISTRATIVE_SPEED;
        eiAttributes.administrativeDuplexMode = ADMINISTRATIVE_DUPLEX_MODE;
        eiAttributes.maximumFrameSize = MAXIMUM_FRAME_SIZE;
        eiAttributes.flowControl = FLOW_CONTROL;
        eiAttributes.MTU = MTU;
        eiAttributes.encapsulation = ENCAPSULATION;
        eiAttributes.bandwidth = BANDWIDTH;
        eiAttributes.switchPort = SWITCH_PORT;
        eiAttributes.switchMode = SWITCH_MODE;
        eiAttributes.accessFunction = ACCESS_FUNCTION;
        eiAttributes.description = DESCRIPTION;

        return eiAttributes;
    }

    private EIAttributes getEIAttributesForClearing() {
        EIAttributes eiAttributes = new EIAttributes();
        eiAttributes.autoNegotiation = AUTO_NEGOTIATION_MANDATORY_CLEAR_CASE;
        eiAttributes.flowControl = FLOW_CONTROL_EMPTY_VALUE;
        eiAttributes.encapsulation = ENCAPSULATION;
        eiAttributes.switchPort = SWITCH_PORT_EMPTY_VALUE;
        eiAttributes.administrativeDuplexMode = ADMINISTRATIVE_DUPLEX_MODE_PROPERTY_TABLE_EMPTY;

        return eiAttributes;
    }

    private EIInventoryViewPage navigateToEIInventoryViewPage() {
        driver.get(EI_INVENTORY_VIEW_URL);
        return new EIInventoryViewPage(driver);
    }

    private void fillEIWizardForUpdate(EIAttributes eiAttributes, EIWizardPage eiWizard) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAdministrativeState(eiAttributes.administrativeState);
        eiWizard.setAutoNegotiation(eiAttributes.autoNegotiation);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAdministrativeSpeed(eiAttributes.administrativeSpeed);
        eiWizard.setAdministrativeDuplexMode(eiAttributes.administrativeDuplexMode);
        eiWizard.setMaximumFrameSize(eiAttributes.maximumFrameSize);
        eiWizard.setFlowControl(eiAttributes.flowControl);
        eiWizard.setMTU(eiAttributes.MTU);
        eiWizard.setEncapsulation(eiAttributes.encapsulation);
        eiWizard.setBandwidth(eiAttributes.bandwidth);
        eiWizard.setSwitchPort(eiAttributes.switchPort);
        eiWizard.setSwitchMode(eiAttributes.switchMode);
        eiWizard.setAccessFunction(eiAttributes.accessFunction);
        eiWizard.setDescription(eiAttributes.description);
    }

    private void fillEIWizardEmptyAttributes(EIAttributes eiAttributes, EIWizardPage eiWizard) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAutoNegotiation(eiAttributes.autoNegotiation);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.clearAdministrativeState();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.clearMaximumFrameSize();
        eiWizard.clearFlowControl();
        eiWizard.clearMTU();
        eiWizard.clearBandwidth();
        eiWizard.clearSwitchPort();
        eiWizard.clearSwitchMode();
        eiWizard.clearAccessFunction();
        eiWizard.clearDescription();
    }

    private void assertEIAttributes(EIAttributes eiAttributes, EIInventoryViewPage eiInventoryViewPage) {
        eiInventoryViewPage.openPropertiesTab();
        eiInventoryViewPage.obtainPropertyValues();

        String administrativeState = eiInventoryViewPage.getAdministrativeStateValue();
        Assertions.assertThat(administrativeState).isEqualTo(eiAttributes.administrativeState);
        String autoNegotiation = eiInventoryViewPage.getAutoNegotiationValue();
        Assertions.assertThat(autoNegotiation).isEqualTo(eiAttributes.autoNegotiation);
        String administrativeSpeed = eiInventoryViewPage.getAdministrativeSpeed();
        Assertions.assertThat(administrativeSpeed).isEqualTo(eiAttributes.administrativeSpeed);
        String maximumFrameSize = eiInventoryViewPage.getMaximumFrameSizeValue();
        Assertions.assertThat(maximumFrameSize).isEqualTo(eiAttributes.maximumFrameSize);
        String flowControl = eiInventoryViewPage.getFlowControlValue();
        Assertions.assertThat(flowControl).isEqualTo(eiAttributes.flowControl);
        String MTUValue = eiInventoryViewPage.getMTUValue();
        Assertions.assertThat(MTUValue).isEqualTo(eiAttributes.MTU);
        String encapsulation = eiInventoryViewPage.getEncapsulationValue();
        Assertions.assertThat(encapsulation).isEqualTo(eiAttributes.encapsulation);
        String bandWidth = eiInventoryViewPage.getBandwidthValue();
        Assertions.assertThat(bandWidth).isEqualTo(eiAttributes.bandwidth);
        String switchPort = eiInventoryViewPage.getSwitchPortValue();
        Assertions.assertThat(switchPort).isEqualTo(eiAttributes.switchPort);
        String switchMode = eiInventoryViewPage.getSwitchModeValue();
        Assertions.assertThat(switchMode).isEqualTo(eiAttributes.switchMode);
        String accessFunction = eiInventoryViewPage.getAccessFunctionValue();
        Assertions.assertThat(accessFunction).isEqualTo(eiAttributes.accessFunction);
        String description = eiInventoryViewPage.getDescriptionValue();
        Assertions.assertThat(description).isEqualTo(eiAttributes.description);
    }

    private void assertAdministrativeDuplexMode(EIInventoryViewPage eiInventoryViewPage) {
        String administrativeDuplexMode = eiInventoryViewPage.getAdministrativeDuplexMode();
        Assertions.assertThat(administrativeDuplexMode).isEqualTo(ADMINISTRATIVE_DUPLEX_MODE_PROPERTY_TABLE);
    }

    private void assertAdministrativeDuplexModeClear(EIAttributes eiAttributes, EIInventoryViewPage eiInventoryViewPage) {
        String administrativeDuplexMode = eiInventoryViewPage.getAdministrativeDuplexMode();
        Assertions.assertThat(administrativeDuplexMode).isEqualTo(eiAttributes.administrativeDuplexMode);
    }

    private static class EIAttributes {
        private String administrativeState;
        private String autoNegotiation;
        private String administrativeSpeed;
        private String administrativeDuplexMode;
        private String maximumFrameSize;
        private String flowControl;
        private String MTU;
        private String encapsulation;
        private String bandwidth;
        private String switchPort;
        private String switchMode;
        private String accessFunction;
        private String description;
    }
}
