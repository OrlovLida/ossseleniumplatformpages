package com.oss.pages.transport;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.transport.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.helper.IPSubnetWizardProperties;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * @author Ewa Frączek
 */

public class IPSubnetWizardPage extends BasePage {
    private static final String START_IP_COMPONENT_ID = "startIpComponentId";
    private static final String END_IP_COMPONENT_ID = "endIpComponentId";
    private static final String OPERATOR_COMPONENT_ID = "operatorComponentId";
    private static final String MASK_LENGTH_COMPONENT_ID = "maskLengthComponentId";
    private static final String SUBNET_TYPE_COMPONENT_ID = "subnetTypeComponentId";
    private static final String SUBNET_STATUS_COMPONENT_ID = "subnetStatusComponentId";
    private static final String ROLE_COMPONENT_ID = "roleComponentId";
    private static final String DESCRIPTION_COMPONENT_ID = "descriptionComponentId";
    private static final String FIND_SUBNETS = "Find Subnets";
    private static final String FIND_NEXT_SUBNET = "Find next Subnet";
    private static final String A_BLOCK = "a";

    public IPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    private TableWidget getTableWidget() {
        Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
        return TableWidget.create(driver, TableWidget.TABLE_WIDGET_CLASS, wait);
    }

    @Step("IP Subnet Wizard select step")
    private void ipSubnetWizardSelectStep(IPSubnetFilterProperties ipSubnetFilterProperties) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard selectStep = Wizard.createWizard(driver, wait);
        fillSubnetWizardSelectStep(selectStep, ipSubnetFilterProperties);
        Button button = Button.create(driver, FIND_NEXT_SUBNET, A_BLOCK);
        button.click();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTableWidget().selectFirstRow();
        selectStep.clickNext();
    }

    public void ipSubnetWizardSelectStep(IPSubnetFilterProperties ipSubnetFilterProperties, int amount) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard selectStep = Wizard.createWizard(driver, wait);
        fillSubnetWizardSelectStep(selectStep, ipSubnetFilterProperties);
        Button button = Button.create(driver, FIND_SUBNETS, A_BLOCK);
        button.click();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        for (int i = 0; i < amount; i++)
            getTableWidget().selectRow(i);
        selectStep.clickNext();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }

    @Step("IP Subnet Wizard properties step")
    private void ipSubnetWizardPropertiesStep(String type) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard propertiesStep = Wizard.createWizard(driver, wait);
        getTableWidget().selectFirstRow();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Input componentSubnetType = propertiesStep.getComponent(SUBNET_TYPE_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentSubnetType.setSingleStringValue(type);
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        propertiesStep.clickNext();
    }

    @Step("IP Subnet Wizard properties step")
    public void ipSubnetWizardPropertiesStep(IPSubnetWizardProperties... ipSubnetWizardPropertiesArray) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard propertiesStep = Wizard.createWizard(driver, wait);
        for (int i = 0; i < ipSubnetWizardPropertiesArray.length; i++)
        {
            getTableWidget().selectRow(i);
            DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
            Input componentSubnetType = propertiesStep.getComponent(SUBNET_TYPE_COMPONENT_ID, Input.ComponentType.COMBOBOX);
            componentSubnetType.setSingleStringValue(ipSubnetWizardPropertiesArray[i].getSubnetType());
            if (ipSubnetWizardPropertiesArray[i].getStatus()!=null) {
                Input componentSubnetStatus = propertiesStep.getComponent(SUBNET_STATUS_COMPONENT_ID, Input.ComponentType.COMBOBOX);
                componentSubnetStatus.setSingleStringValue(ipSubnetWizardPropertiesArray[i].getStatus());
            }
            if (ipSubnetWizardPropertiesArray[i].getRole()!=null) {
                Input componentRole = propertiesStep.getComponent(ROLE_COMPONENT_ID, Input.ComponentType.SEARCH_FIELD);
                componentRole.setSingleStringValue(ipSubnetWizardPropertiesArray[i].getRole());
                DelayUtils.sleep(500);
            }
            if (ipSubnetWizardPropertiesArray[i].getDescription()!=null) {
                Input componentRole = propertiesStep.getComponent(DESCRIPTION_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
                componentRole.setSingleStringValue(ipSubnetWizardPropertiesArray[i].getDescription());
            }
            DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
            getTableWidget().unselectTableRow(i);
        }
        DelayUtils.sleep(500);
        propertiesStep.clickNext();
    }

    @Step("IP Subnet Wizard summary step")
    public void ipSubnetWizardSummaryStep() {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard summaryStep = Wizard.createWizard(driver, wait);
        summaryStep.clickAccept();
    }

    private void fillSubnetWizardSelectStep(Wizard selectStep, IPSubnetFilterProperties ipSubnetFilterProperties) {
        Input componentStartIP = selectStep.getComponent(START_IP_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        componentStartIP.setSingleStringValue(ipSubnetFilterProperties.getStartIp());
        Input componentEndIP = selectStep.getComponent(END_IP_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        componentEndIP.setSingleStringValue(ipSubnetFilterProperties.getEndIp());
        Input componentOperator = selectStep.getComponent(OPERATOR_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentOperator.setSingleStringValue(ipSubnetFilterProperties.getOperator());
        Input componentMaskLength = selectStep.getComponent(MASK_LENGTH_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentMaskLength.setSingleStringValue(ipSubnetFilterProperties.getMaskLength());
    }
}
