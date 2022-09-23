package com.oss.nfv.manualOnboardNetworkService;

import com.oss.framework.components.mainheader.PerspectiveChooser;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.nfv.common.ResourceSpecificationsViewService;
import com.oss.nfv.common.SideMenuService;
import com.oss.pages.nfv.onboardnetworkservice.ManualOnboardNetworkServiceWizardPage;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;

import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.DEFAULT_REMOTE_FOLDER_VALUE;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.ERICSSON_NFVO_NAME;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NETWORK_SERVICE_PACKAGE_ONBOARDING_ACTION_LABEL;
import static com.oss.nfv.manualOnboardNetworkService.ManualOnboardNetworkServiceConstants.NS_PACKAGE_IDENTIFIER;

@Listeners({TestListener.class})
public class ManualOnboardNetworkServiceTest extends BaseManualOnboardNetworkServiceTest {

    @Test(priority = 1, description = "Open 'Network Service Package Onboarding to NFVO' wizard")
    @Description("Got to Network Service Package Onboarding to NFVO wizard")
    public void openOnboardNetworkServiceWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //given
        SideMenuService.goToResourceSpecificationsView(driver, webDriverWait);
        //when
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        ResourceSpecificationsViewService.selectResourceSpecificationOnTree(NS_PACKAGE_IDENTIFIER, NS_PACKAGE_IDENTIFIER, resourceSpecificationsViewPage);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //then
        openManualNSPackageOnboardWizard(resourceSpecificationsViewPage);
    }

    @Test(priority = 2, description = "Select EOCMNFVO")
    @Description("Select EOCMNFVO and validate that proper fields are displayed and filled with proper values")
    public void selectEOCMNFVO() {
        //given
        ManualOnboardNetworkServiceWizardPage onboardNetworkServiceWizardPage = ManualOnboardNetworkServiceWizardPage.create(driver, webDriverWait);
        //when
        onboardNetworkServiceWizardPage.searchNFVO(ERICSSON_NFVO_NAME);
        onboardNetworkServiceWizardPage.fillDefaultRemoteFolder(DEFAULT_REMOTE_FOLDER_VALUE);
        //then
        validateFields(onboardNetworkServiceWizardPage);
    }

    private void validateFields(ManualOnboardNetworkServiceWizardPage onboardNetworkServiceWizardPage) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(onboardNetworkServiceWizardPage.getSelectedNFVO(), ERICSSON_NFVO_NAME);
        softly.assertEquals(onboardNetworkServiceWizardPage.getDefaultRemoteFolder(), DEFAULT_REMOTE_FOLDER_VALUE);
        softly.assertAll();
    }

    private void openManualNSPackageOnboardWizard(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.callActionByLabel(ActionsContainer.OTHER_GROUP_ID, NETWORK_SERVICE_PACKAGE_ONBOARDING_ACTION_LABEL);
    }
}
