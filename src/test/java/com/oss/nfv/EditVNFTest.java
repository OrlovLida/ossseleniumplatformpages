package com.oss.nfv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.VNFWizardFirstStep;
import com.oss.pages.nfv.VNFWizardFourthStep;
import com.oss.pages.nfv.VNFWizardPage;
import com.oss.pages.nfv.VNFWizardSecondStep;
import com.oss.pages.nfv.VNFWizardStep;
import com.oss.pages.nfv.VNFWizardThirdStep;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.services.nfv.VNFApiClient;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.nfv.EditVNFTestConstants.EDIT_ACTION_GROUP_ID;
import static com.oss.nfv.EditVNFTestConstants.EDIT_LOGICAL_ELEMENT_ACTION_LABEL;
import static com.oss.nfv.EditVNFTestConstants.NEW_VNF_NAME;
import static com.oss.nfv.EditVNFTestConstants.VNF_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({TestListener.class})
public class EditVNFTest extends BaseVNFTest {

    private static final String JSON_FILE_PATH_TO_TEST = "src/test/resources/nfv/createVNF.json";

    @BeforeClass
    public void prepareData() throws IOException {
        super.prepareData();
        createVNFInstance();
    }

    private void createVNFInstance() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH_TO_TEST)));
        VNFApiClient.getInstance(env).createVnf(jsonContent);
    }

    @Test(priority = 1,description = "Open 'Edit VNF' wizard")
    @Description("Go directly to Inventory View page, find VNF, select it on results and open VNF wizard from context menu")
    public void openEditVNFWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NewInventoryViewPage inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "LogicalFunction");
        //when
        searchForVNF(inventoryViewPage);
        selectVNFInTree(inventoryViewPage);
        openVNFWizardForVNFEdit(inventoryViewPage);
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

    @Test(priority = 3,description = "Submit VNF")
    @Description("Submit VNF wizard and check system messages")
    public void submitVNF() {
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        //when
        submitVNFUpdate(wizard);
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void submitVNFUpdate(VNFWizardPage wizard) {
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
        softly.assertTrue(vnfWizardStep.nodeExists(NEW_VNF_NAME),
                "No node with name " + NEW_VNF_NAME + " has been found in " + stepName + " step");
    }

    private void assertThatNameHasBeenSetInTextField(VNFWizardFirstStep firstStep, SoftAssert softly) {
        softly.assertEquals(firstStep.getName(), NEW_VNF_NAME, "Name has not been set");
    }

    private void changeVNFName(VNFWizardFirstStep firstStep) {
        firstStep.setName(NEW_VNF_NAME);
    }

    private void selectTreeRootNode(VNFWizardFirstStep firstStep) {
        firstStep.selectNode(VNF_NAME);
    }

    private void enableRelationsStep(VNFWizardFirstStep firstStep) {
        firstStep.toggleRelationConfiguration();
    }

    private void assertThatVNFWizardIsOpenAndStructureTreeIsVisible() {
        assertTrue(VNFWizardPage.create(driver, webDriverWait).getFirstStep().isStructureTreeVisible());
    }

    private void openVNFWizardForVNFEdit(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.callActionByLabel(EDIT_ACTION_GROUP_ID, EDIT_LOGICAL_ELEMENT_ACTION_LABEL);
    }

    private void selectVNFInTree(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.selectFirstRow();
    }

    private void searchForVNF(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.searchObject(VNF_NAME);
    }

}
