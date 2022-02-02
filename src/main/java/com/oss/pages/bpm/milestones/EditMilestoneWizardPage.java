/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.milestones;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class EditMilestoneWizardPage extends BasePage {
    private static final String ACCEPT_BUTTON = "wizard-submit-button-milestones-edit_wizard-app";
    private static final String CANCEL_BUTTON = "wizard-cancel-button-milestones-edit_wizard-app";
    private static final String EDIT_MILESTONE_LIST = "milestones-edit_wizard-editable-list";
    private static final String MILESTONE_TABLE = "bpm_milestones_view_milestonesTableWidget";
    private static final String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";
    private static final String BPM_MILESTONE_NAME = "name";
    private static final String BPM_MILESTONE_DESCRIPTION = "description";
    private static final String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private static final String BPM_MILESTONE_DUE_DATE = "dueDate";
    private static final String BPM_MILESTONE_RELATED_TASK = "relatedObject";
    private static final String BPM_MILESTONE_IS_MANUAL_COMPLETION = "isManualCompletion";
    private static final String MILESTONE_EDIT_WIZARD_ID = "milestones-edit_wizard-app";

    public EditMilestoneWizardPage(WebDriver driver) {
        super(driver);
    }

    public Milestone editMilestone(Milestone milestone) throws RuntimeException {
        Wizard editWizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
        EditableList milestoneList = EditableList.createById(driver, wait, EDIT_MILESTONE_LIST);
        EditableList.Row editMilestoneRow = milestoneList.getVisibleRows().get(0);
        DelayUtils.sleep(2000);
        if (milestone.getName().isPresent()) {
            if (!editMilestoneRow.isAttributeEditable(BPM_MILESTONE_NAME)) {
                throw new RuntimeException("Name is not editable. You need Admin permission");
            }
            editMilestoneRow.setValue(milestone.getName().get(), BPM_MILESTONE_NAME, "name-TEXT_FIELD",
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getDueDate().isPresent()) {
            editMilestoneRow.setValue(milestone.getDueDate().get(), BPM_MILESTONE_DUE_DATE, "dueDate-DATE",
                    Input.ComponentType.DATE);
        }
        if (milestone.getLeadTime().isPresent()) {
            editMilestoneRow.setValue(milestone.getLeadTime().get(), BPM_MILESTONE_LEAD_TIME, "leadTime-NUMBER_FIELD",
                    Input.ComponentType.NUMBER_FIELD);
        }
        if (milestone.getDescription().isPresent()) {
            editMilestoneRow.setValue(milestone.getDescription().get(), BPM_MILESTONE_DESCRIPTION,
                    "description-TEXT_FIELD",
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getRelatedTask().isPresent()) {
            if (milestone.getRelatedTask().get().equals("")) {
                editMilestoneRow.clearValue(BPM_MILESTONE_RELATED_TASK, "relatedObject-COMBOBOX-input", Input.ComponentType.COMBOBOX);
            } else {
                editMilestoneRow.setValue(milestone.getRelatedTask().get(), BPM_MILESTONE_RELATED_TASK,
                        "relatedObject-COMBOBOX-input",
                        Input.ComponentType.COMBOBOX);
            }
        }
        if (milestone.getIsManualCompletion().isPresent()) {
            editMilestoneRow.setValue(milestone.getIsManualCompletion().get(), BPM_MILESTONE_IS_MANUAL_COMPLETION,
                    "isManualCompletion-CHECKBOX",
                    Input.ComponentType.CHECKBOX);
        }
        Milestone editedMilestone = getMilestoneFromRow(milestoneList, 0);
        if (driver.getPageSource().contains("milestones-edit_delay-reason")) {
            {
                editWizard.setComponentValue("milestones-edit_delay-reason", "Selenium Test - Delay reason",
                        Input.ComponentType.TEXT_FIELD);
            }
        }
        editWizard.clickButtonById(ACCEPT_BUTTON);
        return editedMilestone;
    }

    public void cancel() {
        Wizard editWizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
        editWizard.clickButtonById(CANCEL_BUTTON);
    }

    private Milestone getMilestoneFromRow(EditableList list, int row) {
        String name = list.getRow(row).getCellValue(BPM_MILESTONE_NAME);
        String dueDate = list.getRow(row).getCellValue(BPM_MILESTONE_DUE_DATE);
        String leadTime = list.getRow(row).getCellValue(BPM_MILESTONE_LEAD_TIME);
        String description = list.getRow(row).getCellValue(BPM_MILESTONE_DESCRIPTION);
        String relatedTask = list.getRow(row).getCellValue(BPM_MILESTONE_RELATED_TASK);
        String isManualCompletion = list.getRow(row).getCellValue(BPM_MILESTONE_IS_MANUAL_COMPLETION);
        return Milestone.builder().setName(name)
                .setDueDate(dueDate)
                .setLeadTime(leadTime)
                .setDescription(description)
                .setRelatedTask(relatedTask)
                .setIsManualCompletion(isManualCompletion)
                .build();
    }
}
