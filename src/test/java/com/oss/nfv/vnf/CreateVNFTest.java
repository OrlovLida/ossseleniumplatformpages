package com.oss.nfv.vnf;

import java.util.List;

import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.pages.logicalfunction.LogicalFunctionWizardPreStep;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.nfv.common.SideMenuService;
import com.oss.pages.nfv.vnf.VNFWizardFirstStep;
import com.oss.pages.nfv.vnf.VNFWizardFourthStep;
import com.oss.pages.nfv.vnf.VNFWizardPage;
import com.oss.pages.nfv.vnf.VNFWizardSecondStep;
import com.oss.pages.nfv.vnf.VNFWizardStep;
import com.oss.pages.nfv.vnf.VNFWizardThirdStep;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;

import static com.oss.nfv.vnf.CreateVNFTestConstants.NEW_VNF_NAME;
import static com.oss.nfv.vnf.CreateVNFTestConstants.ROOT_NODE_PATH;
import static com.oss.nfv.vnf.CreateVNFTestConstants.VNF_INSTANTIATION_LEVEL_0_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestListener.class})
public class CreateVNFTest extends BaseVNFTest {

    @Test(priority = 1, description = "Open 'Create VNF' wizard")
    @Description("Got to Resource Specifications view, find and select VNF specification and open VNF wizard from context menu")
    public void openCreateVNFWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //given
        SideMenuService.goToCreateVNFView(driver, webDriverWait);
        //when
        LogicalFunctionWizardPreStep logicalFunctionWizardPreStep = LogicalFunctionWizardPreStep.create(driver, webDriverWait);
        logicalFunctionWizardPreStep.searchResourceSpecification(VNF_INSTANTIATION_LEVEL_0_NAME);
        logicalFunctionWizardPreStep.clickAccept();
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
}
