package com.oss.smoketests;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.HomePage;
import com.comarch.oss.web.pages.LoginPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.navigation.toolsmanager.Application;
import com.oss.framework.navigation.toolsmanager.Subcategory;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class ViewsSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewsSmokeTest.class);
    private static final String NOT_RESOLVED_LABEL = "navigation_";
    private final List<View> viewList = new ArrayList<>();
    private List<String> categories;

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
    }

    @Test(priority = 1, description = "Get Categories from main Page")
    @Description("Get Categories from main Page")
    public void getCategoriesFromToolsManager() {
        checkErrors("Start");
        ToolsManagerWindow toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
        categories = toolsManagerWindow.getCategoriesName();
        for (String category : categories) {
            List<String> subcategoriesNames = toolsManagerWindow.getSubcategoriesNames(category);
            toolsManagerWindow.getCategoryByName(category).clickShowAllForAplicationWithoutSubcategory();
            List<Application> aplicationsWithourSubcategory = toolsManagerWindow.getApplicationWithoutoSubcategory(category);
            collectApplications(category, "", aplicationsWithourSubcategory);
            for (String subcategoryName : subcategoriesNames) {
                Subcategory subcategory = toolsManagerWindow.getSubcategoryByName(subcategoryName, category);
                subcategory.clickShowAll();
                List<Application> applications = subcategory.getApplications();
                collectApplications(category, subcategoryName, applications);
            }
            toolsManagerWindow.getCategoryByName(category).collapseCategory();
        }
    }

    @Test(dataProvider = "categories", priority = 2, description = "Checking views from category")
    @Description("Checking views from category")
    public void checkForCategory(String viewName) {
        LoginPage loginPage = new LoginPage(driver, BASIC_URL);
        Assert.assertFalse(loginPage.isLoginPageDisplayed(), "User has been logged out.");
        if (viewName.startsWith(NOT_RESOLVED_LABEL)) {
            Assert.fail(String.format("Not resolved label for category %s.", viewName));
        }
        List<View> views = viewList.stream().filter(category -> category.getCategory().equals(viewName)).collect(Collectors.toList());
        List<String> subcategories = views.stream().map(View::getSubcategory).distinct().collect(Collectors.toList());
        for (String subcategory : subcategories) {
            if (subcategory.startsWith(NOT_RESOLVED_LABEL)) {
                Assert.fail(String.format("Not resolved label for subcategory %s.", subcategory));
            }
            List<View> viewsInSubcategory = views.stream().filter(subc -> subc.getSubcategory().equals(subcategory)).collect(Collectors.toList());
            List<String> applications = viewsInSubcategory.stream().map(View::getViewName).distinct().collect(Collectors.toList());
            for (String application : applications) {
                if (application.startsWith(NOT_RESOLVED_LABEL)) {
                    Assert.fail(String.format("Not resolved label for application %s.", application));
                }
                checkView(application, viewName, subcategory);
            }
        }
    }

    @Step("Checking {viewName} page from {groupName}.")
    public void checkView(String viewName, String groupName, String path) {
        if (viewName.equals("Floor Plan and Elevation View") || //View not in OSS Console
                viewName.equals("Network View") || //OSSWEB-10879
                viewName.equals("Packet Viewer") || //BIGDATA-7857
                viewName.equals("BPM Workspace") || //OSSPLA-15614
                viewName.equals("Workspace")) { //OSSPLA-15614
            LOGGER.warn("{} is on ignored list.", viewName);
            return;
        }
        chooseFromLeftSideMenu(viewName, groupName, path);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(DelayUtils.isPageLoaded(driver, webDriverWait, 90000), String.format("View %s not loaded in 90 seconds.", viewName));
        checkErrors(viewName);
        softAssert.assertAll();
    }

    @DataProvider(name = "categories")
    private Object[] getCategories() {
        return categories.toArray();
    }

    private void chooseFromLeftSideMenu(String viewName, String groupName, String path) {
        HomePage homePage = new HomePage(driver);
        try {
            if (path.isEmpty()) {
                LOGGER.debug("Checking " + viewName);
                homePage.chooseFromLeftSideMenu(viewName, groupName);
            } else {
                LOGGER.debug("Checking " + viewName + " in " + path);
                homePage.chooseFromLeftSideMenu(viewName, groupName, path);
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("Cannot open {}. {}", viewName, e);
        }
    }

    private void checkErrors(String viewName) {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            if (errorInformation.getErrorText().endsWith("403")) {
                LOGGER.warn("Access denied for {} page.", viewName);
                SystemMessageContainer.create(driver, webDriverWait).close();
                return;
            } else {
                LOGGER.error(errorInformation.getErrorText());
                LOGGER.error(errorInformation.getErrorDescription());
                LOGGER.error(errorInformation.getErrorMessage());
                Assert.fail(String.format("Error Page is shown on %s page.", viewName));
            }
        }
        checkSystemMessage();
        if (!viewName.equals("Inventory View") && !viewName.equals("Hierarchy View")) {
            checkGlobalNotificationContainer(viewName);
        }
    }

    private void checkGlobalNotificationContainer(String viewName) {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail(String.format("Global Notification shows error on %s page.", viewName));
        }
    }

    private void checkSystemMessage() {
        SystemMessageContainer systemMessage = SystemMessageContainer.create(this.driver, new WebDriverWait(this.driver, Duration.ofSeconds(5)));
        List<String> errors = systemMessage.getErrors();
        errors.forEach(LOGGER::error);
        Assert.assertTrue(errors.isEmpty(), "Some errors occurred during the test. Please check logs for details.\n");
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void collectApplications(String category, String subcategoryName, List<Application> applications) {
        for (Application application : applications) {
            String appName = application.getApplicationName();
            if (application.getApplicationsURL().isPresent()) {
                viewList.add(new View(category, subcategoryName, appName));
            } else {
                LOGGER.error("{} doesn't have related URL.", appName);
            }
        }
    }

    private static class View {
        private final String category;
        private final String subcategory;
        private final String viewName;

        private View(String category, String subcategory, String viewName) {
            this.category = category;
            this.subcategory = subcategory;
            this.viewName = viewName;
        }

        private String getCategory() {
            return category;
        }

        private String getSubcategory() {
            return subcategory;
        }

        private String getViewName() {
            return viewName;
        }
    }
}
