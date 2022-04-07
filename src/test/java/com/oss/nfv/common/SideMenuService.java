package com.oss.nfv.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.navigation.sidemenu.SideMenu;

/**
 * @author Marzena Tolpa
 */
public class SideMenuService {

    public static final String RESOURCE_SPECIFICATIONS_ACTION_LABEL = "Resource Specifications";
    public static final String RESOURCE_CATALOG_PATH = "Resource Catalog";

    public static void goToResourceSpecificationsView(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(RESOURCE_SPECIFICATIONS_ACTION_LABEL, RESOURCE_CATALOG_PATH, RESOURCE_CATALOG_PATH);
    }
}
