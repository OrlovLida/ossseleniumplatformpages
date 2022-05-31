package com.oss.pages.bpm.milestones;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.BasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Paweł Rother
 */
public class EditMilestoneDefinitionPage extends BasePage {
    private static final String BPM_MILESTONE_NAME = "name";
    private static final String BPM_MILESTONE_DESCRIPTION = "description";
    private static final String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private static final String BPM_MILESTONE_DUE_DATE = "dueDate";
    private static final String BPM_MILESTONE_RELATED_TASK = "relatedTaskIdentifier";
    private static final String BPM_MILESTONE_IS_ACTIVE = "active";
    private static final String BPM_MILESTONE_IS_MANUAL_COMPLETION = "isManualCompletion";
    private static final String BPM_MILESTONE_RELATED_TASK_INPUT = "relatedTaskIdentifier-input";
    private static final String BPM_MILESTONE_NAME_INPUT = "name-TEXT_FIELD";
    private static final String BPM_MILESTONE_DUE_DATE_INPUT = "dueDate-DATE";
    private static final String BPM_MILESTONE_LEAD_TIME_INPUT = "leadTime-NUMBER_FIELD";
    private static final String BPM_MILESTONE_MANUAL_COMPLETION_INPUT = "isManualCompletion-CHECKBOX";
    private static final String BPM_MILESTONE_DESCRIPTION_INPUT = "description-TEXT_FIELD";
    private static final String BPM_MILESTONE_IS_ACTIVE_INPUT = "active-CHECKBOX";


    public EditMilestoneDefinitionPage(WebDriver driver) {
        super(driver);
    }

    public static Milestone addMilestoneRow(Milestone milestone, String addMilestonesListId, WebDriver driver, WebDriverWait wait) {
        EditableList addMilestoneList = EditableList.createById(driver, wait, addMilestonesListId);
        EditableList.Row row = addMilestoneList.addRow();
        row.setValue(milestone.getName().get(), BPM_MILESTONE_NAME, BPM_MILESTONE_NAME_INPUT, Input.ComponentType.TEXT_FIELD);
        if (milestone.getDueDate().isPresent()) {
            row.setValue(milestone.getDueDate().get(), BPM_MILESTONE_DUE_DATE, BPM_MILESTONE_DUE_DATE_INPUT,
                    Input.ComponentType.DATE);
        }
        if (milestone.getLeadTime().isPresent()) {
            row.setValue(milestone.getLeadTime().get(), BPM_MILESTONE_LEAD_TIME, BPM_MILESTONE_LEAD_TIME_INPUT,
                    Input.ComponentType.NUMBER_FIELD);
        }
        if (milestone.getDescription().isPresent()) {
            row.setValue(milestone.getDescription().get(), BPM_MILESTONE_DESCRIPTION, BPM_MILESTONE_DESCRIPTION_INPUT,
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getRelatedTask().isPresent()) {
            row.setValue(milestone.getRelatedTask().get(), BPM_MILESTONE_RELATED_TASK,
                    BPM_MILESTONE_RELATED_TASK_INPUT,
                    Input.ComponentType.COMBOBOX);
        }
        if (milestone.getIsActive().isPresent()) {
            row.setValue(milestone.getIsActive().get(), BPM_MILESTONE_IS_ACTIVE, BPM_MILESTONE_IS_ACTIVE_INPUT,
                    Input.ComponentType.CHECKBOX);
        }
        if (milestone.getIsManualCompletion().isPresent()) {
            row.setValue(milestone.getIsManualCompletion().get(), BPM_MILESTONE_IS_MANUAL_COMPLETION,
                    BPM_MILESTONE_MANUAL_COMPLETION_INPUT,
                    Input.ComponentType.CHECKBOX);
        }
        return getMilestoneFromRow(addMilestoneList, addMilestoneList.getVisibleRows().size() - 1);
    }

    public static Milestone editMilestoneRow(Milestone milestone, int row, String milestonesListId, WebDriver driver, WebDriverWait wait) {
        EditableList milestoneList = EditableList.createById(driver, wait, milestonesListId);
        EditableList.Row editMilestoneRow = milestoneList.getRow(row - 1);
        DelayUtils.sleep(2000);

        milestone.getName().ifPresent(name -> {
            if (!editMilestoneRow.isAttributeEditable(BPM_MILESTONE_NAME)) {
                throw new RuntimeException("Name is not editable. You need Admin permission");
            }
            editMilestoneRow.setValue(name, BPM_MILESTONE_NAME, BPM_MILESTONE_NAME_INPUT,
                    Input.ComponentType.TEXT_FIELD);
        });

        milestone.getDueDate().ifPresent(dueDate -> editMilestoneRow.setValue(dueDate, BPM_MILESTONE_DUE_DATE, BPM_MILESTONE_DUE_DATE_INPUT,
                Input.ComponentType.DATE));

        milestone.getLeadTime().ifPresent(leadTime -> editMilestoneRow.setValue(leadTime, BPM_MILESTONE_LEAD_TIME, BPM_MILESTONE_LEAD_TIME_INPUT,
                Input.ComponentType.NUMBER_FIELD));

        milestone.getDescription().ifPresent(description -> editMilestoneRow.setValue(description, BPM_MILESTONE_DESCRIPTION,
                BPM_MILESTONE_DESCRIPTION_INPUT,
                Input.ComponentType.TEXT_FIELD));

        milestone.getRelatedTask().ifPresent(relatedTask -> {
            if (relatedTask.isEmpty()) {
                editMilestoneRow.clearValue(BPM_MILESTONE_RELATED_TASK, BPM_MILESTONE_RELATED_TASK_INPUT, Input.ComponentType.COMBOBOX);
            } else {
                editMilestoneRow.setValue(relatedTask, BPM_MILESTONE_RELATED_TASK,
                        BPM_MILESTONE_RELATED_TASK_INPUT,
                        Input.ComponentType.COMBOBOX);
            }
        });

        milestone.getIsManualCompletion().ifPresent(isManualCompletion -> editMilestoneRow.setValue(isManualCompletion, BPM_MILESTONE_IS_MANUAL_COMPLETION,
                BPM_MILESTONE_MANUAL_COMPLETION_INPUT,
                Input.ComponentType.CHECKBOX));

        return getMilestoneFromRow(milestoneList, row - 1);
    }

    private static Milestone getMilestoneFromRow(EditableList list, int row) {
        String name = list.getRow(row).getCellValue(BPM_MILESTONE_NAME);
        String dueDate = list.getRow(row).getCellValue(BPM_MILESTONE_DUE_DATE);
        String leadTime = list.getRow(row).getCellValue(BPM_MILESTONE_LEAD_TIME);
        String description = list.getRow(row).getCellValue(BPM_MILESTONE_DESCRIPTION);
        String relatedTask = list.getRow(row).getCellValue(BPM_MILESTONE_RELATED_TASK);
        String isActive = list.getRow(row).getCellValue(BPM_MILESTONE_IS_ACTIVE);
        String isManualCompletion = list.getRow(row).getCellValue(BPM_MILESTONE_IS_MANUAL_COMPLETION);
        return Milestone.builder().setName(name)
                .setDueDate(dueDate)
                .setLeadTime(leadTime)
                .setDescription(description)
                .setRelatedTask(relatedTask)
                .setIsActive(isActive)
                .setIsManualCompletion(isManualCompletion)
                .build();
    }


}
