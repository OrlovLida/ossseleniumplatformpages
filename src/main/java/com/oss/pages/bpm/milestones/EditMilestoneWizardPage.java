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
    private static final String BPM_MILESTONE_NAME = "name";
    private static final String BPM_MILESTONE_DESCRIPTION = "description";
    private static final String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private static final String BPM_MILESTONE_DUE_DATE = "dueDate";
    private static final String BPM_MILESTONE_RELATED_TASK = "relatedObject";
    private static final String BPM_MILESTONE_IS_MANUAL_COMPLETION = "isManualCompletion";
    private static final String MILESTONE_EDIT_WIZARD_ID = "milestones-edit_wizard-app";
    private static final String BPM_MILESTONE_NAME_INPUT = "name-TEXT_FIELD";
    private static final String BPM_MILESTONE_DUE_DATE_INPUT = "dueDate-DATE";
    private static final String BPM_MILESTONE_RELATED_TASK_INPUT = "relatedObject-input";
    private static final String BPM_MILESTONE_LEAD_TIME_INPUT = "leadTime-NUMBER_FIELD";
    private static final String BPM_MILESTONE_MANUAL_COMPLETION_INPUT = "isManualCompletion-CHECKBOX";
    private static final String BPM_MILESTONE_DESCRIPTION_INPUT = "description-TEXT_FIELD";

    public EditMilestoneWizardPage(WebDriver driver) {
        super(driver);
    }

    public Milestone editMilestone(Milestone milestone) throws RuntimeException {
        Wizard editWizard = Wizard.createByComponentId(driver, wait, MILESTONE_EDIT_WIZARD_ID);
//        EditableList milestoneList = EditableList.createById(driver, wait, EDIT_MILESTONE_LIST);
//        EditableList.Row editMilestoneRow = milestoneList.getVisibleRows().get(0);
//        DelayUtils.sleep(2000);
//
//        milestone.getName().ifPresent(name -> {
//            if (!editMilestoneRow.isAttributeEditable(BPM_MILESTONE_NAME)) {
//                throw new RuntimeException("Name is not editable. You need Admin permission");
//            }
//            editMilestoneRow.setValue(name, BPM_MILESTONE_NAME, BPM_MILESTONE_NAME_INPUT,
//                    Input.ComponentType.TEXT_FIELD);
//        });
//
//        milestone.getDueDate().ifPresent(dueDate -> editMilestoneRow.setValue(dueDate, BPM_MILESTONE_DUE_DATE, BPM_MILESTONE_DUE_DATE_INPUT,
//                Input.ComponentType.DATE));
//
//        milestone.getLeadTime().ifPresent(leadTime -> editMilestoneRow.setValue(leadTime, BPM_MILESTONE_LEAD_TIME, BPM_MILESTONE_LEAD_TIME_INPUT,
//                Input.ComponentType.NUMBER_FIELD));
//
//        milestone.getDescription().ifPresent(description -> editMilestoneRow.setValue(description, BPM_MILESTONE_DESCRIPTION,
//                BPM_MILESTONE_DESCRIPTION_INPUT,
//                Input.ComponentType.TEXT_FIELD));
//
//        milestone.getRelatedTask().ifPresent(relatedTask -> {
//            if (relatedTask.isEmpty()) {
//                editMilestoneRow.clearValue(BPM_MILESTONE_RELATED_TASK, BPM_MILESTONE_RELATED_TASK_INPUT, Input.ComponentType.COMBOBOX);
//            } else {
//                editMilestoneRow.setValue(relatedTask, BPM_MILESTONE_RELATED_TASK,
//                        BPM_MILESTONE_RELATED_TASK_INPUT,
//                        Input.ComponentType.COMBOBOX);
//            }
//        });
//
//        milestone.getIsManualCompletion().ifPresent(isManualCompletion -> editMilestoneRow.setValue(isManualCompletion, BPM_MILESTONE_IS_MANUAL_COMPLETION,
//                BPM_MILESTONE_MANUAL_COMPLETION_INPUT,
//                Input.ComponentType.CHECKBOX));
//
//        Milestone editedMilestone = getMilestoneFromRow(milestoneList, 0);
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

//    private Milestone getMilestoneFromRow(EditableList list, int row) {
//        String name = list.getRow(row).getCellValue(BPM_MILESTONE_NAME);
//        String dueDate = list.getRow(row).getCellValue(BPM_MILESTONE_DUE_DATE);
//        String leadTime = list.getRow(row).getCellValue(BPM_MILESTONE_LEAD_TIME);
//        String description = list.getRow(row).getCellValue(BPM_MILESTONE_DESCRIPTION);
//        String relatedTask = list.getRow(row).getCellValue(BPM_MILESTONE_RELATED_TASK);
//        String isManualCompletion = list.getRow(row).getCellValue(BPM_MILESTONE_IS_MANUAL_COMPLETION);
//        return Milestone.builder().setName(name)
//                .setDueDate(dueDate)
//                .setLeadTime(leadTime)
//                .setDescription(description)
//                .setRelatedTask(relatedTask)
//                .setIsManualCompletion(isManualCompletion)
//                .build();
//    }
}
