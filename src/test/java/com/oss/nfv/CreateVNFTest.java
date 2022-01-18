package com.oss.nfv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.comarch.oss.logical.function.api.dto.LogicalFunctionIdentificationDTO;
import com.comarch.oss.resourcecatalog.tmf.api.dto.ResourceSpecificationDTO;
import com.oss.BaseTestCase;
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
import com.oss.services.LogicalFunctionClient;
import com.oss.services.nfv.OnboardClient;
import com.oss.services.nfv.VNFApiClient;
import com.oss.services.nfv.VNFSpecificationClient;
import com.oss.services.resourcecatalog.tmf.TMFCatalogClient;
import com.oss.untils.Environment;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.nfv.CreateVNFTestConstants.CREATE_ACTION_GROUP_ID;
import static com.oss.nfv.CreateVNFTestConstants.CREATE_LOGICAL_FUNCTION_ACTION_LABEL;
import static com.oss.nfv.CreateVNFTestConstants.RESOURCE_CATALOG_PATH;
import static com.oss.nfv.CreateVNFTestConstants.RESOURCE_SPECIFICATIONS_ACTION_LABEL;
import static com.oss.nfv.CreateVNFTestConstants.ROOT_NODE_PATH;
import static com.oss.nfv.CreateVNFTestConstants.SPECIFICATION_NAME_ATTRIBUTE_NAME_LABEL;
import static com.oss.nfv.CreateVNFTestConstants.VNF_INSTANTIATION_LEVEL_0_IDENTIFIER;
import static com.oss.nfv.CreateVNFTestConstants.VNF_INSTANTIATION_LEVEL_0_NAME;
import static com.oss.nfv.CreateVNFTestConstants.VNF_ROOT_IDENTIFIER;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners({TestListener.class})
public class CreateVNFTest extends BaseTestCase {

    private final String XML_FILE_PATH_TO_TEST = "src/test/resources/nfv/specificationDescriptor.xml";

    private static final String NEW_VNF_NAME = "SELENIUM-TEST";

    private Environment env = Environment.getInstance();

    @BeforeClass
    public void prepareData() throws IOException {
        deleteAnyVNFInstances();
        deleteVNFSpecifications();
        onboardNewVNFSpecificationsStructure();
    }

    private void onboardNewVNFSpecificationsStructure() throws IOException {
        String xmlContent = new String(Files.readAllBytes(Paths.get(XML_FILE_PATH_TO_TEST)));
        OnboardClient.getInstance(env).uploadResourceSpecificationAndGetVNFId(xmlContent);
    }

    @Test(priority = 1)
    @Description("Open 'Create VNF' wizard")
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

    @Test(priority = 2)
    @Description("Edit VNF name")
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
        SoftAssertions softly = new SoftAssertions();
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

    @Test(priority = 3)
    @Description("Create VNF")
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
        Assertions.assertThat(messages).describedAs("There is no 1 message").hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).describedAs("There is no successful message")
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    private void assertThatNameHasChangedInTree(SoftAssertions softly, VNFWizardStep vnfWizardStep, String stepName) {
        softly.assertThat(vnfWizardStep.nodeExists(NEW_VNF_NAME))
                .describedAs("No node with name " + NEW_VNF_NAME + " has been found in " + stepName + " step")
                .isTrue();
    }

    private void assertThatNameHasBeenSetInTextField(VNFWizardFirstStep firstStep, SoftAssertions softly) {
        softly.assertThat(firstStep.getName()).describedAs("Name has not been set").isEqualTo(NEW_VNF_NAME);
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
        assertThat(VNFWizardPage.create(driver, webDriverWait).getFirstStep().isStructureTreeVisible()).isTrue();
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

    @AfterClass(alwaysRun = true)
    public void cleanData() {
        deleteAnyVNFInstances();
        deleteVNFSpecifications();
    }

    private void deleteAnyVNFInstances() {
        VNFApiClient vnfApiClient = VNFApiClient.getInstance(env);
        LogicalFunctionClient.getInstance(env).getLogicalFunctionBySpecification(VNF_INSTANTIATION_LEVEL_0_IDENTIFIER)
                .getLogicalFunctionsIdentifications().stream().map(LogicalFunctionIdentificationDTO::getId)
                .forEach(id -> vnfApiClient.deleteVnfById(id.toString()));
    }

    private void deleteVNFSpecifications() {
        Optional<ResourceSpecificationDTO> vnfRootSpec = TMFCatalogClient.getInstance(env).getResourceSpecification(VNF_ROOT_IDENTIFIER);
        vnfRootSpec.map(ResourceSpecificationDTO::getXId).ifPresent(VNFSpecificationClient.getInstance(env)::deleteVnfSpecificationById);
    }
}
