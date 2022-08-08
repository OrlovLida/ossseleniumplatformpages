package com.oss.pages.iaa.servicedesk.issue.wizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.iaa.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class ExportWizardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(ExportWizardPage.class);
    private final Wizard exportWizard;

    private static final String FILE_NAME_TEXTFIELD_ID = "exportgui-components-filenametxt";
    private static final String DATE_MASK_ID = "exportgui-components-dateMaskchoose";

    public ExportWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        exportWizard = Wizard.createByComponentId(driver, wait, wizardId);
    }

    @Step("Fill file name")
    public void fillFileName(String fileName) {
        exportWizard.setComponentValue(FILE_NAME_TEXTFIELD_ID, fileName, Input.ComponentType.TEXT_FIELD);
        log.info("Filling file name with: {}", fileName);
    }

    @Step("Fill date mask")
    public void fillDateMask(String dateMask) {
        ComponentFactory.create(DATE_MASK_ID, driver, wait).setSingleStringValueContains(dateMask);
        log.info("Filling date mask containing: {}", dateMask);
    }

    @Step("Click Accept")
    public void clickAccept() {
        exportWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Accept");
    }
}

