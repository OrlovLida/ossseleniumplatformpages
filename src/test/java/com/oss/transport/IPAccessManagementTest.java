package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.pages.transport.IPAddressManagementViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ TestListener.class })
public class IPAccessManagementTest extends BaseTestCase {

    private IPAddressManagementViewPage ipAddressManagementViewPage;

    @BeforeClass
    public void openIPAddressManagementView() {
        ipAddressManagementViewPage = ipAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Open Roles")
    public void createRole() {
        ipAddressManagementViewPage.OpenRoleView();
    }

    @Test(priority = 2)
    @Description("Open new Role wizard")
    public void CreateNewRole() {
        ipAddressManagementViewPage.OpenCreateNewRole();
    }

    @Test(priority = 3)
    @Description("Set name to: CreateRoleSeleniumTest")
    public void InputNewRoleName() {
        ipAddressManagementViewPage.SetNewRoleName("CreateRoleSeleniumTest");
    }

    @Test(priority = 4)
    @Description("Accept Role")
    public void AcceptRoleName() {
        ipAddressManagementViewPage.AcceptRole();
    }

    @Test(priority = 5)
    @Description("Edit Role")
    public void EditRoleName() {
        ipAddressManagementViewPage.ExitWizard();
        ipAddressManagementViewPage.OpenRoleView();
        ipAddressManagementViewPage.OpenEditRole();
        ipAddressManagementViewPage.SetEditedRoleName("EditRoleSeleniumTest2");
        ipAddressManagementViewPage.ExitWizard();
    }

    @Test(priority = 6)
    @Description("Delete Role")
    public void DeleteRoleName() {
        ipAddressManagementViewPage.OpenRoleView();
        ipAddressManagementViewPage.DeleteRoleName();
        ipAddressManagementViewPage.ExitWizard();
    }

}
