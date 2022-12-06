/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm.processinstances.creation;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Gabriela Kasza
 * @author Paweł Rother
 */
public class ProcessWizardPage extends BasePage {
    public static final String NRP = "Network Resource Process";
    public static final String DCP = "Data Correction Process";
    protected static final String NEXT_BUTTON = "wizard-next-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    protected static final String MILESTONE_ENABLED_CHECKBOX_ID = "milestonesEnabledCheckboxId";
    protected static final String PROCESS_WIZARD_STEP_2 = "bpm_processes_view_start-process-details-prompt_prompt-card";
    protected static final String PROCESS_NAME = "Selenium Test " + Math.random();
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessWizardPage.class);
    private static final String CANNOT_EXTRACT_PROCESS_CODE_EXCEPTION = "Cannot extract Process Code from message: ";
    private static final String CANNOT_EXTRACT_PROGRAM_CODE_EXCEPTION = "Cannot extract Program Code from message: ";
    private static final String PROCESS_WIZARD_STEP_1 = "bpm_processes_view_start-process-prompt_prompt-card";
    private static final String DOMAIN_ATTRIBUTE_ID = "domain-combobox";
    private static final String DEFINITION_ATTRIBUTE_ID = "definition-combobox";
    private static final String RELEASE_ATTRIBUTE_ID = "release-combobox";
    private static final String PROCESS_NAME_ATTRIBUTE_ID = "processNameTextFieldId";
    private static final String FINISH_DUE_DATE_ID = "FINISHED_DUE_DATE";
    private static final String DUE_DATE_ID = "programDueDateId";
    private static final String ACCEPT_BUTTON = "wizard-submit-button-start-process-wizard";
    private static final String CREATE_BUTTON = "wizard-submit-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String CANCEL_BUTTON = "wizard-cancel-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private static final String INVENTORY_PROCESS = "Inventory Processes";
    private static final String LATEST = "Latest";
    private static final String FORECAST_ENABLED_CHECKBOX_ID = "forecastsEnabledCheckboxId";
    private static final String SCHEDULE_ENABLED_CHECKBOX_ID = "scheduleEnabledCheckboxId";
    private static final String CREATE_PROCESS_CHECKBOX_ID = "createProcessesCheckboxId";
    private static final String CREATE_MULTIPLE_CHECKBOX_ID = "createMultipleCheckboxId";
    private static final String NUMBER_OF_PROCESSES_ID = "createMultipleNumberComponentId";
    private static final String PROGRAMS_SEARCH_ID = "programsSearchBoxId";

    private static final String PROCESSES_OUT_OF_RANGE_EXCEPTION = "Number of Processes must be between 1 and 300";
    private static final String CHECKBOX_NOT_PRESENT_EXCEPTION = "Checkbox %s is not present in the wizard.";
    private static final String PROCESS_ROLES_ASSIGNMENT_STEP = "Role assignment";
    private static final String MILESTONES_ASSIGNMENT_STEP = "Milestones assignment";
    private static final String LACK_OF_STEP_EXCEPTION = "Step %s is not visible on Process creation wizard";
    private static final String CREATING_PROCESS_INFO = "Creating Process";
    private static final String CREATING_PROGRAM_INFO = "Creating Program";


    public ProcessWizardPage(WebDriver driver) {
        super(driver);
    }

    protected Wizard definedBasicProcess(String processName, String processType, Long plusDays) {
        LOGGER.info(CREATING_PROCESS_INFO);
        Wizard wizardSecondStep = selectProcessDefinition(processType);
        wizardSecondStep.setComponentValue(PROCESS_NAME_ATTRIBUTE_ID, processName);
        if (driver.getPageSource().contains(FINISH_DUE_DATE_ID)) {
            wizardSecondStep.setComponentValue(FINISH_DUE_DATE_ID, LocalDate.now().plusDays(plusDays).toString());
        }
        return getSecondStepWizard();
    }

    protected Wizard definedBasicProgram(String programName, String programType, Long plusDays) {
        LOGGER.info(CREATING_PROGRAM_INFO);
        Wizard wizardSecondStep = selectProcessDefinition(programType);
        wizardSecondStep.setComponentValue(PROCESS_NAME_ATTRIBUTE_ID, programName);
        if (driver.getPageSource().contains(DUE_DATE_ID)) {
            wizardSecondStep.setComponentValue(DUE_DATE_ID, LocalDate.now().plusDays(plusDays).toString());
        }
        return getSecondStepWizard();
    }

    public String createProcessIPD(String processName, Long plusDays, String processType) {
        definedBasicProcess(processName, processType, plusDays).clickButtonById(CREATE_BUTTON);
        return extractProcessCode(getProcessCreationMessage());
    }

    public String createProgram(String programName, Long plusDays, String programType) {
        definedBasicProgram(programName, programType, plusDays).clickButtonById(CREATE_BUTTON);
        return extractProgramCode(getProcessCreationMessage());
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

    public String createProcessLinkedToPrograms(String processName, Long plusDays, String processType,
                                                List<String> programNamesList) {
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        setProgramsToLink(programNamesList);
        processWizard.clickButtonById(CREATE_BUTTON);
        return extractProcessCode(getProcessCreationMessage());
    }

    public String createProcessLinkedToProgram(String processName, Long plusDays, String processType,
                                               String programName) {
        return createProcessLinkedToPrograms(processName, plusDays, processType, Collections.singletonList(programName));
    }

    public void createMultipleProcesses(String processName, Long plusDays, String processType, int processesAmount) {
        if (processesAmount <= 0 || processesAmount > 300) {
            throw new IllegalArgumentException(PROCESSES_OUT_OF_RANGE_EXCEPTION);
        } else {
            Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
            selectCheckbox(processWizard, CREATE_MULTIPLE_CHECKBOX_ID);
            processWizard.setComponentValue(NUMBER_OF_PROCESSES_ID, String.valueOf(processesAmount));
            processWizard.clickButtonById(CREATE_BUTTON);
        }
    }

    public void createProgramWithMultipleProcesses(String programName, Long plusDaysProgram, String programType,
                                                   String processName, Long plusDaysProcess, String processType,
                                                   int processesAmount) {
        if (processesAmount <= 0 || processesAmount > 300) {
            throw new IllegalArgumentException(PROCESSES_OUT_OF_RANGE_EXCEPTION);
        } else {
            Wizard programWizard = definedBasicProgram(programName, programType, plusDaysProgram);
            selectCheckbox(programWizard, CREATE_PROCESS_CHECKBOX_ID);
            programWizard.clickButtonById(CREATE_BUTTON);
            Wizard processWizard = definedBasicProcess(processName, processType, plusDaysProcess);
            selectCheckbox(processWizard, CREATE_MULTIPLE_CHECKBOX_ID);
            processWizard.setComponentValue(NUMBER_OF_PROCESSES_ID, String.valueOf(processesAmount));
            processWizard.clickButtonById(CREATE_BUTTON);
        }
    }

    public Map<String, String> createProgramWithProcess(String programName, Long plusDaysProgram, String programType,
                                                        String processName, Long plusDaysProcess, String processType) {
        Wizard programWizard = definedBasicProgram(programName, programType, plusDaysProgram);
        selectCheckbox(programWizard, CREATE_PROCESS_CHECKBOX_ID);
        programWizard.clickButtonById(CREATE_BUTTON);
        definedBasicProcess(processName, processType, plusDaysProcess).clickButtonById(CREATE_BUTTON);
        String processCode = extractProcessCode(getProcessCreationMessage());
        String programCode = extractProgramCode(getProcessCreationMessage());
        return ImmutableMap.<String, String>builder()
                .put("processCode", processCode)
                .put("programCode", programCode)
                .build();
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
        return getSecondStepWizard();
    }

    protected void selectCheckbox(Wizard wizard, String checkBoxId) {
        try {
            wizard.setComponentValue(checkBoxId, "true");
            DelayUtils.sleep();
        } catch (TimeoutException e) {
            throw new NoSuchElementException(String.format(CHECKBOX_NOT_PRESENT_EXCEPTION, checkBoxId));
        }
    }

    public MilestonesStepWizardPage defineProcessAndGoToMilestonesStep(String processName, Long plusDays, String processType) {
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        if (!isStepVisible(MILESTONES_ASSIGNMENT_STEP)) {
            selectCheckbox(processWizard, MILESTONE_ENABLED_CHECKBOX_ID);
            DelayUtils.sleep();
        }
        processWizard.clickButtonById(NEXT_BUTTON);
        return new MilestonesStepWizardPage(driver);
    }

    public ForecastsStepWizardPage defineProcessAndGoToForecastsStep(String processName, Long plusDays, String processType) {
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        selectCheckbox(processWizard, FORECAST_ENABLED_CHECKBOX_ID);
        DelayUtils.sleep();
        processWizard.clickButtonById(NEXT_BUTTON);
        return new ForecastsStepWizardPage(driver);
    }

    public ScheduleStepWizardPage defineProcessAndGoToScheduleStep(String processName, Long plusDays, String processType) {
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        selectCheckbox(processWizard, SCHEDULE_ENABLED_CHECKBOX_ID);
        DelayUtils.sleep();
        processWizard.clickButtonById(NEXT_BUTTON);
        return new ScheduleStepWizardPage(driver);
    }

    @Step("Defining simple DCP with process roles and waiting for information's about roles")
    public ProcessRolesStepWizardPage defineSimpleDCPAndGoToProcessRolesStep() {
        return defineProcessAndGoToProcessRolesStep(PROCESS_NAME, (long) 0, DCP);
    }

    public ProcessRolesStepWizardPage defineProcessAndGoToProcessRolesStep(String processName, Long plusDays, String processType) {
        Wizard processWizard = definedBasicProcess(processName, processType, plusDays);
        if (isStepVisible(PROCESS_ROLES_ASSIGNMENT_STEP)) {
            processWizard.clickNext();
            return new ProcessRolesStepWizardPage(driver);
        } else
            throw new NoSuchElementException(String.format(LACK_OF_STEP_EXCEPTION, PROCESS_ROLES_ASSIGNMENT_STEP));
    }

    public void clickAcceptButton() {
        getSecondStepWizard().clickButtonById(CREATE_BUTTON);
    }

    public void clickCancelButton() {
        getSecondStepWizard().clickButtonById(CANCEL_BUTTON);
    }

    private Wizard getSecondStepWizard() {
        return Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
    }

    private void setProgramsToLink(List<String> programNamesList) {
        Input programs = ComponentFactory.create(PROGRAMS_SEARCH_ID, driver, wait);
        programNamesList.forEach(programs::setSingleStringValue);
    }

    private String getProcessCreationMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        return messages.get(0).getText();
    }

    private List<String> getProcessWizardSteps() {
        return getSecondStepWizard().getWizardStepsTitles();
    }

    private boolean isStepVisible(String stepName) {
        return getProcessWizardSteps().stream().anyMatch(s -> s.contains(stepName));
    }

    private String extractProcessCode(String message) {
        Iterable<String> messageParts = Splitter.on(CharMatcher.anyOf("() ")).split(message);
        for (String part : messageParts) {
            if (part.startsWith("NRP-") || part.startsWith("DCP-")
                    || part.startsWith("IP-") || part.startsWith("DRP-")
                    || part.startsWith("ML-") || part.startsWith("audit_roles-")
                    || part.startsWith("audit1-") || part.startsWith("audit2-")
                    || part.startsWith("audit_milestone-") || part.startsWith("audit_param-")) {
                return part;
            }
        }
        throw new NoSuchElementException(CANNOT_EXTRACT_PROCESS_CODE_EXCEPTION + message);
    }

    private String extractProgramCode(String message) {
        Iterable<String> messageParts = Splitter.on(CharMatcher.anyOf("() ")).split(message);
        for (String part : messageParts) {
            if (part.startsWith("program_roles-") || part.startsWith("audit_program-")) {
                return part;
            }
        }
        throw new NoSuchElementException(CANNOT_EXTRACT_PROGRAM_CODE_EXCEPTION + message);
    }

    public void createInstance(ProcessCreationWizardProperties properties) {
        if (properties.isProcessCreation()) {

            Wizard processWizard = definedBasicProcess(properties.getProcessName(), properties.getProcessType(),
                    properties.getProcessPlusDays());

            proceedProgramsToLink(properties);

            if (properties.isScheduleCreation()) {

                selectCheckbox(processWizard, SCHEDULE_ENABLED_CHECKBOX_ID);

                proceedRolesStep(processWizard, properties, false);
                proceedScheduleStep(processWizard, properties);
                clickAcceptButton();
            } else
                proceedProcessSteps(processWizard, properties);

        } else if (properties.isProgramCreation()) {
            Wizard programWizard = definedBasicProgram(properties.getProgramName(), properties.getProgramType(),
                    properties.getProgramPlusDays());

            if (properties.isProgramForecastsCreation())
                selectCheckbox(programWizard, FORECAST_ENABLED_CHECKBOX_ID);

            if (properties.isProgramMilestonesCreation() && !isStepVisible(MILESTONES_ASSIGNMENT_STEP))
                selectCheckbox(programWizard, MILESTONE_ENABLED_CHECKBOX_ID);

            if (properties.isProgramWithProcessCreation())
                selectCheckbox(programWizard, CREATE_PROCESS_CHECKBOX_ID);

            proceedRolesStep(programWizard, properties, true);
            proceedMilestonesStep(programWizard, properties, true);
            proceedForecastsStep(programWizard, properties, true);

            clickAcceptButton();

            if (properties.isProgramWithProcessCreation()) {
                Wizard processWizard = definedBasicProcess(properties.getProcessName(), properties.getProcessType(),
                        properties.getProcessPlusDays());

                proceedProcessSteps(processWizard, properties);
            }
        }
    }

    private void proceedForecastsStep(Wizard processWizard, ProcessCreationWizardProperties properties, boolean isForProgram) {
        if (properties.isProgramForecastsCreation() || properties.isProcessForecastsCreation()) {
            processWizard.clickNext();
            ForecastsStepWizardPage forecastsStepWizardPage = new ForecastsStepWizardPage(driver);
            if (isForProgram) {
                forecastsStepWizardPage.setProcessForecast(properties.getProgramMainForecast());
                if (properties.getProgramForecastsList() != null) {
                    forecastsStepWizardPage.addForecasts(properties.getProgramForecastsList());
                }
            } else {
                forecastsStepWizardPage.setProcessForecast(properties.getProcessMainForecast());
                if (properties.getProcessForecastsList() != null) {
                    forecastsStepWizardPage.addForecasts(properties.getProcessForecastsList());
                }
            }
        }
    }

    private void proceedProgramsToLink(ProcessCreationWizardProperties properties) {
        if (properties.isProgramsToLink()) {
            setProgramsToLink(properties.getProgramNamesList());
        }
    }

    private void proceedMilestonesStep(Wizard processWizard, ProcessCreationWizardProperties properties, boolean isForProgram) {
        if (properties.isProgramMilestonesCreation() || properties.isProcessMilestonesCreation()) {
            processWizard.clickNext();
            MilestonesStepWizardPage milestonesStepWizardPage = new MilestonesStepWizardPage(driver);
            if (isForProgram)
                milestonesStepWizardPage.addMilestones(properties.getProgramMilestoneList());
            else
                milestonesStepWizardPage.addMilestones(properties.getProcessMilestoneList());
        } else {
            if (isStepVisible(MILESTONES_ASSIGNMENT_STEP))
                processWizard.clickNext();
        }
    }

    private void proceedRolesStep(Wizard processWizard, ProcessCreationWizardProperties properties, boolean isForProgram) {
        if (properties.isProcessRolesAssignment() || properties.isProgramRolesAssignment()) {
            if (isStepVisible(PROCESS_ROLES_ASSIGNMENT_STEP)) {
                processWizard.clickNext();
                ProcessRolesStepWizardPage processRolesStepWizardPage = new ProcessRolesStepWizardPage(driver);
                if (isForProgram)
                    processRolesStepWizardPage.addPlanners(properties.getProgramRolesList());
                else
                    processRolesStepWizardPage.addPlanners(properties.getProcessRolesList());
            } else
                throw new NoSuchElementException(String.format(LACK_OF_STEP_EXCEPTION, PROCESS_ROLES_ASSIGNMENT_STEP));
        }
    }

    private void proceedScheduleStep(Wizard processWizard, ProcessCreationWizardProperties properties) {
        processWizard.clickNext();
        ScheduleStepWizardPage scheduleStepWizardPage = new ScheduleStepWizardPage(driver);
        if (properties.getCronExpression() != null) {
            scheduleStepWizardPage.setCRONExpression(properties.getCronExpression());
        } else {
            scheduleStepWizardPage.setSchedule(properties.getScheduleProperties());
        }
    }

    private void proceedMultipleProcesses(Wizard processWizard, ProcessCreationWizardProperties properties) {
        if (properties.isMultipleProcessesCreation()) {
            selectCheckbox(processWizard, CREATE_MULTIPLE_CHECKBOX_ID);
            processWizard.setComponentValue(NUMBER_OF_PROCESSES_ID, String.valueOf(properties.getProcessAmount()));
        }
    }

    private void proceedProcessSteps(Wizard processWizard, ProcessCreationWizardProperties properties) {
        proceedMultipleProcesses(processWizard, properties);

        if (properties.isProcessForecastsCreation())
            selectCheckbox(processWizard, FORECAST_ENABLED_CHECKBOX_ID);

        if (properties.isProcessMilestonesCreation() && !isStepVisible(MILESTONES_ASSIGNMENT_STEP))
            selectCheckbox(processWizard, MILESTONE_ENABLED_CHECKBOX_ID);

        proceedRolesStep(processWizard, properties, false);
        proceedMilestonesStep(processWizard, properties, false);
        proceedForecastsStep(processWizard, properties, false);
        clickAcceptButton();
    }
}
