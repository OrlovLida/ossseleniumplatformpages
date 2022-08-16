package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.AttachmentWizardPage;

import io.qameta.allure.Step;

public class AttachmentsTab extends BaseSDPage {

    private static final String ATTACHMENT_WIZARD_ID = "addFileComponentId";
    private static final String ATTACH_FILE_BUTTON_ID = "newFileActionName";
    private static final String ATTACHMENTS_LIST_ID = "attachmentManagerBusinessView_commonList";
    private static final String OWNER_COLUMN_NAME = "Owner";
    private static final String DOWNLOAD_ATTACHMENT_LABEL = "DOWNLOAD";
    private static final String DELETE_ATTACHMENT_LABEL = "Delete";
    private static final String CONFIRM_DELETE_ATTACHMENT_BUTTON_ID = "ConfirmationBox_removeAttachmentPopup_confirmationBox_action_button";

    public AttachmentsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Attach File")
    public AttachmentWizardPage clickAttachFile() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, ATTACH_FILE_BUTTON_ID).click();
        return new AttachmentWizardPage(driver, wait, ATTACHMENT_WIZARD_ID);
    }

    @Step("Check if Attachment List is empty")
    public boolean isAttachmentListEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttachmentList().hasNoData();
    }

    @Step("Check if Attachment owner is visible")
    public String getAttachmentOwner() {
        return getAttachmentList().getRows().get(0).getValue(OWNER_COLUMN_NAME);
    }

    @Step("Click Download Attachment")
    public void clickDownloadAttachment() {
        getAttachmentList().getRows().get(0).callActionIcon(DOWNLOAD_ATTACHMENT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click Delete Attachment")
    public void clickDeleteAttachment() {
        getAttachmentList().getRows().get(0).callActionByLabel(DELETE_ATTACHMENT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirmDelete();
    }

    public AttachmentsTab addAttachment(String fileToUpload) {
        clickAttachFile()
                .uploadAttachmentFile(fileToUpload)
                .clickAccept();
        return this;
    }

    private void confirmDelete() {
        Button.createById(driver, CONFIRM_DELETE_ATTACHMENT_BUTTON_ID).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private CommonList getAttachmentList() {
        return CommonList.create(driver, wait, ATTACHMENTS_LIST_ID);
    }
}
