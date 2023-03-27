package com.oss.smoketests;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;

public class NotTranslatedTypesSmokeTest extends BaseTestCase {
    private SoftAssert softAssert;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotTranslatedTypesSmokeTest.class);
    private static final String RESOURCE_INVENTORY_CATEGORY = "Resource Inventory";
    private static final String INVENTORY_VIEW = "Inventory View";
    private static final String DM_PREFIX = "DM_";
    private static final String TEST_STRING = "test";
    private static final String SEARCH_FIELD_COMPONENT = "type-chooser";
    private static final String OBJECT_TYPE_TRANSLATION_ERROR_MESSAGE = " Object Type is not translated";

    @Test(priority = 1, description = "Open browser and check environment status")
    @Description("Open browser and check environment status")
    public void openBrowserAndCheckEnvironmentStatus() {
        softAssert = new SoftAssert();
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check Inventory View for Site", dependsOnMethods = {"openBrowserAndCheckEnvironmentStatus"})
    @Description("Check Inventory View for Site")
    public void checkLabelsForObjectTypes() {
        openSearchPage();
        checkNotTranslatedObjectTypes();
    }

    @Description("Open Search Page")
    private void openSearchPage() {
        homePage.goToHomePage(driver, BASIC_URL);
        homePage.openApplication(RESOURCE_INVENTORY_CATEGORY, INVENTORY_VIEW);
        waitForPageToLoad();
    }

    @Description("Getting all available options on list for {objectType} object type")
    private Set<String> getNotTranslatedObjectTypes() {
        SearchField searchField = (SearchField) ComponentFactory.create(SEARCH_FIELD_COMPONENT, SEARCH_FIELD, driver, webDriverWait);
        Data modelData = Data.createSingleData(DM_PREFIX);
        return searchField.getOptionsContains(modelData);
    }

    @Description("Checking not translated object types labels")
    private void checkNotTranslatedObjectTypes() {
        Set<String> notTranslatedObjectTypes = getNotTranslatedObjectTypes().stream()
                .filter(notTranslatedObjectType -> !StringUtils.containsIgnoreCase(notTranslatedObjectType, TEST_STRING))
                .collect(Collectors.toSet());
        for (String notTranslatedObjectType : notTranslatedObjectTypes) {
            softAssert.assertFalse(notTranslatedObjectType.contains(DM_PREFIX), notTranslatedObjectType + OBJECT_TYPE_TRANSLATION_ERROR_MESSAGE);
        }
        softAssert.assertAll();
    }

    @Step("Waiting for page to load")
    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Checking if error page is shown")
    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    @Step("Checking global notifications")
    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }
}
