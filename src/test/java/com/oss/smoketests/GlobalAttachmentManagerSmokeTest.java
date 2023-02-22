package com.oss.smoketests;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

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
import com.oss.pages.dms.AttachFileWizardPage;
import com.oss.pages.dms.CreateDirectoryWizardPage;
import com.oss.pages.dms.GlobalAttachmentManagerPage;
import com.oss.pages.dms.UpdateDirectoryWizardPage;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.ATTACH_FILE_ACTION_ID;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.CREATE_DIRECTORY_ACTION_ID;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.DELETE_DIRECTORY_ACTION_ID;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.DELETE_FILE_ACTION_ID;
import static com.oss.pages.dms.GlobalAttachmentManagerPage.EDIT_DIRECTORY_ACTION_ID;
import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.DELETE_BUTTON_LABEL;

public class GlobalAttachmentManagerSmokeTest extends BaseTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalAttachmentManagerSmokeTest.class);
    private static final String RESOURCE_INVENTORY_CATEGORY_NAME = "Resource Inventory";
    private static final String GLOBAL_ATTACHMENT_MANAGER_APPLICATION_NAME = "Global Attachment Manager";
    private static final String FILE_NAME = "GamTest.txt";
    private static final String DIRECTORY_NAME = "01SmokeTestDirectory";
    private static final String UPDATED_DIRECTORY_NAME = "02SmokeTestUpdatedFolder";
    private static final String UPDATED_TAG_FOLDER = "tagtest";
    private static final String EXPECTED_TAGS_LIST = "tagtest, tagsmoke";
    private static final String TAG_FOLDER = "tagsmoke";
    private static final String OWNER = CONFIGURATION.getLogin();
    private static final String HOME_DIRECTORY = "HOME";
    private static final String NAME_MESSAGE = " name is other than expected";
    private static final String OWNER_MESSAGE = " owner is other than expected";

    @Test(priority = 1, description = "Check Home Page")
    @Description("Check Home Page")
    public void checkHomePage() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
    }

    @Test(priority = 2, description = "Open Global Attachment Manager", dependsOnMethods = {"checkHomePage"})
    @Description("Open Global Attachment Manager")
    public void openGlobalAttachmentManager() {
        HomePage homePage = new HomePage(driver);
        homePage.openApplication(RESOURCE_INVENTORY_CATEGORY_NAME, GLOBAL_ATTACHMENT_MANAGER_APPLICATION_NAME);
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
    }

    @Test(priority = 3, description = "Create Directory", dependsOnMethods = {"openGlobalAttachmentManager"})
    @Description("Create Directory")
    public void createDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DIRECTORY_ACTION_ID);
        createNewDirectory();
        checkPopup();
        closeMessage();
        assertDirectoryAttributes(DIRECTORY_NAME, TAG_FOLDER);
        waitForPageToLoad();
    }

    @Test(priority = 4, description = "Edit Directory", dependsOnMethods = {"createDirectory"})
    @Description("Edit Directory")
    public void editDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.selectRow(DIRECTORY_NAME);
        globalAttachmentManagerPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_DIRECTORY_ACTION_ID);
        updateDirectory();
        assertDirectoryAttributes(UPDATED_DIRECTORY_NAME, EXPECTED_TAGS_LIST);
        checkPopup();
        closeMessage();
    }

    @Test(priority = 5, description = "Attach File", dependsOnMethods = {"createDirectory"})
    @Description("Attach File")
    public void attachFile() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.selectRow(UPDATED_DIRECTORY_NAME);
        globalAttachmentManagerPage.callAction(ActionsContainer.CREATE_GROUP_ID, ATTACH_FILE_ACTION_ID);
        attachNewFile();
        checkPopup();
        closeMessage();
        globalAttachmentManagerPage.openDirectory(UPDATED_DIRECTORY_NAME);

    }

    @Test(priority = 6, description = "Check nd delete attached file", dependsOnMethods = {"attachFile"})
    @Description("Check and delete attached file")
    public void checkAndDeleteAttachedFile() {
        assertFilePresenceInDirectory();
        deleteFile();
        confirmDeletion();

        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        Assert.assertTrue(globalAttachmentManagerPage.isListEmpty());

        globalAttachmentManagerPage.clickLink(HOME_DIRECTORY);
    }

    @Test(priority = 7, description = "Delete Directory", dependsOnMethods = {"createDirectory"})
    @Description("Delete Directory")
    public void deleteDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.selectRow(UPDATED_DIRECTORY_NAME);
        globalAttachmentManagerPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_DIRECTORY_ACTION_ID);
        confirmDeletion();
        checkPopup();
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
    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS, "The list is empty");
    }

    @Step("Assert Directory Attributes")
    private void assertDirectoryAttributes(String expectedDirectoryName, String expectedTags) {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(globalAttachmentManagerPage.isNamePresent(expectedDirectoryName), "Directory" + NAME_MESSAGE);
        softAssert.assertTrue(globalAttachmentManagerPage.isOwnerPresent(OWNER), "Directory" + OWNER_MESSAGE);
        softAssert.assertTrue(globalAttachmentManagerPage.areTagsPresent(expectedTags), "Directory tags are other than expected");
        softAssert.assertAll();
    }

    @Step("Assert attached file")
    private void assertFilePresenceInDirectory() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(globalAttachmentManagerPage.isNamePresent(FILE_NAME), "File" + NAME_MESSAGE);
        softAssert.assertTrue(globalAttachmentManagerPage.isOwnerPresent(OWNER), "File" + OWNER_MESSAGE);
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

    @Step("Delete file")
    private void deleteFile() {
        GlobalAttachmentManagerPage globalAttachmentManagerPage = new GlobalAttachmentManagerPage(driver);
        globalAttachmentManagerPage.selectRow(FILE_NAME);
        globalAttachmentManagerPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_FILE_ACTION_ID);
    }

    @Step("Confirm Deletion")
    private void confirmDeletion() {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, webDriverWait);
        prompt.clickButtonByLabel(DELETE_BUTTON_LABEL);
        waitForPageToLoad();
    }

    @Step("Close Message")
    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    @Step("Attach File")
    private void attachNewFile() {
        try {
            java.net.URL resource = GlobalAttachmentManagerSmokeTest.class.getClassLoader().getResource("gam/" + FILE_NAME);
            assert resource != null;
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            AttachFileWizardPage attachFileWizardPage = new AttachFileWizardPage(driver);
            attachFileWizardPage.attachFile(absolutePatch);
            attachFileWizardPage.selectDirectory(UPDATED_DIRECTORY_NAME);
            attachFileWizardPage.skipAndAccept();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
    }
}