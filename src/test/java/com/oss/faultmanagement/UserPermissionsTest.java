package com.oss.faultmanagement;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.pages.administration.permissions.UserPermissionsPage;

import io.qameta.allure.Description;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class UserPermissionsTest extends BaseTestCase {

    private static final String PROFILES_TAB_ID = "1";
    private static final String ALL_PROFILES_LABEL = "All profiles";
    private static final String INHERITED_PROFILE = "Inherited";

    private UserPermissionsPage userPermissionsPage;

    @BeforeClass
    public void goToUserPermissionsPage() {
        userPermissionsPage = UserPermissionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "User Permissions test for FM component", description = "User Permissions test for FM component")
    @Description("User Permissions test for FM component")
    public void permissionsTest() {
        SoftAssert softAssert = new SoftAssert();
        userPermissionsPage.searchForUserInUsersPanel(CONFIGURATION.getLogin());
        userPermissionsPage.selectFirstUserInUserPanelList();
        userPermissionsPage.selectTabInPermissionsPanel(PROFILES_TAB_ID);
        userPermissionsPage.clickButtonWithLabel(ALL_PROFILES_LABEL);

        userPermissionsPage.searchInUserProfileTable("FM Operator");
        softAssert.assertEquals(userPermissionsPage.getProfileStatus(), INHERITED_PROFILE);
        userPermissionsPage.assignRole();

        userPermissionsPage.searchInUserProfileTable("FM Observer");
        softAssert.assertEquals(userPermissionsPage.getProfileStatus(), INHERITED_PROFILE);
        userPermissionsPage.assignRole();

        userPermissionsPage.searchInUserProfileTable("FM Configurator");
        softAssert.assertEquals(userPermissionsPage.getProfileStatus(), INHERITED_PROFILE);
        userPermissionsPage.assignRole();
        softAssert.assertAll();
    }
}
