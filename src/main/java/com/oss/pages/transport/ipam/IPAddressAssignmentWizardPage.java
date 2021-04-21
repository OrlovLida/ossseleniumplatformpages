package com.oss.pages.transport.ipam;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPAddressAssignmentWizardProperties;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static com.oss.framework.components.inputs.Input.ComponentType.*;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.*;

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

    public void assignMoToIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
        assignIPAddressSummaryStep();
    }

    public void assignIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressSummaryStep();
    }

    public void assignIPAddress(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties,
                                     IPAddressAssignmentWizardProperties oppositeSideIpAddressAssignmentWizardProperties){
        assignIPAddressMainStep(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressOppositeAssignmentStep(oppositeSideIpAddressAssignmentWizardProperties);
        assignIPAddressSummaryStep();
    }

    public void assignIPAddressFromIPAddressContext(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties,
                                IPAddressAssignmentWizardProperties oppositeSideIpAddressAssignmentWizardProperties){
        assignIPAddressMainStepWithoutMode(ipAddressAssignmentWizardProperties);
        assignIPAddressAssignmentStep(ipAddressAssignmentWizardProperties);
        assignIPAddressOppositeAssignmentStep(oppositeSideIpAddressAssignmentWizardProperties);
        assignIPAddressSummaryStep();
    }

    @Step("Assign IP Address Main Step")
    private void assignIPAddressMainStep(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard mainStep = getWizard();
        if(ipAddressAssignmentWizardProperties.getWizardMode().isPresent()){
            String mode = ipAddressAssignmentWizardProperties.getWizardMode().get();
            if(!mainStep.getComponent(MODE_COMPONENT_ID, COMBOBOX).getStringValue().equals(mode)){
                mainStep.setComponentValue(MODE_COMPONENT_ID, mode, COMBOBOX);
                DelayUtils.waitForPageToLoad(driver, wait);
            }
            switch (mode){
                case MANUAL_MODE:
                    fillManualModeFields(mainStep, ipAddressAssignmentWizardProperties);
                    break;
                case AUTOMATIC_MODE:
                    fillAutomaticModeFields(mainStep, ipAddressAssignmentWizardProperties);
                    break;
                case RESERVED_MODE:
                    fillReservedModeFields(mainStep, ipAddressAssignmentWizardProperties);
                    break;
            }
        } else {
            fillManualModeFields(mainStep, ipAddressAssignmentWizardProperties);
        }
        fillOptionalFieldsInMainStep(mainStep, ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, wait);
        mainStep.clickNext();
    }

    @Step("Assign IP Address Main Step")
    private void assignIPAddressMainStepWithoutMode(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard mainStep = getWizard();
        fillOptionalFieldsInMainStep(mainStep, ipAddressAssignmentWizardProperties);
        DelayUtils.waitForPageToLoad(driver, wait);
        mainStep.clickNext();
    }

    private void fillManualModeFields(Wizard wizard, IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        ipAddressAssignmentWizardProperties.getSubnet()
                .ifPresent(subnet -> wizard.getComponent(IP_SUBNET_COMPONENT_ID,SEARCH_FIELD).setSingleStringValueContains(subnet));
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> wizard.setComponentValue(IP_ADDRESS_MANUAL_MODE_COMPONENT_ID, address, TEXT_FIELD));
    }

    private void fillAutomaticModeFields(Wizard wizard, IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> wizard.setComponentValue(IP_ADDRESS_AUTOMATIC_MODE_COMPONENT_ID, address, TEXT_FIELD));
        ipAddressAssignmentWizardProperties.getMask()
                .ifPresent(mask -> wizard.setComponentValue(MASK_COMPONENT_ID, mask, TEXT_FIELD));
        ipAddressAssignmentWizardProperties.getIpNetwork()
                .ifPresent(network -> wizard.setComponentValue(IP_NETWORK_COMPONENT_ID, network, SEARCH_FIELD));
    }

    private void fillReservedModeFields(Wizard wizard, IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        ipAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> wizard.getComponent(IP_ADDRESS_RESERVED_MODE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(address)));
    }

    private void fillOptionalFieldsInMainStep(Wizard wizard, IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        ipAddressAssignmentWizardProperties.isPrimary()
                .ifPresent(isPrimary -> wizard.setComponentValue(IS_PRIMARY_COMPONENT_ID, isPrimary, CHECKBOX));
        ipAddressAssignmentWizardProperties.isInNAT()
                .ifPresent(isInNAT -> wizard.setComponentValue(IS_IN_NAT_COMPONENT_ID, isInNAT, CHECKBOX));
        ipAddressAssignmentWizardProperties.getRole()
                .ifPresent(role -> wizard.setComponentValue(ROLE_COMPONENT_ID, role, SEARCH_FIELD));
        ipAddressAssignmentWizardProperties.getDescription()
                .ifPresent(description -> wizard.setComponentValue(DESCRIPTION_COMPONENT_ID, description, TEXT_AREA));
    }

    @Step("Assign IP Address Assignment Step")
    private void assignIPAddressAssignmentStep(IPAddressAssignmentWizardProperties ipAddressAssignmentWizardProperties){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard assignmentStep = getWizard();
        String assignmentType = ipAddressAssignmentWizardProperties.getAssignmentType().
                orElseThrow(() -> new RuntimeException("Assignment Type is not set"));
        String assignmentName = ipAddressAssignmentWizardProperties.getAssignmentName().
                orElseThrow(() -> new RuntimeException("Assignment Name is not set"));
        assignmentStep.setComponentValue(ASSIGNMENT_TYPE_COMPONENT_ID, assignmentType, COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        switch (assignmentType){
            case INTERFACE:
                assignmentStep.getComponent(SEARCH_INTERFACE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
            case PHYSICAL_DEVICE:
                assignmentStep.getComponent(SEARCH_PHYSICAL_DEVICE_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
            case CARD:
                assignmentStep.getComponent(SEARCH_CARD_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
            case LOGICAL_FUNCTION:
                assignmentStep.getComponent(SEARCH_LOGICAL_FUNCTION_COMPONENT_ID, SEARCH_FIELD).setValueContains(Data.createFindFirst(assignmentName));
                break;
        }
        DelayUtils.waitForPageToLoad(driver, wait);
        assignmentStep.clickNext();
    }

    @Step("Assign IP Address Opposite assignment Step")
    private void assignIPAddressOppositeAssignmentStep(IPAddressAssignmentWizardProperties oppositeIpAddressAssignmentWizardProperties){
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard oppositeAssignmentStep = getWizard();
        oppositeIpAddressAssignmentWizardProperties.getAddress()
                .ifPresent(address -> oppositeAssignmentStep.setComponentValue(IP_ADDRESS_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, address, SEARCH_FIELD));
        oppositeIpAddressAssignmentWizardProperties.isPrimary()
                .ifPresent(isPrimary -> oppositeAssignmentStep.setComponentValue(IS_PRIMARY_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, isPrimary, CHECKBOX));
        oppositeIpAddressAssignmentWizardProperties.isInNAT()
                .ifPresent(isInNAT -> oppositeAssignmentStep.setComponentValue(IS_IN_NAT_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, isInNAT, CHECKBOX));
        oppositeIpAddressAssignmentWizardProperties.getRole()
                .ifPresent(role -> oppositeAssignmentStep.setComponentValue(ROLE_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, role, SEARCH_FIELD));
        oppositeIpAddressAssignmentWizardProperties.getDescription()
                .ifPresent(description -> oppositeAssignmentStep.setComponentValue(DESCRIPTION_OPPOSTE_ASSIGNMENT_STEP_COMPONENT_ID, description, TEXT_AREA));
        DelayUtils.waitForPageToLoad(driver, wait);
        oppositeAssignmentStep.clickNext();
    }

    @Step("Assign IP Address Summary Step")
    private void assignIPAddressSummaryStep(){
        Wizard summaryStep = Wizard.createWizard(driver, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        summaryStep.clickAccept();
        summaryStep.waitToClose();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
