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
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.nfv.networkslice.NetworkSliceWizardFirstStep;
import com.oss.pages.nfv.networkslice.NetworkSliceWizardPage;
import com.oss.pages.nfv.networkslice.NetworkSliceWizardSecondStep;
import com.oss.pages.nfv.vnf.VNFWizardPage;
import com.oss.pages.resourcecatalog.ResourceSpecificationsViewPage;
import com.oss.services.LogicalFunctionClient;
import com.oss.services.nfv.networkslice.NetworkSliceApiClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Optional;

import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.CREATE_LOGICAL_FUNCTION_ACTION_LABEL;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.MCC_VALUE;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.MNC_VALUE;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.NETWORK_SLICE_DESCRIPTION;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.NETWORK_SLICE_NAME;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.NETWORK_SLICE_SPECIFICATION_IDENTIFIER;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.NETWORK_SLICE_SPECIFICATION_NAME;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.OPERATIONAL_STATE_VALUE;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.PLMN_INFO_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.PROFILE_SERVICE_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.RESOURCE_CATALOG_PATH;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.RESOURCE_SPECIFICATIONS_ACTION_LABEL;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.SD_VALUE;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.SERVICE_PROFILE_NAME;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL;
import static com.oss.nfv.networkSlice.CreateNetworkSliceConstants.SST_VALUE;
import static org.testng.Assert.assertEquals;

/**
 * @author Marcin Kozioł
 */
@Listeners({TestListener.class})
public class CreateNetworkSliceTest extends BaseTestCase {
    protected Environment env = Environment.getInstance();

    @BeforeClass
    public void prepareData() {
        deleteAnyNetworkSliceInstancesByName();
    }

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteAnyNetworkSliceInstancesByName();
    }

    private void deleteAnyNetworkSliceInstancesByName() {
        NetworkSliceApiClient networkSliceApiClient = NetworkSliceApiClient.getInstance(env);
        LogicalFunctionClient.getInstance(env).getLogicalFunctionByName(NETWORK_SLICE_NAME).stream()
                .map(LogicalFunctionViewDTO::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(networkSliceApiClient::deleteNetworkSlice);
    }

    @Test(priority = 1, description = "Open 'Create NetworkSlice' wizard")
    @Description("Got to Resource Specifications view, find and select NetworkSlice specification and open NetworkSlice wizard from context menu")
    public void openCreateNetworkSliceWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        //when
        goToResourceSpecificationsView(sideMenu);
        ResourceSpecificationsViewPage resourceSpecificationsViewPage = ResourceSpecificationsViewPage.create(driver, webDriverWait);
        searchForNetworkSliceSpecification(resourceSpecificationsViewPage);
        selectNetworkSliceSpecificationInTree(resourceSpecificationsViewPage);
        openNetworkSliceWizardForNetworkSliceCreation(resourceSpecificationsViewPage);
    }

    @Test(priority = 2, description = "Edit NetworkSlice in wizard")
    @Description("Change NetworkSlice parameters in first step, add Service Profile with PLMNInfo in second step, create NetworkSlice")
    public void editNetworkSlice() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NetworkSliceWizardPage wizard = NetworkSliceWizardPage.create(driver, webDriverWait);
        NetworkSliceWizardFirstStep firstStep = wizard.getFirstStep();
        //when
        fillNetworkServiceParams(firstStep);
        //then
        assertNetworkServiceFirstStepParams(firstStep);
        //when
        wizard.goToNextStep();
        NetworkSliceWizardSecondStep secondStep = wizard.getSecondStep();
        secondStep.clickAddServiceProfile();
        selectServiceProfile(secondStep);
        fillServiceProfileParams(secondStep);
        //then
        assertServiceProfileParams(secondStep);
        //when
        secondStep.clickAddPLMNInfo();
        selectPLMNInfo(secondStep);
        fillPLMNInfoParams(secondStep);
        //then
        assertPLMNInfoParams(secondStep);
        wizard.goToNextStep();
    }

    @Test(priority = 3, description = "Create NetworkSlice")
    @Description("Submit NetworkSlice wizard and check system messages")
    public void createNetworkSlice() {
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        //when
        wizard.clickAccept();
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void goToResourceSpecificationsView(SideMenu sideMenu) {
        sideMenu.callActionByLabel(RESOURCE_SPECIFICATIONS_ACTION_LABEL, RESOURCE_CATALOG_PATH, RESOURCE_CATALOG_PATH);
    }

    private void searchForNetworkSliceSpecification(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.setSearchText(NETWORK_SLICE_SPECIFICATION_IDENTIFIER);
    }

    private void openNetworkSliceWizardForNetworkSliceCreation(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.callActionByLabel(ActionsContainer.CREATE_GROUP_ID, CREATE_LOGICAL_FUNCTION_ACTION_LABEL);
    }

    private void selectNetworkSliceSpecificationInTree(ResourceSpecificationsViewPage resourceSpecificationsViewPage) {
        resourceSpecificationsViewPage.selectTreeNode(NETWORK_SLICE_SPECIFICATION_NAME, SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL);
    }

    private void fillNetworkServiceParams(NetworkSliceWizardFirstStep firstStep) {
        firstStep.setName(NETWORK_SLICE_NAME);
        firstStep.setDescription(NETWORK_SLICE_DESCRIPTION);
        firstStep.setOperationalState(OPERATIONAL_STATE_VALUE);
    }

    private void assertNetworkServiceFirstStepParams(NetworkSliceWizardFirstStep firstStep) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(firstStep.getName(), NETWORK_SLICE_NAME, "Name has not been set");
        softly.assertEquals(firstStep.getDescription(), NETWORK_SLICE_DESCRIPTION, "Network Slice has not been set");
        softly.assertEquals(firstStep.getOperationalState(), OPERATIONAL_STATE_VALUE, "Operational State has not been set");
        softly.assertAll();
    }

    private void selectServiceProfile(NetworkSliceWizardSecondStep secondStep) {
        secondStep.selectNode(PROFILE_SERVICE_DEFAULT_LABEL_PATH);
    }

    private void selectPLMNInfo(NetworkSliceWizardSecondStep secondStep) {
        secondStep.selectNode(PLMN_INFO_DEFAULT_LABEL_PATH);
    }

    private void fillServiceProfileParams(NetworkSliceWizardSecondStep secondStep) {
        secondStep.setServiceProfileName(SERVICE_PROFILE_NAME);
    }

    private void assertServiceProfileParams(NetworkSliceWizardSecondStep secondStep) {
        assertEquals(secondStep.getServiceProfileName(), SERVICE_PROFILE_NAME, "Name has not been set");
    }

    private void fillPLMNInfoParams(NetworkSliceWizardSecondStep secondStep) {
        secondStep.setPLMNInfoMCC(MCC_VALUE);
        secondStep.setPLMNInfoMNC(MNC_VALUE);
        secondStep.setPLMNInfoSST(SST_VALUE);
        secondStep.setPLMNInfoSD(SD_VALUE);
    }

    private void assertPLMNInfoParams(NetworkSliceWizardSecondStep secondStep) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(secondStep.getPLMNInfoMCC(), MCC_VALUE, "MCC has not been set");
        softly.assertEquals(secondStep.getPLMNInfoMNC(), MNC_VALUE, "MNC has not been set");
        softly.assertEquals(secondStep.getPLMNInfoSST(), SST_VALUE, "SST has not been set");
        softly.assertEquals(secondStep.getPLMNInfoSD(), SD_VALUE, "SD has not been set");
    }

    private void assertThatThereIsOneSuccessfulMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        assertEquals(messages.size(), 1, "There is no 1 message");
        assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS,
                "There is no successful message");
    }

}
