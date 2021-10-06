package com.oss.pages.transport.ipam;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import static com.oss.framework.components.inputs.Input.ComponentType.*;

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
    private static final String TABLE_COMPONENT_ID = "subnetWizardWidgetId";
    private static final int FIRST_ROW = 0;

    public IPSubnetWizardPage(WebDriver driver) {
        super(driver);
    }

    private TableComponent getTableComponent() {
        return TableComponent.create(driver, wait, TABLE_COMPONENT_ID);
    }

    @Step("IP Subnet Wizard select step")
    public void ipSubnetWizardSelectStep(IPSubnetFilterProperties ipSubnetFilterProperties) {
        waitForPageToLoad();
        Wizard selectStep = createWizard();
        fillSubnetWizardSelectStep(selectStep, ipSubnetFilterProperties);
        Button button = Button.create(driver, FIND_NEXT_SUBNET, A_BLOCK);
        button.click();
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
        Button button = Button.create(driver, FIND_SUBNETS, A_BLOCK);
        button.click();
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
        for (int i = 0; i < ipSubnetWizardPropertiesArray.length; i++)
        {
            getTableComponent().selectRow(i);
            waitForPageToLoad();
            propertiesStep.setComponentValue(SUBNET_TYPE_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getSubnetType(), COMBOBOX);
            if (ipSubnetWizardPropertiesArray[i].getStatus()!=null) {
                propertiesStep.setComponentValue(SUBNET_STATUS_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getStatus(), COMBOBOX);
            }
            if (ipSubnetWizardPropertiesArray[i].getRole()!=null) {
                propertiesStep.setComponentValue(ROLE_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getRole(), SEARCH_FIELD);
                DelayUtils.sleep(500);
            }
            if (ipSubnetWizardPropertiesArray[i].getDescription()!=null) {
                propertiesStep.setComponentValue(DESCRIPTION_COMPONENT_ID, ipSubnetWizardPropertiesArray[i].getDescription(), TEXT_FIELD);
            }
            waitForPageToLoad();
            getTableComponent().unselectRow(i);
        }
        DelayUtils.sleep(500);
        propertiesStep.clickNext();
    }

    @Step("IP Subnet Wizard summary step")
    public void ipSubnetWizardSummaryStep() {
        waitForPageToLoad();
        Wizard summaryStep = createWizard();
        summaryStep.clickAccept();
    }

    private void fillSubnetWizardSelectStep(Wizard selectStep, IPSubnetFilterProperties ipSubnetFilterProperties) {
        selectStep.setComponentValue(START_IP_COMPONENT_ID, ipSubnetFilterProperties.getStartIp(), TEXT_FIELD);
        selectStep.setComponentValue(END_IP_COMPONENT_ID, ipSubnetFilterProperties.getEndIp(), TEXT_FIELD);
        if (ipSubnetFilterProperties.getOperator()!=null) {
            selectStep.setComponentValue(OPERATOR_COMPONENT_ID, ipSubnetFilterProperties.getOperator(), COMBOBOX);
        }
        if (ipSubnetFilterProperties.getMaskLength()!=null) {
            selectStep.setComponentValue(MASK_LENGTH_COMPONENT_ID, ipSubnetFilterProperties.getMaskLength(), COMBOBOX);
        }
    }

    private void waitForPageToLoad(){
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard createWizard(){
        return Wizard.createWizard(driver, wait);
    }
}
