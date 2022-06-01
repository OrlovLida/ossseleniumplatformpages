/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.processinstances;

import java.time.LocalDate;
import java.util.List;

import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.milestones.Milestone;

import io.qameta.allure.Description;

/**
 * @author Gabriela Kasza
 */
public class ProcessWizardPage extends BasePage {

    private static final String TABLE_PROCESSES = "bpm_processes_view_processes";
    private static final String PROCESS_WIZARD_STEP_1 = "start-process-wizard";
    private static final String PROCESS_WIZARD_STEP_2 = "bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String DOMAIN_ATTRIBUTE_ID = "domain-combobox-input";
    private static final String DEFINITION_ATTRIBUTE_ID = "definition-combobox-input";
    private static final String RELEASE_ATTRIBUTE_ID = "release-combobox-input";
    private static final String PROCESS_NAME_ATTRIBUTE_ID = "bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String FINISH_DUE_DATE_ID = "FINISHED_DUE_DATE";
    private static final String ACCEPT_BUTTON = "wizard-submit-button-start-process-wizard";
    private static final String CREATE_BUTTON = "wizard-submit-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String NEXT_BUTTON = "wizard-next-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String CANCEL_BUTTON = "wizard-cancel-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String PROCESS_NAME = "Selenium Test " + Math.random();
    private static final String INVENTORY_PROCESS = "Inventory Processes";
    private static final String LATEST = "Latest";
    private static final String NRP = "Network Resource Process";
    private static final String DCP = "Data Correction Process";
    private static final String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    private static final String ADD_MILESTONE_LIST = "addMilestonesComponentId";

    public ProcessWizardPage(WebDriver driver) {
        super(driver);
    }

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
        TableInterface table = OldTable.createById(driver, wait, TABLE_PROCESSES);
        table.callAction("create", "start-process");
        definedBasicProcess(processName, processType, plusDays).clickButtonById(CREATE_BUTTON);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }

    @Description("Go to Milestone Step)")
    public MilestoneStepWizard definedMilestoneInProcess(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createById(driver, wait, TABLE_PROCESSES);
        table.callAction("create", "start-process");
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        if (driver.getPageSource().contains("Add Milestones")) {
            processWizard.setComponentValue("milestonesEnabledCheckboxId", "true", Input.ComponentType.CHECKBOX);
        }
        DelayUtils.sleep();
        processWizard.clickButtonById(NEXT_BUTTON);
        return new MilestoneStepWizard(driver, wait);
    }

    public void clickAcceptButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CREATE_BUTTON);
    }

    public void clickCancelButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CANCEL_BUTTON);
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
        wizardFirstStep.clickButtonById(ACCEPT_BUTTON);
        Wizard wizardSecondStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
        Input processNameTextField = wizardSecondStep.getComponent(PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        processNameTextField.setSingleStringValue(processName);
        if (driver.getPageSource().contains(FINISH_DUE_DATE_ID)) {
            Input finishedDueDate = wizardSecondStep.getComponent(FINISH_DUE_DATE_ID, Input.ComponentType.DATE);
            finishedDueDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
        }
        return Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
    }

    private String extractProcessCode(String message) {
        Iterable<String> messageParts = Splitter.on(CharMatcher.anyOf("() ")).split(message);
        for (String part : messageParts) {
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
            MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver);
            return milestoneWizardPage.addMilestoneRow(milestone, ADD_MILESTONE_LIST);
        }

        public Milestone editPredefinedMilestone(Milestone milestone, int row) {
            MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver);
            return milestoneWizardPage.editMilestoneRow(milestone, row, PREDEFINED_MILESTONE_LIST);
        }

        public void clickAcceptButton() {
            Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CREATE_BUTTON);
        }

        public void clickCancelButton() {
            Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CANCEL_BUTTON);
        }
    }
}
