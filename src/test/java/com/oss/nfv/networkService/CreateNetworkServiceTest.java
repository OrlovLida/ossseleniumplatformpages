package com.oss.nfv.networkService;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardFirstStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardPage;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardSecondStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardThirdStep;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.CREATE_LOGICAL_FUNCTION_ACTION_LABEL;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.NETWORK_SERVICE_IDENTIFIER;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.NEW_NETWORK_SERVICE_NAME;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.RESOURCE_CATALOG_PATH;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.RESOURCE_SPECIFICATIONS_ACTION_LABEL;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL;
import static com.oss.nfv.networkService.CreateNetworkServiceTestConstants.NETWORK_SERVICE_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestListener.class})
public class CreateNetworkServiceTest extends BaseNetworkServiceTest {

    @Test(priority = 1, description = "Open 'Create NetworkService' wizard")
    @Description("Got to Resource Specifications view, find and select NetworkService specification and open NetworkService wizard from context menu")
    public void openCreateNetworkServiceWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        //when
        goToResourceSpecificationsView(sideMenu);
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        searchForNetworkServiceSpecification(resourceSpecificationsViewPage);
        selectNetworkServiceSpecificationInTree(resourceSpecificationsViewPage);
        openNetworkServiceWizardForNetworkServiceCreation(resourceSpecificationsViewPage);
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

    private void openNetworkServiceWizardForNetworkServiceCreation(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.callActionByLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_LOGICAL_FUNCTION_ACTION_LABEL);
    }

    private void selectNetworkServiceSpecificationInTree(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.selectTreeNode(NETWORK_SERVICE_NAME, SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL);
    }

    private void searchForNetworkServiceSpecification(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.setSearchText(NETWORK_SERVICE_IDENTIFIER);
    }

    private void goToResourceSpecificationsView(SideMenu sideMenu) {
        sideMenu.callActionByLabel(RESOURCE_SPECIFICATIONS_ACTION_LABEL, new String[]{RESOURCE_CATALOG_PATH, RESOURCE_CATALOG_PATH});
    }

}
