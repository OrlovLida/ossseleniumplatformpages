/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.milestones;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import org.openqa.selenium.NoSuchElementException;

/**
 * @author Gabriela Kasza
 */
public class EditMilestoneWizardPage extends BasePage {
    private static final String ACCEPT_BUTTON = "wizard-submit-button-milestones-edit_wizard-app";
    private static final String CANCEL_BUTTON = "wizard-cancel-button-milestones-edit_wizard-app";
    private static final String EDIT_MILESTONE_LIST = "milestones-edit_wizard-editable-list";
    private static final String MILESTONE_EDIT_WIZARD_ID = "milestones-edit_wizard-app";
    private static final String DELAY_REASON_ID = "milestones-edit_delay-reason";
    private static final String DELAY_REASON_TEXT = "Selenium Test - Delay reason";

    public EditMilestoneWizardPage(WebDriver driver) {
        super(driver);
    }

    public Milestone editMilestone(Milestone milestone) throws NoSuchElementException {
        Wizard editWizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver);
        Milestone editedMilestone = milestoneWizardPage.editMilestoneRow(milestone, 1, EDIT_MILESTONE_LIST);
        if (driver.getPageSource().contains(DELAY_REASON_ID)) {
            editWizard.setComponentValue(DELAY_REASON_ID, DELAY_REASON_TEXT,
                    Input.ComponentType.TEXT_FIELD);
        }
        editWizard.clickButtonById(ACCEPT_BUTTON);
        return editedMilestone;
    }

    public void cancel() {
        Wizard editWizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
        editWizard.clickButtonById(CANCEL_BUTTON);
    }
}
