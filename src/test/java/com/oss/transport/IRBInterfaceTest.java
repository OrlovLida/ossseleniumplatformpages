package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.bpm.CreateProcessNRPTest;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.oss.pages.transport.IRBInterfaceWizardPage;

@Listeners({ TestListener.class })
public class IRBInterfaceTest extends BaseTestCase {

    private Wizard createDeviceWizard;
    private CreateProcessNRPTest createProcessNRPTest;

    public Wizard getWizard() {
        if (createDeviceWizard == null) {
            createDeviceWizard = Wizard.createWizard(driver, webDriverWait);
        }
        return createDeviceWizard;
    }

    //TODO: ASSERTIONS, check if NRP process and edit/delete functions work correctly

    @Test(priority = 1)
    @Description("Create new process and move to High level planning")
    public void createAndMoveProcess() {
        createProcessNRPTest.openProcessInstancesPage();
        createProcessNRPTest.createProcessNRP();
        createProcessNRPTest.startHLPTask();
    }

    @Test(priority = 2)
    public void createNewIRBInterface(){
        IRBInterfaceWizardPage irbInterfaceWizardPage = IRBInterfaceWizardPage.goToIRBInterfaceWizardPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        getWizard().setComponentValue("irb-main-device", "IRBInterfaceSeleniumTest", Input.ComponentType.SEARCH_FIELD);
        DelayUtils.sleep();
        getWizard().setComponentValue("irb-main-vlanid", "100", Input.ComponentType.TEXT_FIELD);
        DelayUtils.sleep();
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    @Description("Checks if IRB Interface is visible")
    public void checkIRBInterface() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IRBInterface_TP");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD,"identifier", "IRBInterfaceSeleniumTest").applyFilter();
        DelayUtils.sleep();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 4)
    @Description("Assign IP Host Address")
    public void assignIPHostAddress() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        homePage.goToInventoryViewPage(BASIC_URL + "/#/view/inventory/view/type/IRBInterface?perspective=LIVE");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObjectIdentifier("IRBInterfaceSeleniumTest", "IRBInterface")
                .selectRow("Identifier", "IRBInterfaceSeleniumTest-BU1-Router-1\\IRB:100");
        oldInventoryViewPage.useContextAction("CREATE", "AssignIPv4Host");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().setComponentValue("uid-ipaddress", "126003", Input.ComponentType.TEXT_FIELD);
        getWizard().setComponentValue("uid-ip-subnet", "IRBInterfaceSeleniumTest", Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().clickButtonByLabel("Next Step");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().clickAccept();
    }

    @Test(priority = 5)
    @Description("Edit")
    public void editIRBInterface(){
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IRBInterface_TP");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD,"identifier", "IRBInterfaceSeleniumTest").applyFilter();
        newInventoryViewPage.selectFirstRow()
                .editObject("Edit IRB Interface");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().setComponentValue("irb-main-mtu", "1000", Input.ComponentType.TEXT_FIELD);
        getWizard().clickAccept();
    }

    @Test(priority = 6)
    @Description("Edit")
    public void deleteIRBInterface(){
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IRBInterface_TP");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD,"identifier", "IRBInterfaceSeleniumTest").applyFilter();
        newInventoryViewPage.selectFirstRow()
                .editObject("Delete IRB Interface");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().clickOK();
    }

    @Test(priority = 7)
    @Description("NRP bs")
    public void finishPlanning(){
        createProcessNRPTest.completeHLPTask();
        createProcessNRPTest.startLLPTask();
        createProcessNRPTest.completeLLPTask();
        createProcessNRPTest.startRFITask();
        createProcessNRPTest.setupIntegration();
        createProcessNRPTest.getIPCode();
        createProcessNRPTest.completeRFITask();
        createProcessNRPTest.startSDTaskIP1();
        createProcessNRPTest.completeSDTaskIP1();
        createProcessNRPTest.startImplementationTaskIP1();
        createProcessNRPTest.completeImplementationTaskIP1();
        createProcessNRPTest.startAcceptanceTaskIP1();
        createProcessNRPTest.completeAcceptanceTaskIP1();
        createProcessNRPTest.startVerificationTask();
        createProcessNRPTest.completeVerificationTask();
    }

}