package com.oss.pages.bpm.processinstances.edition;

import com.comarch.oss.web.pages.BasePage;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

public class ChangeFDDWizardPage extends BasePage {
    private static final String WIZARD_ID = "shift_wizard_id_prompt-card";
    private static final String NEW_FDD_INPUT_ID = "shift_date_component_id";
    private static final String REASON_INPUT_ID = "shift_change_reason_component_id";
    private static final String WIZARD_INFO_TEXT_FIELD_ID = "OLD_TEXT_FIELD_APP-shift_text_field_id";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-shift_wizard_id";
    private static final String CANCEL_BUTTON_ID = "wizard-cancel-button-shift_wizard_id";
    private static final String CLOSE_BUTTON_ID = "shift_wizard_id_prompt-card_close-prompt";
    private final Wizard wizard;

    public ChangeFDDWizardPage(WebDriver driver) {
        super(driver);
        this.wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void accept() {
        wizard.clickButtonById(ACCEPT_BUTTON_ID);
        wizard.waitToClose();
    }

    public void cancel() {
        wizard.clickButtonById(CANCEL_BUTTON_ID);
        wizard.waitToClose();
    }

    public void close() {
        wizard.clickButtonById(CLOSE_BUTTON_ID);
        wizard.waitToClose();
    }

    public String getInformation() {
        return wizard.getComponent(WIZARD_INFO_TEXT_FIELD_ID).getStringValue();
    }

    public ChangeFDDWizardPage setNewFDD(LocalDate newFDD) {
        wizard.setComponentValue(NEW_FDD_INPUT_ID, String.valueOf(newFDD));
        return this;
    }

    public ChangeFDDWizardPage setReason(String reason) {
        wizard.setComponentValue(REASON_INPUT_ID, reason);
        return this;
    }

    public void changeFDD(LocalDate newFDD, String reason) {
        setNewFDD(newFDD);
        setReason(reason);
        accept();
    }

    public void changeFDD(LocalDate newFDD) {
        setNewFDD(newFDD);
        accept();
    }

    public static boolean isWizardVisible(WebDriver driver) {
        return CSSUtils.isElementPresent(driver, WIZARD_ID);
    }


}
