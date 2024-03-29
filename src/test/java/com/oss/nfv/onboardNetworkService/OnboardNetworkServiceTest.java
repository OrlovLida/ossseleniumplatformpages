package com.oss.nfv.onboardNetworkService;

import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.nfv.common.SideMenuService;
import com.oss.pages.nfv.onboardnetworkservice.OnboardNetworkServiceWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.DEFAULT_REMOTE_FOLDER_VALUE;
import static com.oss.nfv.onboardNetworkService.OnboardNetworkServiceConstants.ERICSSON_NFVO_NAME;

@Listeners({TestListener.class})
public class OnboardNetworkServiceTest extends BaseOnboardNetworkServiceTest {

    @Test(priority = 1, description = "Open 'Network Service Package Onboarding' wizard")
    @Description("Got to Network Service Package Onboarding wizard")
    public void openOnboardNetworkServiceWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //given
        SideMenuService.goToNetworkServicePackageOnboardingWizard(driver, webDriverWait);
        //then
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, description = "Select 'Onboard to Resource Catalog and NFVO'")
    @Description("Select 'Onboard to Resource Catalog and NFVO' and validate fields")
    public void checkRCToNFVO() {
        //given
        OnboardNetworkServiceWizardPage onboardNetworkServiceWizardPage = OnboardNetworkServiceWizardPage.create(driver, webDriverWait);
        //when
        onboardNetworkServiceWizardPage.selectRadioButton("Onboard to Resource Catalog and NFVO");
        onboardNetworkServiceWizardPage.searchNFVO(ERICSSON_NFVO_NAME);
        onboardNetworkServiceWizardPage.fillDefaultRemoteFolder(DEFAULT_REMOTE_FOLDER_VALUE);
        //then
        validateRCToNFVOoFields(onboardNetworkServiceWizardPage);
    }

    private void validateRCToNFVOoFields(OnboardNetworkServiceWizardPage onboardNetworkServiceWizardPage) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(onboardNetworkServiceWizardPage.getSelectedNFVO(), ERICSSON_NFVO_NAME);
        softly.assertEquals(onboardNetworkServiceWizardPage.getDefaultRemoteFolder(), DEFAULT_REMOTE_FOLDER_VALUE);
        softly.assertAll();
    }
}
