package com.oss.pages.transport.ipam;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IRBInterfaceWizardPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

/**
 * @author Ewa Frączek
 */

public class IPv4AddressAssignmentWizardPage extends IPAddressAssignmentWizardPage {
    private final static String IPV4ADDRESS_ASSIGNMENT_WIZARD_URL = "%s/#/view/transport/ipmgt/host/v4?";
    private final static String PERSPECTIVE_LIVE = "perspective=LIVE";
    private final static String PERSPECTIVE_PLAN = "perspective=PLAN";
    private final static String PROJECT_ID = "project_id=%d";
    private static final String WIZARD_ID = "hostWizard";
    private static final String IPADDRESS_ID = "uid-ipaddress";
    private static final String IPSUBNET_ID = "uid-ip-subnet";
    private static final String NEXT_ID = "hostWizardApp-next";
    private static final String ACCEPT_ID = "hostWizardApp-finish";

    public static IPv4AddressAssignmentWizardPage goToIPv4AddressAssignmentWizardPageLive(WebDriver driver, String basicURL){
        driver.get(String.format(IPV4ADDRESS_ASSIGNMENT_WIZARD_URL+PERSPECTIVE_LIVE, basicURL));
        return new IPv4AddressAssignmentWizardPage(driver);
    }

    public static IPv4AddressAssignmentWizardPage goToIPv4AddressAssignmentWizardPagePlan(WebDriver driver, String basicURL, int projectId){
        driver.get(String.format(IPV4ADDRESS_ASSIGNMENT_WIZARD_URL+PROJECT_ID+PERSPECTIVE_PLAN, basicURL, projectId));
        return new IPv4AddressAssignmentWizardPage(driver);
    }

    @Step("Assign IP address {ipAddress} and Subnet {subnet} to IRB Interface")
    public void assignIPtoIRBInterface(String ipAddress, String subnet) {
        waitForPageToLoad();
        getWizard().setComponentValue(IPADDRESS_ID, ipAddress, TEXT_FIELD);
        getWizard().setComponentValue(IPSUBNET_ID, subnet, SEARCH_FIELD);
        waitForPageToLoad();
        getWizard().clickActionById(NEXT_ID);
        waitForPageToLoad();
        getWizard().clickActionById(ACCEPT_ID);
    }

    public IPv4AddressAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
