package com.oss.pages.faultmanagement;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EditNoteWizardPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(EditNoteWizardPage.class);
    private static final String NOTE_TEXT_FIELD_ID = "EditNoteInput";
    private static final String WIZARD_ID = "_EditNoteModal";

    private final Wizard editWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public EditNoteWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Type Name of the folder")
    public void typeNote(String note) {
        editWizard.getComponent(NOTE_TEXT_FIELD_ID, Input.ComponentType.TEXT_AREA).setSingleStringValue(note);
        editWizard.clickAccept();
        log.info("Adding note to alarm {}", note);
    }
}
