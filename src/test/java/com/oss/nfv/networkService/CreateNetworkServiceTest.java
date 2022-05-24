package com.oss.nfv.networkService;

import java.util.List;

import com.oss.pages.logicalfunction.LogicalFunctionWizardPreStep;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.nfv.common.SideMenuService;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardFirstStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardPage;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardSecondStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardThirdStep;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;

import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.NETWORK_SERVICE_NAME;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.NEW_NETWORK_SERVICE_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestListener.class})
public class CreateNetworkServiceTest extends BaseNetworkServiceTest {

    @Test(priority = 1, description = "Open 'Create NetworkService' wizard")
    @Description("Got to Resource Specifications view, find and select NetworkService specification and open NetworkService wizard from context menu")
    public void openCreateNetworkServiceWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        SideMenuService.goToCreateNetworkServiceView(driver, webDriverWait);
        //when
        LogicalFunctionWizardPreStep logicalFunctionWizardPreStep = LogicalFunctionWizardPreStep.create(driver, webDriverWait);
        logicalFunctionWizardPreStep.searchResourceSpecification(NETWORK_SERVICE_NAME);
        logicalFunctionWizardPreStep.clickAccept();

        //then
        assertThatNetworkServiceWizardIsOpenAndStructureTreeIsVisible();
    }

    @Test(priority = 2, description = "Edit NetworkService name")
    @Description("Change NetworkService name in first step, enable second, third and check name in each step")
    public void editNetworkServiceName() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NetworkServiceWizardPage wizard = NetworkServiceWizardPage.create(driver, webDriverWait);
        NetworkServiceWizardFirstStep firstStep = wizard.getFirstStep();
        //when
        enableRelationsStep(firstStep);
        selectTreeRootNode(firstStep);
        changeNetworkServiceName(firstStep);
        //then
        SoftAssert softly = new SoftAssert();
        assertThatNameHasBeenSetInTextField(firstStep, softly);
        assertThatNameHasChangedInTree(softly, firstStep, "first");
        //when
        wizard.goToNextStep();
        NetworkServiceWizardSecondStep secondStep = wizard.getSecondStep();
        //then
        assertThatNameHasChangedInTree(softly, secondStep, "second");
        //when
        wizard.goToNextStep();
        NetworkServiceWizardThirdStep thirdStep = wizard.getThirdStep();
        //then
        assertThatNameHasChangedInTree(softly, thirdStep, "third");
        softly.assertAll();
    }


    @Test(priority = 3, description = "Create NetworkService")
    @Description("Submit NetworkService wizard and check system messages")
    public void createNetworkService() {
        //given
        NetworkServiceWizardPage wizard = NetworkServiceWizardPage.create(driver, webDriverWait);
        //when
        submitNetworkServiceCreation(wizard);
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void submitNetworkServiceCreation(NetworkServiceWizardPage wizard) {
        wizard.clickAccept();
    }

    private void assertThatThereIsOneSuccessfulMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        assertEquals(messages.size(), 1, "There is no 1 message");
        assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS,
                "There is no successful message");
    }

    private void assertThatNameHasChangedInTree(SoftAssert softly,
                                                NetworkServiceWizardStep networkServiceWizardStep,
                                                String stepName) {
        softly.assertTrue(networkServiceWizardStep.nodeExists(NEW_NETWORK_SERVICE_NAME), "No node with name " + NEW_NETWORK_SERVICE_NAME + " has been found in " + stepName + " step");
    }

    private void assertThatNameHasBeenSetInTextField(NetworkServiceWizardFirstStep firstStep, SoftAssert softly) {
        softly.assertEquals(firstStep.getName(), NEW_NETWORK_SERVICE_NAME, "Name has not been set");
    }

    private void changeNetworkServiceName(NetworkServiceWizardFirstStep firstStep) {
        firstStep.setName(NEW_NETWORK_SERVICE_NAME);
    }

    private void selectTreeRootNode(NetworkServiceWizardFirstStep firstStep) {
        firstStep.selectNode(NETWORK_SERVICE_NAME);
    }

    private void enableRelationsStep(NetworkServiceWizardFirstStep firstStep) {
        firstStep.toggleRelationConfiguration();
    }

    private void assertThatNetworkServiceWizardIsOpenAndStructureTreeIsVisible() {
        assertTrue(NetworkServiceWizardPage.create(driver, webDriverWait).getFirstStep().isStructureTreeVisible());
    }
}
