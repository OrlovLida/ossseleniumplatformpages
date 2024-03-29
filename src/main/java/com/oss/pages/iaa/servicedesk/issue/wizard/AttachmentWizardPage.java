package com.oss.pages.iaa.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.iaa.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class AttachmentWizardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(AttachmentWizardPage.class);
    private static final String UPLOAD_FILE_INPUT_ID = "file";
    private final Wizard attachementWizard;

    public AttachmentWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        attachementWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    @Step("I upload an attachment file")
    public AttachmentWizardPage uploadAttachmentFile(String path) {
        attachementWizard.setComponentValue(UPLOAD_FILE_INPUT_ID, getAbsolutePath(path), Input.ComponentType.FILE_CHOOSER);
        log.info("Uploading attachment file");
        return this;
    }

    @Step("Click Accept")
    public void clickAccept() {
        attachementWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Accept");
    }
}

