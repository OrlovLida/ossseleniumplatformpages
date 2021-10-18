/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Gabriela Kasza
 */
public class ProcessWizardPage extends BasePage {
    
    public ProcessWizardPage(WebDriver driver) {
        super(driver);
    }
    
    private final static String TABLE_PROCESSES = "bpm_processes_view_processes";
    private final static String PROCESS_WIZARD_STEP_1 = "start-process-wizard";
    private final static String PROCESS_WIZARD_STEP_2 = "bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private final static String DOMAIN_ATTRIBUTE_ID = "domain-combobox-input";
    private final static String DEFINITION_ATTRIBUTE_ID = "definition-combobox-input";
    private final static String RELEASE_ATTRIBUTE_ID = "release-combobox-input";
    private final static String PROCESS_NAME_ATTRIBUTE_ID = "bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private final static String FINISH_DUE_DATE_ID = "FINISHED_DUE_DATE";
    private final static String CREATE_PROCESS_BUTTON = "Create new process";
    private final static String ACCEPT_BUTTON = "wizard-submit-button-start-process-wizard";
    private final static String CREATE_BUTTON = "wizard-submit-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private final static String NEXT_BUTTON = "wizard-next-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private final static String CANCEL_BUTTON = "wizard-cancel-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private final static String PROCESS_NAME = "Selenium Test " + Math.random();
    private final static String INVENTORY_PROCESS = "Inventory Processes";
    private final static String LATEST = "Latest";
    private final static String NRP = "Network Resource Process";
    private final static String DCP = "Data Correction Process";
    private final static String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    private final static String ADD_MILESTONE_LIST = "addMilestonesComponentId";
    
    private final static String BPM_MILESTONE_NAME = "name";
    private final static String BPM_MILESTONE_DESCRIPTION = "description";
    private final static String BPM_MILESTONE_LEAD_TIME = "leadTime";
    private final static String BPM_MILESTONE_DUE_DATE = "dueDate";
    private final static String BPM_MILESTONE_RELATED_TASK = "relatedTaskIdentifier";
    private final static String BPM_MILESTONE_IS_ACTIVE = "active";
    private final static String BPM_MILESTONE_IS_MANUAL_COMPLETION = "isManualCompletion";
    
    public String createSimpleNRP() {
        return createProcess(PROCESS_NAME, (long) 0, NRP);
    }
    
    public String createSimpleDCP() {
        return createProcess(PROCESS_NAME, (long) 0, DCP);
    }
    
    public String createDCPPlusDays(Long plusDays) {
        
        return createProcess(PROCESS_NAME, plusDays, DCP);
    }
    
    public String createNRPPlusDays(Long plusDays) {
        
        return createProcess(PROCESS_NAME, plusDays, NRP);
    }
    
    public String createProcess(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_PROCESSES);
        table.callAction("create", "start-process");
        definedBasicProcess(processName, processType, plusDays).clickActionById(CREATE_BUTTON);
        
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }
    
    @Description("Go to Milestone Step)")
    public MilestoneStepWizard definedMilestoneInProcess(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_PROCESSES);
        table.callAction("create", "start-process");
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        if (driver.getPageSource().contains("Add Milestones")) {
            processWizard.setComponentValue("milestonesEnabledCheckboxId", "true", Input.ComponentType.CHECKBOX);
        }
        DelayUtils.sleep();
        processWizard.clickActionById(NEXT_BUTTON);
        return new MilestoneStepWizard(driver, wait);
    }
    
    private Wizard definedBasicProcess(String processName, String processType, Long plusDays) {
        
        Wizard wizardFirstStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_1);
        Input componentDomain = wizardFirstStep.getComponent(DOMAIN_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
        if (!componentDomain.getValue().getStringValue().equals(INVENTORY_PROCESS)) {
            componentDomain.setSingleStringValue(INVENTORY_PROCESS);
        }
        Input componentDefinition = wizardFirstStep.getComponent(DEFINITION_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
        componentDefinition.setSingleStringValue(processType);
        DelayUtils.sleep(1000);
        Input componentRelease = wizardFirstStep.getComponent(RELEASE_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
        componentRelease.clear();
        componentRelease.setSingleStringValue(LATEST);
        wizardFirstStep.clickActionById(ACCEPT_BUTTON);
        
        Wizard wizardSecondStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
        Input processNameTextField = wizardSecondStep.getComponent(PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        processNameTextField.setSingleStringValue(processName);
        if (driver.getPageSource().contains("FINISHED_DUE_DATE")) {
            Input finishedDueDate = wizardSecondStep.getComponent(FINISH_DUE_DATE_ID, Input.ComponentType.DATE);
            finishedDueDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
        }
        return Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
    }
    
    public void clickAcceptButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickActionById(CREATE_BUTTON);
        
    }
    
    public void clickCancelButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickActionById(CANCEL_BUTTON);
    }
    
    private String extractProcessCode(String message) {
        Iterable<String> messageParts = Splitter.on(CharMatcher.anyOf("() ")).split(message);
        for (String part: messageParts) {
            if (part.startsWith("NRP-") || part.startsWith("DCP-")
                    || part.startsWith("IP-") || part.startsWith("DRP-")) {
                return part;
            }
        }
        throw new RuntimeException("Cannot extract Process Code from message: " + message);
    }
    
    public static class MilestoneStepWizard {
        
        WebDriver driver;
        WebDriverWait wait;
        
        private MilestoneStepWizard(WebDriver driver, WebDriverWait wait) {
            this.driver = driver;
            this.wait = wait;
        }
        
        public EditableList getMilestonePredefinedList() {
            return EditableList.createById(driver, wait, PREDEFINED_MILESTONE_LIST);
        }
        
        public Milestone addMilestoneRow(Milestone milestone) {
            EditableList addMilestoneList = EditableList.createById(driver, wait, ADD_MILESTONE_LIST);
            EditableList.Row row = addMilestoneList.addRow();
            row.setEditableAttributeValue(milestone.getName().get(), BPM_MILESTONE_NAME, "name-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
            
            if (milestone.getDueDate().isPresent()) {
                row.setEditableAttributeValue(milestone.getDueDate().get(), BPM_MILESTONE_DUE_DATE, "dueDate-DATE",
                        Input.ComponentType.DATE);
                
            }
            if (milestone.getLeadTime().isPresent()) {
                row.setEditableAttributeValue(milestone.getLeadTime().get(), BPM_MILESTONE_LEAD_TIME, "leadTime-NUMBER_FIELD",
                        Input.ComponentType.NUMBER_FIELD);
                
            }
            if (milestone.getDescription().isPresent()) {
                row.setEditableAttributeValue(milestone.getDescription().get(), BPM_MILESTONE_DESCRIPTION, "description-TEXT_FIELD",
                        Input.ComponentType.TEXT_FIELD);
            }
            if (milestone.getRelatedTask().isPresent()) {
                row.setEditableAttributeValue(milestone.getRelatedTask().get(), BPM_MILESTONE_RELATED_TASK,
                        "relatedTaskIdentifier-COMBOBOX",
                        Input.ComponentType.COMBOBOX);
            }
            if (milestone.getIsActive().isPresent()) {
                row.setEditableAttributeValue(milestone.getIsActive().get(), BPM_MILESTONE_IS_ACTIVE, "active-CHECKBOX",
                        Input.ComponentType.CHECKBOX);
                
            }
            if (milestone.getIsManualCompletion().isPresent()) {
                row.setEditableAttributeValue(milestone.getIsManualCompletion().get(), BPM_MILESTONE_IS_MANUAL_COMPLETION,
                        "isManualCompletion-CHECKBOX",
                        Input.ComponentType.CHECKBOX);
            }
            return getMilestoneFromRow(addMilestoneList, addMilestoneList.getVisibleRows().size() - 1);
        }
        
        public Milestone editPredefinedMilestone(Milestone milestone, int row) {
            EditableList predefinedMilestoneList = getMilestonePredefinedList();
            EditableList.Row predefineMilestoneRow = predefinedMilestoneList.getRow(row - 1);
            
            if (milestone.getName().isPresent()) {
                predefineMilestoneRow.setEditableAttributeValue(milestone.getName().get(), BPM_MILESTONE_NAME, "name-TEXT_FIELD",
                        Input.ComponentType.TEXT_FIELD);
            }
            
            if (milestone.getDueDate().isPresent()) {
                predefineMilestoneRow.setEditableAttributeValue(milestone.getDueDate().get(), BPM_MILESTONE_DUE_DATE, "dueDate-DATE",
                        Input.ComponentType.DATE);
            }
            if (milestone.getLeadTime().isPresent()) {
                predefineMilestoneRow.setEditableAttributeValue(milestone.getLeadTime().get(), BPM_MILESTONE_LEAD_TIME,
                        "leadTime-NUMBER_FIELD",
                        Input.ComponentType.NUMBER_FIELD);
            }
            if (milestone.getDescription().isPresent()) {
                predefineMilestoneRow.setEditableAttributeValue(milestone.getDescription().get(), BPM_MILESTONE_DESCRIPTION,
                        "description-TEXT_FIELD",
                        Input.ComponentType.TEXT_FIELD);
            }
            if (milestone.getRelatedTask().isPresent()) {
                if (milestone.getRelatedTask().get().equals("")) {
                    predefineMilestoneRow.clearValue(BPM_MILESTONE_RELATED_TASK, "relatedTaskIdentifier-COMBOBOX",
                            Input.ComponentType.COMBOBOX);
                } else {
                    predefineMilestoneRow.setEditableAttributeValue(milestone.getRelatedTask().get(), BPM_MILESTONE_RELATED_TASK,
                            "relatedTaskIdentifier-COMBOBOX",
                            Input.ComponentType.COMBOBOX);
                }
            }
            if (milestone.getIsActive().isPresent()) {
                predefineMilestoneRow.setEditableAttributeValue(milestone.getIsActive().get(), BPM_MILESTONE_IS_ACTIVE, "active-CHECKBOX",
                        Input.ComponentType.CHECKBOX);
                
            }
            if (milestone.getIsManualCompletion().isPresent()) {
                predefineMilestoneRow.setEditableAttributeValue(milestone.getIsManualCompletion().get(), BPM_MILESTONE_IS_MANUAL_COMPLETION,
                        "isManualCompletion-CHECKBOX",
                        Input.ComponentType.CHECKBOX);
            }
            return getMilestoneFromRow(predefinedMilestoneList, row - 1);
            
        }
        
        private Milestone getMilestoneFromRow(EditableList list, int row) {
            String name = list.getRow(row).getAttributeValue(BPM_MILESTONE_NAME);
            String dueDate = list.getRow(row).getAttributeValue(BPM_MILESTONE_DUE_DATE);
            String leadTime = list.getRow(row).getAttributeValue(BPM_MILESTONE_LEAD_TIME);
            String description = list.getRow(row).getAttributeValue(BPM_MILESTONE_DESCRIPTION);
            String relatedTask = list.getRow(row).getAttributeValue(BPM_MILESTONE_RELATED_TASK);
            String isActive = list.getRow(row).getAttributeValue(BPM_MILESTONE_IS_ACTIVE);
            String isManualCompletion = list.getRow(row).getAttributeValue(BPM_MILESTONE_IS_MANUAL_COMPLETION);
            return Milestone.builder().setName(name)
                    .setDueDate(dueDate)
                    .setLeadTime(leadTime)
                    .setDescription(description)
                    .setRelatedTask(relatedTask)
                    .setIsActive(isActive)
                    .setIsManualCompletion(isManualCompletion)
                    .build();
        }
        
        public void clickAcceptButton() {
            Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickActionById(CREATE_BUTTON);
            
        }
        
        public void clickCancelButton() {
            Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickActionById(CANCEL_BUTTON);
        }
        
    }
    
}
