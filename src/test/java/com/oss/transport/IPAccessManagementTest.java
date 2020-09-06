package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.IPAddressManagementViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ TestListener.class })
public class IPAccessManagementTest extends BaseTestCase {

    private int RolesBefore;
    private IPAddressManagementViewPage ipAddressManagementViewPage;

//TODO: replace sleep() - waitForPageToLoad does not work; OpenCreateNewRole() cannot work as a Wizard method due to waitForInvisibility() function not
// completing (button still visible under new Wizard); confirmDeletion() - lack of dataattribute


    @BeforeClass
    public void openIPAddressManagementView() {
        ipAddressManagementViewPage = ipAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Open Roles")
    public void createRole() {
        ipAddressManagementViewPage.OpenRoleView();
            DelayUtils.sleep();
            RolesBefore = ipAddressManagementViewPage.howManyRoles();
    }

    @Test(priority = 2)
    @Description("Open new Role wizard")

    public void CreateNewRole() {
        ipAddressManagementViewPage.OpenCreateNewRole();
    }

    @Test(priority = 3)
    @Description("Set name to: CreateRoleSeleniumTest")
    public void InputNewRoleName() {
        ipAddressManagementViewPage.SetNewRoleName();
    }

    @Test(priority = 4)
    @Description("Accept Role")
    public void AcceptRoleName() {
        ipAddressManagementViewPage.AcceptRole();
        DelayUtils.sleep();
        Assert.assertEquals(ipAddressManagementViewPage.howManyRoles() - RolesBefore, 1);
        ipAddressManagementViewPage.ExitWizard();

    }

    @Test(priority = 5)
    @Description("Edit Role")
    public void EditRoleName() {
        ipAddressManagementViewPage
                .OpenRoleView()
                .OpenEditRole()
                .SetEditedRoleName()
                .ExitWizard();
    }

    @Test(priority = 6)
    @Description("Delete Role")
    public void DeleteRoleName() {
        ipAddressManagementViewPage.OpenRoleView();
        DelayUtils.sleep();
        RolesBefore = ipAddressManagementViewPage.howManyRoles();
        ipAddressManagementViewPage.DeleteRoleName()
                .ExitDelete();
        DelayUtils.sleep();
        Assert.assertEquals(RolesBefore - ipAddressManagementViewPage.howManyRoles(), 1);
        ipAddressManagementViewPage.ExitWizard();
    }

}
