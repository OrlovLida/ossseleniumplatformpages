/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.dms;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class AttachFileWizardPage extends BasePage {

    private static final String WIZARD_ID = "addFileComponentId";
    private static final String FILE_COMPONENT_ID = "file";
    private static final String DIRECTORY_COMPONENT_ID = "parentId";
    private static final String NEXT_BUTTON_ID = "wizard-next-button-addFileComponentId";
    private static final String ACCEPT_BUTTON_ID = "wizard-submit-button-addFileComponentId";
    private static final String RADIO_BUTTONS_ID = "duplicateFilesRadioButtons";

    private final Wizard addFileWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public AttachFileWizardPage(WebDriver driver) {
        super(driver);
    }

    public static boolean isWizardVisible(WebDriver driver) {
        return CSSUtils.isElementPresent(driver, WIZARD_ID);
    }

    public void attachFile(String filePath) {
        addFileWizard.setComponentValue(FILE_COMPONENT_ID, filePath);
    }

    public void selectDirectory(String directoryName) {
        addFileWizard.setComponentValue(DIRECTORY_COMPONENT_ID, directoryName);
    }

    public void nextButton() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickButtonById(NEXT_BUTTON_ID);
    }

    public void acceptButton() {
        addFileWizard.clickButtonById(ACCEPT_BUTTON_ID);
    }

    public void cancelButton() {
        addFileWizard.clickCancel();
    }

    public void deleteFiles() {
        Input input = addFileWizard.getComponent(FILE_COMPONENT_ID);
        input.clear();
    }

    public List<String> getAttachmentName() {
        Input input = addFileWizard.getComponent(FILE_COMPONENT_ID);
        return input.getStringValues();
    }

    public void selectRadioButton(String label) {
        Input radio = Wizard.createByComponentId(driver, wait, WIZARD_ID)
                .getComponent(RADIO_BUTTONS_ID);
        radio.setSingleStringValue(label);
        DelayUtils.sleep();
    }

    public void skipAndAccept() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).skipAndAccept(ACCEPT_BUTTON_ID);
    }
}
