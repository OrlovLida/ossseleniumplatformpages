package com.oss.nfv.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.navigation.sidemenu.SideMenu;

/**
 * @author Marzena Tolpa
 */
public class SideMenuService {
    public static final String NETWORK_DOMAINS_PATH = "Network Domains";

    public static final String NETWORK_SERVICES_PATH = "Network Services";
    public static final String CREATE_NETWORK_SERVICE_ACTION_LABEL = "Create Network Service";
    public static final String NETWORK_SERVICE_PACKAGE_ONBOARDING_ACTION_LABEL = "Network Service Package Onboarding";

    public static final String NFV_VIRTUALIZATION_PATH = "NFV - Virtualization";
    public static final String CREATE_VNF_ACTION_LABEL = "Create VNF";
    public static final String VNF_PACKAGE_ONBOARDING_ACTION_LABEL = "VNF Package Onboarding";

    public static final String RESOURCE_SPECIFICATIONS_ACTION_LABEL = "Resource Specifications";
    public static final String RESOURCE_CATALOG_PATH = "Resource Catalog";

    public static final String LOGICAL_FUNCTIONS_PATH = "Logical Functions";
    public static final String CREATE_LOGICAL_FUNCTION_LABEL = "Create Logical Function";

    public static void goToResourceSpecificationsView(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(RESOURCE_SPECIFICATIONS_ACTION_LABEL, RESOURCE_CATALOG_PATH, RESOURCE_CATALOG_PATH);
    }

    public static void goToCreateLogicalFunctionView(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(CREATE_LOGICAL_FUNCTION_LABEL, NETWORK_DOMAINS_PATH, LOGICAL_FUNCTIONS_PATH);
    }

    public static void goToCreateNetworkServiceView(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(CREATE_NETWORK_SERVICE_ACTION_LABEL, NETWORK_DOMAINS_PATH, NETWORK_SERVICES_PATH);
    }

    public static void goToCreateVNFView(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(CREATE_VNF_ACTION_LABEL, NETWORK_DOMAINS_PATH, NFV_VIRTUALIZATION_PATH);
    }

    public static void goToVNFPackageOnboardingWizard(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(VNF_PACKAGE_ONBOARDING_ACTION_LABEL, NETWORK_DOMAINS_PATH, NFV_VIRTUALIZATION_PATH);
    }

    public static void goToNetworkServicePackageOnboardingWizard(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(NETWORK_SERVICE_PACKAGE_ONBOARDING_ACTION_LABEL, NETWORK_DOMAINS_PATH, NETWORK_SERVICES_PATH);
    }

}
