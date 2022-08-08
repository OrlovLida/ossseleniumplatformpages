package com.oss.iaa.faultmanagement;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.pages.administration.permissions.UserPermissionsPage;

import io.qameta.allure.Description;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class UserPermissionsTest extends BaseTestCase {

    private static final String PROFILES_TAB_ID = "1";
    private static final String ALL_PROFILES_LABEL = "All profiles";

    private UserPermissionsPage userPermissionsPage;

    @BeforeClass
    public void goToUserPermissionsPage() {
        userPermissionsPage = UserPermissionsPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"component"})
    @Test(priority = 1, testName = "User Permissions test for FM component", description = "User Permissions test for FM component")
    @Description("User Permissions test for FM component")
    public void permissionsTest(
            @Optional("FM") String component
    ) {
        SoftAssert softAssert = new SoftAssert();
        userPermissionsPage.searchForUserInUsersPanel(CONFIGURATION.getLogin());
        userPermissionsPage.selectFirstUserInUserPanelList();
        userPermissionsPage.selectTabInPermissionsPanel(PROFILES_TAB_ID);
        userPermissionsPage.clickButtonWithLabel(ALL_PROFILES_LABEL);

        userPermissionsPage.searchInUserProfileTable(component + " Operator");
        softAssert.assertTrue(userPermissionsPage.isRoleAssigned(), "No Operator role");
        userPermissionsPage.assignRole();

        userPermissionsPage.searchInUserProfileTable(component + " Observer");
        softAssert.assertTrue(userPermissionsPage.isRoleAssigned(), "No Observer role");
        userPermissionsPage.assignRole();

        userPermissionsPage.searchInUserProfileTable(component + " Configurator");
        softAssert.assertTrue(userPermissionsPage.isRoleAssigned(), "No Configurator role");
        userPermissionsPage.assignRole();
        softAssert.assertAll();
    }
}
