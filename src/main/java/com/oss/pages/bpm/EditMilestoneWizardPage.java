/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class EditMilestoneWizardPage extends BasePage {
    public EditMilestoneWizardPage(WebDriver driver) {
        super(driver);
    }
    
    private final static String ACCEPT_BUTTON = "wizard-submit-button-milestones-edit_wizard-app";
    private final static String EDIT_MILESTONE_LIST = "milestones-edit_wizard-editable-list";
    private final static String MILESTONE_TABLE = "bpm_milestones_view_milestonesTableWidget";
    private final static String EDIT_MILESTONE_BUTTON = "editMilestonesContextAction";
    
    private final static String BPM_MILESTONE_NAME = "name";
    private final static String BPM_MILESTONE_DESCRIPTION = "description";
    private final static String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private final static String BPM_MILESTONE_DUE_DATE = "dueDate";
    private final static String BPM_MILESTONE_RELATED_TASK = "relatedObject";
    private final static String BPM_MILESTONE_IS_MANUAL_COMPLETION = "isManualCompletion";
    
    public Milestone editMilestone(Milestone milestone) throws RuntimeException {
        
        TableWidget milestoneTable = TableWidget.createById(driver, MILESTONE_TABLE, wait);
        milestoneTable.callAction(EDIT_MILESTONE_BUTTON);
        Wizard editWizard = Wizard.createByComponentId(driver, wait, "Popup");
        EditableList milestoneList = EditableList.createById(driver, wait, EDIT_MILESTONE_LIST);
        EditableList.Row editMilestoneRow = milestoneList.getVisibleRows().get(0);
        DelayUtils.sleep(2000);
        if (milestone.getName().isPresent()) {
            if (!editMilestoneRow.isEditableAttribute(BPM_MILESTONE_NAME))
            {throw new RuntimeException("Name is not editable. You need Admin permission"); }
            editMilestoneRow.setEditableAttributeValue(milestone.getName().get(), BPM_MILESTONE_NAME, "name-TEXT_FIELD",
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getDueDate().isPresent()) {
            editMilestoneRow.setEditableAttributeValue(milestone.getDueDate().get(), BPM_MILESTONE_DUE_DATE, "dueDate-DATE",
                    Input.ComponentType.DATE);
        }
        if (milestone.getLeadTime().isPresent()) {
            editMilestoneRow.setEditableAttributeValue(milestone.getLeadTime().get(), BPM_MILESTONE_LEAD_TIME, "leadTime-NUMBER_FIELD",
                    Input.ComponentType.NUMBER_FIELD);
        }
        if (milestone.getDescription().isPresent()) {
            editMilestoneRow.setEditableAttributeValue(milestone.getDescription().get(), BPM_MILESTONE_DESCRIPTION,
                    "description-TEXT_FIELD",
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getRelatedTask().isPresent()) {
            if (milestone.getRelatedTask().get().equals("")) {
                editMilestoneRow.clearValue(BPM_MILESTONE_RELATED_TASK, "relatedObject-COMBOBOX-input", Input.ComponentType.COMBOBOX);
            } else {
                editMilestoneRow.setEditableAttributeValue(milestone.getRelatedTask().get(), BPM_MILESTONE_RELATED_TASK,
                        "relatedObject-COMBOBOX-input",
                        Input.ComponentType.COMBOBOX);
            }
        }
        if (milestone.getIsManualCompletion().isPresent()) {
            editMilestoneRow.setEditableAttributeValue(milestone.getIsManualCompletion().get(), BPM_MILESTONE_IS_MANUAL_COMPLETION,
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
        editWizard.clickActionById(ACCEPT_BUTTON);
        return editedMilestone;
    }
    
    private Milestone getMilestoneFromRow(EditableList list, int row) {
        String name = list.selectRow(row).getAttributeValue(BPM_MILESTONE_NAME);
        String dueDate = list.selectRow(row).getAttributeValue(BPM_MILESTONE_DUE_DATE);
        String leadTime = list.selectRow(row).getAttributeValue(BPM_MILESTONE_LEAD_TIME);
        String description = list.selectRow(row).getAttributeValue(BPM_MILESTONE_DESCRIPTION);
        String relatedTask = list.selectRow(row).getAttributeValue(BPM_MILESTONE_RELATED_TASK);
        String isManualCompletion = list.selectRow(row).getAttributeValue(BPM_MILESTONE_IS_MANUAL_COMPLETION);
        return Milestone.builder().setName(name)
                .setDueDate(dueDate)
                .setLeadTime(leadTime)
                .setDescription(description)
                .setRelatedTask(relatedTask)
                .setIsManualCompletion(isManualCompletion)
                .build();
    }
}
