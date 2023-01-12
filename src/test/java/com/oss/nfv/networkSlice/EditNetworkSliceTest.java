/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.networkSlice;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionViewDTO;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.networkslice.NetworkSliceWizardFirstStep;
import com.oss.pages.nfv.networkslice.NetworkSliceWizardPage;
import com.oss.pages.nfv.networkslice.NetworkSliceWizardSecondStep;
import com.oss.pages.nfv.vnf.VNFWizardPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.services.LogicalFunctionCoreClient;
import com.oss.services.nfv.networkslice.NetworkSliceApiClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.MCC_VALUE;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.MNC_VALUE;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.NETWORK_SLICE_DESCRIPTION_NEW;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.NETWORK_SLICE_NAME;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.PLMN_INFO_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.PROFILE_SERVICE_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.SD_VALUE;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.SERVICE_PROFILE_NAME;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.SST_VALUE;
import static com.oss.nfv.networkSlice.EditNetworkSliceConstants.EDIT_LOGICAL_ELEMENT_ACTION_LABEL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Marcin Kozioł
 */
@Listeners({TestListener.class})
public class EditNetworkSliceTest extends BaseTestCase {
    private final String JSON_FILE_PATH_TO_TEST = "src/test/resources/nfv/networkSlice/createNetworkSlice.json";
    protected Environment env = Environment.getInstance();

    @BeforeClass
    public void prepareData() throws IOException {
        deleteAnyNetworkSliceInstancesByName();
        createNetworkSliceInstance();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteAnyNetworkSliceInstancesByName();
    }

    private void deleteAnyNetworkSliceInstancesByName() {
        NetworkSliceApiClient networkSliceApiClient = NetworkSliceApiClient.getInstance(env);
        LogicalFunctionCoreClient.getInstance(env).getLogicalFunctionByName(NETWORK_SLICE_NAME).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(networkSliceApiClient::deleteNetworkSlice);
    }

    private void createNetworkSliceInstance() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH_TO_TEST)));
        NetworkSliceApiClient.getInstance(env).createNetworkSlice(jsonContent);
    }

    @Test(priority = 1,description = "Open 'Network Slice' wizard")
    @Description("Go directly to Inventory View page, find NetworkSlice, select it on results and open NetworkSlice wizard from context menu")
    public void openEditNetworkSliceWizard() {
        //given
        NewInventoryViewPage inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "NetworkSlice");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //when
        searchForNetworkSlice(inventoryViewPage);
        selectNetworkSliceInTree(inventoryViewPage);
        openNetworkSliceWizardForNetworkSliceEdit(inventoryViewPage);
    }

    @Test(priority = 2, description = "Edit NetworkSlice in wizard")
    @Description("Change NetworkSlice description in first step, check NetworkSlice in second step, update NetworkSlice")
    public void editNetworkSlice() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NetworkSliceWizardPage wizard = NetworkSliceWizardPage.create(driver, webDriverWait);
        NetworkSliceWizardFirstStep firstStep = wizard.getFirstStep();
        //when
        changeNetworkSliceDescription(firstStep);
        //then
        validateNetworkSliceFirstStepParams(firstStep);
        //when
        wizard.goToNextStep();
        NetworkSliceWizardSecondStep secondStep = wizard.getSecondStep();
        //then
        validateServiceProfileInTree(secondStep);
        //when
        selectServiceProfile(secondStep);
        //then
        validateServiceProfileParams(secondStep);
        validatePLMNInfoInTree(secondStep);
        //when
        selectPLMNInfo(secondStep);
        //then
        validatePLMNInfoParams(secondStep);
        wizard.goToNextStep();
    }

    @Test(priority = 3, description = "Update NetworkSlice")
    @Description("Submit NetworkSlice wizard and check system messages")
    public void updateNetworkSlice() {
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        //when
        wizard.clickAccept();
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void validateServiceProfileInTree(NetworkSliceWizardSecondStep secondStep) {
        assertTrue(secondStep.nodeExists(SERVICE_PROFILE_NAME),
                "No node with name " + SERVICE_PROFILE_NAME + " has been found");
    }

    private void validatePLMNInfoInTree(NetworkSliceWizardSecondStep secondStep) {
        assertTrue(secondStep.nodeExists(PLMN_INFO_DEFAULT_LABEL_PATH),
                "No node with name " + PLMN_INFO_DEFAULT_LABEL_PATH + " has been found");
    }

    private void openNetworkSliceWizardForNetworkSliceEdit(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.callActionByLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_LOGICAL_ELEMENT_ACTION_LABEL);
    }

    private void selectNetworkSliceInTree(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.selectFirstRow();
    }

    private void searchForNetworkSlice(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.searchObject(NETWORK_SLICE_NAME);
    }

    private void changeNetworkSliceDescription(NetworkSliceWizardFirstStep firstStep) {
        firstStep.setDescription(NETWORK_SLICE_DESCRIPTION_NEW);
    }

    private void validateNetworkSliceFirstStepParams(NetworkSliceWizardFirstStep firstStep) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(firstStep.getName(), NETWORK_SLICE_NAME, "Name has not been set");
        softly.assertEquals(firstStep.getDescription(), NETWORK_SLICE_DESCRIPTION_NEW, "Description has not been set");
        softly.assertAll();
    }

    private void selectServiceProfile(NetworkSliceWizardSecondStep secondStep) {
        secondStep.selectNode(PROFILE_SERVICE_DEFAULT_LABEL_PATH);
    }

    private void selectPLMNInfo(NetworkSliceWizardSecondStep secondStep) {
        secondStep.selectNode(PLMN_INFO_DEFAULT_LABEL_PATH);
    }

    private void validateServiceProfileParams(NetworkSliceWizardSecondStep secondStep) {
        assertEquals(secondStep.getServiceProfileName(), SERVICE_PROFILE_NAME, "Name has not been set");
    }

    private void validatePLMNInfoParams(NetworkSliceWizardSecondStep secondStep) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(secondStep.getPLMNInfoMCC(), MCC_VALUE, "MCC has not been set");
        softly.assertEquals(secondStep.getPLMNInfoMNC(), MNC_VALUE, "MNC has not been set");
        softly.assertEquals(secondStep.getPLMNInfoSST(), SST_VALUE, "SST has not been set");
        softly.assertEquals(secondStep.getPLMNInfoSD(), SD_VALUE, "SD has not been set");
        softly.assertAll();
    }

    private void assertThatThereIsOneSuccessfulMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        assertEquals(messages.size(), 1, "There is no 1 message");
        assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS,
                "There is no successful message");
    }

}
