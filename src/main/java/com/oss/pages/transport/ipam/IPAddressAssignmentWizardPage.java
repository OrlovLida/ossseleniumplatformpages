package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.OBJECT_SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.CARD;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.EXISTING_ADDRESS_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.INTERFACE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.LOGICAL_FUNCTION;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.MANUAL_ADDRESS_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NEW_ADDRESS_MODE;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.PHYSICAL_DEVICE;

/**
 * @author Ewa Frączek
 */

public class IPAddressAssignmentWizardPage extends BasePage {
    private static final String WIZARD_ID = "ipHostAssignmentWizardWidgetId";
    private static final String SUMMARY_WIZARD_ID = "ipHostAssignmentWizardSummaryId_prompt-card";
    private static final String MODE_COMPONENT_ID = "hostAssignmentWizardMainStepWizardModeComponentId";
    private static final String IP_ADDRESS_MANUAL_MODE_COMPONENT_ID = "hostAssignmentWizardMainStepAddressManualModeFieldComponentId";
    private static final String IP_ADDRESS_AUTOMATIC_MODE_COMPONENT_ID = "hostAssignmentWizardMainStepAddressAutomaticModeFieldComponentId";
    private static final String IP_ADDRESS_RESERVED_MODE_COMPONENT_ID = "hostAssignmentWizardMainStepASIPHostAddressFieldComponentId";
    private static final String IP_SUBNET_COMPONENT_ID = "hostAssignmentWizardMainStepSubnetFieldComponentId";
    private static final String MASK_COMPONENT_ID = "hostAssignmentWizardMainStepMaskFieldComponentId";
    private static final String IP_NETWORK_COMPONENT_ID = "hostAssignmentWizardMainStepIPNetworkSearchFieldComponentId";
    private static final String IS_PRIMARY_COMPONENT_ID = "hostAssignmentWizardAssignmentStepIsPrimaryFieldComponentId";
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
    private static final String ASSIGN_BUTTON = "wizard-submit-button-ipHostAssignmentWizardWidgetId";
    private static final String CLOSE_BUTTON = "hostAssignmentWizardSummaryStepButtonsComponentId-0";
    private static final String OPPOSITE_CHECKBOX_ID = "hostAssignmentWizardAssignmentStepOppositeAssignmentComponentId";

    public IPAddressAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }

    public void assignMoToIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep();
        assignIPAddressSummaryStep();
    }

    public void assignIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
    }

    public void assignIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties,
                                IPAddressAssignmentWizardProperties oppositeSideIpAddressAssignmentWizardProperties) {
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressOppositeAssignmentStep(oppositeSideIpAddressAssignmentWizardProperties);
    }

    public void assignIPAddressFromIPAddressContext(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties,
                                                    IPAddressAssignmentWizardProperties oppositeSideIpAddressAssignmentWizardProperties) {
        assignIPAddressMainStepWithoutMode(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressOppositeAssignmentStep(oppositeSideIpAddressAssignmentWizardProperties);
    }

    @Step("Assign IP Address Summary Step")
    public void assignIPAddressSummaryStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        createPopup().clickButtonById(CLOSE_BUTTON);
    }

    @Step("Assign IP Address Assignment Step")
    public void assignIPAddressAssignmentStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickButtonById(ASSIGN_BUTTON);
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
                case NEW_ADDRESS_MODE:
                    fillManualModeFields(ipAddressAssignmentWizardProperties);
                    break;
                case MANUAL_ADDRESS_MODE:
                    fillAutomaticModeFields(ipAddressAssignmentWizardProperties);
                    break;
                case EXISTING_ADDRESS_MODE:
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
                .ifPresent(address -> getWizard().setComponentValue(IP_ADDRESS_MANUAL_MODE_COMPONENT_ID, address, SEARCH_FIELD));
    }

    private void fillAutomaticModeFields(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().setComponentValue(IP_ADDRESS_AUTOMATIC_MODE_COMPONENT_ID, address, TEXT_FIELD));
        DelayUtils.waitForPageToLoad(driver, wait);
        ipAddressAssignmentWizardProperties.getMask()
                .ifPresent(mask -> getWizard().setComponentValue(MASK_COMPONENT_ID, mask, TEXT_FIELD));
        DelayUtils.waitForPageToLoad(driver, wait);
        ipAddressAssignmentWizardProperties.getIpNetwork()
                .ifPresent(network -> getWizard().setComponentValue(IP_NETWORK_COMPONENT_ID, network, SEARCH_FIELD));
    }

    private void fillReservedModeFields(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().getComponent(IP_ADDRESS_RESERVED_MODE_COMPONENT_ID, OBJECT_SEARCH_FIELD).setValueContains(Data.createFindFirst(address)));
    }

    private void fillOptionalFieldsInMainStep(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties) {
        ipAddressAssignmentWizardProperties.isInNAT()
                .ifPresent(isInNAT -> getWizard().setComponentValue(IS_IN_NAT_COMPONENT_ID, isInNAT, CHECKBOX));
        DelayUtils.waitForPageToLoad(driver, wait);
        ipAddressAssignmentWizardProperties.getRole()
                .ifPresent(role -> getWizard().setComponentValue(ROLE_COMPONENT_ID, role, SEARCH_FIELD));
        DelayUtils.waitForPageToLoad(driver, wait);
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
                getWizard().getComponent(SEARCH_INTERFACE_COMPONENT_ID, OBJECT_SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                DelayUtils.waitForPageToLoad(driver, wait);
                if (getWizard().isNextStepPresent()) {
                    getWizard().clickNext();
                } else {
                    assignIPAddressAssignmentStep();
                }
                break;
            case PHYSICAL_DEVICE:
                getWizard().getComponent(SEARCH_PHYSICAL_DEVICE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                getWizard().clickButtonById(ASSIGN_BUTTON);
                break;
            case CARD:
                getWizard().getComponent(SEARCH_CARD_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                getWizard().clickButtonById(ASSIGN_BUTTON);
                break;
            case LOGICAL_FUNCTION:
                getWizard().getComponent(SEARCH_LOGICAL_FUNCTION_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                getWizard().clickButtonById(ASSIGN_BUTTON);
                break;
        }
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Assign IP Address Opposite assignment Step")
    private void assignIPAddressOppositeAssignmentStep(IPAddressAssignmentWizardProperties oppositeIpAddressAssignmentWizardProperties) {
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeIpAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> getWizard().setComponentValue(IP_ADDRESS_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, address, SEARCH_FIELD));
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeIpAddressAssignmentWizardProperties.isPrimary()
                .ifPresent(isPrimary -> getWizard().setComponentValue(IS_PRIMARY_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, isPrimary, CHECKBOX));
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeIpAddressAssignmentWizardProperties.isInNAT()
                .ifPresent(isInNAT -> getWizard().setComponentValue(IS_IN_NAT_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, isInNAT, CHECKBOX));
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeIpAddressAssignmentWizardProperties.getRole()
                .ifPresent(role -> getWizard().setComponentValue(ROLE_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, role, SEARCH_FIELD));
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeIpAddressAssignmentWizardProperties.getDescription()
                .ifPresent(description -> getWizard().setComponentValue(DESCRIPTION_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, description, TEXT_AREA));
        DelayUtils.waitForPageToLoad(driver, wait);
        assignIPAddressAssignmentStep();
    }

    private Popup createPopup() {
        return Popup.create(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
