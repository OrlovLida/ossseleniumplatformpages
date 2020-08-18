package com.oss.pages.transport;

import com.oss.framework.components.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * @author Ewa Frączek
 */

public abstract class IPAddressAssignmentWizardPage extends BasePage {
    private static final String UID_ADDRESS = "uid-ipaddress";
    private static final String UID_IP_SUBNET = "uid-ip-subnet";
    private static final String UID_IS_PRIMARY = "uid-is-primary";
    private static final String COMMON_HIERARCHY_APP = "CommonHierarchyApp";

    protected IPAddressAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }

    public void assignMoToIPAddress(String ipAddress, String ipSubnet, String isPrimary){
        assignIPAddressMainStep(ipAddress,ipSubnet,isPrimary);
        assignIPAddressSummaryStep();
    }

    @Step("Assign IP Address Main Step")
    public void assignIPAddressMainStep(String ipAddress, String ipSubnet, String isPrimary){
        Wizard mainStep = Wizard.createWizard(driver, wait);
        Input componentAddress = mainStep.getComponent(UID_ADDRESS, Input.ComponentType.TEXT_FIELD);
        componentAddress.setSingleStringValue(ipAddress);
        Input componentSubnet = mainStep.getComponent(UID_IP_SUBNET, Input.ComponentType.SEARCH_FIELD);
        componentSubnet.setSingleStringValue(ipSubnet);
        Input componentIsPrimary = mainStep.getComponent(UID_IS_PRIMARY, Input.ComponentType.CHECKBOX);
        componentIsPrimary.setSingleStringValue(isPrimary);
        DelayUtils.sleep(1000);
        mainStep.clickNextStep();
    }

    @Step("Assign IP Address Assignment Step")
    public void assignIPAddressAssignmentStep(String firstObjectInHierarchy, String ... nextObjectsInHierarchy){
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.createByClass(driver, COMMON_HIERARCHY_APP, wait);
        commonHierarchyApp.setFirstObjectInHierarchy(firstObjectInHierarchy);
        for(String nextObject:nextObjectsInHierarchy){
            commonHierarchyApp.setNextObjectInHierarchy(nextObject);
        }
    }

    @Step("Assign IP Address Summary Step")
    public void assignIPAddressSummaryStep(){
        Wizard summaryStep = Wizard.createWizard(driver, wait);
        summaryStep.clickAccept();
        summaryStep.waitToClose();
    }
}
