package com.oss.smoketests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;

import io.qameta.allure.Description;

public class CMDomainWizardSmokeTest extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(VSViewer20SmokeTest.class);
    private static final String PATH = "Network Discovery and Reconciliation";
    private static final String VIEW = "Network Discovery Control";

    @Test(priority = 1, description = "Open Network Discovery Control View")
    @Description("Open Network Discovery Control View")
    public void openNDCV() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(VIEW, PATH);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Check CM Domain wizard", dependsOnMethods = {"openNDCV"})
    @Description("Check CM Domain wizard")
    public void checkCMDomainWizard() {
        checkErrorPage();
        NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage = new NetworkDiscoveryControlViewPage(driver);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.typeValueNotificationSource("");
        waitForPageToLoad();
        wizard.typeValueMediationKey("");
        waitForPageToLoad();
        checkGlobalNotificationContainer();
        wizard.cancel();
        waitForPageToLoad();
    }

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

    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
