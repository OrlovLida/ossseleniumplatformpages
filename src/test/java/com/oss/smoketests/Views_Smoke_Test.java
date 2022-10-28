package com.oss.smoketests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.navigation.toolsmanager.Application;
import com.oss.framework.navigation.toolsmanager.Subcategory;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class Views_Smoke_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Views_Smoke_Test.class);
    private static final String NOT_RESOLVED_LABEL = "navigation_";
    private List<String> categories;
    private List<View> viewList = new ArrayList<>();

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
    }

    @Test(priority = 1, description = "Get Categories from main Page")
    @Description("Get Categories from main Page")
    public void getCategoriesFromToolsManager() {
        checkErrorPage("Start");
        checkGlobalNotificationContainer("Start");
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
                try {
                    checkView(application, viewName, subcategory);
                } catch (TimeoutException e) {
                    LOGGER.error("Timeout waiting for {}", viewName);
                }
            }
        }
    }

    @Step("Checking {viewName} page from {groupName}.")
    public void checkView(String viewName, String groupName, String path) {
        if (viewName.equals("Floor Plan and Elevation View") || viewName.equals("Network View") || viewName.equals("Packet Viewer")) {
            LOGGER.warn("{} is on ignored list.", viewName);
            return;
        }
        chooseFromLeftSideMenu(viewName, groupName, path);
        waitForPageToLoad();
        checkErrorPage(viewName);
        checkGlobalNotificationContainer(viewName);
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

    private void checkErrorPage(String viewName) {
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
        SystemMessageContainer systemMessage = SystemMessageContainer.create(this.driver, new WebDriverWait(this.driver, 5));
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