/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.milestones;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

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
    private final Wizard wizard;

    public EditMilestoneWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
    }

    public Milestone editMilestone(Milestone milestone) throws NoSuchElementException {
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, EDIT_MILESTONE_LIST);
        Milestone editedMilestone = milestoneWizardPage.editMilestoneRow(milestone, 1);
        if (driver.getPageSource().contains(DELAY_REASON_ID)) {
            wizard.setComponentValue(DELAY_REASON_ID, DELAY_REASON_TEXT,
                    Input.ComponentType.TEXT_FIELD);
        }
        accept();
        return editedMilestone;
    }

    public void accept() {
        wizard.clickButtonById(ACCEPT_BUTTON);
        wizard.waitToClose();
    }

    public void cancel() {
        wizard.clickButtonById(CANCEL_BUTTON);
        wizard.waitToClose();
    }
}
