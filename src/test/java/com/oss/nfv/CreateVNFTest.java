package com.oss.nfv;

import java.util.List;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.VNFWizardFirstStep;
import com.oss.pages.nfv.VNFWizardFourthStep;
import com.oss.pages.nfv.VNFWizardPage;
import com.oss.pages.nfv.VNFWizardSecondStep;
import com.oss.pages.nfv.VNFWizardStep;
import com.oss.pages.nfv.VNFWizardThirdStep;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.nfv.CreateVNFTestConstants.CREATE_ACTION_GROUP_ID;
import static com.oss.nfv.CreateVNFTestConstants.CREATE_LOGICAL_FUNCTION_ACTION_LABEL;
import static com.oss.nfv.CreateVNFTestConstants.NEW_VNF_NAME;
import static com.oss.nfv.CreateVNFTestConstants.RESOURCE_CATALOG_PATH;
import static com.oss.nfv.CreateVNFTestConstants.RESOURCE_SPECIFICATIONS_ACTION_LABEL;
import static com.oss.nfv.CreateVNFTestConstants.ROOT_NODE_PATH;
import static com.oss.nfv.CreateVNFTestConstants.SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL;
import static com.oss.nfv.CreateVNFTestConstants.VNF_INSTANTIATION_LEVEL_0_IDENTIFIER;
import static com.oss.nfv.CreateVNFTestConstants.VNF_INSTANTIATION_LEVEL_0_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestListener.class})
public class CreateVNFTest extends BaseVNFTest {

    @Test(priority = 1, description = "Open 'Create VNF' wizard")
    @Description("Got to Resource Specifications view, find and select VNF specification and open VNF wizard from context menu")
    public void openCreateVNFWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        //when
        goToResourceSpecificationsView(sideMenu);
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        searchForVNFSpecification(resourceSpecificationsViewPage);
        selectVNFSpecificationInTree(resourceSpecificationsViewPage);
        openVNFWizardForVNFCreation(resourceSpecificationsViewPage);
        //then
        assertThatVNFWizardIsOpenAndStructureTreeIsVisible();
    }

    @Test(priority = 2, description = "Edit VNF name")
    @Description("Change VNF name in first step, enable second, third and fourth step and check name in each step")
    public void editVNFName() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        VNFWizardFirstStep firstStep = wizard.getFirstStep();
        //when
        enableRelationsStep(firstStep);
        selectTreeRootNode(firstStep);
        changeVNFName(firstStep);
        //then
        SoftAssert softly = new SoftAssert();
        assertThatNameHasBeenSetInTextField(firstStep, softly);
        assertThatNameHasChangedInTree(softly, firstStep, "first");
        //when
        wizard.goToNextStep();
        VNFWizardSecondStep secondStep = wizard.getSecondStep();
        //then
        assertThatNameHasChangedInTree(softly, secondStep, "second");
        //when
        enableTerminationPointRelationsStep(secondStep);
        wizard.goToNextStep();

        VNFWizardThirdStep thirdStep = wizard.getThirdStep();
        //then
        assertThatNameHasChangedInTree(softly, thirdStep, "third");
        //when
        wizard.goToNextStep();

        VNFWizardFourthStep fourthStep = wizard.getFourthStep();
        //then
        assertThatNameHasChangedInTree(softly, fourthStep, "fourth");
        softly.assertAll();
    }

    private void enableTerminationPointRelationsStep(VNFWizardSecondStep secondStep) {
        secondStep.toggleTerminationPointRelations();
    }

    @Test(priority = 3, description = "Create VNF")
    @Description("Submit VNF wizard and check system messages")
    public void createVNF() {
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        //when
        submitVNFCreation(wizard);
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void submitVNFCreation(VNFWizardPage wizard) {
        wizard.clickAccept();
    }

    private void assertThatThereIsOneSuccessfulMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        assertEquals(messages.size(), 1, "There is no 1 message");
        assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS,
                "There is no successful message");
    }

    private void assertThatNameHasChangedInTree(SoftAssert softly, VNFWizardStep vnfWizardStep, String stepName) {
        softly.assertTrue(vnfWizardStep.nodeExists(NEW_VNF_NAME), "No node with name " + NEW_VNF_NAME + " has been found in " + stepName + " step");
    }

    private void assertThatNameHasBeenSetInTextField(VNFWizardFirstStep firstStep, SoftAssert softly) {
        softly.assertEquals(firstStep.getName(), NEW_VNF_NAME, "Name has not been set");
    }

    private void changeVNFName(VNFWizardFirstStep firstStep) {
        firstStep.setName(NEW_VNF_NAME);
    }

    private void selectTreeRootNode(VNFWizardFirstStep firstStep) {
        firstStep.selectNode(ROOT_NODE_PATH);
    }

    private void enableRelationsStep(VNFWizardFirstStep firstStep) {
        firstStep.toggleRelationConfiguration();
    }

    private void assertThatVNFWizardIsOpenAndStructureTreeIsVisible() {
        assertTrue(VNFWizardPage.create(driver, webDriverWait).getFirstStep().isStructureTreeVisible());
    }

    private void openVNFWizardForVNFCreation(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.callActionByLabel(CREATE_ACTION_GROUP_ID, CREATE_LOGICAL_FUNCTION_ACTION_LABEL);
    }

    private void selectVNFSpecificationInTree(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.selectTreeNode(VNF_INSTANTIATION_LEVEL_0_NAME, SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL);
    }

    private void searchForVNFSpecification(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.setSearchText(VNF_INSTANTIATION_LEVEL_0_IDENTIFIER);
    }

    private void goToResourceSpecificationsView(SideMenu sideMenu) {
        sideMenu.callActionByLabel(RESOURCE_SPECIFICATIONS_ACTION_LABEL, new String[]{RESOURCE_CATALOG_PATH, RESOURCE_CATALOG_PATH});
    }

}
