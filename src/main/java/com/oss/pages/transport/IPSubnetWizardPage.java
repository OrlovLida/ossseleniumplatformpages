package com.oss.pages.transport;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * @author Ewa Frączek
 */

public class IPSubnetWizardPage extends BasePage {
    private static final String START_IP_COMPONENT_ID = "startIpComponentId";
    private static final String END_IP_COMPONENT_ID = "endIpComponentId";
    private static final String OPERATOR_COMPONENT_ID = "operatorComponentId";
    private static final String MASK_LENGTH_COMPONENT_ID = "maskLengthComponentId";
    private static final String SUBNET_TYPE_COMPONENT_ID = "subnetTypeComponentId";
    private static final String FIND_SUBNETS = "Find Subnets";
    private static final String FIND_NEXT_SUBNET = "Find next Subnet";

    public IPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    public TableWidget getTableWidget() {
        Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
        return TableWidget.create(driver, TableWidget.TABLE_WIDGET_CLASS, wait);
    }

    public void createIPSubnet(String startIP, String endIP, String subnetMask, String type){
        ipSubnetWizardSelectStep(startIP, endIP, "=", subnetMask);
        ipSubnetWizardPropertiesStep(type);
        ipSubnetWizardSummaryStep();
    }

    @Step("IP Subnet Wizard select step")
    public void ipSubnetWizardSelectStep(String startIP, String endIP, String operation, String subnetMask){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard selectStep = Wizard.createWizard(driver, wait);
        fillSubnetWizardSelectStep(selectStep, startIP, endIP, operation, subnetMask);
        Button button = Button.create(driver, FIND_NEXT_SUBNET, "a");
        button.click();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTableWidget().selectFirstRow();
        selectStep.clickNext();
    }

    @Step("IP Subnet Wizard properties step")
    public void ipSubnetWizardPropertiesStep(String type){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard propertiesStep = Wizard.createWizard(driver, wait);
        getTableWidget().selectFirstRow();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Input componentSubnetType = propertiesStep.getComponent(SUBNET_TYPE_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentSubnetType.setSingleStringValue(type);
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        propertiesStep.clickNext();
    }

    @Step("IP Subnet Wizard summary step")
    public void ipSubnetWizardSummaryStep(){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard summaryStep = Wizard.createWizard(driver, wait);
        summaryStep.clickAccept();
    }

    private void fillSubnetWizardSelectStep(Wizard selectStep, String startIP, String endIP, String operation, String subnetMask){
        Input componentStartIP = selectStep.getComponent(START_IP_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        componentStartIP.setSingleStringValue(startIP);
        Input componentEndIP = selectStep.getComponent(END_IP_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        componentEndIP.setSingleStringValue(endIP);
        Combobox componentOperator = (Combobox) selectStep.getComponent(OPERATOR_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentOperator.setValueWithId(Data.createSingleData(operation), "EQUAL-item");
        Input componentMaskLength = selectStep.getComponent(MASK_LENGTH_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentMaskLength.setSingleStringValue(subnetMask);
    }
}
