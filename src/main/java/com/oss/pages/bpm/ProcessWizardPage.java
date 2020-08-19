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
import com.oss.framework.components.Input;
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

    public String createSimpleNRP() {

        return createProcess("Selenium Test " + Math.random(), (long) 0, "Network Resource Process");

    }

    public String createSimpleDCP() {
        return createProcess("Selenium Test " + Math.random(), (long) 0, "Data Correction Process");
    }

    public String createDCPPlusDays(Long plusDays) {

        return createProcess("Selenium Test " + Math.random(), plusDays, "Data Correction Process");
    }

    public String createNRPPlusDays(Long plusDays) {

        return createProcess("Selenium Test " + Math.random(), plusDays, "Network Resource Process");
    }

    public String createProcess(String processName, Long plusDays, String processType) {
        TableInterface table = OldTable.createByWindowTitle(driver, wait, "Process Instances");
        table.callActionByLabel("Create new process");
        Wizard wizardFirstStep = Wizard.createByComponentId(driver, wait, "start-process-wizard");
        Input componentDomain = wizardFirstStep.getComponent("domain-combobox-input", Input.ComponentType.COMBOBOX);
        if (!componentDomain.getValue().getStringValue().equals("Inventory Processes")) {
            componentDomain.setSingleStringValue("Inventory Processes");
        }
        Input componentDefinition = wizardFirstStep.getComponent("definition-combobox-input", Input.ComponentType.COMBOBOX);
        componentDefinition.setSingleStringValue(processType);
        Input componentRelease = wizardFirstStep.getComponent("release-combobox-input", Input.ComponentType.COMBOBOX);
        componentRelease.setSingleStringValue("Latest");
        wizardFirstStep.clickActionById("wizard-submit-button-start-process-wizard");
        //wizardFirstStep.waitToClose();

        Wizard wizardSecondStep = Wizard.createByComponentId(driver, wait, "bpm_processes_view_start-process-details-prompt_processCreateFormId");
        Input processNameTextField = wizardSecondStep.getComponent("processNameTextFieldId", Input.ComponentType.TEXT_FIELD);
        processNameTextField.setSingleStringValue(processName);
        Input finishedDueDate = wizardSecondStep.getComponent("FINISHED_DUE_DATE", Input.ComponentType.DATE);
        finishedDueDate.setSingleStringValue(LocalDate.now().plusDays(plusDays).toString());
        wizardSecondStep.clickActionById("wizard-submit-button-bpm_processes_view_start-process-details-prompt_processCreateFormId");

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
