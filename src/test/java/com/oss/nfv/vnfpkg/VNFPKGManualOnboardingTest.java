package com.oss.nfv.vnfpkg;

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
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.RESOURCE_CATALOG_PATH;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.RESOURCE_SPECIFICATIONS_ACTION_LABEL;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.SamsungNFVO_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_IDENTIFIER;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_NAME;
import static com.oss.nfv.vnfpkg.VNFPKGManualOnboardConstants.VNFPKG_ONBOARDING_ACTION_LABEL;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Listeners({ TestListener.class})
public class VNFPKGManualOnboardingTest extends BaseVNFPKGManualOnboardingTest {

    private VNFPKGManualOnboardingWizardFirstStep firstStep;

    @Test(priority = 1, description = "Open 'VNFPKG manual onboard' wizard")
    @Description("Got to Resource Specifications view, find and select VNFPKG specification and open manual wizard from context menu")
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
    @Description("Select EOCMNFVO and validate that proper fields are displayed")
    public void selectEOCMNFVO() {
        //given
        VNFPKGManualOnboardingWizardPage wizard = VNFPKGManualOnboardingWizardPage.create(driver, webDriverWait);
        firstStep = wizard.getFirstStep();

        //when
        firstStep.selectNfvo(EOCMNFVO_NAME);

        //then
        assertTrue(firstStep.vnfmSearchFieldIsDisplayed());
        assertTrue(firstStep.defaultRemoteFieldIsDisplayed());
        assertFalse(firstStep.marketplaceSearchFieldIsDisplayed());
        assertFalse(firstStep.vimSearchFieldIsDisplayed());
    }

    @Test(priority = 3, description = "Select SamsungNFVO")
    @Description("Select SamsungNFVO and validate that proper fields are displayed")
    public void selectSamsungNFVO() {

        //when
        firstStep.selectNfvo(SamsungNFVO_NAME);

        //then
        assertTrue(firstStep.marketplaceSearchFieldIsDisplayed());
        assertTrue(firstStep.vimSearchFieldIsDisplayed());
        assertTrue(firstStep.defaultRemoteFieldIsDisplayed());
        assertFalse(firstStep.vnfmSearchFieldIsDisplayed());
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
