package com.oss.pages.transport.ipam;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

/**
 * @author Ewa Frączek
 */

public class IPSubnetWizardPage extends BasePage {
    private static final String SUBNET_WIZARD_ID = "SubnetWizardTemplateId";
    private static final String START_IP_COMPONENT_ID = "startIpComponentId";
    private static final String END_IP_COMPONENT_ID = "endIpComponentId";
    private static final String OPERATOR_COMPONENT_ID = "operatorComponentId";
    private static final String MASK_LENGTH_COMPONENT_ID = "maskLengthComponentId";
    private static final String SUBNET_TYPE_COMPONENT_ID = "subnetTypeComponentId";
    private static final String SUBNET_STATUS_COMPONENT_ID = "subnetStatusComponentId";
    private static final String ROLE_COMPONENT_ID = "roleComponentId";
    private static final String DESCRIPTION_COMPONENT_ID = "descriptionComponentId";
    private static final String SHOW_SUBNETS = "showSubnetsComponentId";
    private static final String FIND_NEXT_SUBNET = "findNextSubnetComponentId";
    private static final String TABLE_COMPONENT_ID = "subnetWizardWidgetId";
    private static final int FIRST_ROW = 0;
    private static final String CREATE_BUTTON = "wizard-submit-button-subnetWizardWidgetId";
    private static final String CLOSE_SUMMARY_BUTTON = "subnetWizardSummaryButtonsId-0";

    public IPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("IP Subnet Wizard select step")
    public void ipSubnetWizardSelectStep(IPSubnetFilterProperties ipSubnetFilterProperties) {
        waitForPageToLoad();
        Wizard selectStep = createWizard();
        fillSubnetWizardSelectStep(selectStep, ipSubnetFilterProperties);
        selectStep.clickButtonById(FIND_NEXT_SUBNET);
        waitForPageToLoad();
        getTableComponent().selectRow(FIRST_ROW);
        waitForPageToLoad();
        selectStep.clickNext();
    }

    @Step("IP Subnet Wizard select step")
    public void ipSubnetWizardSelectStep(IPSubnetFilterProperties ipSubnetFilterProperties, int amount) {
        waitForPageToLoad();
        Wizard selectStep = createWizard();
        fillSubnetWizardSelectStep(selectStep, ipSubnetFilterProperties);
        selectStep.clickButtonById(SHOW_SUBNETS);
        waitForPageToLoad();
        for (int i = 0; i < amount; i++)
            getTableComponent().selectRow(i);
        waitForPageToLoad();
        selectStep.clickNext();
        waitForPageToLoad();
    }

    @Step("IP Subnet Wizard select step")
    public void ipSubnetWizardSelectStep(int amount) {
        Wizard selectStep = createWizard();
        waitForPageToLoad();
        for (int i = 0; i < amount; i++)
            getTableComponent().selectRow(i);
        waitForPageToLoad();
        selectStep.clickNext();
        waitForPageToLoad();
    }

    @Step("IP Subnet Wizard properties step")
    public void ipSubnetWizardPropertiesStep(String type) {
        waitForPageToLoad();
        Wizard propertiesStep = createWizard();
        getTableComponent().selectRow(FIRST_ROW);
        waitForPageToLoad();
        propertiesStep.setComponentValue(SUBNET_TYPE_COMPONENT_ID, type, COMBOBOX);
        waitForPageToLoad();
        propertiesStep.clickNext();
    }

    @Step("IP Subnet Wizard properties step")
    public void ipSubnetWizardPropertiesStep(IPSubnetWizardProperties... ipSubnetWizardPropertiesArray) {
        waitForPageToLoad();
        Wizard propertiesStep = createWizard();
        for (int i = 0; i < ipSubnetWizardPropertiesArray.length; i++) {
            getTableComponent().selectRow(i);
            waitForPageToLoad();
            propertiesStep.setComponentValue(SUBNET_TYPE_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getSubnetType(), COMBOBOX);
            waitForPageToLoad();
            if (ipSubnetWizardPropertiesArray[i].getStatus() != null) {
                propertiesStep.setComponentValue(SUBNET_STATUS_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getStatus(), COMBOBOX);
                waitForPageToLoad();
            }
            if (ipSubnetWizardPropertiesArray[i].getRole() != null) {
                propertiesStep.setComponentValue(ROLE_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getRole(), SEARCH_FIELD);
                waitForPageToLoad();
            }
            if (ipSubnetWizardPropertiesArray[i].getDescription() != null) {
                propertiesStep.setComponentValue(DESCRIPTION_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getDescription(), TEXT_AREA);
                waitForPageToLoad();
            }
            getTableComponent().unselectRow(i);
        }
        propertiesStep.clickButtonById(CREATE_BUTTON);
    }

    @Step("IP Subnet Wizard summary step")
    public void ipSubnetWizardSummaryStep() {
        waitForPageToLoad();
        Wizard summaryStep = createWizard();
        summaryStep.clickButtonById(CREATE_BUTTON);
    }

    @Step("IP Subnet Prompt summary")
    public void ipSubnetPromptSummaryStep() {
        waitForPageToLoad();
        Popup summary = createPopup();
        summary.clickButtonById(CLOSE_SUMMARY_BUTTON);
    }

    private TableComponent getTableComponent() {
        return TableComponent.create(driver, wait, TABLE_COMPONENT_ID);
    }

    private void fillSubnetWizardSelectStep(Wizard selectStep, IPSubnetFilterProperties ipSubnetFilterProperties) {
        selectStep.setComponentValue(START_IP_COMPONENT_ID, ipSubnetFilterProperties.getStartIp(), TEXT_FIELD);
        selectStep.setComponentValue(END_IP_COMPONENT_ID, ipSubnetFilterProperties.getEndIp(), TEXT_FIELD);
        if (ipSubnetFilterProperties.getOperator() != null) {
            selectStep.setComponentValue(OPERATOR_COMPONENT_ID, ipSubnetFilterProperties.getOperator(), COMBOBOX);
        }
        if (ipSubnetFilterProperties.getMaskLength() != null) {
            selectStep.setComponentValue(MASK_LENGTH_COMPONENT_ID, ipSubnetFilterProperties.getMaskLength(), COMBOBOX);
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard createWizard() {
        return Wizard.createByComponentId(driver, wait, SUBNET_WIZARD_ID);
    }

    private Popup createPopup() {
        return Popup.create(driver, wait);
    }
}
