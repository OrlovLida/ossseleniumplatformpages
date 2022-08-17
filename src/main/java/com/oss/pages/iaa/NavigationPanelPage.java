package com.oss.pages.iaa;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NavigationPanelPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(NavigationPanelPage.class);

    public NavigationPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open Navigation Panel Page")
    public static NavigationPanelPage goToPage(WebDriver driver, String baseURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        driver.get(baseURL);
        log.info("Opening Navigation Panel page");
        return new NavigationPanelPage(driver, wait);
    }

    @Step("Check if chosen application button leads to url with name: {expectedSuffix}")
    public boolean isChosenUrlOpen(String expectedSuffix) {
        log.info("Check if current url contains text {}", expectedSuffix);
        return driver.getCurrentUrl().contains(expectedSuffix);
    }

    @Step("Open chosen application {applicationName} from category {chosenCategory}")
    public void openChosenApplication(String chosenCategory, String applicationName) {
        ToolsManagerWindow toolsManagerWindow = getToolsManagerWindow();
        toolsManagerWindow.getCategoryByName(chosenCategory).expandCategory();
        List<String> visibleApplicationNames = toolsManagerWindow.getApplicationNames(chosenCategory);
        if (!visibleApplicationNames.contains(applicationName)) {
            toolsManagerWindow.getCategoryByName(chosenCategory).clickShowAllForAplicationWithoutSubcategory();
        }
        toolsManagerWindow.openApplication(chosenCategory, applicationName);
        log.info("Click on application {} from category {}", applicationName, chosenCategory);
    }

    private ToolsManagerWindow getToolsManagerWindow() {
        return ToolsManagerWindow.create(driver, wait);
    }
}