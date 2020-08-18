package com.oss.pages.transport;
import org.openqa.selenium.WebDriver;

/**
 * @author Ewa Frączek
 */

public class IPv4AddressAssignmentWizardPage extends IPAddressAssignmentWizardPage {
    private final static String IPV4ADDRESS_ASSIGNMENT_WIZARD_URL = "%s/#/view/transport/ipmgt/host/v4?";
    private final static String PERSPECTIVE_LIVE = "perspective=LIVE";
    private final static String PERSPECTIVE_PLAN = "perspective=PLAN";
    private final static String PROJECT_ID = "project_id=%d";

    public static IPv4AddressAssignmentWizardPage goToIPv4AddressAssignmentWizardPageLive(WebDriver driver, String basicURL){
        driver.get(String.format(IPV4ADDRESS_ASSIGNMENT_WIZARD_URL+PERSPECTIVE_LIVE, basicURL));
        return new IPv4AddressAssignmentWizardPage(driver);
    }
    public static IPv4AddressAssignmentWizardPage goToIPv4AddressAssignmentWizardPagePlan(WebDriver driver, String basicURL, int projectId){
        driver.get(String.format(IPV4ADDRESS_ASSIGNMENT_WIZARD_URL+PROJECT_ID+PERSPECTIVE_PLAN, basicURL, projectId));
        return new IPv4AddressAssignmentWizardPage(driver);
    }

    public IPv4AddressAssignmentWizardPage(WebDriver driver) {
        super(driver);
    }
}
