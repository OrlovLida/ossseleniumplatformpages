package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.bpm.CreateProcessNRPTest;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.oss.framework.alerts.PopupAlert;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

@Listeners({ TestListener.class })
public class IPLinkTest extends BaseTestCase {

    private Wizard wizard;
    private CreateProcessNRPTest createProcessNRPTest;

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver,webDriverWait);
        }
        return wizard;
    }

//    @Test(priority = 1)
//    @Description("")
//    public void startNRP(){
//        createProcessNRPTest.openProcessInstancesPage();
//        createProcessNRPTest.startHLPTask();
//    }

    @Test(priority = 2)
    @Description("")
    public void createIPLink() {
        NetworkViewPage networkViewPage = NetworkViewPage.goToNetworkViewPageLive(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.useContextAction("CREATE", "Create Trail");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().setComponentValue("trailTypeCombobox", "IP Link", Input.ComponentType.COMBOBOX);
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        wizard = null;
        getWizard().setComponentValue("name-uid", "IPLinkSeleniumTest", Input.ComponentType.TEXT_FIELD);
        getWizard().setComponentValue("capacity-unit-uid", "Mbps", Input.ComponentType.COMBOBOX);
        getWizard().setComponentValue("capacity-value-uid", "100", Input.ComponentType.TEXT_FIELD);
        getWizard().clickButtonByLabel("Proceed");
        wizard=null;
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPLink");

        PopupAlert popupAlert = PopupAlert.create(driver,webDriverWait);
        popupAlert.popupAccept();
//        Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
//        alert.accept();
    }

    @Test(priority = 3)
    @Description("Checks if IP Link was created correctly")
    public void checkIPLink() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPLink");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD,"label", "IPLinkSeleniumTest").applyFilter();
        DelayUtils.sleep();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 4)
    @Description("Checks if IP Link was created correctly")
    public void editIPLink(){
        NetworkViewPage networkViewPage = NetworkViewPage.goToNetworkViewPageLive(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);

        networkViewPage.useContextAction("add_to_view_group", "Trail");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        DelayUtils.sleep(6000);
        networkViewPage.queryElementAndAddItToView("label", TEXT_FIELD, "IPLinkSeleniumTest");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        DelayUtils.sleep();
        networkViewPage.selectObjectInViewContent("Name", "IPLinkSeleniumTest");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.useContextAction("EDIT", "Attributes and terminations");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);

        getWizard().setComponentValue("name-uid", "IPLinkSeleniumTest2", Input.ComponentType.TEXT_FIELD);
        getWizard().setComponentValue("capacity-value-uid", "110", Input.ComponentType.TEXT_FIELD);
        getWizard().clickButtonByLabel("Proceed");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        wizard = null;

        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPLink");

        PopupAlert popupAlert = PopupAlert.create(driver,webDriverWait);
        popupAlert.popupAccept();
    }

    @Test(priority = 5)
    @Description("Checks if IP Link was created correctly")
    public void deleteIPLink(){
        NetworkViewPage networkViewPage = NetworkViewPage.goToNetworkViewPageLive(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.useContextAction("add_to_view_group", "Trail");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        DelayUtils.sleep(6000);
        networkViewPage.queryElementAndAddItToView("label", TEXT_FIELD, "IPLinkSeleniumTest");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.expandDockedPanel("left");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.selectObjectInViewContent("Name", "IPLinkSeleniumTest2");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        networkViewPage.useContextAction("EDIT", "Delete Trail");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
        getWizard().clickButtonByLabel("Proceed");
        DelayUtils.waitForPageToLoad(driver,webDriverWait);
    }
}
