package com.oss.pages.transport;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;
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
    private static final String SUBNET_TYPE = "Subnet type";
    private static final String STATUS = "Status";
    private static final String ROLE = "Role";
    private static final String DESCRIPTION = "Description";
    private static final String A_BLOCK = "a";

    public IPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    private TableWidget getTableWidget() {
        Widget.waitForWidget(wait, TableWidget.TABLE_WIDGET_CLASS);
        return TableWidget.create(driver, TableWidget.TABLE_WIDGET_CLASS, wait);
    }

    @Step("IP Subnet Wizard select step")
    private void ipSubnetWizardSelectStep(String [] filterData) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard selectStep = Wizard.createWizard(driver, wait);
        fillSubnetWizardSelectStep(selectStep, filterData);
        Button button = Button.create(driver, FIND_NEXT_SUBNET, A_BLOCK);
        button.click();
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        getTableWidget().selectFirstRow();
        selectStep.clickNext();
    }

    public void ipSubnetWizardSelectStep(String [] filterData, int amount) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard selectStep = Wizard.createWizard(driver, wait);
        fillSubnetWizardSelectStep(selectStep, filterData);
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
    public void ipSubnetWizardPropertiesStep(Map<String, String>... subnets) {
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Wizard propertiesStep = Wizard.createWizard(driver, wait);
        for (int i = 0; i < subnets.length; i++)
        {
            getTableWidget().selectRow(i);
            DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
            Input componentSubnetType = propertiesStep.getComponent(SUBNET_TYPE_COMPONENT_ID, Input.ComponentType.COMBOBOX);
            componentSubnetType.setSingleStringValue(subnets[i].get(SUBNET_TYPE));
            if (subnets[i].containsKey(STATUS)) {
                Input componentSubnetStatus = propertiesStep.getComponent(SUBNET_STATUS_COMPONENT_ID, Input.ComponentType.COMBOBOX);
                componentSubnetStatus.setSingleStringValue(subnets[i].get(STATUS));
            }
            if (subnets[i].containsKey(ROLE)) {
                Input componentRole = propertiesStep.getComponent(ROLE_COMPONENT_ID, Input.ComponentType.SEARCH_FIELD);
                componentRole.setSingleStringValue(subnets[i].get(ROLE));
                DelayUtils.sleep(500);
            }
            if (subnets[i].containsKey(DESCRIPTION)) {
                Input componentRole = propertiesStep.getComponent(DESCRIPTION_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
                componentRole.setSingleStringValue(subnets[i].get(DESCRIPTION));
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

    private void fillSubnetWizardSelectStep(Wizard selectStep, String [] filterData) {
        Input componentStartIP = selectStep.getComponent(START_IP_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        componentStartIP.setSingleStringValue(filterData[0]);
        Input componentEndIP = selectStep.getComponent(END_IP_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        componentEndIP.setSingleStringValue(filterData[1]);
        Input componentOperator = selectStep.getComponent(OPERATOR_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentOperator.setSingleStringValue(filterData[2]);
        Input componentMaskLength = selectStep.getComponent(MASK_LENGTH_COMPONENT_ID, Input.ComponentType.COMBOBOX);
        componentMaskLength.setSingleStringValue(filterData[3]);
    }
}
