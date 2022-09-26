/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.processinstances;

import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
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
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

/**
 * @author Gabriela Kasza
 */
public class ProcessWizardPage extends BasePage {

    private static final String CANNOT_EXTRACT_PROCESS_CODE_EXCEPTION = "Cannot extract Process Code from message: ";
    private static final String TABLE_PROCESSES = "bpm_processes_view_processes";
    private static final String PROCESS_WIZARD_STEP_1 = "bpm_processes_view_start-process-prompt_prompt-card";
    private static final String DCP = "Data Correction Process";
    private static final String DOMAIN_ATTRIBUTE_ID = "domain-combobox";
    private static final String DEFINITION_ATTRIBUTE_ID = "definition-combobox";
    private static final String RELEASE_ATTRIBUTE_ID = "release-combobox";
    private static final String PROCESS_NAME_ATTRIBUTE_ID = "processNameTextFieldId";
    private static final String FINISH_DUE_DATE_ID = "FINISHED_DUE_DATE";
    private static final String DUE_DATE_ID = "programDueDateId";
    private static final String ACCEPT_BUTTON = "wizard-submit-button-start-process-wizard";
    private static final String CREATE_BUTTON = "wizard-submit-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    protected static final String PROCESS_WIZARD_STEP_2 = "bpm_processes_view_start-process-details-prompt_prompt-card";

    private static final String PREVIOUS_BUTTON = "wizard-previous-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String CANCEL_BUTTON = "wizard-cancel-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    protected static final String NEXT_BUTTON = "wizard-next-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String INVENTORY_PROCESS = "Inventory Processes";
    private static final String LATEST = "Latest";
    private static final String NRP = "Network Resource Process";
    protected static final String PROCESS_NAME = "Selenium Test " + Math.random();
    private static final String PREDEFINED_MILESTONE_LIST = "editMilestonesComponentId";
    private static final String ADD_MILESTONE_LIST = "addMilestonesComponentId";
    private static final String CREATE_GROUP_ACTION_ID = "create";
    private static final String START_PROCESS_ACTION_ID = "start-process";
    private static final String CREATE_PROCESS_OPTION_LABEL = "Create processes";
    private static final String ADD_MILESTONE_OPTION_LABEL = "Add Milestones";
    private static final String MILESTONE_ENABLED_CHECKBOX_ID = "milestonesEnabledCheckboxId";
    private static final String CREATE_PROCESS_CHECKBOX_ID = "createProcessesCheckboxId";

    public ProcessWizardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * @deprecated Along with the 3.0.x version this method will be replaced by {@link #createSimpleNRPV2()}.
     * New method is adapted to run on views other than Process Instances View.
     * As part of the method replacement, in tests where process creation is executed,
     * context action of opening process creation wizard should be called from the class of the given View, e.g.
     * {@link ProcessOverviewPage#openProcessCreationWizard()} (for Process Instances View).
     * It is also possible to call {@link ProcessOverviewPage#createSimpleNRP()} method
     * which will open wizard and proceed process creation.
     */
    @Deprecated
    public String createSimpleNRP() {
        return createProcess(PROCESS_NAME, (long) 0, NRP);
    }

    /**
     * @deprecated Along with the 3.0.x version this method will be replaced by {@link #createSimpleDCPV2()}.
     * New method is adapted to run on views other than Process Instances View.
     * As part of the method replacement, in tests where process creation is executed,
     * context action of opening process creation wizard should be called from the class of the given View, e.g.
     * {@link ProcessOverviewPage#openProcessCreationWizard()} (for Process Instances View).
     * It is also possible to call {@link ProcessOverviewPage#createSimpleDCP()} method
     * which will open wizard and proceed process creation.
     */
    @Deprecated
    public String createSimpleDCP() {
        return createProcess(PROCESS_NAME, (long) 0, DCP);
    }


    /**
     * @deprecated Along with the 3.0.x version this method will be replaced by {@link #createDCPWithPlusDays(Long)}.
     * New method is adapted to run on views other than Process Instances View.
     * As part of the method replacement, in tests where process creation is executed,
     * context action of opening process creation wizard should be called from the class of the given View, e.g.
     * {@link ProcessOverviewPage#openProcessCreationWizard()} (for Process Instances View).
     * It is also possible to call {@link ProcessOverviewPage#createDCPWithPlusDays(Long)} method
     * which will open wizard and proceed process creation.
     */
    @Deprecated
    public String createDCPPlusDays(Long plusDays) {
        return createProcess(PROCESS_NAME, plusDays, DCP);
    }

    /**
     * @deprecated Along with the 3.0.x version this method will be replaced by {@link #createNRPWithPlusDays(Long)}.
     * New method is adapted to run on views other than Process Instances View.
     * As part of the method replacement, in tests where process creation is executed,
     * context action of opening process creation wizard should be called from the class of the given View, e.g.
     * {@link ProcessOverviewPage#openProcessCreationWizard()} (for Process Instances View).
     * It is also possible to call {@link ProcessOverviewPage#createNRPWithPlusDays(Long)} method
     * which will open wizard and proceed process creation.
     */
    @Deprecated
    public String createNRPPlusDays(Long plusDays) {
        return createProcess(PROCESS_NAME, plusDays, NRP);
    }

    /**
     * @deprecated Along with the 3.0.x version this method will be replaced by {@link #createProcessIPD(String, Long, String)}.
     * New method is adapted to run on views other than Process Instances View.
     * As part of the method replacement, in tests where process creation is executed,
     * context action of opening process creation wizard should be called from the class of the given View, e.g.
     * {@link ProcessOverviewPage#openProcessCreationWizard()} (for Process Instances View).
     * It is also possible to call {@link ProcessOverviewPage#createProcessIPD(String, Long, String)} method
     * which will open wizard and proceed process creation.
     */
    @Deprecated
    public String createProcess(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createById(driver, wait, TABLE_PROCESSES);
        table.callAction(CREATE_GROUP_ACTION_ID, START_PROCESS_ACTION_ID);
        definedBasicProcess(processName, processType, plusDays).clickButtonById(CREATE_BUTTON);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }

    public String createSimpleNRPV2() {
        return createProcessIPD(PROCESS_NAME, (long) 0, NRP);
    }

    public String createSimpleDCPV2() {
        return createProcessIPD(PROCESS_NAME, (long) 0, DCP);
    }

    public String createDCPWithPlusDays(Long plusDays) {
        return createProcessIPD(PROCESS_NAME, plusDays, DCP);
    }

    public String createNRPWithPlusDays(Long plusDays) {
        return createProcessIPD(PROCESS_NAME, plusDays, NRP);
    }

    public String createProgramWithProcess(String programName, Long plusDays, String programType, String processName, Long plusDaysProcess, String processType) {
        Wizard programWizard = definedBasicProgram(programName, programType, plusDays);
        if (driver.getPageSource().contains(CREATE_PROCESS_OPTION_LABEL)) {
            programWizard.setComponentValue(CREATE_PROCESS_CHECKBOX_ID, "true");
        }
        DelayUtils.sleep();
        programWizard.clickButtonById(CREATE_BUTTON);
        definedBasicProcess(processName, processType, plusDaysProcess).clickButtonById(CREATE_BUTTON);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }

    private Wizard definedBasicProgram(String programName, String programType, Long plusDays) {
        Wizard wizardSecondStep = selectProcessDefinition(programType);
        wizardSecondStep.setComponentValue(PROCESS_NAME_ATTRIBUTE_ID, programName);
        if (driver.getPageSource().contains(DUE_DATE_ID)) {
            wizardSecondStep.setComponentValue(DUE_DATE_ID, LocalDate.now().plusDays(plusDays).toString());
        }
        return Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
    }

    private Wizard selectProcessDefinition(String processType) {
        Wizard wizardFirstStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_1);
        Input componentDomain = wizardFirstStep.getComponent(DOMAIN_ATTRIBUTE_ID);
        if (!componentDomain.getValue().getStringValue().equals(INVENTORY_PROCESS)) {
            componentDomain.setSingleStringValue(INVENTORY_PROCESS);
            wizardFirstStep.waitForWizardToLoad();
        }
        wizardFirstStep.setComponentValue(DEFINITION_ATTRIBUTE_ID, processType);
        wizardFirstStep.waitForWizardToLoad();
        Input componentRelease = wizardFirstStep.getComponent(RELEASE_ATTRIBUTE_ID);
        if (!componentRelease.getValue().getStringValue().equals(LATEST)) {
            componentRelease.setSingleStringValue(LATEST);
            wizardFirstStep.waitForWizardToLoad();
        }
        wizardFirstStep.clickButtonById(ACCEPT_BUTTON);
        return Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
    }

    public String createProcessIPD(String processName, Long plusDays, String processType) {
        definedBasicProcess(processName, processType, plusDays).clickButtonById(CREATE_BUTTON);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }

    @Description("Go to Milestone Step)")
    public MilestoneStepWizard definedMilestoneInProcess(String processName, Long plusDays, String processType) {
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        if (driver.getPageSource().contains(ADD_MILESTONE_OPTION_LABEL)) {
            processWizard.setComponentValue(MILESTONE_ENABLED_CHECKBOX_ID, "true");
        }
        DelayUtils.sleep();
        processWizard.clickButtonById(NEXT_BUTTON);
        return new MilestoneStepWizard(driver, wait);
    }

    public void clickAcceptButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CREATE_BUTTON);
    }

    @Step("Defining simple DCP with process roles and waiting for information's about roles")
    public ProcessRolesStepWizardPage defineSimpleDCPAndGoToProcessRolesStep() {
        return defineProcessAndGoToProcessRolesStep(PROCESS_NAME, (long) 0, DCP);
    }

    public ProcessRolesStepWizardPage defineProcessAndGoToProcessRolesStep(String processName, Long plusDays, String processType) {
        definedBasicProcess(processName, processType, plusDays).clickButtonById(NEXT_BUTTON);
        return new ProcessRolesStepWizardPage(driver);
    }

    public void clickCancelButton() {
        Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CANCEL_BUTTON);
    }

    protected Wizard definedBasicProcess(String processName, String processType, Long plusDays) {
        Wizard wizardSecondStep = selectProcessDefinition(processType);
        wizardSecondStep.setComponentValue(PROCESS_NAME_ATTRIBUTE_ID, processName);
        if (driver.getPageSource().contains(FINISH_DUE_DATE_ID)) {
            wizardSecondStep.setComponentValue(FINISH_DUE_DATE_ID, LocalDate.now().plusDays(plusDays).toString());
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
        throw new NoSuchElementException(CANNOT_EXTRACT_PROCESS_CODE_EXCEPTION + message);
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
            MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONE_LIST);
            return milestoneWizardPage.addMilestoneRow(milestone);
        }

        public Milestone editPredefinedMilestone(Milestone milestone, int row) {
            MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, PREDEFINED_MILESTONE_LIST);
            return milestoneWizardPage.editMilestoneRow(milestone, row);
        }

        public void clickAcceptButton() {
            Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CREATE_BUTTON);
        }

        public void clickCancelButton() {
            Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2).clickButtonById(CANCEL_BUTTON);
        }
    }
}
