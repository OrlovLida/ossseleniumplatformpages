package com.oss.nfv.vnfpkg;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.vnfpkg.VNFPKGManualOnboardingWizardFirstStep;
import com.oss.pages.nfv.vnfpkg.VNFPKGManualOnboardingWizardPage;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.MARKETPLACE_SAMSUNGNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.RESOURCE_CATALOG_PATH;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.RESOURCE_SPECIFICATIONS_ACTION_LABEL;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VIM_SAMSUNGNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFM_EOCMNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_IDENTIFIER;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_ONBOARDING_ACTION_LABEL;

@Listeners({ TestListener.class})
public class VNFPKGManualOnboardingTest extends BaseVNFPKGManualOnboardingTest {

    private VNFPKGManualOnboardingWizardFirstStep firstStep;

    @Test(priority = 1, description = "Open 'VNFPKG manual onboard' wizard")
    @Description("Go to Resource Specifications view, find and select VNFPKG specification and open manual wizard from context menu")
    public void openVnfpkgOnboardingWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);

        //when
        goToResourceSpecificationsView(sideMenu);
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        searchForVNFPKGSpecification(resourceSpecificationsViewPage);
        selectVNFPKGSpecificationInTree(resourceSpecificationsViewPage);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        //then
        openManualVnfpkgOnboardWizard(resourceSpecificationsViewPage);
    }

    @Test(priority = 2, description = "Select EOCMNFVO")
    @Description("Select EOCMNFVO and validate that proper fields are displayed and filled with proper values")
    public void selectEOCMNFVO() {
        //given
        VNFPKGManualOnboardingWizardPage wizard = VNFPKGManualOnboardingWizardPage.create(driver, webDriverWait);
        firstStep = wizard.getFirstStep();

        //when
        firstStep.selectNfvo(EOCMNFVO_NAME);

        //then
        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(firstStep.vnfmSearchFieldIsDisplayed())
            .describedAs("VNFM field not displayed")
            .isTrue();
        soft.assertThat(firstStep.defaultRemoteFieldIsDisplayed())
            .describedAs("Default remote folder field not displayed")
            .isTrue();
        soft.assertThat(firstStep.marketplaceSearchFieldIsDisplayed())
            .describedAs("Marketplace search field not hidden")
            .isFalse();
        soft.assertThat(firstStep.vimSearchFieldIsDisplayed())
            .describedAs("VIM search field not hidden")
            .isFalse();

        soft.assertThat(firstStep.getVnfmSearchFieldValue())
            .describedAs("VNFM field not filled with proper value")
            .isEqualTo(VNFM_EOCMNFVO_NAME);

        soft.assertAll();
    }

    @Test(priority = 3, description = "Select SamsungNFVO")
    @Description("Select SamsungNFVO and validate that proper fields are displayed")
    public void selectSamsungNFVO() {

        //when
        firstStep.selectNfvo(SamsungNFVO_NAME);

        //then
        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(firstStep.marketplaceSearchFieldIsDisplayed())
            .describedAs("Marketplace field not displayed")
            .isTrue();
        soft.assertThat(firstStep.vimSearchFieldIsDisplayed())
            .describedAs("VIM search field not hidden")
            .isTrue();
        soft.assertThat(firstStep.defaultRemoteFieldIsDisplayed())
            .describedAs("Default remote folder field not displayed")
            .isTrue();
        soft.assertThat(firstStep.vnfmSearchFieldIsDisplayed())
            .describedAs("VNFM search field not hidden")
            .isFalse();

        soft.assertThat(firstStep.getMarketplaceSearchFieldValue())
            .describedAs("Marketplace field not filled with proper value")
            .isEqualTo(MARKETPLACE_SAMSUNGNFVO_NAME);
        soft.assertThat(firstStep.getVimSearchFieldValue())
            .describedAs("VIM field not filled with proper value")
            .isEqualTo(VIM_SAMSUNGNFVO_NAME);

        soft.assertAll();
    }

    private void openManualVnfpkgOnboardWizard(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.callActionByLabel(ActionsContainer.OTHER_GROUP_ID, VNFPKG_ONBOARDING_ACTION_LABEL);
    }

    private void selectVNFPKGSpecificationInTree(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.selectTreeNode(VNFPKG_NAME, SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL);
    }

    private void searchForVNFPKGSpecification(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.setSearchText(VNFPKG_IDENTIFIER);
    }

    private void goToResourceSpecificationsView(SideMenu sideMenu) {
        sideMenu.callActionByLabel(RESOURCE_SPECIFICATIONS_ACTION_LABEL, new String[]{RESOURCE_CATALOG_PATH, RESOURCE_CATALOG_PATH});
    }
}
