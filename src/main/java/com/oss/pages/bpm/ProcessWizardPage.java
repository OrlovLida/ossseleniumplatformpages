/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.bpm;

import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.google.common.base.Splitter;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;

/**
 * @author Gabriela Kasza
 */
public class ProcessWizardPage extends BasePage {

    public ProcessWizardPage(WebDriver driver) {
        super(driver);
    }

    private String TABLE_PROCESSES = "bpm_processes_view_processes";
    private String PROCESS_WIZARD_STEP_1 = "start-process-wizard";
    private String PROCESS_WIZARD_STEP_2 = "bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private String DOMAIN_ATTRIBUTE_ID = "domain-combobox-input";
    private String DEFINITION_ATTRIBUTE_ID = "definition-combobox-input";
    private String RELEASE_ATTRIBUTE_ID = "release-combobox-input";
    private String PROCESS_NAME_ATTRIBUTE_ID = "bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private String FINISH_DUE_DATE_ID = "FINISHED_DUE_DATE";
    private String CREATE_PROCESS_BUTTON = "Create new process";
    private String ACCEPT_BUTTON = "wizard-submit-button-start-process-wizard";
    private String CREATE_BUTTON = "wizard-submit-button-bpm_processes_view_start-process-details-prompt_processCreateFormId";
    private String PROCESS_NAME = "Selenium Test " + Math.random();
    private String INVENTORY_PROCESS = "Inventory Processes";
    private String LATEST = "Latest";
    private String NRP = "Network Resource Process";
    private String DCP = "Data Correction Process";

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
        //wizardFirstStep.waitToClose();

        Wizard wizardSecondStep = Wizard.createByComponentId(driver, wait, PROCESS_WIZARD_STEP_2);
        Input processNameTextField = wizardSecondStep.getComponent(PROCESS_NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD);
        processNameTextField.setSingleStringValue(processName);
        Input finishedDueDate = wizardSecondStep.getComponent(FINISH_DUE_DATE_ID, Input.ComponentType.DATE);
        finishedDueDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
        wizardSecondStep.clickActionById(CREATE_BUTTON);

        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, wait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        String text = messages.get(0).getText();
        return extractProcessCode(text);
    }

    private String extractProcessCode(String message) {
        Iterable<String> messageParts = Splitter.on(" ").split(message);
        for (String part : messageParts) {
            if (part.startsWith("NRP-") || part.startsWith("DCP-")
                    || part.startsWith("IP-") || part.startsWith("DRP-")) {
                return part;
            }
        }
        throw new RuntimeException("Cannot extract Process Code from message: " + message);
    }

}
