/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.nfv.networkSliceSubnet;

import java.util.List;

import com.oss.pages.logicalfunction.LogicalFunctionWizardPreStep;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.nfv.common.SideMenuService;
import com.oss.pages.nfv.networkslicesubnet.NetworkSliceSubnetWizardFirstStep;
import com.oss.pages.nfv.networkslicesubnet.NetworkSliceSubnetWizardPage;
import com.oss.pages.nfv.networkslicesubnet.NetworkSliceSubnetWizardSecondStep;
import com.oss.pages.nfv.vnf.VNFWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;

import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.ADMINISTRATIVE_STATE_VALUE;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.MCC_VALUE;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.MNC_VALUE;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_DESCRIPTION;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_NAME;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.OPERATIONAL_STATE_VALUE;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.PLMN_INFO_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.SD_VALUE;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.SLICE_PROFILE_DEFAULT_LABEL_PATH;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.SLICE_PROFILE_NAME;
import static com.oss.nfv.networkSliceSubnet.CreateNetworkSliceSubnetConstants.SST_VALUE;
import static org.testng.Assert.assertEquals;

/**
 * @author Marcin Kozioł
 */
@Listeners({TestListener.class})
public class CreateNetworkSliceSubnetTest extends BaseNetworkSliceSubnetTest {

    @Test(priority = 1, description = "Open 'Create NetworkSliceSubnet' wizard")
    @Description("Got to Resource Specifications view, find and select NetworkSliceSubnet specification and open NetworkSliceSubnet wizard from context menu")
    public void openCreateNetworkSliceSubnetWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        SideMenuService.goToCreateLogicalFunctionView(driver, webDriverWait);
        //when
        LogicalFunctionWizardPreStep logicalFunctionWizardPreStep = LogicalFunctionWizardPreStep.create(driver, webDriverWait);
        logicalFunctionWizardPreStep.searchResourceSpecification(NETWORK_SLICE_SUBNET_SPECIFICATION_IDENTIFIER);
        logicalFunctionWizardPreStep.clickAccept();
    }

    @Test(priority = 2, description = "Edit NetworkSliceSubnet in wizard")
    @Description("Change NetworkSliceSubnet parameters in first step, add Slice Profile with PLMNInfo in second step, create NetworkSliceSubnet")
    public void editNetworkSliceSubnet() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //given
        NetworkSliceSubnetWizardPage wizard = NetworkSliceSubnetWizardPage.create(driver, webDriverWait);
        NetworkSliceSubnetWizardFirstStep firstStep = wizard.getFirstStep();
        //when
        fillNetworkSliceSubnetParams(firstStep);
        //then
        validateNetworkSliceSubnetFirstStepParams(firstStep);
        //when
        wizard.goToNextStep();
        NetworkSliceSubnetWizardSecondStep secondStep = wizard.getSecondStep();
        secondStep.clickAddSliceProfile();
        selectSliceProfile(secondStep);
        fillServiceProfileParams(secondStep);
        //then
        validateSliceProfileParams(secondStep);
        //when
        secondStep.clickAddPLMNInfo();
        selectPLMNInfo(secondStep);
        fillPLMNInfoParams(secondStep);
        //then
        validatePLMNInfoParams(secondStep);
        wizard.goToNextStep();
        wizard.goToNextStep();
        wizard.goToNextStep();
    }

    @Test(priority = 3, description = "Create NetworkSliceSubnet")
    @Description("Submit NetworkSliceSubnet wizard and check system messages")
    public void createNetworkSliceSubnet() {
        //given
        VNFWizardPage wizard = VNFWizardPage.create(driver, webDriverWait);
        //when
        wizard.clickAccept();
        //then
        assertThatThereIsOneSuccessfulMessage();
    }

    private void fillNetworkSliceSubnetParams(NetworkSliceSubnetWizardFirstStep firstStep) {
        firstStep.setName(NETWORK_SLICE_SUBNET_NAME);
        firstStep.setDescription(NETWORK_SLICE_SUBNET_DESCRIPTION);
        firstStep.setAdministrativeState(ADMINISTRATIVE_STATE_VALUE);
        firstStep.setOperationalState(OPERATIONAL_STATE_VALUE);
    }

    private void validateNetworkSliceSubnetFirstStepParams(NetworkSliceSubnetWizardFirstStep firstStep) {
        SoftAssert softly = new SoftAssert();
        softly.assertEquals(firstStep.getName(), NETWORK_SLICE_SUBNET_NAME, "Name has not been set");
        softly.assertEquals(firstStep.getDescription(), NETWORK_SLICE_SUBNET_DESCRIPTION, "Description has not been set");
        softly.assertEquals(firstStep.getAdministrativeState(), ADMINISTRATIVE_STATE_VALUE, "Administrative State has not been set");
        softly.assertEquals(firstStep.getOperationalState(), OPERATIONAL_STATE_VALUE, "Operational State has not been set");
        softly.assertAll();
    }

    private void selectSliceProfile(NetworkSliceSubnetWizardSecondStep secondStep) {
        secondStep.selectNode(SLICE_PROFILE_DEFAULT_LABEL_PATH);
    }

    private void selectPLMNInfo(NetworkSliceSubnetWizardSecondStep secondStep) {
        secondStep.selectNode(PLMN_INFO_DEFAULT_LABEL_PATH);
    }

    private void fillServiceProfileParams(NetworkSliceSubnetWizardSecondStep secondStep) {
        secondStep.setSliceProfileName(SLICE_PROFILE_NAME);
    }

    private void validateSliceProfileParams(NetworkSliceSubnetWizardSecondStep secondStep) {
        assertEquals(secondStep.getSliceProfileName(), SLICE_PROFILE_NAME, "Name has not been set");
    }

    private void fillPLMNInfoParams(NetworkSliceSubnetWizardSecondStep secondStep) {
        secondStep.setPLMNInfoMCC(MCC_VALUE);
        secondStep.setPLMNInfoMNC(MNC_VALUE);
        secondStep.setPLMNInfoSST(SST_VALUE);
        secondStep.setPLMNInfoSD(SD_VALUE);
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
