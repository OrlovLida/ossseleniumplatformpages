package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.AUTOMATIC_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CARD;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.INTERFACE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.LOGICAL_FUNCTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.MANUAL_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.PHYSICAL_DEVICE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.RESERVED_MODE;

/**
 * @author Ewa Frączek
 */

public class IPAddressAssignmentWizardPage extends BasePage {
    private static final String WIZARD_ID = "ipHostAssignmentWizardWidgetId";
    private static final String MODE_COMPONENT_ID = "hostAssignmentWizardMainStepWizardModeComponentId";
    private static final String IP_ADDRESS_MANUAL_MODE_COMPONENT_ID = "hostAssignmentWizardMainStepAddressManualModeFieldComponentId";
    private static final String IP_ADDRESS_AUTOMATIC_MODE_COMPONENT_ID = "hostAssignmentWizardMainStepAddressAutomaticModeFieldComponentId";
    private static final String IP_ADDRESS_RESERVED_MODE_COMPONENT_ID = "hostAssignmentWizardMainStepASIPHostAddressFieldComponentId";
    private static final String IP_SUBNET_COMPONENT_ID = "hostAssignmentWizardMainStepSubnetFieldComponentId";
    private static final String MASK_COMPONENT_ID = "hostAssignmentWizardMainStepMaskFieldComponentId";
    private static final String IP_NETWORK_COMPONENT_ID = "hostAssignmentWizardMainStepIPNetworkSearchFieldComponentId";
    private static final String IS_PRIMARY_COMPONENT_ID = "hostAssignmentWizardMainStepIsPrimaryFieldComponentId";
    private static final String IS_IN_NAT_COMPONENT_ID = "hostAssignmentWizardMainStepIsInNatFieldComponentId";
    private static final String ROLE_COMPONENT_ID = "hostAssignmentWizardMainStepRoleSearchFieldComponentId";
    private static final String DESCRIPTION_COMPONENT_ID = "hostAssignmentWizardMainStepDescriptionFieldComponentId";
    private static final String ASSIGNMENT_TYPE_COMPONENT_ID = "hostAssignmentWizardAssignmentStepTypeComponentId";
    private static final String SEARCH_INTERFACE_COMPONENT_ID = "hostAssignmentWizardAssignmentStepSearchInterfaceComponentId";
    private static final String SEARCH_PHYSICAL_DEVICE_COMPONENT_ID = "hostAssignmentWizardAssignmentStepSearchDeviceComponentId";
    private static final String SEARCH_LOGICAL_FUNCTION_COMPONENT_ID = "hostAssignmentWizardAssignmentStepSearchLogicalFunctionComponentId";
    private static final String SEARCH_CARD_COMPONENT_ID = "hostAssignmentWizardAssignmentStepSearchCardComponentId";
    private static final String IP_ADDRESS_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID = "hostAssignmentWizardOppositeAssignmentStepLowerIpAddressComponentId";
    private static final String IS_PRIMARY_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID = "hostAssignmentWizardOppositeAssignmentStepIsPrimaryFieldComponentId";
    private static final String IS_IN_NAT_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID = "hostAssignmentWizardOppositeAssignmentStepIsInNatFieldComponentId";
    private static final String ROLE_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID = "hostAssignmentWizardOppositeAssignmentStepRoleSearchFieldComponentId";
    private static final String DESCRIPTION_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID = "hostAssignmentWizardOppositeAssignmentStepDescriptionFieldComponentId";

    public IPAddressAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }

    public void assignMoToIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
        assignIPAddressSummaryStep();
    }

    public void assignIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressSummaryStep();
    }

    public void assignIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties,
                                IPAddressAssignmentWizardProperties oppositeSideIpAddressAssignmentWizardProperties) {
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressOppositeAssignmentStep(oppositeSideIpAddressAssignmentWizardProperties);
        assignIPAddressSummaryStep();
    }

    public void assignIPAddressFromIPAddressContext(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties,
                                                    IPAddressAssignmentWizardProperties oppositeSideIpAddressAssignmentWizardProperties) {
        assignIPAddressMainStepWithoutMode(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressOppositeAssignmentStep(oppositeSideIpAddressAssignmentWizardProperties);
        assignIPAddressSummaryStep();
    }

    @Step("Assign IP Address Main Step")
    private void assignIPAddressMainStep(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (ipAddressAssignmentWizardProperties.getWizardMode().isPresent()) {
            String mode = ipAddressAssignmentWizardProperties.getWizardMode().get();
            if (!getWizard().getComponent(MODE_COMPONENT_ID, COMBOBOX).getStringValue().equals(mode)) {
                getWizard().setComponentValue(MODE_COMPONENT_ID, mode, COMBOBOX);
                DelayUtils.waitForPageToLoad(driver, wait);
            }
            switch (mode) {
                case MANUAL_MODE:
                    fillManualModeFields(ipAddressAssignmentWizardProperties);
                    break;
                case AUTOMATIC_MODE:
                    fillAutomaticModeFields(ipAddressAssignmentWizardProperties);
                    break;
                case RESERVED_MODE:
                    fillReservedModeFields(ipAddressAssignmentWizardProperties);
                    break;
            }
        } else {
            fillManualModeFields(ipAddressAssignmentWizardProperties);
        }
        fillOptionalFieldsInMainStep(ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
    }

    @Step("Assign IP Address Main Step")
    private void assignIPAddressMainStepWithoutMode(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillOptionalFieldsInMainStep(ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
    }

    private void fillManualModeFields(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.getSubnet()
                .ifPresent(subnet -> getWizard().getComponent(IP_SUBNET_COMPONENT_ID, SEARCH_FIELD).setSingleStringValueContains(subnet));
        DelayUtils.waitForPageToLoad(driver, wait);
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().setComponentValue(IP_ADDRESS_MANUAL_MODE_COMPONENT_ID, address, TEXT_FIELD));
    }

    private void fillAutomaticModeFields(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().setComponentValue(IP_ADDRESS_AUTOMATIC_MODE_COMPONENT_ID, address, TEXT_FIELD));
        ipAddressAssignmentWizardProperties.getMask()
                .ifPresent(mask -> getWizard().setComponentValue(MASK_COMPONENT_ID, mask, TEXT_FIELD));
        ipAddressAssignmentWizardProperties.getIpNetwork()
                .ifPresent(network -> getWizard().setComponentValue(IP_NETWORK_COMPONENT_ID, network, SEARCH_FIELD));
    }

    private void fillReservedModeFields(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().getComponent(IP_ADDRESS_RESERVED_MODE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(address)));
    }

    private void fillOptionalFieldsInMainStep(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.isPrimary()
                .ifPresent(isPrimary -> getWizard().setComponentValue(IS_PRIMARY_COMPONENT_ID, isPrimary, CHECKBOX));
        ipAddressAssignmentWizardProperties.isInNAT()
                .ifPresent(isInNAT -> getWizard().setComponentValue(IS_IN_NAT_COMPONENT_ID, isInNAT, CHECKBOX));
        ipAddressAssignmentWizardProperties.getRole()
                .ifPresent(role -> getWizard().setComponentValue(ROLE_COMPONENT_ID, role, SEARCH_FIELD));
        ipAddressAssignmentWizardProperties.getDescription()
                .ifPresent(description -> getWizard().setComponentValue(DESCRIPTION_COMPONENT_ID, description, TEXT_AREA));
    }

    @Step("Assign IP Address Assignment Step")
    private void assignIPAddressAssignmentStep(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String assignmentType = ipAddressAssignmentWizardProperties.getAssignmentType().
                orElseThrow(() -> new RuntimeException("Assignment Type is not set"));
        String assignmentName = ipAddressAssignmentWizardProperties.getAssignmentName().
                orElseThrow(() -> new RuntimeException("Assignment Name is not set"));
        getWizard().setComponentValue(ASSIGNMENT_TYPE_COMPONENT_ID, assignmentType, COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        switch (assignmentType) {
            case INTERFACE:
                getWizard().getComponent(SEARCH_INTERFACE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
            case PHYSICAL_DEVICE:
                getWizard().getComponent(SEARCH_PHYSICAL_DEVICE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
            case CARD:
                getWizard().getComponent(SEARCH_CARD_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
            case LOGICAL_FUNCTION:
                getWizard().getComponent(SEARCH_LOGICAL_FUNCTION_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
    }

    @Step("Assign IP Address Opposite assignment Step")
    private void assignIPAddressOppositeAssignmentStep(IPAddressAssignmentWizardProperties oppositeIpAddressAssignmentWizardProperties) {
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeIpAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().setComponentValue(IP_ADDRESS_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, address, SEARCH_FIELD));
        oppositeIpAddressAssignmentWizardProperties.isPrimary()
                .ifPresent(isPrimary -> getWizard().setComponentValue(IS_PRIMARY_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, isPrimary, CHECKBOX));
        oppositeIpAddressAssignmentWizardProperties.isInNAT()
                .ifPresent(isInNAT -> getWizard().setComponentValue(IS_IN_NAT_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, isInNAT, CHECKBOX));
        oppositeIpAddressAssignmentWizardProperties.getRole()
                .ifPresent(role -> getWizard().setComponentValue(ROLE_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, role, SEARCH_FIELD));
        oppositeIpAddressAssignmentWizardProperties.getDescription()
                .ifPresent(description -> getWizard().setComponentValue(DESCRIPTION_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, description, TEXT_AREA));
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
    }

    @Step("Assign IP Address Summary Step")
    private void assignIPAddressSummaryStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickAccept();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
