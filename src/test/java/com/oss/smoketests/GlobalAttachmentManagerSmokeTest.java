package com.oss.smoketests;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.dms.CreateDirectoryWizardPage;
import com.oss.pages.dms.GlobalAttachmentManagerPage;
import com.oss.pages.dms.UpdateDirectoryWizardPage;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.CREATE_DIRECTORY_ACTION_ID;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.DELETE_DIRECTORY_ACTION_ID;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.EDIT_DIRECTORY_ACTION_ID;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_BUTTON_LABEL;

public class GlobalAttachmentManagerSmokeTest extends BaseTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GisMapSmokeTest.class);
    private static final String RESOURCE_INVENTORY_CATEGORY_NAME = "Resource Inventory";
    private static final String GLOBAL_ATTACHMENT_MANAGER_APPLICATION_NAME = "Global Attachment Manager";

    private static final String DIRECTORY_NAME = "01SmokeTestDirectory";
    private static final String UPDATED_DIRECTORY_NAME = "02SmokeTestUpdatedFolder";
    private static final String UPDATED_TAG_FOLDER = "tagtest";
    private static final String EXPECTED_TAGS_LIST = "tagtest, tagsmoke";
    private static final String TAG_FOLDER = "tagsmoke";
    private static final String OWNER = CONFIGURATION.getLogin();
    private static final String CREATE_DIRECTORY_SUCCESS_MESSAGE = "Success! Directory with name SmokeTestDirectory has been created";
    private static final String UPDATE_DIRECTORY_SUCCESS_MESSAGE = "Success! Directory was successfully edited";
    private static final String DELETE_DIRECTORY_SUCCESS_MESSAGE = "Success! Successful removed folder";

    @Test(priority = 1, description = "Check Home Page")
    @Description("Check Home Page")
    public void checkHomePage() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
    }

    @Test(priority = 2, description = "Open Global Attachment Manager")
    @Description("Open Global Attachment Manager")
    public void openGlobalAttachmentManager() {
        HomePage homePage = new HomePage(driver);
        homePage.openApplication(RESOURCE_INVENTORY_CATEGORY_NAME, GLOBAL_ATTACHMENT_MANAGER_APPLICATION_NAME);
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
    }

    @Test(priority = 3, description = "Create Directory")
    @Description("Create Directory")
    public void createDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DIRECTORY_ACTION_ID);
        createNewDirectory();
        checkPopup(CREATE_DIRECTORY_SUCCESS_MESSAGE);
        closeMessage();
        assertDirectoryAttributes(DIRECTORY_NAME, TAG_FOLDER);
        waitForPageToLoad();
    }

    @Test(priority = 4, description = "Edit Directory")
    @Description("Edit Directory")
    public void editDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.selectRow(DIRECTORY_NAME);
        globalAttachmentManagerPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_DIRECTORY_ACTION_ID);
        updateDirectory();
        assertDirectoryAttributes(UPDATED_DIRECTORY_NAME, EXPECTED_TAGS_LIST);
        checkPopup(UPDATE_DIRECTORY_SUCCESS_MESSAGE);
        closeMessage();
    }

    @Test(priority = 5, description = "Delete Directory")
    @Description("Delete Directory")
    public void deleteDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_DIRECTORY_ACTION_ID);
        confirmDirectoryDeletion();
        checkPopup(DELETE_DIRECTORY_SUCCESS_MESSAGE);
        closeMessage();
    }

    @Step("Waiting for page to load")
    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Check Error Page")
    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    @Step("Assert Directory Attributes")
    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }

    @Step("Check Popup")
    private void checkPopup(String expectedMessage) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        SoftAssert softAssert = new SoftAssert();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        softAssert.assertEquals(expectedMessage, systemMessage.getFirstMessage());
    }

    @Step("Assert Directory Attributes")
    public void assertDirectoryAttributes(String expectedDirectoryName, String expectedTagFolder) {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        String actualName = globalAttachmentManagerPage.getName(expectedDirectoryName);
        String actualOwner = globalAttachmentManagerPage.getOwner(OWNER);
        String actualTags = globalAttachmentManagerPage.getTags(expectedTagFolder);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(expectedDirectoryName, actualName);
        softAssert.assertEquals(OWNER, actualOwner);
        softAssert.assertEquals(expectedTagFolder, actualTags);
        softAssert.assertAll();
    }

    @Step("Create Directory")
    private void createNewDirectory() {
        CreateDirectoryWizardPage createDirectoryWizardPage = new CreateDirectoryWizardPage(driver);
        createDirectoryWizardPage.setDirectoryName(DIRECTORY_NAME);
        createDirectoryWizardPage.setTagFolder(TAG_FOLDER);
        createDirectoryWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Step("Update Directory")
    private void updateDirectory() {
        UpdateDirectoryWizardPage updateDirectoryWizardPage = new UpdateDirectoryWizardPage(driver);
        updateDirectoryWizardPage.setDirectoryName(UPDATED_DIRECTORY_NAME);
        updateDirectoryWizardPage.setTagFolder(UPDATED_TAG_FOLDER);
        updateDirectoryWizardPage.clickAccept();
        waitForPageToLoad();
    }

    @Step("Confirm Directory Deletion")
    private void confirmDirectoryDeletion() {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel(DELETE_BUTTON_LABEL);
        waitForPageToLoad();
    }

    @Step("Close Message")
    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }
}