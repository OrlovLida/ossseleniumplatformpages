package com.oss.nfv.onboardVNF;

import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;

import com.oss.nfv.common.SideMenuService;
import com.oss.pages.nfv.onboardvnf.OnboardVNFWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.oss.nfv.onboardVNF.OnboardVNFConstants.DEFAULT_REMOTE_FOLDER_VALUE;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.MARKETPLACE_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.NFVO_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VIM_NAME;
import static com.oss.nfv.onboardVNF.OnboardVNFConstants.VNFPKG_NAME;

@Listeners({TestListener.class})
public class OnboardVNFTest extends BaseOnboardVNFTest {

    @Test(priority = 1, description = "Open 'VNF Package Onboarding' wizard")
    @Description("Got to VNF Package Onboarding wizard")
    public void openOnboardVNFWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //given
        SideMenuService.goToVNFPackageOnboardingWizard(driver, webDriverWait);
        //then
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, description = "Select 'Onboard to Resource Catalog and NFVO'")
    @Description("Select 'Onboard to Resource Catalog and NFVO' and validate fields")
    public void checkRCToNFVO() {
        //given
        OnboardVNFWizardPage onboardVNFWizardPage = OnboardVNFWizardPage.create(driver, webDriverWait);
        //when
        onboardVNFWizardPage.selectRadioButton("Onboard to Resource Catalog and NFVO");
        onboardVNFWizardPage.searchNFVO(NFVO_NAME);
        onboardVNFWizardPage.fillDefaultRemoteFolder(DEFAULT_REMOTE_FOLDER_VALUE);
        //then
        validateRCToNFVOoFields(onboardVNFWizardPage);
    }

    @Test(priority = 3, description = "Select 'Onboard from NFVO to NFVO'")
    @Description("Select 'Onboard from NFVO to NFVO' and validate fields")
    public void checkFromNFVOToNFVO() {
        //given
        OnboardVNFWizardPage onboardVNFWizardPage = OnboardVNFWizardPage.create(driver, webDriverWait);
        //when
        onboardVNFWizardPage.selectRadioButton("Onboard from NFVO to NFVO");
        onboardVNFWizardPage.searchDownloadNFVO(NFVO_NAME);
        onboardVNFWizardPage.searchNFVO(NFVO_NAME);
        onboardVNFWizardPage.fillDefaultRemoteFolder(DEFAULT_REMOTE_FOLDER_VALUE);
        //then
        validateFromNfvoToNfvoFields(onboardVNFWizardPage);
    }

    private void validateRCToNFVOoFields(OnboardVNFWizardPage onboardVNFWizardPage) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(onboardVNFWizardPage.getSelectedNFVO(), NFVO_NAME);
        softly.assertEquals(onboardVNFWizardPage.getSelectedMarketplace(), MARKETPLACE_NAME);
        softly.assertEquals(onboardVNFWizardPage.getSelectedVIM(), VIM_NAME);
        softly.assertEquals(onboardVNFWizardPage.getDefaultRemoteFolder(), DEFAULT_REMOTE_FOLDER_VALUE);
        softly.assertAll();
    }

    private void validateFromNfvoToNfvoFields(OnboardVNFWizardPage onboardVNFWizardPage) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(onboardVNFWizardPage.getSelectedDownloadVNFPKG(), VNFPKG_NAME);
        softly.assertEquals(onboardVNFWizardPage.getSelectedDownloadNFVO(), NFVO_NAME);
        softly.assertEquals(onboardVNFWizardPage.getSelectedNFVO(), NFVO_NAME);
        softly.assertEquals(onboardVNFWizardPage.getSelectedMarketplace(), MARKETPLACE_NAME);
        softly.assertEquals(onboardVNFWizardPage.getSelectedVIM(), VIM_NAME);
        softly.assertEquals(onboardVNFWizardPage.getDefaultRemoteFolder(), DEFAULT_REMOTE_FOLDER_VALUE);
        softly.assertAll();
    }

}
