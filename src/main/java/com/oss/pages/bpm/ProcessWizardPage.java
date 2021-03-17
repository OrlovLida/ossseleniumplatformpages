/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

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
import com.sun.org.glassfish.gmbal.Description;

import org.openqa.selenium.WebDriver;

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
    private final static String PROCESS_NAME = "Selenium Test " + Math.random();
    private final static String INVENTORY_PROCESS = "Inventory Processes";
    private final static String LATEST = "Latest";
    private final static String NRP = "Network Resource Process";
    private final static String DCP = "Data Correction Process";
    public final static String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    public final static String ADD_MILESTONE_LIST = "addMilestonesComponentId";
    
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
        table.callActionByLabel(CREATE_PROCESS_BUTTON);
//        Wizard wizardFirstStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_1);
//        Input componentDomain = wizardFirstStep.getComponent(DOMAIN_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
//        if (!componentDomain.getValue().getStringValue().equals(INVENTORY_PROCESS)) {
//            componentDomain.setSingleStringValue(INVENTORY_PROCESS);
//        }
//        Input componentDefinition = wizardFirstStep.getComponent(DEFINITION_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
//        componentDefinition.setSingleStringValue(processType);
//        DelayUtils.sleep(1000);
//        Input componentRelease = wizardFirstStep.getComponent(RELEASE_ATTRIBUTE_ID, Input.ComponentType.COMBOBOX);
//        componentRelease.clear();
//        componentRelease.setSingleStringValue(LATEST);
//        wizardFirstStep.clickActionById(ACCEPT_BUTTON);
//        //wizardFirstStep.waitToClose();
//
//        Wizard wizardSecondStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
//        Input processNameTextField = wizardSecondStep.getComponent(PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
//        processNameTextField.setSingleStringValue(processName);
//        Input finishedDueDate = wizardSecondStep.getComponent(FINISH_DUE_DATE_ID, Input.ComponentType.DATE);
//        finishedDueDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
//        wizardSecondStep.clickActionById(CREATE_BUTTON);
        definedBasicProcess(processName, processType, plusDays).clickActionById(CREATE_BUTTON);
        
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }
    
    @Description("Create Process with defined Milestones in Process Definition")
    public String createProcessWithMilestones(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_PROCESSES);
        table.callActionByLabel(CREATE_PROCESS_BUTTON);
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        processWizard.clickActionById(NEXT_BUTTON);
        processWizard.clickActionById(CREATE_BUTTON);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }
    
    @Description("Define new Milestone in Process (go to Milestone Step)")
    public void definedMilestoneInProcess(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_PROCESSES);
        table.callActionByLabel(CREATE_PROCESS_BUTTON);
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        if (driver.getPageSource().contains("Add Milestones")) {
            processWizard.setComponentValue("milestonesEnabledCheckboxId", "true", Input.ComponentType.CHECKBOX);
        }
        processWizard.clickActionById(NEXT_BUTTON);
    }
    
    public void addMilestoneRow(Milestone milestone, int row) {
        EditableList addMilestoneList = EditableList.createById(driver, wait, ADD_MILESTONE_LIST);
        addMilestoneList.addRow();
        addMilestoneList.setValue(milestone.getName().get(), "name", row, "name-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
        if (!milestone.getDueDate().isPresent()) {
            addMilestoneList.setValue(milestone.getDueDate().get(), "dueDate", row, "dueDate-DATE", Input.ComponentType.DATE);
        }
        if (milestone.getLeadTime().isPresent()) {
            addMilestoneList.setValue(milestone.getLeadTime().get(), "leadTime", row, "leadTime-NUMBER_FIELD", Input.ComponentType.NUMBER_FIELD);
        }
        if (milestone.getDescription().isPresent()) {
            addMilestoneList.setValue(milestone.getDescription().get(), "description", row, "description-TEXT_FIELD",
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getRelatedTask().isPresent()) {
            addMilestoneList.setValue(milestone.getRelatedTask().get(), "relatedTaskIdentifier", row, "relatedTaskIdentifier-COMBOBOX",
                    Input.ComponentType.COMBOBOX);
        }
        if (milestone.getIsActive().isPresent()) {
            addMilestoneList.setValue(milestone.getIsActive().get(), "active", row, "active-CHECKBOX", Input.ComponentType.CHECKBOX);
            
        }
        if (milestone.getIsManualCompletion().isPresent()) {
            addMilestoneList.setValue(milestone.getIsManualCompletion().get(), "isManualCompletion", row, "isManualCompletion-CHECKBOX",
                    Input.ComponentType.CHECKBOX);
        }
    }
    
    public void editPredefinedMilestone(Milestone milestone, int row) {
        EditableList predefinedMilestoneList = EditableList.createById(driver, wait, PREDEFINED_MILESTONE_LIST);
        predefinedMilestoneList.addRow();
        predefinedMilestoneList.setValue(milestone.getName().get(), "name", row, "name-TEXT_FIELD", Input.ComponentType.TEXT_FIELD);
        if (milestone.getDueDate().isPresent()) {
            predefinedMilestoneList.setValue(milestone.getDueDate().get(), "dueDate", row, "dueDate-DATE", Input.ComponentType.DATE);
        }
        if (milestone.getLeadTime().isPresent()) {
            predefinedMilestoneList.setValue(milestone.getLeadTime().get(), "leadTime", row, "leadTime-NUMBER_FIELD",
                    Input.ComponentType.NUMBER_FIELD);
        }
        if (milestone.getDescription().isPresent()) {
            predefinedMilestoneList.setValue(milestone.getDescription().get(), "description", row, "description-TEXT_FIELD",
                    Input.ComponentType.TEXT_FIELD);
        }
        if (milestone.getRelatedTask().isPresent()) {
            predefinedMilestoneList.setValue(milestone.getRelatedTask().get(), "relatedTaskIdentifier", row, "relatedTaskIdentifier-COMBOBOX",
                    Input.ComponentType.COMBOBOX);
        }
        if (milestone.getIsActive().isPresent()) {
            predefinedMilestoneList.setValue(milestone.getIsActive().get(), "active", row, "active-CHECKBOX", Input.ComponentType.CHECKBOX);
            
        }
        if (milestone.getIsManualCompletion().isPresent()) {
            predefinedMilestoneList.setValue(milestone.getIsManualCompletion().get(), "isManualCompletion", row, "isManualCompletion-CHECKBOX",
                    Input.ComponentType.CHECKBOX);
        }
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
        if (driver.getPageSource().contains("Finished Due Date")) {
            Input finishedDueDate = wizardSecondStep.getComponent(FINISH_DUE_DATE_ID, Input.ComponentType.DATE);
            finishedDueDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
        }
        return Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
    }
    
    private void clickAcceptButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickActionById(ACCEPT_BUTTON);
    }
    
    private String extractProcessCode(String message) {
        Iterable<String> messageParts = Splitter.on(" ").split(message);
        for (String part: messageParts) {
            if (part.startsWith("NRP-") || part.startsWith("DCP-")
                    || part.startsWith("IP-") || part.startsWith("DRP-")) {
                return part;
            }
        }
        throw new RuntimeException("Cannot extract Process Code from message: " + message);
    }
    
}
