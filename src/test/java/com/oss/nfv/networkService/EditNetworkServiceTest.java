package com.oss.nfv.networkService;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardFirstStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardPage;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardSecondStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardStep;
import com.oss.pages.nfv.networkservice.NetworkServiceWizardThirdStep;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.services.nfv.networkservice.NetworkServiceApiClient;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.oss.nfv.networkService.EditNetworkServiceTestConstants.EDIT_LOGICAL_ELEMENT_ACTION_LABEL;
import static com.oss.nfv.networkService.EditNetworkServiceTestConstants.NEW_NETWORK_SERVICE_NAME;
import static com.oss.nfv.networkService.EditNetworkServiceTestConstants.NETWORK_SERVICE_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestListener.class})
public class EditNetworkServiceTest extends BaseNetworkServiceTest {

    private static final String JSON_FILE_PATH_TO_TEST = "src/test/resources/nfv/networkService/createNetworkService.json";

    @BeforeClass
    public void prepareData() throws IOException {
        super.prepareData();
        createNetworkServiceInstance();
    }

    private void createNetworkServiceInstance() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH_TO_TEST)));
        NetworkServiceApiClient.getInstance(env).createNetworkService(jsonContent);
    }

    @Test(priority = 1,description = "Open 'Edit NetworkService' wizard")
    @Description("Go directly to Inventory View page, find NetworkService, select it on results and open NetworkService wizard from context menu")
    public void openEditNetworkServiceWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NewInventoryViewPage inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "LogicalFunction");
        //when
        searchForNetworkService(inventoryViewPage);
        selectNetworkServiceInTree(inventoryViewPage);
        openNetworkServiceWizardForNetworkServiceEdit(inventoryViewPage);
        //then
        assertThatNetworkServiceWizardIsOpenAndStructureTreeIsVisible();
    }

    @Test(priority = 2, description = "Edit NetworkService name")
    @Description("Change NetworkService name in first step, enable second, third step and check name in each step")
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

    @Test(priority = 3,description = "Submit NetworkService")
    @Description("Submit NetworkService wizard and check system messages")
    public void submitNetworkService() {
        //given
        NetworkServiceWizardPage wizard = NetworkServiceWizardPage.create(driver, webDriverWait);
        //when
        submitNetworkServiceUpdate(wizard);
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void submitNetworkServiceUpdate(NetworkServiceWizardPage wizard) {
        wizard.clickAccept();
    }

    private void assertThatThereIsOneSuccessfulMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        assertEquals(messages.size(), 1, "There is no 1 message");
        assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS,
                "There is no successful message");
    }

    private void assertThatNameHasChangedInTree(SoftAssert softly, NetworkServiceWizardStep networkServiceWizardStep, String stepName) {
        softly.assertTrue(networkServiceWizardStep.nodeExists(NEW_NETWORK_SERVICE_NAME),
                "No node with name " + NEW_NETWORK_SERVICE_NAME + " has been found in " + stepName + " step");
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

    private void openNetworkServiceWizardForNetworkServiceEdit(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.callActionByLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_LOGICAL_ELEMENT_ACTION_LABEL);
    }

    private void selectNetworkServiceInTree(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.selectFirstRow();
    }

    private void searchForNetworkService(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.searchObject(NETWORK_SERVICE_NAME);
    }

}
