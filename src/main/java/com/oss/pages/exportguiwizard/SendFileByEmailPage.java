package com.oss.pages.exportguiwizard;

import org.openqa.selenium.WebDriver;

import io.qameta.allure.Step;

public class SendFileByEmailPage extends ExportGuiWizardPage {

    public SendFileByEmailPage(WebDriver driver) {
        super(driver);
        getWizard();
    }

    private static final String RECEIPIENTS_ID = "exportgui-components-emailreceipientsmultisearchtag";
    private static final String ATTACH_EXPORTED_FILE_ID = "exportgui-components-emailattachexportedcheckbox";

    @Step("Choose email address to send")
    public SendFileByEmailPage chooseEmail(String email) {
        getWizard().getComponent(RECEIPIENTS_ID).setSingleStringValueContains(email);
        return this;
    }

    @Step("Check the checkbox to attach exported file")
    public SendFileByEmailPage chooseAttachExportedFile() {
        checkTheCheckbox(ATTACH_EXPORTED_FILE_ID);
        return this;
    }
}
