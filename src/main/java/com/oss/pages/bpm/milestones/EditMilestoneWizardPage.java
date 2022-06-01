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

/**
 * @author Gabriela Kasza
 */
public class EditMilestoneWizardPage extends BasePage {
    private static final String ACCEPT_BUTTON = "wizard-submit-button-milestones-edit_wizard-app";
    private static final String CANCEL_BUTTON = "wizard-cancel-button-milestones-edit_wizard-app";
    private static final String EDIT_MILESTONE_LIST = "milestones-edit_wizard-editable-list";
    private static final String MILESTONE_EDIT_WIZARD_ID = "milestones-edit_wizard-app";

    public EditMilestoneWizardPage(WebDriver driver) {
        super(driver);
    }

    public Milestone editMilestone(Milestone milestone) throws RuntimeException {
        Wizard editWizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
        Milestone editedMilestone = EditMilestoneDefinitionPage.editMilestoneRow(milestone, 1, EDIT_MILESTONE_LIST, driver, wait);
        if (driver.getPageSource().contains("milestones-edit_delay-reason")) {
            editWizard.setComponentValue("milestones-edit_delay-reason", "Selenium Test - Delay reason",
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
