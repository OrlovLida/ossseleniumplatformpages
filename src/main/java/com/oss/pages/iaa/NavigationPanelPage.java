package com.oss.pages.iaa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NavigationPanelPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(NavigationPanelPage.class);

    private final ToolsManagerWindow toolsManagerWindow;

    public NavigationPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        toolsManagerWindow = ToolsManagerWindow.create(driver, wait);
    }

    @Step("Open Home Page")
    public static NavigationPanelPage goToHomePage(WebDriver driver, String baseURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        driver.get(baseURL);
        log.info("Opening Home Page");
        return new NavigationPanelPage(driver, wait);
    }

    @Step("Check if chosen application button leads to url with name: {expectedSuffix}")
    public boolean isChosenUrlOpen(String expectedSuffix) {
        log.info("Check if current url contains text {}", expectedSuffix);
        return driver.getCurrentUrl().contains(expectedSuffix);
    }

    @Step("Get View Title")
    public String getViewTitle() {
        log.info("Getting View title");
        return ToolbarWidget.create(driver, wait).getViewTitle();
    }

    @Step("Open application {applicationName} from category {categoryName}")
    public void openApplicationInCategory(String categoryName, String applicationName) {
        showApplicationIfNotVisible(categoryName, applicationName);
        toolsManagerWindow.openApplication(categoryName, applicationName);
        log.info("Click on application {} from category {}", applicationName, categoryName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Open application {applicationName} from category {categoryName} in subcategory {subcategoryName}")
    public void openApplicationInSubCategory(String categoryName, String subcategoryName, String applicationName) {
        toolsManagerWindow.openApplication(categoryName, subcategoryName, applicationName);
        log.info("Click on application {} from category {} in subcategory {}", applicationName, categoryName, subcategoryName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void showApplicationIfNotVisible(String categoryName, String applicationName) {
        if (!isApplicationVisible(categoryName, applicationName)) {
            toolsManagerWindow.getCategoryByName(categoryName).clickShowAllForAplicationWithoutSubcategory();
        }
    }

    private boolean isApplicationVisible(String categoryName, String applicationName) {
        return toolsManagerWindow.getApplicationNames(categoryName).contains(applicationName);
    }
}