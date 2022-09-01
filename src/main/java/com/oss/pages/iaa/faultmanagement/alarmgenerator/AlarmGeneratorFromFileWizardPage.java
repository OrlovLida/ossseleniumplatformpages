package com.oss.pages.iaa.faultmanagement.alarmgenerator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class AlarmGeneratorFromFileWizardPage extends BasePage {

    private static final String FILE_TYPE_INPUT_ID = "FileTypeInput";
    private static final String FILE_CHOOSER_ID = "ImportFileInput";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-AAGImportWizard";

    private final Popup alarmGeneratorPopup;

    public AlarmGeneratorFromFileWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        alarmGeneratorPopup = Popup.create(driver, wait);
    }

    @Step("Set File Format Type")
    public void setFileFormatType(String fileFormatType) {
        alarmGeneratorPopup.setComponentValue(FILE_TYPE_INPUT_ID, fileFormatType);
    }

    @Step("Upload an Alarm xml file")
    public void uploadAlarmXmlFile(String path) {
        alarmGeneratorPopup.getComponent(FILE_CHOOSER_ID).setSingleStringValue(getAbsolutePath(path));
    }

    @Step("Click Accept")
    public void clickAcceptButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        alarmGeneratorPopup.clickButtonById(ACCEPT_BUTTON_ID);
    }
}