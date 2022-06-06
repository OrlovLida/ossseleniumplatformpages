package com.oss.pages.bpm.milestones;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.BasePage;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/**
 * @author Paweł Rother
 */
public class MilestoneWizardPage extends BasePage {
    private static final String BPM_MILESTONE_NAME = "name";
    private static final String BPM_MILESTONE_DESCRIPTION = "description";
    private static final String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private static final String BPM_MILESTONE_DUE_DATE = "dueDate";
    private static final String BPM_MILESTONE_RELATED_TASK = "relatedTaskIdentifier";
    private static final String BPM_MILESTONE_IS_ACTIVE = "active";
    private static final String BPM_MILESTONE_IS_MANUAL_COMPLETION = "isManualCompletion";
    private static final String BPM_MILESTONE_RELATED_TASK_INPUT = "relatedTaskIdentifier";
    private static final String BPM_MILESTONE_NAME_INPUT = "name-TEXT_FIELD";
    private static final String BPM_MILESTONE_DUE_DATE_INPUT = "dueDate-DATE";
    private static final String BPM_MILESTONE_LEAD_TIME_INPUT = "leadTime-NUMBER_FIELD";
    private static final String BPM_MILESTONE_IS_MANUAL_COMPLETION_INPUT = "isManualCompletion-CHECKBOX";
    private static final String BPM_MILESTONE_DESCRIPTION_INPUT = "description-TEXT_FIELD";
    private static final String BPM_MILESTONE_IS_ACTIVE_INPUT = "active-CHECKBOX";
    private static final String DELETE_MILESTONE_ROW_ACTION_ID = "deleteButton1";
    private static final String NAME_NOT_EDITABLE_EXCEPTION = "Name is not editable. You need Admin permission";
    private static final String DUE_DATE_LABEL = "Due Date";
    private static final String ACTIVE_LABEL = "Active";

    public MilestoneWizardPage(WebDriver driver) {
        super(driver);
    }

    public Milestone addMilestoneRow(Milestone milestone, String addMilestonesListId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        EditableList addMilestoneList = EditableList.createById(driver, wait, addMilestonesListId);
        EditableList.Row row = addMilestoneList.addRow();
        DelayUtils.waitForPageToLoad(driver, wait);

        milestone.getName().ifPresent(name -> row.setValue(milestone.getName().get(), BPM_MILESTONE_NAME,
                BPM_MILESTONE_NAME_INPUT));

        milestone.getDueDate().ifPresent(dueDate -> row.setValue(dueDate, BPM_MILESTONE_DUE_DATE,
                BPM_MILESTONE_DUE_DATE_INPUT));

        milestone.getLeadTime().ifPresent(leadTime -> row.setValue(leadTime, BPM_MILESTONE_LEAD_TIME,
                BPM_MILESTONE_LEAD_TIME_INPUT));

        milestone.getDescription().ifPresent(description -> row.setValue(description, BPM_MILESTONE_DESCRIPTION,
                BPM_MILESTONE_DESCRIPTION_INPUT));

        milestone.getRelatedTask().ifPresent(relatedTask -> row.setValue(relatedTask, BPM_MILESTONE_RELATED_TASK,
                BPM_MILESTONE_RELATED_TASK_INPUT));

        milestone.getIsActive().ifPresent(isActive -> row.setValue(isActive, BPM_MILESTONE_IS_ACTIVE,
                BPM_MILESTONE_IS_ACTIVE_INPUT));

        milestone.getIsManualCompletion().ifPresent(isManualCompletion -> row.setValue(isManualCompletion,
                BPM_MILESTONE_IS_MANUAL_COMPLETION,
                BPM_MILESTONE_IS_MANUAL_COMPLETION_INPUT));

        return getMilestoneFromRow(addMilestoneList, addMilestoneList.getVisibleRows().size() - 1);
    }

    public Milestone editMilestoneRow(Milestone milestone, int row, String milestonesListId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        EditableList milestoneList = EditableList.createById(driver, wait, milestonesListId);
        EditableList.Row editMilestoneRow = milestoneList.getRow(row - 1);
        DelayUtils.sleep(2000);

        milestone.getName().ifPresent(name -> {
            if (!editMilestoneRow.isAttributeEditable(BPM_MILESTONE_NAME)) {
                throw new NoSuchElementException(NAME_NOT_EDITABLE_EXCEPTION);
            }
            editMilestoneRow.setValue(name, BPM_MILESTONE_NAME, BPM_MILESTONE_NAME_INPUT);
        });

        milestone.getDueDate().ifPresent(dueDate -> editMilestoneRow.setValue(dueDate, BPM_MILESTONE_DUE_DATE,
                BPM_MILESTONE_DUE_DATE_INPUT));

        milestone.getLeadTime().ifPresent(leadTime -> editMilestoneRow.setValue(leadTime, BPM_MILESTONE_LEAD_TIME,
                BPM_MILESTONE_LEAD_TIME_INPUT));

        milestone.getDescription().ifPresent(description -> editMilestoneRow.setValue(description,
                BPM_MILESTONE_DESCRIPTION,
                BPM_MILESTONE_DESCRIPTION_INPUT));

        milestone.getRelatedTask().ifPresent(relatedTask -> {
            if (relatedTask.isEmpty()) {
                editMilestoneRow.clearValue(BPM_MILESTONE_RELATED_TASK, BPM_MILESTONE_RELATED_TASK_INPUT);
            } else {
                editMilestoneRow.setValue(relatedTask, BPM_MILESTONE_RELATED_TASK,
                        BPM_MILESTONE_RELATED_TASK_INPUT);
            }
        });

        milestone.getIsManualCompletion().ifPresent(isManualCompletion -> editMilestoneRow.setValue(isManualCompletion,
                BPM_MILESTONE_IS_MANUAL_COMPLETION,
                BPM_MILESTONE_IS_MANUAL_COMPLETION_INPUT));

        milestone.getIsActive().ifPresent(isActive -> editMilestoneRow.setValue(isActive, BPM_MILESTONE_IS_ACTIVE,
                BPM_MILESTONE_IS_ACTIVE_INPUT));

        return getMilestoneFromRow(milestoneList, row - 1);
    }

    public void removeMilestoneRow(int row, String milestonesListId) {
        EditableList milestoneList = EditableList.createById(driver, wait, milestonesListId);
        EditableList.Row editMilestoneRow = milestoneList.getRow(row - 1);
        DelayUtils.sleep(2000);
        editMilestoneRow.callAction(DELETE_MILESTONE_ROW_ACTION_ID);
    }

    private Milestone getMilestoneFromRow(EditableList list, int row) {
        String name = list.getRow(row).getCellValue(BPM_MILESTONE_NAME);
        String dueDate = "";
        if (list.getColumnHeadersLabels().contains(DUE_DATE_LABEL)) {
            dueDate = list.getRow(row).getCellValue(BPM_MILESTONE_DUE_DATE);
        }
        String leadTime = list.getRow(row).getCellValue(BPM_MILESTONE_LEAD_TIME);
        String description = list.getRow(row).getCellValue(BPM_MILESTONE_DESCRIPTION);
        String relatedTask = list.getRow(row).getCellValue(BPM_MILESTONE_RELATED_TASK);
        String isActive = "";
        if (list.getColumnHeadersLabels().contains(ACTIVE_LABEL)) {
            isActive = list.getRow(row).getCellValue(BPM_MILESTONE_IS_ACTIVE);
        }
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
