/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.networkSliceSubnet;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.networkslicesubnet.NetworkSliceSubnetWizardFirstStep;
import com.oss.pages.nfv.networkslicesubnet.NetworkSliceSubnetWizardPage;
import com.oss.pages.nfv.networkslicesubnet.NetworkSliceSubnetWizardSecondStep;
import com.oss.pages.nfv.vnf.VNFWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.services.nfv.networkslice.NetworkSliceApiClient;
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

import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.ADMINISTRATIVE_STATE_VALUE;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.OPERATIONAL_STATE_VALUE;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.MCC_VALUE;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.MNC_VALUE;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_DESCRIPTION_NEW;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_NAME;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.PLMN_INFO_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.SD_VALUE;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.SLICE_PROFILE_NAME;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.SST_VALUE;
import static com.oss.nfv.networkSliceSubnet.EditNetworkSliceSubnetConstants.EDIT_LOGICAL_ELEMENT_ACTION_LABEL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Marcin Kozioł
 */
@Listeners({TestListener.class})
public class EditNetworkSliceSubnetTest extends BaseNetworkSliceSubnetTest {
    private final String JSON_FILE_PATH_TO_TEST = "src/test/resources/nfv/networkSliceSubnet/createNetworkSliceSubnet.json";

    @BeforeClass
    public void prepareData() throws IOException {
        super.prepareData();
        createNetworkSliceSubnetInstance();
    }

    private void createNetworkSliceSubnetInstance() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH_TO_TEST)));
        NetworkSliceApiClient.getInstance(env).createNetworkSliceSubnet(jsonContent);
    }

    @Test(priority = 1,description = "Open 'Network Slice Subnet' wizard")
    @Description("Go directly to Inventory View page, find NetworkSliceSubnet, select it on results and open NetworkSlice wizard from context menu")
    public void openEditNetworkSliceSubnetWizard() {
        //given
        NewInventoryViewPage inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "NetworkSliceSubnet");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        //when
        searchForNetworkSliceSubnet(inventoryViewPage);
        selectNetworkSliceSubnetInTree(inventoryViewPage);
        openNetworkSliceSubnetWizardForNetworkSliceSubnetEdit(inventoryViewPage);
    }

    @Test(priority = 2, description = "Edit NetworkSliceSubnet in wizard")
    @Description("Change NetworkSliceSubnet description in first step, check NetworkSliceSubnet in second step, update NetworkSliceSubnet")
    public void editNetworkSliceSubnet() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NetworkSliceSubnetWizardPage wizard = NetworkSliceSubnetWizardPage.create(driver, webDriverWait);
        NetworkSliceSubnetWizardFirstStep firstStep = wizard.getFirstStep();
        //when
        changeNetworkSliceSubnetFirstStepParams(firstStep);
        //then
        validateNetworkSliceSubnetFirstStepParams(firstStep);
        //when
        wizard.goToNextStep();
        NetworkSliceSubnetWizardSecondStep secondStep = wizard.getSecondStep();
        //then
        validateSliceProfileInTree(secondStep);
        //when
        selectSliceProfile(secondStep);
        //then
        validateSliceProfileParams(secondStep);
        validatePLMNInfoInTree(secondStep);
        //when
        selectPLMNInfo(secondStep);
        //then
        validatePLMNInfoParams(secondStep);
        wizard.goToNextStep();
        wizard.goToNextStep();
        wizard.goToNextStep();
    }

    @Test(priority = 3, description = "Update NetworkSliceSubnet")
    @Description("Submit NetworkSliceSubnet wizard and check system messages")
    public void updateNetworkSliceSubnet() {
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        //when
        wizard.clickAccept();
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void validateSliceProfileInTree(NetworkSliceSubnetWizardSecondStep secondStep) {
        assertTrue(secondStep.nodeExists(SLICE_PROFILE_NAME),
                "No node with name " + SLICE_PROFILE_NAME + " has been found");
    }

    private void validatePLMNInfoInTree(NetworkSliceSubnetWizardSecondStep secondStep) {
        assertTrue(secondStep.nodeExists(PLMN_INFO_DEFAULT_LABEL_PATH),
                "No node with name " + PLMN_INFO_DEFAULT_LABEL_PATH + " has been found");
    }

    private void openNetworkSliceSubnetWizardForNetworkSliceSubnetEdit(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.callActionByLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_LOGICAL_ELEMENT_ACTION_LABEL);
    }

    private void selectNetworkSliceSubnetInTree(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.selectFirstRow();
    }

    private void searchForNetworkSliceSubnet(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.searchObject(NETWORK_SLICE_SUBNET_NAME);
    }

    private void changeNetworkSliceSubnetFirstStepParams(NetworkSliceSubnetWizardFirstStep firstStep) {
        firstStep.setDescription(NETWORK_SLICE_SUBNET_DESCRIPTION_NEW);
        firstStep.setAdministrativeState(ADMINISTRATIVE_STATE_VALUE);
        firstStep.setOperationalState(OPERATIONAL_STATE_VALUE);
    }

    private void validateNetworkSliceSubnetFirstStepParams(NetworkSliceSubnetWizardFirstStep firstStep) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(firstStep.getName(), NETWORK_SLICE_SUBNET_NAME, "Name has not been set");
        softly.assertEquals(firstStep.getDescription(), NETWORK_SLICE_SUBNET_DESCRIPTION_NEW, "Description has not been set");
        softly.assertEquals(firstStep.getAdministrativeState(), ADMINISTRATIVE_STATE_VALUE, "Administrative State has not been set");
        softly.assertEquals(firstStep.getOperationalState(), OPERATIONAL_STATE_VALUE, "Operational State has not been set");
        softly.assertAll();
    }

    private void selectSliceProfile(NetworkSliceSubnetWizardSecondStep secondStep) {
        secondStep.selectNode(SLICE_PROFILE_NAME);
    }

    private void selectPLMNInfo(NetworkSliceSubnetWizardSecondStep secondStep) {
        secondStep.selectNode(PLMN_INFO_DEFAULT_LABEL_PATH);
    }

    private void validateSliceProfileParams(NetworkSliceSubnetWizardSecondStep secondStep) {
        assertEquals(secondStep.getSliceProfileName(), SLICE_PROFILE_NAME, "Name has not been set");
    }

    private void validatePLMNInfoParams(NetworkSliceSubnetWizardSecondStep secondStep) {
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
