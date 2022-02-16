/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.platform.toolsmanager;

import org.openqa.selenium.WebDriver;

import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Zaranek
 */
public class ToolsManagerPage extends BasePage {
    
    public ToolsManagerPage(WebDriver driver) {
        super(driver);
    }
    
    public static ToolsManagerPage goToHomePage(WebDriver driver, String basicUrl) {
        driver.get(String.format("%s/#/", basicUrl));
        return new ToolsManagerPage(driver);
    }

    public ToolsManagerWindow getToolsManager() {
        return ToolsManagerWindow.create(driver, wait);
    }
}
